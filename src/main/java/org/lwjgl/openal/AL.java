/*
 * Copyright LWJGL. All rights reserved.
 * License terms: https://www.lwjgl.org/license
 */
package org.lwjgl.openal;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.PointerBuffer;
import org.lwjgl.system.*;

import java.nio.IntBuffer;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.function.IntFunction;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.EXTThreadLocalContext.alcGetThreadContext;
import static org.lwjgl.system.APIUtil.*;
import static org.lwjgl.system.JNI.*;
import static org.lwjgl.system.MemoryStack.stackGet;
import static org.lwjgl.system.MemoryUtil.*;


public final class AL {

    @Nullable
    private static ALCapabilities processCaps;

    private static final ThreadLocal<ALCapabilities> capabilitiesTLS = new ThreadLocal<>();

    private static ICD icd = new ICDStatic();

    private static long _contextPtr;
    private static long _devicePtr;
    private static boolean _created;

    private AL() {}

    public static boolean isCreated() {
        return _created;
    }

    public static void create(String deviceArguments, int contextFrequency, int contextRefresh, boolean contextSynchronized) throws LWJGLException {
        create(deviceArguments, contextFrequency, contextRefresh, contextSynchronized, true);
    }

    public static void create(String deviceArguments, int contextFrequency, int contextRefresh, boolean contextSynchronized, boolean openDevice) throws LWJGLException {
        if (_created) {
            throw new IllegalStateException("Only one OpenAL context may be instantiated at any one time.");
        } else {
            init(deviceArguments, contextFrequency, contextRefresh, contextSynchronized, openDevice);
            _created = true;
        }
    }

    private static void init(String deviceArguments, int contextFrequency, int contextRefresh, boolean contextSynchronized, boolean openDevice) throws LWJGLException {
        try {
            if (openDevice) {
                _devicePtr = ALC10.alcOpenDevice(deviceArguments);
                if (_devicePtr == -1L) {
                    throw new LWJGLException("Could not open ALC device");
                }

                ALCCapabilities deviceCaps = ALC.createCapabilities(_devicePtr);
                if (contextFrequency == -1) {
                    _contextPtr = ALC10.alcCreateContext(_devicePtr, (IntBuffer)null);
                } else {
                    MemoryStack stack = MemoryStack.stackPush();
                    long var8 = _devicePtr;
                    int var10003 = contextSynchronized ? 1 : 0;
                    _contextPtr = ALC10.alcCreateContext(var8, createAttributeList(contextFrequency, contextRefresh, var10003, stack));
                    stack.close();
                }

                ALC10.alcMakeContextCurrent(_contextPtr);
                createCapabilities(deviceCaps);
            }

        } catch (LWJGLException var7) {
            destroy();
            throw var7;
        }
    }

    public static void create() throws LWJGLException {
        create(null, 44100, 60, false);
    }

    private static final IntBuffer createAttributeList(int contextFrequency, int contextRefresh, int contextSynchronized, MemoryStack stack) {
        IntBuffer buffer = stack.callocInt(7);
        buffer.put(0, 4103);
        buffer.put(1, contextFrequency);
        buffer.put(2, 4104);
        buffer.put(3, contextRefresh);
        buffer.put(4, 4105);
        buffer.put(5, contextSynchronized);
        buffer.put(6, 0);
        return buffer;
    }

    public static void destroy() {
        if (_contextPtr != -1L) {
            ALC10.alcMakeContextCurrent(0L);
            ALC10.alcDestroyContext(_contextPtr);
            _contextPtr = -1L;
        }

        if (_devicePtr != -1L) {
            ALC10.alcCloseDevice(_devicePtr);
            _devicePtr = -1L;
        }

        _created = false;
        al_destroy();
    }

    static void init() {
    }

    static void al_destroy() {
        setCurrentProcess(null);
    }

    
    public static void setCurrentProcess(@Nullable ALCapabilities caps) {
        processCaps = caps;
        capabilitiesTLS.set(null); 
        icd.set(caps);
    }

    
    public static void setCurrentThread(@Nullable ALCapabilities caps) {
        capabilitiesTLS.set(caps);
        icd.set(caps);
    }

    
    public static ALCapabilities getCapabilities() {
        ALCapabilities caps = capabilitiesTLS.get();
        if (caps == null) {
            caps = processCaps;
        }

        return checkCapabilities(caps);
    }

    private static ALCapabilities checkCapabilities(@Nullable ALCapabilities caps) {
        if (caps == null) {
            throw new IllegalStateException(
                "No ALCapabilities instance set for the current thread or process. Possible solutions:\n" +
                "\ta) Call AL.createCapabilities() after making a context current.\n" +
                "\tb) Call AL.setCurrentProcess() or AL.setCurrentThread() if an ALCapabilities instance already exists."
            );
        }
        return caps;
    }

    
    public static ALCapabilities createCapabilities(ALCCapabilities alcCaps) {
        return createCapabilities(alcCaps, null);
    }

    
    public static ALCapabilities createCapabilities(ALCCapabilities alcCaps, @Nullable IntFunction<PointerBuffer> bufferFactory) {
        
        
        
        long alGetProcAddress = ALC.getFunctionProvider().getFunctionAddress(NULL, "alGetProcAddress");
        if (alGetProcAddress == NULL) {
            throw new RuntimeException("A core AL function is missing. Make sure that the OpenAL library has been loaded correctly.");
        }

        FunctionProvider functionProvider = functionName -> {
            long address = invokePP(memAddress(functionName), alGetProcAddress);
            if (address == NULL && Checks.DEBUG_FUNCTIONS) {
                apiLogMissing("AL", functionName);
            }
            return address;
        };

        long GetString          = functionProvider.getFunctionAddress("alGetString");
        long GetError           = functionProvider.getFunctionAddress("alGetError");
        long IsExtensionPresent = functionProvider.getFunctionAddress("alIsExtensionPresent");
        if (GetString == NULL || GetError == NULL || IsExtensionPresent == NULL) {
            throw new IllegalStateException("Core OpenAL functions could not be found. Make sure that the OpenAL library has been loaded correctly.");
        }

        String versionString = memASCIISafe(invokeP(AL_VERSION, GetString));
        if (versionString == null || invokeI(GetError) != AL_NO_ERROR) {
            throw new IllegalStateException("There is no OpenAL context current in the current thread or process.");
        }

        APIVersion apiVersion = apiParseVersion(versionString);

        int majorVersion = apiVersion.major;
        int minorVersion = apiVersion.minor;

        int[][] AL_VERSIONS = {
            {0, 1}  
        };

        Set<String> supportedExtensions = new HashSet<>(32);

        for (int major = 1; major <= AL_VERSIONS.length; major++) {
            int[] minors = AL_VERSIONS[major - 1];
            for (int minor : minors) {
                if (major < majorVersion || (major == majorVersion && minor <= minorVersion)) {
                    supportedExtensions.add("OpenAL" + major + minor);
                }
            }
        }

        
        String extensionsString = memASCIISafe(invokeP(AL_EXTENSIONS, GetString));
        if (extensionsString != null) {
            MemoryStack stack = stackGet();

            StringTokenizer tokenizer = new StringTokenizer(extensionsString);
            while (tokenizer.hasMoreTokens()) {
                String extName = tokenizer.nextToken();
                try (MemoryStack frame = stack.push()) {
                    if (invokePZ(memAddress(frame.ASCII(extName, true)), IsExtensionPresent)) {
                        supportedExtensions.add(extName);
                    }
                }
            }
        }

        if (alcCaps.ALC_EXT_EFX) {
            supportedExtensions.add("ALC_EXT_EFX");
        }
        apiFilterExtensions(supportedExtensions, Configuration.OPENAL_EXTENSION_FILTER);

        ALCapabilities caps = new ALCapabilities(functionProvider, supportedExtensions, bufferFactory == null ? BufferUtils::createPointerBuffer : bufferFactory);

        if (alcCaps.ALC_EXT_thread_local_context && alcGetThreadContext() != NULL) {
            setCurrentThread(caps);
        } else {
            setCurrentProcess(caps);
        }

        return caps;
    }

    static ALCapabilities getICD() {
        return ALC.check(icd.get());
    }

    
    private interface ICD {
        default void set(@Nullable ALCapabilities caps) {}
        @Nullable ALCapabilities get();
    }

    
    private static class ICDStatic implements ICD {

        @Nullable
        private static ALCapabilities tempCaps;

        @SuppressWarnings("AssignmentToStaticFieldFromInstanceMethod")
        @Override
        public void set(@Nullable ALCapabilities caps) {
            if (tempCaps == null) {
                tempCaps = caps;
            } else if (caps != null && caps != tempCaps && ThreadLocalUtil.areCapabilitiesDifferent(tempCaps.addresses, caps.addresses)) {
                apiLog("[WARNING] Incompatible context detected. Falling back to thread/process lookup for AL contexts.");
                icd = AL::getCapabilities; 
            }
        }

        @Override
        public ALCapabilities get() {
            return WriteOnce.caps;
        }

        private static final class WriteOnce {
            
            static final ALCapabilities caps;

            static {
                ALCapabilities tempCaps = ICDStatic.tempCaps;
                if (tempCaps == null) {
                    throw new IllegalStateException("No ALCapabilities instance has been set");
                }
                caps = tempCaps;
            }
        }

    }

}
