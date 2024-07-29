package net.minecraft.block;

import com.google.common.collect.Lists;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class BlockSponge extends Block {
    public static final PropertyBool WET = PropertyBool.create("wet");

    protected BlockSponge() {
        super(Material.sponge);
        this.setDefaultState(this.blockState.getBaseState().withProperty(WET, Boolean.valueOf(false)));
        this.setCreativeTab(CreativeTabs.tabBlock);
    }

    /**
     * Gets the localized name of this block. Used for the statistics page.
     */
    public String getLocalizedName() {
        return StatCollector.translateToLocal(this.getUnlocalizedName() + ".dry.name");
    }

    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    public int damageDropped(IBlockState state) {
        return state.getValue(WET).booleanValue() ? 1 : 0;
    }

    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        this.tryAbsorb(worldIn, pos, state);
    }

    /**
     * Called when a neighboring block changes.
     */
    public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
        this.tryAbsorb(worldIn, pos, state);
        super.onNeighborBlockChange(worldIn, pos, state, neighborBlock);
    }

    protected void tryAbsorb(World worldIn, BlockPos pos, IBlockState state) {
        if (!state.getValue(WET).booleanValue() && this.absorb(worldIn, pos)) {
            worldIn.setBlockState(pos, state.withProperty(WET, Boolean.valueOf(true)), 2);
            worldIn.playAuxSFX(2001, pos, Block.getIdFromBlock(Blocks.water));
        }
    }

    private boolean absorb(World worldIn, BlockPos pos) {
        Queue<Tuple<BlockPos, Integer>> queue = Lists.newLinkedList();
        ArrayList<BlockPos> arraylist = Lists.newArrayList();
        queue.add(new Tuple(pos, Integer.valueOf(0)));
        int i = 0;

        while (!queue.isEmpty()) {
            Tuple<BlockPos, Integer> tuple = queue.poll();
            BlockPos blockpos = tuple.getFirst();
            int j = tuple.getSecond().intValue();

            for (EnumFacing enumfacing : EnumFacing.values()) {
                BlockPos blockpos1 = blockpos.offset(enumfacing);

                if (worldIn.getBlockState(blockpos1).getBlock().getMaterial() == Material.water) {
                    worldIn.setBlockState(blockpos1, Blocks.air.getDefaultState(), 2);
                    arraylist.add(blockpos1);
                    ++i;

                    if (j < 6) {
                        queue.add(new Tuple(blockpos1, Integer.valueOf(j + 1)));
                    }
                }
            }

            if (i > 64) {
                break;
            }
        }

        for (BlockPos blockpos2 : arraylist) {
            worldIn.notifyNeighborsOfStateChange(blockpos2, Blocks.air);
        }

        return i > 0;
    }

    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    public void getSubBlocks(Item itemIn, CreativeTabs tab, List<ItemStack> list) {
        list.add(new ItemStack(itemIn, 1, 0));
        list.add(new ItemStack(itemIn, 1, 1));
    }

    /**
     * Convert the given metadata into a BlockState for this Block
     */
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(WET, Boolean.valueOf((meta & 1) == 1));
    }

    /**
     * Convert the BlockState into the correct metadata value
     */
    public int getMetaFromState(IBlockState state) {
        return state.getValue(WET).booleanValue() ? 1 : 0;
    }

    protected BlockState createBlockState() {
        return new BlockState(this, WET);
    }

    public void randomDisplayTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (state.getValue(WET).booleanValue()) {
            EnumFacing enumfacing = EnumFacing.random(rand);

            if (enumfacing != EnumFacing.UP && !World.doesBlockHaveSolidTopSurface(worldIn, pos.offset(enumfacing))) {
                double d0 = pos.getX();
                double d1 = pos.getY();
                double d2 = pos.getZ();

                if (enumfacing == EnumFacing.DOWN) {
                    d1 = d1 - 0.05D;
                    d0 += rand.nextDouble();
                    d2 += rand.nextDouble();
                } else {
                    d1 = d1 + rand.nextDouble() * 0.8D;

                    if (enumfacing.getAxis() == EnumFacing.Axis.X) {
                        d2 += rand.nextDouble();

                        if (enumfacing == EnumFacing.EAST) {
                            ++d0;
                        } else {
                            d0 += 0.05D;
                        }
                    } else {
                        d0 += rand.nextDouble();

                        if (enumfacing == EnumFacing.SOUTH) {
                            ++d2;
                        } else {
                            d2 += 0.05D;
                        }
                    }
                }

                worldIn.spawnParticle(EnumParticleTypes.DRIP_WATER, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }
}
