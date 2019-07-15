package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [-30-60], Precipitation: [10-20]
 */
public class LowGrasslandBiome extends BaseTFCRBiome {

    private static final Biome.Builder defaultBuilder =
            new Biome.Builder()
                    .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
                    .category(Category.PLAINS)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private LowGrasslandBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private LowGrasslandBiome(TerrainType type, Biome.Builder builder) {
        super(-30, 60, 10, 20, type, builder);
    }

    public static BaseTFCRBiome[] generate() {
        return new BaseTFCRBiome[] {
                new LowGrasslandBiome(TerrainType.FLAT),
                new LowGrasslandBiome(TerrainType.SMALL_HILLS),
                new LowGrasslandBiome(TerrainType.BIG_HILLS),
                new LowGrasslandBiome(TerrainType.MOUNTAINS),
        };
    }
}
