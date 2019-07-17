package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [60-100], Precipitation: [65-100]
 */
public class TropicalRainforestBiome extends BaseTFCRBiome {

    private static final BiomeBuilder defaultBuilder =
            new BiomeBuilder()
                    .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                    .category(Category.JUNGLE)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private TropicalRainforestBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private TropicalRainforestBiome(TerrainType type, BiomeBuilder builder) {
        super(60, 100, 65, 100, type, builder);
    }

    public static BaseTFCRBiome[] generate() {
        return new BaseTFCRBiome[] {
                new TropicalRainforestBiome(TerrainType.FLAT),
                new TropicalRainforestBiome(TerrainType.SMALL_HILLS),
                new TropicalRainforestBiome(TerrainType.BIG_HILLS),
                new TropicalRainforestBiome(TerrainType.MOUNTAINS),
        };
    }
}
