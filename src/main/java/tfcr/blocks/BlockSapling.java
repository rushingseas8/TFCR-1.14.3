package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import tfcr.TFCR;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;
import tfcr.init.ModBlocks;
import tfcr.init.ModItems;
import tfcr.tileentity.TileEntityTree;

import javax.annotation.Nullable;

public class BlockSapling extends BlockBush implements ISelfRegisterBlock, ISelfRegisterItem {

    // TODO carry wood type as field

    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 5);

    public BlockSapling() {
        super(Block.Properties.from(Blocks.OAK_SAPLING));

        this.setRegistryName(TFCR.MODID, "sapling");
    }

    /**
     * Fills out the possible states for this Block.
     *
     * Needed to setup blockstates for this block. Replaces metadata in <=1.12.
     * @param builder
     */
    @Override
    public void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(AGE);
    }

    /**
     * All TFCR 1-tall saplings have a TileEntity.
     * @param state
     * @return
     */
    @Override
    public boolean hasTileEntity(IBlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
        return new TileEntityTree();
    }

    @Override
    public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving) {
        if (!worldIn.isRemote) {
            return;
        }
        System.out.println("onReplaced for Sapling. Remote true.");
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public void getDrops(IBlockState state, NonNullList<ItemStack> drops, World world, BlockPos pos, int fortune) {
        int age = state.get(AGE);
        drops.clear();
        switch (age) {
            case 0: drops.add(new ItemStack(this, 1)); break;
            case 1: drops.add(new ItemStack(ModBlocks.block_branch_8, 1)); break;
            default: break;
        }
        System.out.println(drops);
    }

    @Override
    public float getBlockHardness(IBlockState blockState, IBlockReader worldIn, BlockPos pos) {
        return super.getBlockHardness(blockState, worldIn, pos);
    }
}
