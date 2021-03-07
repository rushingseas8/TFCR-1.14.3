package tfcr.init;

import net.minecraft.block.Block;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import tfcr.TFCR;
import tfcr.blocks.TFCRFluidBlock;
import tfcr.fluid.TFCRFluid;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = TFCR.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(TFCR.MODID)
public class ModFluids {

    // TODO consider overriding calculateCorrectFlowingState to figure out how
    // salt + fresh water should make new sources next to each other. Brakish?
    public static final TFCRFluid FRESH_WATER = TFCRFluid.create("fresh_water",
            new TFCRFluid.Source() {
                @Override
                public boolean isEquivalentTo(Fluid fluidIn) {
                    return fluidIn == FRESH_WATER.sourceFluid || fluidIn == FRESH_WATER.flowingFluid ||
                            fluidIn == SALT_WATER.sourceFluid || fluidIn == SALT_WATER.flowingFluid ||
                            fluidIn == BRAKISH_WATER.sourceFluid || fluidIn == BRAKISH_WATER.flowingFluid;
                }
            },
            new TFCRFluid.Flowing() {
                @Override
                public boolean isEquivalentTo(Fluid fluidIn) {
                    return fluidIn == FRESH_WATER.sourceFluid || fluidIn == FRESH_WATER.flowingFluid ||
                            fluidIn == SALT_WATER.sourceFluid || fluidIn == SALT_WATER.flowingFluid ||
                            fluidIn == BRAKISH_WATER.sourceFluid || fluidIn == BRAKISH_WATER.flowingFluid;
                }
            }
    );
    public static final TFCRFluid SALT_WATER = TFCRFluid.create("salt_water",
            new TFCRFluid.Source() {
                @Override
                public boolean isEquivalentTo(Fluid fluidIn) {
                    return fluidIn == FRESH_WATER.sourceFluid || fluidIn == FRESH_WATER.flowingFluid ||
                            fluidIn == SALT_WATER.sourceFluid || fluidIn == SALT_WATER.flowingFluid ||
                            fluidIn == BRAKISH_WATER.sourceFluid || fluidIn == BRAKISH_WATER.flowingFluid;
                }
            },
            new TFCRFluid.Flowing() {
                @Override
                public boolean isEquivalentTo(Fluid fluidIn) {
                    return fluidIn == FRESH_WATER.sourceFluid || fluidIn == FRESH_WATER.flowingFluid ||
                            fluidIn == SALT_WATER.sourceFluid || fluidIn == SALT_WATER.flowingFluid ||
                            fluidIn == BRAKISH_WATER.sourceFluid || fluidIn == BRAKISH_WATER.flowingFluid;
                }
            }
    );

    public static final TFCRFluid BRAKISH_WATER = TFCRFluid.create("brakish_water",
            new TFCRFluid.Source() {
                @Override
                public boolean isEquivalentTo(Fluid fluidIn) {
                    return fluidIn == FRESH_WATER.sourceFluid || fluidIn == FRESH_WATER.flowingFluid ||
                            fluidIn == SALT_WATER.sourceFluid || fluidIn == SALT_WATER.flowingFluid ||
                            fluidIn == BRAKISH_WATER.sourceFluid || fluidIn == BRAKISH_WATER.flowingFluid;
                }
            },
            new TFCRFluid.Flowing() {
                @Override
                public boolean isEquivalentTo(Fluid fluidIn) {
                    return fluidIn == FRESH_WATER.sourceFluid || fluidIn == FRESH_WATER.flowingFluid ||
                            fluidIn == SALT_WATER.sourceFluid || fluidIn == SALT_WATER.flowingFluid ||
                            fluidIn == BRAKISH_WATER.sourceFluid || fluidIn == BRAKISH_WATER.flowingFluid;
                }
            }
    );

    // For compatibility with TFCRWaterFluid
    public static final FlowingFluid FRESH_WATER_FLUID_SOURCE = FRESH_WATER.sourceFluid;
    public static final FlowingFluid FRESH_WATER_FLUID_FLOWING = FRESH_WATER.flowingFluid;
//    public static final FlowingFluid FRESH_WATER_FLUID_SOURCE = new TFCRWaterFluid.Source();
//    public static final FlowingFluid FRESH_WATER_FLUID_FLOWING = new TFCRWaterFluid.Flowing();

    // For compatibility with TFCRWaterFluid
    public static TFCRFluidBlock FRESH_WATER_FLUID_BLOCK = FRESH_WATER.fluidBlock;


    public static final FlowingFluid SALT_WATER_FLUID_SOURCE = SALT_WATER.sourceFluid;
    public static final FlowingFluid SALT_WATER_FLUID_FLOWING = SALT_WATER.flowingFluid;
//    public static final FlowingFluid FRESH_WATER_FLUID_SOURCE = new TFCRWaterFluid.Source();
//    public static final FlowingFluid FRESH_WATER_FLUID_FLOWING = new TFCRWaterFluid.Flowing();

    // For compatibility with TFCRWaterFluid
    public static TFCRFluidBlock SALT_WATER_FLUID_BLOCK = SALT_WATER.fluidBlock;

    // TODO go back to using TFCRWaterFluid-like classes
    // TODO add interaction where fresh + salt water next to each other react to form
    // brakish water, but otherwise act like water would.
    private static final TFCRFluid[] allFluids = {
            FRESH_WATER,
            SALT_WATER
    };

    private static void initFluids() {
    }

    /**
     * @param fluidIn Fluid to test against
     * @return true, if the Fluid is a fresh water source block (or equivalent)
     */
    public static boolean isFreshWaterLike(Fluid fluidIn) {
        if (fluidIn == Fluids.WATER) {
            return true;
        }
        if (fluidIn instanceof TFCRFluid) {
            TFCRFluid fluid = (TFCRFluid) fluidIn;
            return fluid.sourceFluid == ModFluids.FRESH_WATER.sourceFluid;
        }
        return false;
    }

    /**
     * @param fluidIn Fluid to test against
     * @return true, if the Fluid is a salt water source block (or equivalent)
     */
    public static boolean isSaltWaterLike(Fluid fluidIn) {
        if (fluidIn instanceof TFCRFluid) {
            TFCRFluid fluid = (TFCRFluid) fluidIn;
            return fluid.sourceFluid == ModFluids.BRAKISH_WATER.sourceFluid ||
                    fluid.sourceFluid == ModFluids.SALT_WATER.sourceFluid;
        }
        return false;
    }

    /**
     * @param fluidIn Fluid to test against
     * @return true, if the Fluid is any water source block
     */
    public static boolean isWaterLike(Fluid fluidIn) {
        return isFreshWaterLike(fluidIn) || isSaltWaterLike(fluidIn);
    }

    public static List<Fluid> getAllFluids() {
        if (allFluids == null) {
            initFluids();
        }

        ArrayList<Fluid> toReturn = new ArrayList<>();
        for (FlowingFluid fluid : allFluids) {
            if (fluid instanceof TFCRFluid) {
                toReturn.addAll(((TFCRFluid) fluid).getFluids());
            } else {
                toReturn.add(fluid);
            }
        }

        return toReturn;
    }

    public static List<Block> getAllBlocks() {
        if (allFluids == null) {
            initFluids();
        }

        ArrayList<Block> toReturn = new ArrayList<>();
        for (TFCRFluid fluid : allFluids) {
            toReturn.add(fluid.fluidBlock);
        }

        return toReturn;
    }

    public static List<Item> getAllItems() {
        if (allFluids == null) {
            initFluids();
        }

        ArrayList<Item> toReturn = new ArrayList<>();
        for (TFCRFluid fluid : allFluids) {
            toReturn.add(fluid.bucketItem);
        }

        return toReturn;
    }

    @SubscribeEvent
    public static void registerFluids(RegistryEvent.Register<Fluid> event) {
//        FRESH_WATER_FLUID_SOURCE.setRegistryName(TFCR.MODID, "fresh_water_source");
//        FRESH_WATER_FLUID_FLOWING.setRegistryName(TFCR.MODID, "fresh_water_flowing");
//
//        event.getRegistry().register(FRESH_WATER_FLUID_SOURCE);
//        event.getRegistry().register(FRESH_WATER_FLUID_FLOWING);
        for (TFCRFluid fluid : allFluids) {
            event.getRegistry().register(fluid.sourceFluid);
            event.getRegistry().register(fluid.flowingFluid);
        }

    }
}
