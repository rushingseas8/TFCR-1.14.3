package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [-60--30], Precipitation: [10-30]
 */
public class TundraBiome extends BaseTFCRBiome {

    private static final Biome.Builder defaultBuilder =
            new Biome.Builder()
                    .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
                    .category(Category.TAIGA)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private TundraBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private TundraBiome(TerrainType type, Biome.Builder builder) {
        super(-60, -30, 10, 30, type, builder);
    }

    public static BaseTFCRBiome[] generate() {
        return new BaseTFCRBiome[] {
                new TundraBiome(TerrainType.FLAT),
                new TundraBiome(TerrainType.SMALL_HILLS),
                new TundraBiome(TerrainType.BIG_HILLS),
                new TundraBiome(TerrainType.MOUNTAINS),
        };
    }
}
