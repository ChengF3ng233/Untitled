package cn.feng.untitled.util.player;

import cn.feng.untitled.event.impl.MotionEvent;
import cn.feng.untitled.util.MinecraftInstance;
import com.google.common.collect.Multimap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.*;
import org.lwjgl.util.vector.Vector3f;

import java.util.*;


/**
 * @author ChengFeng
 * @since 2024/8/7
 **/
public class PlayerUtil extends MinecraftInstance {
    private static final Minecraft MC = Minecraft.getMinecraft();
    public static final Map<String, Boolean> serverResponses = new HashMap<>();

    public static boolean isAirUnder(Entity ent) {
        return mc.theWorld.getBlockState(new BlockPos(ent.posX, ent.posY - 1, ent.posZ)).getBlock() == Blocks.air;
    }
    /**
     * Checks if there is a block under the player
     *
     * @return block under
     */
    public boolean isBlockUnder(final double height) {
        return isBlockUnder(height, true);
    }

    public static boolean isBlockUnder(final double height, final boolean boundingBox) {
        if (boundingBox) {
            for (int offset = 0; offset < height; offset += 2) {
                final AxisAlignedBB bb = mc.thePlayer.getEntityBoundingBox().offset(0, -offset, 0);

                if (!mc.theWorld.getCollidingBoundingBoxes(mc.thePlayer, bb).isEmpty()) {
                    return true;
                }
            }
        } else {
            for (int offset = 0; offset < height; offset++) {
                if (PlayerUtil.blockRelativeToPlayer(0, -offset, 0).isFullBlock()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isBlockUnder() {
        return isBlockUnder(mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
    }

    /**
     * Gets the block relative to the player from the offset
     *
     * @return block relative to the player
     */
    public static Block blockRelativeToPlayer(final double offsetX, final double offsetY, final double offsetZ) {
        return mc.theWorld.getBlockState(new BlockPos(mc.thePlayer).add(offsetX, offsetY, offsetZ)).getBlock();
    }

    public Block blockAheadOfPlayer(final double offsetXZ, final double offsetY) {
        return blockRelativeToPlayer(-Math.sin(MoveUtil.direction()) * offsetXZ, offsetY, Math.cos(MoveUtil.direction()) * offsetXZ);
    }


    public static boolean isHoldingSword() {
        return mc.thePlayer.getCurrentEquippedItem() != null
                && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword;
    }

    public static double getBaseMoveSpeed() {
        double baseSpeed = 0.2873;
        if (Minecraft.getMinecraft().thePlayer.isPotionActive(Potion.moveSpeed)) {
            int amplifier = Minecraft.getMinecraft().thePlayer.getActivePotionEffect(Potion.moveSpeed).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }
    public static boolean inLiquid() {
        return mc.thePlayer.isInWater() || mc.thePlayer.isInLava();
    }


    public static float getMaxFallDist() {
        PotionEffect potioneffect = PlayerUtil.mc.thePlayer.getActivePotionEffect(Potion.jump);
        int f = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0;
        return PlayerUtil.mc.thePlayer.getMaxFallHeight() + f;
    }

    public static float getDirection() {
        float yaw = PlayerUtil.mc.thePlayer.rotationYaw;
        if (PlayerUtil.mc.thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (PlayerUtil.mc.thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (PlayerUtil.mc.thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (PlayerUtil.mc.thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (PlayerUtil.mc.thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        yaw *= 0.017453292f;
        return yaw;
    }

    public static boolean isInWater() {
        return PlayerUtil.mc.theWorld.getBlockState(
                        new BlockPos(PlayerUtil.mc.thePlayer.posX, PlayerUtil.mc.thePlayer.posY, PlayerUtil.mc.thePlayer.posZ))
                .getBlock().getMaterial() == Material.water;
    }

    public static boolean isInLiquid() {
        if (mc.thePlayer.isInWater()) {
            return true;
        }
        boolean inLiquid = false;
        final int y = (int) mc.thePlayer.getEntityBoundingBox().minY;
        for (int x = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minX); x < MathHelper
                .floor_double(mc.thePlayer.getEntityBoundingBox().maxX) + 1; x++) {
            for (int z = MathHelper.floor_double(mc.thePlayer.getEntityBoundingBox().minZ); z < MathHelper
                    .floor_double(mc.thePlayer.getEntityBoundingBox().maxZ) + 1; z++) {
                final Block block = mc.theWorld.getBlockState(new BlockPos(x, y, z)).getBlock();
                if (block != null && block.getMaterial() != Material.air) {
                    if (!(block instanceof BlockLiquid))
                        return false;
                    inLiquid = true;
                }
            }
        }
        return inLiquid;
    }

    public static void toFwd(final double speed) {
        final float yaw = PlayerUtil.mc.thePlayer.rotationYaw * 0.017453292f;
        final EntityPlayerSP thePlayer = PlayerUtil.mc.thePlayer;
        thePlayer.motionX -= MathHelper.sin(yaw) * speed;
        final EntityPlayerSP thePlayer2 = PlayerUtil.mc.thePlayer;
        thePlayer2.motionZ += MathHelper.cos(yaw) * speed;
    }

    public static double getSpeed() {
        return Math.sqrt(Minecraft.getMinecraft().thePlayer.motionX * Minecraft.getMinecraft().thePlayer.motionX
                + Minecraft.getMinecraft().thePlayer.motionZ * Minecraft.getMinecraft().thePlayer.motionZ);
    }
    public static void setSpeedWithoutEvent(double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward,boolean chase) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if(pseudoForward == 0.0D && pseudoStrafe == 0.0D) {
            mc.thePlayer.motionX = (0.0D);
            mc.thePlayer.motionZ = (0.0D);
        } else {
            if(pseudoForward != 0.0D) {
                if (!chase) {
                    if (pseudoStrafe > 0.0D) {
                        yaw = pseudoYaw + (float) (pseudoForward > 0.0D ? -45 : 45);
                    } else if (pseudoStrafe < 0.0D) {
                        yaw = pseudoYaw + (float) (pseudoForward > 0.0D ? 45 : -45);
                    }
                }
                strafe = 0.0D;
                if(pseudoForward > 0.0D) {
                    forward = 1.0D;
                } else if(pseudoForward < 0.0D) {
                    forward = -1.0D;
                }
            }

            double cos = Math.cos(Math.toRadians(yaw + 90.0F));
            double sin = Math.sin(Math.toRadians(yaw + 90.0F));
            mc.thePlayer.motionX = (forward * moveSpeed * cos + strafe * moveSpeed * sin);
            mc.thePlayer.motionZ = (forward * moveSpeed * sin - strafe * moveSpeed * cos);
        }
    }
    public static void setSpeed(MotionEvent moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward, boolean chase) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if(pseudoForward == 0.0D && pseudoStrafe == 0.0D) {
            moveEvent.z = 0.0D;
            moveEvent.x = 0.0D;
        } else {
            if(pseudoForward != 0.0D) {
                if (!chase) {
                    if (pseudoStrafe > 0.0D) {
                        yaw = pseudoYaw + (float) (pseudoForward > 0.0D ? -44 : 44);
                    } else if (pseudoStrafe < 0.0D) {
                        yaw = pseudoYaw + (float) (pseudoForward > 0.0D ? 44 : -44);
                    }
                }
                strafe = 0.0D;
                if(pseudoForward > 0.0D) {
                    forward = 1.0D;
                } else if(pseudoForward < 0.0D) {
                    forward = -1.0D;
                }
            }

            double cos = Math.cos(Math.toRadians(yaw + 90.0F));
            double sin = Math.sin(Math.toRadians(yaw + 90.0F));
            moveEvent.x = forward * moveSpeed * cos + strafe * moveSpeed * sin;
            moveEvent.z = forward * moveSpeed * sin - strafe * moveSpeed * cos;
        }
    }


    public static void setSpeed(final double speed) {
        PlayerUtil.mc.thePlayer.motionX = -(Math.sin(getDirection()) * speed);
        PlayerUtil.mc.thePlayer.motionZ = Math.cos(getDirection()) * speed;
    }

    public static Block getBlockUnderPlayer(final EntityPlayer inPlayer) {
        return getBlock(new BlockPos(inPlayer.posX, inPlayer.posY - 1.0, inPlayer.posZ));
    }

    public static Block getBlock(final BlockPos pos) {
        return Minecraft.getMinecraft().theWorld.getBlockState(pos).getBlock();
    }

    public static Block getBlockAtPosC(final EntityPlayer inPlayer, final double x, final double y, final double z) {
        return getBlock(new BlockPos(inPlayer.posX - x, inPlayer.posY - y, inPlayer.posZ - z));
    }

    public static ArrayList<Vector3f> vanillaTeleportPositions(final double tpX, final double tpY, final double tpZ,
                                                               final double speed) {
        final ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
        final Minecraft mc = Minecraft.getMinecraft();
        final double posX = tpX - mc.thePlayer.posX;
        final double posY = tpY - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight() + 1.1);
        final double posZ = tpZ - mc.thePlayer.posZ;
        final float yaw = (float) (Math.atan2(posZ, posX) * 180.0 / 3.141592653589793 - 90.0);
        final float pitch = (float) (-Math.atan2(posY, Math.sqrt(posX * posX + posZ * posZ)) * 180.0
                / 3.141592653589793);
        double tmpX = mc.thePlayer.posX;
        double tmpY = mc.thePlayer.posY;
        double tmpZ = mc.thePlayer.posZ;
        double steps = 1.0;
        for (double d = speed; d < getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY,
                tpZ); d += speed) {
            ++steps;
        }
        for (double d = speed; d < getDistance(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, tpX, tpY,
                tpZ); d += speed) {
            tmpX = mc.thePlayer.posX - Math.sin(getDirection(yaw)) * d;
            tmpZ = mc.thePlayer.posZ + Math.cos(getDirection(yaw)) * d;
            tmpY -= (mc.thePlayer.posY - tpY) / steps;
            positions.add(new Vector3f((float) tmpX, (float) tmpY, (float) tmpZ));
        }
        positions.add(new Vector3f((float) tpX, (float) tpY, (float) tpZ));
        return positions;
    }

    public static float getDirection(float yaw) {
        if (Minecraft.getMinecraft().thePlayer.moveForward < 0.0f) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (Minecraft.getMinecraft().thePlayer.moveForward < 0.0f) {
            forward = -0.5f;
        } else if (Minecraft.getMinecraft().thePlayer.moveForward > 0.0f) {
            forward = 0.5f;
        }
        if (Minecraft.getMinecraft().thePlayer.moveStrafing > 0.0f) {
            yaw -= 90.0f * forward;
        }
        if (Minecraft.getMinecraft().thePlayer.moveStrafing < 0.0f) {
            yaw += 90.0f * forward;
        }
        yaw *= 0.017453292f;
        return yaw;
    }

    public static double getDistance(final double x1, final double y1, final double z1, final double x2,
                                     final double y2, final double z2) {
        final double d0 = x1 - x2;
        final double d2 = y1 - y2;
        final double d3 = z1 - z2;
        return MathHelper.sqrt_double(d0 * d0 + d2 * d2 + d3 * d3);
    }

    public static boolean MovementInput() {
        return mc.gameSettings.keyBindForward.isPressed()
                || mc.gameSettings.keyBindLeft.isPressed()
                || mc.gameSettings.keyBindRight.isPressed()
                || mc.gameSettings.keyBindBack.isPressed();
    }

    public static void blockHit(Entity en, boolean value) {
        Minecraft mc = Minecraft.getMinecraft();
        ItemStack stack = mc.thePlayer.getCurrentEquippedItem();

        if (mc.thePlayer.getCurrentEquippedItem() != null && en != null && value) {
            if (stack.getItem() instanceof ItemSword && mc.thePlayer.swingProgress > 0.2) {
                mc.thePlayer.getCurrentEquippedItem().useItemRightClick(mc.theWorld, mc.thePlayer);
            }
        }
    }

    public static float getItemAtkDamage(ItemStack itemStack) {
        final Multimap multimap = itemStack.getAttributeModifiers();
        if (!multimap.isEmpty()) {
            final Iterator iterator = multimap.entries().iterator();
            if (iterator.hasNext()) {
                final Map.Entry entry = (Map.Entry) iterator.next();
                final AttributeModifier attributeModifier = (AttributeModifier) entry.getValue();
                double damage = attributeModifier.getOperation() != 1 && attributeModifier.getOperation() != 2
                        ? attributeModifier.getAmount()
                        : attributeModifier.getAmount() * 100.0;

                if (attributeModifier.getAmount() > 1.0) {
                    return 1.0f + (float) damage;
                }
                return 1.0f;
            }
        }
        return 1.0f;
    }

    public static List<EntityLivingBase> getLivingEntities() {
        return Arrays.asList(
                Minecraft.getMinecraft().theWorld.loadedEntityList.stream()
                        .filter(entity -> entity instanceof EntityLivingBase)
                        .filter(entity -> entity != Minecraft.getMinecraft().thePlayer)
                        .map(entity -> (EntityLivingBase) entity)
                        .toArray(EntityLivingBase[]::new)
        );
    }

    public static void shiftClick(Item i) {
        for (int i1 = 9; i1 < 37; ++i1) {
            ItemStack itemstack = mc.thePlayer.inventoryContainer.getSlot(i1).getStack();

            if (itemstack != null && itemstack.getItem() == i) {
                mc.playerController.windowClick(0, i1, 0, 1, mc.thePlayer);
                break;
            }
        }
    }

    public static boolean hotbarIsFull() {
        for (int i = 0; i <= 36; ++i) {
            ItemStack itemstack = mc.thePlayer.inventory.getStackInSlot(i);

            if (itemstack == null) {
                return false;
            }
        }

        return true;
    }

    public static void tellPlayer(String string) {
        if (string != null && mc.thePlayer != null)
            mc.thePlayer.addChatMessage(new ChatComponentText("\247b[Hanabi] ��r " + string));
    }

    public static boolean isMoving() {
        if ((!mc.thePlayer.isCollidedHorizontally) && (!mc.thePlayer.isSneaking())) {
            return ((mc.thePlayer.movementInput.moveForward != 0.0F || mc.thePlayer.movementInput.moveStrafe != 0.0F));
        }
        return false;
    }

    public static boolean isMoving2() {
        return mc.thePlayer != null && ((mc.thePlayer.moveForward != 0.0F || mc.thePlayer.moveStrafing != 0.0F));
    }

    public static void blinkToPos(double[] startPos, BlockPos endPos, double slack, double[] pOffset) {
        double curX = startPos[0];
        double curY = startPos[1];
        double curZ = startPos[2];
        double endX = (double) endPos.getX() + 0.5D;
        double endY = (double) endPos.getY() + 1.0D;
        double endZ = (double) endPos.getZ() + 0.5D;
        double distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);

        for (int count = 0; distance > slack; ++count) {
            distance = Math.abs(curX - endX) + Math.abs(curY - endY) + Math.abs(curZ - endZ);
            if (count > 120) {
                break;
            }

            boolean next = false;
            double diffX = curX - endX;
            double diffY = curY - endY;
            double diffZ = curZ - endZ;
            double offset = (count & 1) == 0 ? pOffset[0] : pOffset[1];
            if (diffX < 0.0D) {
                if (Math.abs(diffX) > offset) {
                    curX += offset;
                } else {
                    curX += Math.abs(diffX);
                }
            }

            if (diffX > 0.0D) {
                if (Math.abs(diffX) > offset) {
                    curX -= offset;
                } else {
                    curX -= Math.abs(diffX);
                }
            }

            if (diffY < 0.0D) {
                if (Math.abs(diffY) > 0.25D) {
                    curY += 0.25D;
                } else {
                    curY += Math.abs(diffY);
                }
            }

            if (diffY > 0.0D) {
                if (Math.abs(diffY) > 0.25D) {
                    curY -= 0.25D;
                } else {
                    curY -= Math.abs(diffY);
                }
            }

            if (diffZ < 0.0D) {
                if (Math.abs(diffZ) > offset) {
                    curZ += offset;
                } else {
                    curZ += Math.abs(diffZ);
                }
            }

            if (diffZ > 0.0D) {
                if (Math.abs(diffZ) > offset) {
                    curZ -= offset;
                } else {
                    curZ -= Math.abs(diffZ);
                }
            }

            Minecraft.getMinecraft().getNetHandler()
                    .addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(curX, curY, curZ, true));
        }

    }

    public static void damage(int damage) {
        for (int index = 0; index <= 67 + (23 * (damage - 1)); ++index) {
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX, mc.thePlayer.posY + 2.535E-9D, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX, mc.thePlayer.posY + 1.05E-10D, mc.thePlayer.posZ, false));
            mc.thePlayer.sendQueue.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(
                    mc.thePlayer.posX, mc.thePlayer.posY + 0.0448865D, mc.thePlayer.posZ, false));
        }
    }

    public static List<AxisAlignedBB> getCollidingBoundingList(EntityPlayerSP thePlayer, float f) {
        return mc.theWorld.getCollidingBoundingBoxes(thePlayer,
                thePlayer.getEntityBoundingBox().offset(0.0D, -f, 0.0D));

    }

    public static final Block getBlockBelowEntity(Entity entity, double offset) {
        final Vec3 below = entity.getPositionVector();
        return MC.theWorld.getBlockState(new BlockPos(below).add(0, -offset, 0)).getBlock();
    }

    public static final Block getBlockBelowEntity(Entity entity) {
        return getBlockBelowEntity(entity, 1);
    }

    public static final Block getBlockBelowPlayer() {
        return getBlockBelowEntity(MC.thePlayer);
    }

    public static void setSpeed(double speed, float yaw, double strafe, double forward) {
        if (forward == 0 && strafe == 0) {
            mc.thePlayer.motionX = 0;
            mc.thePlayer.motionZ = 0;
        } else {
            if (forward != 0) {
                if (strafe > 0) {
                    yaw += (forward > 0 ? -45 : 45);
                } else if (strafe < 0) {
                    yaw += (forward > 0 ? 45 : -45);
                }
                strafe = 0;
                if (forward > 0) {
                    forward = 1;
                } else {
                    forward = -1;
                }
            }
            mc.thePlayer.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90))
                    + strafe * speed * Math.sin(Math.toRadians(yaw + 90));
            mc.thePlayer.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90))
                    - strafe * speed * Math.cos(Math.toRadians(yaw + 90));
        }
    }

    public static double getLastDist() {
        double xDist = mc.thePlayer.posX - mc.thePlayer.prevPosX;
        double zDist = mc.thePlayer.posZ - mc.thePlayer.prevPosZ;
        return Math.sqrt(xDist * xDist + zDist * zDist);
    }

    public void portMove(float yaw, float multiplyer, float up) {
        double moveX = -Math.sin(Math.toRadians(yaw)) * (double) multiplyer;
        double moveZ = Math.cos(Math.toRadians(yaw)) * (double) multiplyer;
        double moveY = up;
        mc.thePlayer.setPosition(moveX + mc.thePlayer.posX, moveY + mc.thePlayer.posY,
                moveZ + mc.thePlayer.posZ);
    }

    public final Block getBlockBelowPlayer(double offset) {
        return getBlockBelowEntity(MC.thePlayer, offset);
    }

    public final boolean isTeamMate(EntityLivingBase entity) {
        if (!(entity instanceof EntityPlayer))
            return false;
        if (MC.thePlayer.getTeam() != null && entity.getTeam() != null) {
            if (MC.thePlayer.isOnSameTeam(entity)) {
                return true;
            }
        }

        if (MC.thePlayer.getDisplayName() != null && entity.getDisplayName() != null) {
            final String playerName = MC.thePlayer.getDisplayName().getFormattedText().replace("�r", "");
            final String entityName = entity.getDisplayName().getFormattedText().replace("�r", "");
            if (playerName.isEmpty() || entityName.isEmpty())
                return false;
            return playerName.charAt(1) == entityName.charAt(1);
        }

        return false;
    }
}
