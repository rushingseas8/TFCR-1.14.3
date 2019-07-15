package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [60-100], Precipitation: [65-100]
 */
public class TropicalRainforestBiome extends BaseTFCRBiome {

    private static final Biome.Builder defaultBuilder =
            new Biome.Builder()
                    .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
                    .category(Category.JUNGLE)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private TropicalRainforestBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private TropicalRainforestBiome(TerrainType type, Biome.Builder builder) {
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
