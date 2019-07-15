package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [15-60], Precipitation: [40-65]
 */
public class DeciduousForestBiome extends BaseTFCRBiome {

    private static final Biome.Builder defaultBuilder =
            new Biome.Builder()
                    .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
                    .category(Category.FOREST)
                    .waterColor(4159204)
                    .waterFogColor(329011);


    private DeciduousForestBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private DeciduousForestBiome(TerrainType type, Biome.Builder builder) {
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
