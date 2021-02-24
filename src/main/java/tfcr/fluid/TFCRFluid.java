package tfcr.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.BucketItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import tfcr.TFCR;
import tfcr.blocks.TFCRFluidBlock;
import tfcr.init.ModFluids;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TFCRFluid extends FlowingFluid {

    public TFCRFluid.Source sourceFluid;
    public TFCRFluid.Flowing flowingFluid;
    public TFCRFluidBlock fluidBlock;
    public BucketItem bucketItem;

    public static TFCRFluid create(String name) {
        return create(name, createGenericSourceFluid(), createGenericFlowingFluid());
    }

    public static TFCRFluid create(String name, TFCRFluid.Source source, TFCRFluid.Flowing flowing) {
        // Create a new holder fluid object
        TFCRFluid fluid = new TFCRFluid();

        // Create generic water-like fluids
        fluid.sourceFluid = source;
        fluid.flowingFluid = flowing;

        // Set registry names
        fluid.sourceFluid.setRegistryName(TFCR.MODID, name + "_source");
        fluid.flowingFluid.setRegistryName(TFCR.MODID, name + "_flowing");

        // Self-references
        fluid.sourceFluid.sourceFluid = fluid.sourceFluid;
        fluid.sourceFluid.flowingFluid = fluid.flowingFluid;
        fluid.flowingFluid.sourceFluid = fluid.sourceFluid;
        fluid.flowingFluid.flowingFluid = fluid.flowingFluid;

        // Fluid block (physical representation of the fluid when placed in-world)
        fluid.fluidBlock = new TFCRFluidBlock(
                fluid.sourceFluid,
                Block.Properties
                        .create(Material.WATER)
                        .doesNotBlockMovement()
                        .hardnessAndResistance(100f)
                        .noDrops()
        );

        // Registry name
        fluid.fluidBlock.setRegistryName(TFCR.MODID, name + "_block");

        // Create a bucket entry
        fluid.bucketItem = new BucketItem(
                fluid.sourceFluid,
                new Item.Properties()
                        .containerItem(Items.BUCKET)
                        .maxStackSize(1)
                        .group(ItemGroup.MISC)
        );

        // Registry name
        fluid.bucketItem.setRegistryName(TFCR.MODID, name + "_bucket");

        // Assign sub-references to source
        fluid.sourceFluid.fluidBlock = fluid.fluidBlock;
        fluid.sourceFluid.bucketItem = fluid.bucketItem;

        // Assign sub-references to flowing
        fluid.flowingFluid.fluidBlock = fluid.fluidBlock;
        fluid.flowingFluid.bucketItem = fluid.bucketItem;

        return fluid;
    }

    public TFCRFluid() {
    }

    public List<Fluid> getFluids() {
        return Arrays.asList(new Fluid[] {sourceFluid, flowingFluid});
    }

    public static TFCRFluid.Flowing createGenericFlowingFluid() {
        return new TFCRFluid.Flowing() {};
    }

    public static TFCRFluid.Source createGenericSourceFluid() {
        return new TFCRFluid.Source() {};
    }

    public static abstract class Flowing extends TFCRFluid {
        protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder) {
            super.fillStateContainer(builder);
            builder.add(LEVEL_1_8);
        }

        public int getLevel(IFluidState state) {
            return state.get(LEVEL_1_8);
        }

        public boolean isSource(IFluidState state) {
            return false;
        }
    }

    public static abstract class Source extends TFCRFluid {
        public int getLevel(IFluidState state) {
            return 8;
        }

        public boolean isSource(IFluidState state) {
            return true;
        }
    }

    @Override
    public boolean isEquivalentTo(Fluid fluidIn) {
        return fluidIn == sourceFluid || fluidIn == flowingFluid;
    }

    @Override
    public Fluid getFlowingFluid() {
        return this.flowingFluid;
    }

    @Override
    public Fluid getStillFluid() {
        return this.sourceFluid;
    }

    @Override
    protected BlockState getBlockState(IFluidState state) {
        return fluidBlock.getDefaultState().with(FlowingFluidBlock.LEVEL, getLevelFromState(state));
    }

    @Override
    protected boolean canSourcesMultiply() {
        return true;
    }

    @Override
    protected void beforeReplacingBlock(IWorld worldIn, BlockPos pos, BlockState state) {
        TileEntity tileentity = state.getBlock().hasTileEntity() ? worldIn.getTileEntity(pos) : null;
        Block.spawnDrops(state, worldIn.getWorld(), pos, tileentity);
    }

    @Override
    protected int getSlopeFindDistance(IWorldReader worldIn) {
        return 4;
    }

    @Override
    protected int getLevelDecreasePerBlock(IWorldReader worldIn) {
        return 1;
    }

    @Override
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.TRANSLUCENT;
    }

    @Override
    public Item getFilledBucket() {
        return bucketItem;
    }

    @Override
    protected boolean func_215665_a(IFluidState p_215665_1_, IBlockReader p_215665_2_, BlockPos p_215665_3_, Fluid p_215665_4_, Direction p_215665_5_) {
        return p_215665_5_ == Direction.DOWN && !p_215665_4_.isIn(FluidTags.WATER);
    }

    @Override
    public int getTickRate(IWorldReader p_205569_1_) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0f;
    }

    @Override
    public boolean isSource(IFluidState state) {
        throw new RuntimeException("isSource should not be called on raw TFCRFluid.");
    }

    @Override
    public int getLevel(IFluidState p_207192_1_) {
        throw new RuntimeException("getLevel should not be called on raw TFCRFluid.");
    }
}
