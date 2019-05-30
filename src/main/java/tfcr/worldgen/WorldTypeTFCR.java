package tfcr.worldgen;

import net.minecraft.init.Biomes;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.provider.BiomeProviderType;
import net.minecraft.world.biome.provider.SingleBiomeProviderSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.IContextExtended;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.area.IAreaFactory;
import tfcr.init.ModBiomes;

import java.util.function.LongFunction;

public class WorldTypeTFCR extends WorldType {

    public WorldTypeTFCR() {
        super("TerraFirmaCraft");
        System.out.println("TFCR WorldType created");
    }

    @Override
    public IChunkGenerator<?> createChunkGenerator(World world) {
        SingleBiomeProviderSettings settings = BiomeProviderType.FIXED.createSettings().setBiome(ModBiomes.BIOME_TFCR_BASE);
        return new ChunkGeneratorTFCR(world, BiomeProviderType.FIXED.create(settings), new OverworldGenSettings());
    }

    // TODO look at ChunkGeneratorOverworld and OverworldDimension classes for reference
}
