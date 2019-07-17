package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [60-100], Precipitation: [0-10]
 */
public class DesertBiome extends BaseTFCRBiome {

    private static final BiomeBuilder defaultBuilder =
            new Biome.BiomeBuilder()
                    .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, SAND_SAND_GRAVEL_SURFACE))
                    .category(Category.DESERT)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private DesertBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private DesertBiome(TerrainType type, BiomeBuilder builder) {
        super(60, 100, 0, 10, type, builder);
    }

    public static BaseTFCRBiome[] generate() {
        return new BaseTFCRBiome[] {
                new DesertBiome(TerrainType.FLAT),
                new DesertBiome(TerrainType.SMALL_HILLS),
                new DesertBiome(TerrainType.BIG_HILLS),
                new DesertBiome(TerrainType.MOUNTAINS),
        };
    }
}
