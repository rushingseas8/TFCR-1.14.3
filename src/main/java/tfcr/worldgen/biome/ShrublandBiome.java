package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.data.TerrainType;
import tfcr.worldgen.LayerUtilsTFCR;

/**
 * Concrete biome implementation.
 * Temperature: [60-100], Precipitation: [20-30]
 */
public class ShrublandBiome extends BaseTFCRBiome {

    private static final BiomeBuilder defaultBuilder =
            new BiomeBuilder()
                    .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                    .category(Category.PLAINS)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private static final BaseTFCRBiome[] shrublandBiomes = new BaseTFCRBiome[] {
            new ShrublandBiome(TerrainType.FLAT),
            new ShrublandBiome(TerrainType.SMALL_HILLS),
            new ShrublandBiome(TerrainType.BIG_HILLS),
            new ShrublandBiome(TerrainType.MOUNTAINS),
    };

    private ShrublandBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private ShrublandBiome(TerrainType type, BiomeBuilder builder) {
        super(60, 100, 20, 30, type, builder);
    }

    public static BaseTFCRBiome get(TerrainType type) {
        if (LayerUtilsTFCR.isWater(type.ordinal())) {
            return null;
        }
        return shrublandBiomes[type.ordinal() - 5];
    }

    public static BaseTFCRBiome[] generate() {
        return shrublandBiomes;
    }
}
