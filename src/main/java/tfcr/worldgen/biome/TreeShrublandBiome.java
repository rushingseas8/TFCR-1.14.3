package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [60-100], Precipitation: [30-65]
 */
public class TreeShrublandBiome extends BaseTFCRBiome {

    private static final Biome.Builder defaultBuilder =
            new Biome.Builder()
                    .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
                    .category(Category.FOREST)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private TreeShrublandBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private TreeShrublandBiome(TerrainType type, Biome.Builder builder) {
        super(60, 100, 30, 65, type, builder);
    }

    public static BaseTFCRBiome[] generate() {
        return new BaseTFCRBiome[] {
                new TreeShrublandBiome(TerrainType.FLAT),
                new TreeShrublandBiome(TerrainType.SMALL_HILLS),
                new TreeShrublandBiome(TerrainType.BIG_HILLS),
                new TreeShrublandBiome(TerrainType.MOUNTAINS),
        };
    }
}
