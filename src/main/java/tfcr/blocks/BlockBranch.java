package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import tfcr.TFCR;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;

public class BlockBranch extends Block implements ISelfRegisterBlock, ISelfRegisterItem {

    public static final EnumProperty<EnumFacing.Axis> AXIS = BlockStateProperties.AXIS;

    private boolean hasTileEntity;

    private int diameter;
    // TODO carry wood type as field

    // TODO add all different wood types, instead of just placeholder ash variant
    public BlockBranch(int diameter) {
        super(Block.Properties.from(Blocks.OAK_WOOD));

        this.diameter = diameter;

        this.setRegistryName(TFCR.MODID, "branch/block_branch_" + diameter);
        this.setDefaultState(this.stateContainer.getBaseState()
            .with(AXIS, EnumFacing.Axis.Y)
        );
    }

    /**
     * The shape this block takes up. Displayed as the outline around it.
     * @param state The IBlockState for this block. Used to get Axis info.
     * @param worldIn Unused.
     * @param pos Unused.
     * @return The Voxel bounding box of this shape.
     */
    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        //int diameter = state.get(DIAMETER).diameter;
        int radius = diameter / 2;

        int min = 8 - radius;
        int max = 8 + radius;

        switch(state.get(AXIS)) {
            case X: return Block.makeCuboidShape(0, min, min, 16, max, max);
            case Y: return Block.makeCuboidShape(min, 0, min, max, 16, max);
            case Z: return Block.makeCuboidShape(min,  min, 0, max, max, 16);
        }

        // TODO get the proper AABB based on the direction we're facing + diameter.
        return Block.makeCuboidShape(0, 0, 0, 16, 16, 16);
    }


    @Override
    public boolean isBlockNormalCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState blockState) {
        return false;
    }

    @Override
    public boolean isNormalCube(IBlockState blockState) {
        return false;
    }

    /**
     * Fills out the possible states for this Block.
     *
     * Needed to setup blockstates for this block. Replaces metadata in <=1.12.
     * @param builder
     */
    @Override
    public void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(AXIS);
    }

    /**
     * Returns an IBlockState with a different Axis value depending on the placement direction.
     *
     * TODO this isn't quite right- it should not be based on facing direction, but placing direction.
     * See the log placement code? update: test if it's now correct
     *
     * @param context
     * @return
     */
    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(AXIS, context.getFace().getAxis());
    }
}
