package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [60-100], Precipitation: [30-65]
 */
public class TemperateDesertBiome extends BaseTFCRBiome {

    private static final Biome.Builder defaultBuilder =
            new Biome.Builder()
                    .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.SAND_SAND_GRAVEL_CONFIG)
                    .category(Category.DESERT)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private TemperateDesertBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private TemperateDesertBiome(TerrainType type, Biome.Builder builder) {
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
