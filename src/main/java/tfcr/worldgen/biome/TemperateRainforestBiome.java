package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [60-100], Precipitation: [30-65]
 */
public class TemperateRainforestBiome extends BaseTFCRBiome {

    private static final BiomeBuilder defaultBuilder =
            new BiomeBuilder()
                    .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                    .category(Category.JUNGLE)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private TemperateRainforestBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private TemperateRainforestBiome(TerrainType type, BiomeBuilder builder) {
        super(-30, 60, 65, 100, type, builder);
    }

    public static BaseTFCRBiome[] generate() {
        return new BaseTFCRBiome[] {
                new TemperateRainforestBiome(TerrainType.FLAT),
                new TemperateRainforestBiome(TerrainType.SMALL_HILLS),
                new TemperateRainforestBiome(TerrainType.BIG_HILLS),
                new TemperateRainforestBiome(TerrainType.MOUNTAINS),
        };
    }
}
