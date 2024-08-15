package net.minecraft.network;

import cn.feng.untitled.Client;
import cn.feng.untitled.event.impl.PacketEvent;
import cn.feng.untitled.event.type.PacketType;
import com.google.common.collect.Queues;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.viaversion.viaversion.api.connection.UserConnection;
import com.viaversion.viaversion.connection.UserConnectionImpl;
import com.viaversion.viaversion.protocol.ProtocolPipelineImpl;
import de.florianmichael.vialoadingbase.ViaLoadingBase;
import de.florianmichael.viamcp.MCPVLBPipeline;
import de.florianmichael.viamcp.ViaMCP;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.local.LocalChannel;
import io.netty.channel.local.LocalServerChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.TimeoutException;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.util.*;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.crypto.SecretKey;
import java.net.InetAddress;
import java.net.SocketAddress;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class NetworkManager extends SimpleChannelInboundHandler<Packet> {
    public static final Marker logMarkerNetwork = MarkerManager.getMarker("NETWORK");
    public static final Marker logMarkerPackets = MarkerManager.getMarker("NETWORK_PACKETS").addParents(logMarkerNetwork);
    public static final AttributeKey<EnumConnectionState> attrKeyConnectionState = AttributeKey.valueOf("protocol");
    public static final LazyLoadBase<NioEventLoopGroup> CLIENT_NIO_EVENTLOOP = new LazyLoadBase<>() {
        protected NioEventLoopGroup load() {
            return new NioEventLoopGroup(0, Executors.newCachedThreadPool((new ThreadFactoryBuilder()).setNameFormat("Netty Client IO #%d").setDaemon(true).build()));
        }
    };
    public static final LazyLoadBase<EpollEventLoopGroup> CLIENT_EPOLL_EVENTLOOP = new LazyLoadBase<>() {
        protected EpollEventLoopGroup load() {
            return new EpollEventLoopGroup(0, Executors.newCachedThreadPool((new ThreadFactoryBuilder()).setNameFormat("Netty Epoll Client IO #%d").setDaemon(true).build()));
        }
    };
    public static final LazyLoadBase<DefaultEventLoop> CLIENT_LOCAL_EVENTLOOP = new LazyLoadBase<>() {
        protected DefaultEventLoop load() {
            return new DefaultEventLoop(Executors.newCachedThreadPool((new ThreadFactoryBuilder()).setNameFormat("Netty Local Client IO #%d").setDaemon(true).build()));
        }
    };
    private static final Logger logger = LogManager.getLogger();
    private final EnumPacketDirection direction;
    private final Queue<NetworkManager.InboundHandlerTuplePacketListener> outboundPacketsQueue = Queues.newConcurrentLinkedQueue();
    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    /**
     * The active channel
     */
    private Channel channel;

    /**
     * The address of the remote party
     */
    private SocketAddress socketAddress;

    /**
     * The INetHandler instance responsible for processing received packets
     */
    private INetHandler packetListener;

    /**
     * A String indicating why the network has shutdown.
     */
    private IChatComponent terminationReason;
    private boolean isEncrypted;
    private boolean disconnected;

    public NetworkManager(EnumPacketDirection packetDirection) {
        this.direction = packetDirection;
    }

    /**
     * Create a new NetworkManager from the server host and connect it to the server
     *
     * @param address            The address of the server
     * @param serverPort         The server port
     * @param useNativeTransport True if the client use the native transport system
     */
    public static NetworkManager createNetworkManagerAndConnect(InetAddress address, int serverPort, boolean useNativeTransport) {
        final NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
        Class<? extends SocketChannel> oclass;
        LazyLoadBase<? extends EventLoopGroup> lazyloadbase;

        if (Epoll.isAvailable() && useNativeTransport) {
            oclass = EpollSocketChannel.class;
            lazyloadbase = CLIENT_EPOLL_EVENTLOOP;
        } else {
            oclass = NioSocketChannel.class;
            lazyloadbase = CLIENT_NIO_EVENTLOOP;
        }

        (new Bootstrap()).group(lazyloadbase.getValue()).handler(new ChannelInitializer<Channel>() {
            protected void initChannel(Channel p_initChannel_1_) throws Exception {
                try {
                    p_initChannel_1_.config().setOption(ChannelOption.TCP_NODELAY, Boolean.TRUE);
                } catch (ChannelException var3) {
                }

                p_initChannel_1_.pipeline().addLast("timeout", new ReadTimeoutHandler(30)).addLast("splitter", new MessageDeserializer2()).addLast("decoder", new MessageDeserializer(EnumPacketDirection.CLIENTBOUND)).addLast("prepender", new MessageSerializer2()).addLast("encoder", new MessageSerializer(EnumPacketDirection.SERVERBOUND)).addLast("packet_handler", networkmanager);

                if (p_initChannel_1_ instanceof SocketChannel && ViaLoadingBase.getInstance().getTargetVersion().getVersion() != ViaMCP.NATIVE_VERSION) {
                    final UserConnection user = new UserConnectionImpl(p_initChannel_1_, true);
                    new ProtocolPipelineImpl(user);

                    Client.instance.networkManager.userConnection = user;

                    p_initChannel_1_.pipeline().addLast(new MCPVLBPipeline(user));
                }
            }
        }).channel(oclass).connect(address, serverPort).syncUninterruptibly();
        return networkmanager;
    }

    /**
     * Prepares a clientside NetworkManager: establishes a connection to the socket supplied and configures the channel
     * pipeline. Returns the newly created instance.
     */
    public static NetworkManager provideLocalClient(SocketAddress address) {
        final NetworkManager networkmanager = new NetworkManager(EnumPacketDirection.CLIENTBOUND);
        (new Bootstrap()).group(CLIENT_LOCAL_EVENTLOOP.getValue()).handler(new ChannelInitializer<Channel>() {
            protected void initChannel(Channel p_initChannel_1_) throws Exception {
                p_initChannel_1_.pipeline().addLast("packet_handler", networkmanager);
            }
        }).channel(LocalChannel.class).connect(address).syncUninterruptibly();
        return networkmanager;
    }

    public void channelActive(ChannelHandlerContext p_channelActive_1_) throws Exception {
        super.channelActive(p_channelActive_1_);
        this.channel = p_channelActive_1_.channel();
        this.socketAddress = this.channel.remoteAddress();

        try {
            this.setConnectionState(EnumConnectionState.HANDSHAKING);
        } catch (Throwable throwable) {
            logger.fatal(throwable);
        }
    }

    /**
     * Sets the new connection state and registers which packets this channel may send and receive
     */
    public void setConnectionState(EnumConnectionState newState) {
        this.channel.attr(attrKeyConnectionState).set(newState);
        this.channel.config().setAutoRead(true);
        logger.debug("Enabled auto read");
    }

    public void channelInactive(ChannelHandlerContext p_channelInactive_1_) throws Exception {
        this.closeChannel(new ChatComponentTranslation("disconnect.endOfStream"));
    }

    public void exceptionCaught(ChannelHandlerContext p_exceptionCaught_1_, Throwable p_exceptionCaught_2_) throws Exception {
        ChatComponentTranslation chatcomponenttranslation;

        if (p_exceptionCaught_2_ instanceof TimeoutException) {
            chatcomponenttranslation = new ChatComponentTranslation("disconnect.timeout");
        } else {
            chatcomponenttranslation = new ChatComponentTranslation("disconnect.genericReason", "Internal Exception: " + p_exceptionCaught_2_);
        }

        this.closeChannel(chatcomponenttranslation);
    }

    @Override
    protected void messageReceived(ChannelHandlerContext p_channelRead0_1_, Packet packet) {
        if (this.channel.isOpen()) {
            try {
                PacketEvent e = new PacketEvent(packet);
                e.setPacketType(PacketType.RECEIVE);
                Client.instance.eventBus.post(e);
                if (e.isCancelled()) return;
                packet.processPacket(this.packetListener);
            } catch (ThreadQuickExitException var4) {
            }
        }
    }

    public void sendPacket(Packet packetIn) {
        sendPacket(packetIn, true);
    }

    public void sendPacket(Packet<?> packetIn, boolean event) {
        if (this.isChannelOpen()) {
            if (event) {
                PacketEvent e = new PacketEvent(packetIn);
                e.setPacketType(PacketType.SEND);
                Client.instance.eventBus.post(e);
                if (e.isCancelled()) return;
            }

            this.flushOutboundQueue();
            this.dispatchPacket(packetIn, null);
        } else {
            this.readWriteLock.writeLock().lock();

            try {
                this.outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(packetIn, (GenericFutureListener[]) null));
            } finally {
                this.readWriteLock.writeLock().unlock();
            }
        }
    }

    public void sendPacket(Packet packetIn, GenericFutureListener<? extends Future<? super Void>> listener, GenericFutureListener<? extends Future<? super Void>>... listeners) {
        if (this.isChannelOpen()) {
            this.flushOutboundQueue();
            this.dispatchPacket(packetIn, ArrayUtils.add(listeners, 0, listener));
        } else {
            this.readWriteLock.writeLock().lock();

            try {
                this.outboundPacketsQueue.add(new NetworkManager.InboundHandlerTuplePacketListener(packetIn, ArrayUtils.add(listeners, 0, listener)));
            } finally {
                this.readWriteLock.writeLock().unlock();
            }
        }
    }

    /**
     * Will commit the packet to the channel. If the current thread 'owns' the channel it will write and flush the
     * packet, otherwise it will add a task for the channel eventloop thread to do that.
     */
    private void dispatchPacket(final Packet inPacket, final GenericFutureListener<? extends Future<? super Void>>[] futureListeners) {
        final EnumConnectionState enumconnectionstate = EnumConnectionState.getFromPacket(inPacket);
        final EnumConnectionState enumconnectionstate1 = this.channel.attr(attrKeyConnectionState).get();

        if (enumconnectionstate1 != enumconnectionstate) {
            logger.debug("Disabled auto read");
            this.channel.config().setAutoRead(false);
        }

        if (this.channel.eventLoop().inEventLoop()) {
            if (enumconnectionstate != enumconnectionstate1) {
                this.setConnectionState(enumconnectionstate);
            }

            ChannelFuture channelfuture = this.channel.writeAndFlush(inPacket);

            if (futureListeners != null) {
                channelfuture.addListeners(futureListeners);
            }

            channelfuture.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        } else {
            this.channel.eventLoop().execute(new Runnable() {
                public void run() {
                    if (enumconnectionstate != enumconnectionstate1) {
                        NetworkManager.this.setConnectionState(enumconnectionstate);
                    }

                    ChannelFuture channelfuture1 = NetworkManager.this.channel.writeAndFlush(inPacket);

                    if (futureListeners != null) {
                        channelfuture1.addListeners(futureListeners);
                    }

                    channelfuture1.addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
                }
            });
        }
    }

    /**
     * Will iterate through the outboundPacketQueue and dispatch all Packets
     */
    private void flushOutboundQueue() {
        if (this.channel != null && this.channel.isOpen()) {
            this.readWriteLock.readLock().lock();

            try {
                while (!this.outboundPacketsQueue.isEmpty()) {
                    NetworkManager.InboundHandlerTuplePacketListener networkmanager$inboundhandlertuplepacketlistener = this.outboundPacketsQueue.poll();
                    this.dispatchPacket(networkmanager$inboundhandlertuplepacketlistener.packet, networkmanager$inboundhandlertuplepacketlistener.futureListeners);
                }
            } finally {
                this.readWriteLock.readLock().unlock();
            }
        }
    }

    /**
     * Checks timeouts and processes all packets received
     */
    public void processReceivedPackets() {
        this.flushOutboundQueue();

        if (this.packetListener instanceof ITickable) {
            ((ITickable) this.packetListener).update();
        }

        this.channel.flush();
    }

    /**
     * Returns the socket address of the remote side. Server-only.
     */
    public SocketAddress getRemoteAddress() {
        return this.socketAddress;
    }

    /**
     * Closes the channel, the parameter can be used for an exit message (not certain how it gets sent)
     */
    public void closeChannel(IChatComponent message) {
        if (this.channel.isOpen()) {
            this.channel.close().awaitUninterruptibly();
            this.terminationReason = message;
        }
    }

    /**
     * True if this NetworkManager uses a memory connection (single player game). False may imply both an active TCP
     * connection or simply no active connection at all
     */
    public boolean isLocalChannel() {
        return this.channel instanceof LocalChannel || this.channel instanceof LocalServerChannel;
    }

    /**
     * Adds an encoder+decoder to the channel pipeline. The parameter is the secret key used for encrypted communication
     */
    public void enableEncryption(SecretKey key) {
        this.isEncrypted = true;
        this.channel.pipeline().addBefore("splitter", "decrypt", new NettyEncryptingDecoder(CryptManager.createNetCipherInstance(2, key)));
        this.channel.pipeline().addBefore("prepender", "encrypt", new NettyEncryptingEncoder(CryptManager.createNetCipherInstance(1, key)));
    }

    public boolean getIsencrypted() {
        return this.isEncrypted;
    }

    /**
     * Returns true if this NetworkManager has an active channel, false otherwise
     */
    public boolean isChannelOpen() {
        return this.channel != null && this.channel.isOpen();
    }

    public boolean hasNoChannel() {
        return this.channel == null;
    }

    /**
     * Gets the current handler for processing packets
     */
    public INetHandler getNetHandler() {
        return this.packetListener;
    }

    /**
     * Sets the NetHandler for this NetworkManager, no checks are made if this handler is suitable for the particular
     * connection state (protocol)
     */
    public void setNetHandler(INetHandler handler) {
        Validate.notNull(handler, "packetListener");
        logger.debug("Set listener of {} to {}", this, handler);
        this.packetListener = handler;
    }

    /**
     * If this channel is closed, returns the exit message, null otherwise.
     */
    public IChatComponent getExitMessage() {
        return this.terminationReason;
    }

    /**
     * Switches the channel to manual reading modus
     */
    public void disableAutoRead() {
        this.channel.config().setAutoRead(false);
    }

    public void setCompressionTreshold(int treshold) {
        if (treshold >= 0) {
            if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                ((NettyCompressionDecoder) this.channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
            } else {
                this.channel.pipeline().addBefore("decoder", "decompress", new NettyCompressionDecoder(treshold));
            }

            if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                ((NettyCompressionEncoder) this.channel.pipeline().get("decompress")).setCompressionTreshold(treshold);
            } else {
                this.channel.pipeline().addBefore("encoder", "compress", new NettyCompressionEncoder(treshold));
            }
        } else {
            if (this.channel.pipeline().get("decompress") instanceof NettyCompressionDecoder) {
                this.channel.pipeline().remove("decompress");
            }

            if (this.channel.pipeline().get("compress") instanceof NettyCompressionEncoder) {
                this.channel.pipeline().remove("compress");
            }
        }
    }

    public void checkDisconnected() {
        if (this.channel != null && !this.channel.isOpen()) {
            if (!this.disconnected) {
                this.disconnected = true;

                if (this.getExitMessage() != null) {
                    this.getNetHandler().onDisconnect(this.getExitMessage());
                } else if (this.getNetHandler() != null) {
                    this.getNetHandler().onDisconnect(new ChatComponentText("Disconnected"));
                }
            } else {
                logger.warn("handleDisconnection() called twice");
            }
        }
    }

    static class InboundHandlerTuplePacketListener {
        private final Packet packet;
        private final GenericFutureListener<? extends Future<? super Void>>[] futureListeners;

        public InboundHandlerTuplePacketListener(Packet inPacket, GenericFutureListener<? extends Future<? super Void>>... inFutureListeners) {
            this.packet = inPacket;
            this.futureListeners = inFutureListeners;
        }
    }
}
