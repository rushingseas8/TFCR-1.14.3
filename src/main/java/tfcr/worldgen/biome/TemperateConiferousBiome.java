package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [60-100], Precipitation: [30-65]
 */
public class TemperateConiferousBiome extends BaseTFCRBiome {

    private static final BiomeBuilder defaultBuilder =
            new BiomeBuilder()
                    .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                    .category(Category.FOREST)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private TemperateConiferousBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private TemperateConiferousBiome(TerrainType type, BiomeBuilder builder) {
        super(-30, 60, 30, 40, type, builder);
    }

    public static BaseTFCRBiome[] generate() {
        return new BaseTFCRBiome[] {
                new TemperateConiferousBiome(TerrainType.FLAT),
                new TemperateConiferousBiome(TerrainType.SMALL_HILLS),
                new TemperateConiferousBiome(TerrainType.BIG_HILLS),
                new TemperateConiferousBiome(TerrainType.MOUNTAINS),
        };
    }
}
