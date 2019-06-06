package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.data.TerrainType;

public class WetlandBiome extends BaseTFCRBiome {

    private static final BiomeBuilder defaultBuilder =
            new BiomeBuilder()
                    .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                    .category(Category.SWAMP)
                    .waterColor(4159204)
                    .waterFogColor(329011);

    private WetlandBiome(TerrainType type) {
        this(type, defaultBuilder);
    }

    private WetlandBiome(TerrainType type, BiomeBuilder builder) {
        super(-30, 15, 40, 65, type, builder);
    }

    public static BaseTFCRBiome[] generate() {
        return new BaseTFCRBiome[] {
                new WetlandBiome(TerrainType.FLAT, defaultBuilder.surfaceBuilder(
                        new CompositeSurfaceBuilder<>(SWAMP_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE)
                )),
                new WetlandBiome(TerrainType.SMALL_HILLS),
                new WetlandBiome(TerrainType.BIG_HILLS),
                new WetlandBiome(TerrainType.MOUNTAINS),
        };
    }
}
