package tfcr.fluid;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.Item;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.IStringSerializable;
import tfcr.init.ModFluids;

// This class was a test for combining fresh/brakish/salty water into one block
// but that goes against the flattening idea so remove me
public abstract class TFCRWaterFluid extends WaterFluid {

    public enum Salinity implements IStringSerializable {
        FRESH,
        BRAKISH,
        SALTY
        ;

        @Override
        public String getName() {
            return name().toLowerCase();
        }
    }

    public abstract Salinity getSalinity();

//    public static final EnumProperty<Salinity> SALINITY = EnumProperty.create("salinity", Salinity.class);

//    public Fluid getFlowingFluid() {
//        return ModFluids.FRESH_WATER_FLUID_FLOWING;
//    }
//
//    public Fluid getStillFluid() {
//        return ModFluids.FRESH_WATER_FLUID_SOURCE;
//    }

    public abstract Fluid getFlowingFluid();

    public abstract Fluid getStillFluid();

//    @Override
//    public Item getFilledBucket() {
////        return ModItems.fresh_water_bucket;
//        return null;
//    }

    @Override
    public abstract Item getFilledBucket();

    public abstract Block getFluidBlock();

    @Override
    public BlockState getBlockState(IFluidState state) {
        return getFluidBlock().getDefaultState().with(FlowingFluidBlock.LEVEL, getLevelFromState(state));
//        return null;
    }

    @Override
    public boolean isEquivalentTo(Fluid fluidIn) {
//        return fluidIn == ModFluids.FRESH_WATER_FLUID_SOURCE || fluidIn == ModFluids.FRESH_WATER_FLUID_FLOWING;
//        return false;
        return fluidIn instanceof TFCRWaterFluid;
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder) {
        super.fillStateContainer(builder);
//        builder.add(SALINITY);
    }

//    /**
//     * Inner class for flowing variant, following WaterFluid
//     */
//    public static class Flowing extends TFCRWaterFluid {
//        protected void fillStateContainer(StateContainer.Builder<Fluid, IFluidState> builder) {
//            super.fillStateContainer(builder);
//            builder.add(LEVEL_1_8);
//        }
//
//        public int getLevel(IFluidState state) {
//            return state.get(LEVEL_1_8);
//        }
//
//        public boolean isSource(IFluidState state) {
//            return false;
//        }
//    }
//
//    /**
//     * Inner class for source variant, following WaterFluid
//     */
//    public static class Source extends TFCRWaterFluid {
//        public int getLevel(IFluidState state) {
//            return 8;
//        }
//
//        public boolean isSource(IFluidState state) {
//            return true;
//        }
//    }
}
