package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [60-100], Precipitation: [30-65]
 */
public class TemperateDesertBiome extends BaseTFCRBiome {

    private static final BiomeBuilder defaultBuilder =
            new BiomeBuilder()
                    .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, SAND_SAND_GRAVEL_SURFACE))
                    .category(Category.DESERT)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private TemperateDesertBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private TemperateDesertBiome(TerrainType type, BiomeBuilder builder) {
        super(-30, 30, 0, 10, type, builder);
    }

    public static BaseTFCRBiome[] generate() {
        return new BaseTFCRBiome[] {
                new TemperateDesertBiome(TerrainType.FLAT),
                new TemperateDesertBiome(TerrainType.SMALL_HILLS),
                new TemperateDesertBiome(TerrainType.BIG_HILLS),
                new TemperateDesertBiome(TerrainType.MOUNTAINS),
        };
    }
}
