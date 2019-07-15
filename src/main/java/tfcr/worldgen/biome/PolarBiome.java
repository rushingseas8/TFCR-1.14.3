package tfcr.worldgen.biome;

import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [-100--60], Precipitation: [0-100]
 */
public class PolarBiome extends BaseTFCRBiome {

    private static final Biome.Builder defaultBuilder =
            new Biome.Builder()
                    .surfaceBuilder(SurfaceBuilder.DEFAULT,
                            new SurfaceBuilderConfig(
                                Blocks.SNOW_BLOCK.getDefaultState(),
                                Blocks.PACKED_ICE.getDefaultState(),
                                Blocks.ICE.getDefaultState()
                            )
                    )
                    .category(Category.ICY)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private PolarBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private PolarBiome(TerrainType type, Biome.Builder builder) {
        super(-100, -60, 0, 100, type, builder);
    }

    public static BaseTFCRBiome[] generate() {
        return new BaseTFCRBiome[] {
                new PolarBiome(TerrainType.FLAT),
                new PolarBiome(TerrainType.SMALL_HILLS),
                new PolarBiome(TerrainType.BIG_HILLS),
                new PolarBiome(TerrainType.MOUNTAINS),
        };
    }
}
