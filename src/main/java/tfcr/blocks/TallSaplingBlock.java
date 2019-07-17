package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.entity.Entity;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import tfcr.TFCR;
import tfcr.data.WoodType;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;
import tfcr.tileentity.TreeTileEntity;

import javax.annotation.Nullable;

import static tfcr.blocks.BranchBlock.ROOT;

public class TallSaplingBlock extends DoublePlantBlock implements ISelfRegisterBlock, ISelfRegisterItem {

    // TODO implement multiple wood type saplings
    private WoodType woodType;

    public TallSaplingBlock() {
        super(Block.Properties.from(Blocks.TALL_GRASS));
        setRegistryName(TFCR.MODID, "tall_sapling");
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(HALF) == DoubleBlockHalf.LOWER;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new TreeTileEntity(this.woodType);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (newState.getBlock() instanceof BranchBlock) {
            if (!newState.get(ROOT)) {
                System.out.println("TallSapling replaced by BranchBlock. Setting root to true. Keeping TE.");
                worldIn.setBlockState(pos, newState.with(ROOT, true));
            } else {
                System.out.println("TallSapling replaced by BranchBlock. Keeping TileEntity.");
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
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        entityIn.setMotion(entityIn.getMotion().mul(SLOWDOWN, 1.0D, SLOWDOWN));
    }
}
