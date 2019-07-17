package tfcr.worldgen;

import net.minecraft.world.IWorld;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.OverworldChunkGenerator;
import net.minecraft.world.gen.OverworldGenSettings;
import tfcr.data.TerrainType;

public class ChunkGeneratorTFCR extends OverworldChunkGenerator {

    public ChunkGeneratorTFCR(IWorld worldIn, BiomeProvider biomeProviderIn, OverworldGenSettings settings) {
        super(worldIn, biomeProviderIn, settings);
    }
}
