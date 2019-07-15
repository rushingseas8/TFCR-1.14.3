package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tfcr.data.TerrainType;

public class WetlandBiome extends BaseTFCRBiome {

    private static final Biome.Builder defaultBuilder =
            new Biome.Builder()
                    .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
                    .category(Category.SWAMP)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private WetlandBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private WetlandBiome(TerrainType type, Biome.Builder builder) {
        super(-30, 15, 40, 65, type, builder);
    }

    public static BaseTFCRBiome[] generate() {
        return new BaseTFCRBiome[] {
                new WetlandBiome(TerrainType.FLAT, defaultBuilder.surfaceBuilder(
                        SurfaceBuilder.SWAMP, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG
                )),
                new WetlandBiome(TerrainType.SMALL_HILLS),
                new WetlandBiome(TerrainType.BIG_HILLS),
                new WetlandBiome(TerrainType.MOUNTAINS),
        };
    }
}
