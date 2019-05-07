package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import tfcr.TFCR;

public class BlockBranch extends Block {

    public String name;

    public static final EnumProperty<EnumDiameter> DIAMETER = EnumProperty.create("width", EnumDiameter.class);
    public static final EnumProperty<EnumAxis> AXIS = EnumProperty.create("axis", EnumAxis.class);

    public BlockBranch() {
        super(Block.Properties.from(Blocks.OAK_WOOD));
        this.setRegistryName(TFCR.MODID, "block_branch");
//        setDefaultState(getDefaultState().with(DIAMETER, EnumDiameter.TWO).with(AXIS, EnumAxis.Y));
        this.setDefaultState(this.stateContainer.getBaseState().with(DIAMETER, EnumDiameter.TWO).with(AXIS, EnumAxis.Y));

        // TODO put all variants of block state in creative inventory
        // see Block#fillItemGroup
    }

    // Not needed. Solid is correct, just needed to set the right shape.
//    @Override
//    public BlockRenderLayer getRenderLayer() {
//        return BlockRenderLayer.CUTOUT;
//    }


    // Doesn't do anything?
    @Override
    public VoxelShape getRenderShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return Block.makeCuboidShape(0, 0, 0, 4, 4, 4);
    }

    /**
     * The shape this block takes up. Displayed as the outline around it.
     * @param state Unused.
     * @param worldIn Unused.
     * @param pos Unused.
     * @return The Voxel bounding box of this shape.
     */
    @Override
    public VoxelShape getShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        int diameter = state.get(DIAMETER).diameter;
        int radius = diameter / 2;

        // TODO get the proper AABB based on the direction we're facing + diameter.
        return Block.makeCuboidShape(0, 0, 0, 8, 8, 8);
    }

    /**
     * The collision box of this block.
     * @param state Unused.
     * @param worldIn Unused.
     * @param pos Unused.
     * @return The collision Voxel of this block.
     */
    @Override
    public VoxelShape getCollisionShape(IBlockState state, IBlockReader worldIn, BlockPos pos) {
        return Block.makeCuboidShape(0, 0, 0, 12, 12, 12);
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
        builder.add(DIAMETER).add(AXIS);
    }

    @Override
    public IBlockState getStateForPlacement(BlockItemUseContext context) {
//        IBlockReader reader = context.getWorld();
//        BlockPos pos = context.getPos();

        System.out.println("Get state for placement called");

        return getDefaultState()
            .with(DIAMETER, EnumDiameter.FOUR)
            .with(AXIS, EnumAxis.fromFacingAxis(context.getNearestLookingDirection().getAxis()));
    }

    /**
     * An Enum representing the diameter of this branch.
     */
    public enum EnumDiameter implements IStringSerializable {
        TWO(2),
        FOUR(4),
        SIX(6),
        EIGHT(8),
        TEN(10),
        TWELVE(12),
        FOURTEEN(14);

        /**
         * The diameter of this branch Block in pixels.
         */
        private int diameter;

        EnumDiameter(int diameter) {
            this.diameter = diameter;
        }

        @Override
        public String getName() {
            return this.name().toLowerCase();
        }
    }

    public enum EnumAxis implements IStringSerializable {
        X,
        Y,
        Z;

        /**
         * Used to map EnumFacing -> EnumAxis for placement.
         * @param axis The facing axis.
         * @return The EnumAxis Property for this Block.
         */
        public static EnumAxis fromFacingAxis(EnumFacing.Axis axis) {
            switch(axis) {
                case X: return X;
                case Y: return Y;
                case Z: return Z;
                default: return Y;
            }
        }

        @Override
        public String getName() {
            return this.name().toLowerCase();
        }
    }
}
