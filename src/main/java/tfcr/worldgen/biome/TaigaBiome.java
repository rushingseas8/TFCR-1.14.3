package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [-60--30], Precipitation: [30-100]
 */
public class TaigaBiome extends BaseTFCRBiome {

    private static final Biome.Builder defaultBuilder =
            new Biome.Builder()
                    .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
                    .category(Category.TAIGA)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private TaigaBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private TaigaBiome(TerrainType type, Biome.Builder builder) {
        super(-60, -30, 30, 100, type, builder);
    }

    public static BaseTFCRBiome[] generate() {
        return new BaseTFCRBiome[] {
                new TaigaBiome(TerrainType.FLAT),
                new TaigaBiome(TerrainType.SMALL_HILLS),
                new TaigaBiome(TerrainType.BIG_HILLS),
                new TaigaBiome(TerrainType.MOUNTAINS),
        };
    }
}
