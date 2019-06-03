package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import tfcr.TFCR;
import tfcr.data.WoodType;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;
import tfcr.tileentity.TileEntityTree;

import javax.annotation.Nullable;

import static tfcr.blocks.BlockBranch.ROOT;

public class BlockTallSapling extends BlockDoublePlant implements ISelfRegisterBlock, ISelfRegisterItem {

    // TODO implement multiple wood type saplings
    private WoodType woodType;

    public BlockTallSapling() {
        super(Block.Properties.from(Blocks.TALL_GRASS));
        setRegistryName(TFCR.MODID, "tall_sapling");
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return state.get(HALF) == DoubleBlockHalf.LOWER;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
        return new TileEntityTree(this.woodType);
    }

    @Override
    public void onReplaced(IBlockState state, World worldIn, BlockPos pos, IBlockState newState, boolean isMoving) {
        if (newState.getBlock() instanceof BlockBranch) {
            if (!newState.get(ROOT)) {
                System.out.println("TallSapling replaced by BlockBranch. Setting root to true. Keeping TE.");
                worldIn.setBlockState(pos, newState.with(ROOT, true));
            } else {
                System.out.println("TallSapling replaced by BlockBranch. Keeping TileEntity.");
            }
            return;
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    private static final float SLOWDOWN = 0.05f;

    /**
     * Slow down entities passing through this block.
     */
    @Override
    public void onEntityCollision(IBlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        entityIn.motionX *= SLOWDOWN;
        entityIn.motionY *= SLOWDOWN;
        entityIn.motionZ *= SLOWDOWN;
    }
}
