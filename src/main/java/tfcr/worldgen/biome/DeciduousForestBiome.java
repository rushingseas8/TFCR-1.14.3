package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [15-60], Precipitation: [40-65]
 */
public class DeciduousForestBiome extends BaseTFCRBiome {

    private static final BiomeBuilder defaultBuilder =
            new BiomeBuilder()
                    .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                    .category(Category.FOREST)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private DeciduousForestBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private DeciduousForestBiome(TerrainType type, BiomeBuilder builder) {
        super(15, 60, 40, 65, type, builder);
    }

    public static BaseTFCRBiome[] generate() {
        return new BaseTFCRBiome[] {
                new DeciduousForestBiome(TerrainType.FLAT),
                new DeciduousForestBiome(TerrainType.SMALL_HILLS),
                new DeciduousForestBiome(TerrainType.BIG_HILLS),
                new DeciduousForestBiome(TerrainType.MOUNTAINS),
        };
    }
}
