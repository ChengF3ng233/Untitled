package net.minecraft.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTrapDoor extends Block {
    public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
    public static final PropertyBool OPEN = PropertyBool.create("open");
    public static final PropertyEnum<BlockTrapDoor.DoorHalf> HALF = PropertyEnum.create("half", BlockTrapDoor.DoorHalf.class);

    protected BlockTrapDoor(Material materialIn) {
        super(materialIn);
        this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(OPEN, Boolean.FALSE).withProperty(HALF, BlockTrapDoor.DoorHalf.BOTTOM));
        float f = 0.5F;
        float f1 = 1.0F;
        this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
        this.setCreativeTab(CreativeTabs.tabRedstone);
    }

    protected static EnumFacing getFacing(int meta) {
        return switch (meta & 3) {
            case 0 -> EnumFacing.NORTH;
            case 1 -> EnumFacing.SOUTH;
            case 2 -> EnumFacing.WEST;
            default -> EnumFacing.EAST;
        };
    }

    protected static int getMetaForFacing(EnumFacing facing) {
        return switch (facing) {
            case NORTH -> 0;
            case SOUTH -> 1;
            case WEST -> 2;
            default -> 3;
        };
    }

    private static boolean isValidSupportBlock(Block blockIn) {
        return blockIn.blockMaterial.isOpaque() && blockIn.isFullCube() || blockIn == Blocks.glowstone || blockIn instanceof BlockSlab || blockIn instanceof BlockStairs;
    }

    /**
     * Used to determine ambient occlusion and culling when rebuilding chunks for render
     */
    public boolean isOpaqueCube() {
        return false;
    }

    public boolean isFullCube() {
        return false;
    }

    public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
        return !worldIn.getBlockState(pos).getValue(OPEN);
    }

    public AxisAlignedBB getSelectedBoundingBox(World worldIn, BlockPos pos) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getSelectedBoundingBox(worldIn, pos);
    }

    public AxisAlignedBB getCollisionBoundingBox(World worldIn, BlockPos pos, IBlockState state) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.getCollisionBoundingBox(worldIn, pos, state);
    }

    public void setBlockBoundsBasedOnState(IBlockAccess worldIn, BlockPos pos) {
        this.setBounds(worldIn.getBlockState(pos));
    }

    /**
     * Sets the block's bounds for rendering it as an item
     */
    public void setBlockBoundsForItemRender() {
        float f = 0.1875F;
        this.setBlockBounds(0.0F, 0.40625F, 0.0F, 1.0F, 0.59375F, 1.0F);
    }

    public void setBounds(IBlockState state) {
        if (state.getBlock() == this) {
            boolean flag = state.getValue(HALF) == BlockTrapDoor.DoorHalf.TOP;
            Boolean obool = state.getValue(OPEN);
            EnumFacing enumfacing = state.getValue(FACING);
            float f = 0.1875F;

            if (flag) {
                this.setBlockBounds(0.0F, 0.8125F, 0.0F, 1.0F, 1.0F, 1.0F);
            } else {
                this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.1875F, 1.0F);
            }

            if (obool) {
                if (enumfacing == EnumFacing.NORTH) {
                    this.setBlockBounds(0.0F, 0.0F, 0.8125F, 1.0F, 1.0F, 1.0F);
                }

                if (enumfacing == EnumFacing.SOUTH) {
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.1875F);
                }

                if (enumfacing == EnumFacing.WEST) {
                    this.setBlockBounds(0.8125F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
                }

                if (enumfacing == EnumFacing.EAST) {
                    this.setBlockBounds(0.0F, 0.0F, 0.0F, 0.1875F, 1.0F, 1.0F);
                }
            }
        }
    }

    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
        if (this.blockMaterial == Material.iron) {
            return true;
        } else {
            state = state.cycleProperty(OPEN);
            worldIn.setBlockState(pos, state, 2);
            worldIn.playAuxSFXAtEntity(playerIn, state.getValue(OPEN) ? 1003 : 1006, pos, 0);
            return true;
        }
    }

    /**
     * Called when a neighboring block changes.
     */
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        if (!worldIn.isRemote) {
            BlockPos blockpos = pos.offset(state.getValue(FACING).getOpposite());

            if (!isValidSupportBlock(worldIn.getBlockState(blockpos).getBlock())) {
                worldIn.setBlockToAir(pos);
                this.dropBlockAsItem(worldIn, pos, state, 0);
            } else {
                boolean flag = worldIn.isBlockPowered(pos);

                if (flag || neighborBlock.canProvidePower()) {
                    boolean flag1 = state.getValue(OPEN);

                    if (flag1 != flag) {
                        worldIn.setBlockState(pos, state.withProperty(OPEN, flag), 2);
                        worldIn.playAuxSFXAtEntity(null, flag ? 1003 : 1006, pos, 0);
                    }
                }
            }
        }
    }

    /**
     * Ray traces through the blocks collision from start vector to end vector returning a ray trace hit.
     */
    public MovingObjectPosition collisionRayTrace(World worldIn, BlockPos pos, Vec3 start, Vec3 end) {
        this.setBlockBoundsBasedOnState(worldIn, pos);
        return super.collisionRayTrace(worldIn, pos, start, end);
    }

    /**
     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
     * IBlockstate
     */
    public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        IBlockState iblockstate = this.getDefaultState();

        if (facing.getAxis().isHorizontal()) {
            iblockstate = iblockstate.withProperty(FACING, facing).withProperty(OPEN, Boolean.FALSE);
            iblockstate = iblockstate.withProperty(HALF, hitY > 0.5F ? BlockTrapDoor.DoorHalf.TOP : BlockTrapDoor.DoorHalf.BOTTOM);
        }

        return iblockstate;
    }

    /**
     * Check whether this Block can be placed on the given side
     */
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side) {
        return !side.getAxis().isVertical() && isValidSupportBlock(worldIn.getBlockState(pos.offset(side.getOpposite())).getBlock());
    }

    public EnumWorldBlockLayer getBlockLayer() {
        return EnumWorldBlockLayer.CUTOUT;
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(OPEN, (meta & 4) != 0).withProperty(HALF, (meta & 8) == 0 ? BlockTrapDoor.DoorHalf.BOTTOM : BlockTrapDoor.DoorHalf.TOP);
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        int i = 0;
        i = i | getMetaForFacing(state.getValue(FACING));

        if (state.getValue(OPEN)) {
            i |= 4;
        }

        if (state.getValue(HALF) == BlockTrapDoor.DoorHalf.TOP) {
            i |= 8;
        }

        return i;
    }

    protected BlockState createBlockState() {
        return new BlockState(this, FACING, OPEN, HALF);
    }

    public enum DoorHalf implements IStringSerializable {
        TOP("top"),
        BOTTOM("bottom");

        private final String name;

        DoorHalf(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }

        public String getName() {
            return this.name;
        }
    }
}
