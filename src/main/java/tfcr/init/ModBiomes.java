package tfcr.init;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tfcr.TFCR;
import tfcr.worldgen.BiomeTFCRBase;
import tfcr.worldgen.WorldTypeTFCR;

@Mod.EventBusSubscriber(modid = TFCR.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModBiomes {

    public static final WorldTypeTFCR WORLD_TYPE_TFCR = new WorldTypeTFCR();

    public static final BiomeTFCRBase BIOME_TFCR_BASE = new BiomeTFCRBase();

    @SubscribeEvent
    public static void registerBiomes(RegistryEvent.Register<Biome> event) {
        System.out.println("Biome registation called");
        event.getRegistry().register(BIOME_TFCR_BASE);
    }
}
