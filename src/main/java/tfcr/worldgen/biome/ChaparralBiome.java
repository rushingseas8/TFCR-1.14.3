package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [15-60], Precipitation: [40-65]
 */
public class ChaparralBiome extends BaseTFCRBiome {

    private static final Biome.Builder defaultBuilder =
            new Biome.Builder()
                    .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
                    .category(Category.PLAINS)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private static final BaseTFCRBiome[] chaparralBiomes = new BaseTFCRBiome[] {
            new ChaparralBiome(TerrainType.SMALL_HILLS),
            new ChaparralBiome(TerrainType.BIG_HILLS),
            new ChaparralBiome(TerrainType.MOUNTAINS)
    };

    private ChaparralBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private ChaparralBiome(TerrainType type, Biome.Builder builder) {
        super(30, 60, 0, 10, type, builder);
    }

    /**
     * Flat Chaparral --> Savanna
     * @param type
     * @return
     */
    @Override
    public BaseTFCRBiome getActualBiome(TerrainType type) {
        if (type == TerrainType.FLAT) {
            return SavannaBiome.get(type);
        }
        return super.getActualBiome(type);
    }

    public static BaseTFCRBiome get(TerrainType type) {
        switch (type) {
            case SMALL_HILLS: return chaparralBiomes[0];
            case BIG_HILLS: return chaparralBiomes[1];
            case MOUNTAINS: return chaparralBiomes[2];
            default: return null;
        }
    }

    public static BaseTFCRBiome[] generate() {
        return chaparralBiomes;
    }
}
