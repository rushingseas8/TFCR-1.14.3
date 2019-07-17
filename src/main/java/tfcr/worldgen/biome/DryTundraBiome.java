package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [-60--30], Precipitation: [0-10]
 */
public class DryTundraBiome extends BaseTFCRBiome {

    private static final BiomeBuilder defaultBuilder =
            new BiomeBuilder()
                    .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                    .category(Category.ICY)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private DryTundraBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private DryTundraBiome(TerrainType type, BiomeBuilder builder) {
        super(-60, -30, 0, 10, type, builder);
    }

    public static BaseTFCRBiome[] generate() {
        return new BaseTFCRBiome[] {
                new DryTundraBiome(TerrainType.FLAT),
                new DryTundraBiome(TerrainType.SMALL_HILLS),
                new DryTundraBiome(TerrainType.BIG_HILLS),
                new DryTundraBiome(TerrainType.MOUNTAINS),
        };
    }
}
