package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [-60--30], Precipitation: [10-30]
 */
public class TundraBiome extends BaseTFCRBiome {

    private static final BiomeBuilder defaultBuilder =
            new BiomeBuilder()
                    .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                    .category(Category.TAIGA)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private TundraBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private TundraBiome(TerrainType type, BiomeBuilder builder) {
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
