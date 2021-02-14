package tfcr.init;

import net.minecraft.block.Block;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.material.Material;
import net.minecraft.fluid.FlowingFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import tfcr.TFCR;
import tfcr.blocks.TFCRFluidBlock;
import tfcr.fluid.FreshWaterFluid;
import tfcr.fluid.TFCRFluid;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = TFCR.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(TFCR.MODID)
public class ModFluids {

    public static final TFCRFluid FRESH_WATER = TFCRFluid.create("fresh_water");

    public static final FlowingFluid FRESH_WATER_FLUID_SOURCE = FRESH_WATER.sourceFluid;
    public static final FlowingFluid FRESH_WATER_FLUID_FLOWING = FRESH_WATER.flowingFluid;

    public static FlowingFluidBlock FRESH_WATER_FLUID_BLOCK = (FlowingFluidBlock) FRESH_WATER.fluidBlock;


    private static final TFCRFluid[] allFluids = {
            FRESH_WATER,
            TFCRFluid.create("salt_water")
    };

    private static void initFluids() {
    }

    public static List<Fluid> getAllFluids() {
        if (allFluids == null) {
            initFluids();
        }

        ArrayList<Fluid> toReturn = new ArrayList<>();
        for (TFCRFluid fluid : allFluids) {
            toReturn.addAll(fluid.getFluids());
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
        for (Fluid fluid : allFluids) {
            event.getRegistry().register(fluid);
        }

    }
}
