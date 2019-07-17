package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.data.TerrainType;
import tfcr.worldgen.LayerUtilsTFCR;

/**
 * Concrete biome implementation.
 * Temperature: [60-100], Precipitation: [10-20]
 */
public class SavannaBiome extends BaseTFCRBiome {

    private static final BiomeBuilder defaultBuilder =
            new BiomeBuilder()
                    .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                    .category(Category.SAVANNA)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private static final BaseTFCRBiome[] savannaBiomes = new BaseTFCRBiome[] {
            new SavannaBiome(TerrainType.FLAT)
    };

    private SavannaBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private SavannaBiome(TerrainType type, BiomeBuilder builder) {
        super(60, 100, 10, 20, type, builder);
    }

    @Override
    public BaseTFCRBiome getActualBiome(TerrainType type) {
        if (type == TerrainType.SMALL_HILLS || type == TerrainType.BIG_HILLS) {
            return ShrublandBiome.get(type);
        } else if (type == TerrainType.MOUNTAINS) {
            return ChaparralBiome.get(type);
        }
        return super.getActualBiome(type);
    }

    public static BaseTFCRBiome get(TerrainType type) {
        if (LayerUtilsTFCR.isWater(type.ordinal())) {
            return null;
        }

        if (type == TerrainType.FLAT) {
            return savannaBiomes[0];
        }
        return null;
    }

    public static BaseTFCRBiome[] generate() {
        return savannaBiomes;
    }
}
