package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [-30-60], Precipitation: [10-20]
 */
public class HighGrasslandBiome extends BaseTFCRBiome {

    private static final Biome.Builder defaultBuilder =
            new Biome.Builder()
                    .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
                    .category(Category.PLAINS)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private HighGrasslandBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private HighGrasslandBiome(TerrainType type, Biome.Builder builder) {
        super(-30, 60, 20, 30, type, builder);
    }

    public static BaseTFCRBiome[] generate() {
        return new BaseTFCRBiome[] {
                new HighGrasslandBiome(TerrainType.FLAT),
                new HighGrasslandBiome(TerrainType.SMALL_HILLS),
                new HighGrasslandBiome(TerrainType.BIG_HILLS),
                new HighGrasslandBiome(TerrainType.MOUNTAINS),
        };
    }
}
