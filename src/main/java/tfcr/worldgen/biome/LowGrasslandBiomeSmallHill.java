package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [-30-60], Precipitation: [10-20]
 */
public class LowGrasslandBiomeSmallHill extends BaseTFCRBiome {
    public LowGrasslandBiomeSmallHill() {
        super(-30, 60, 10, 20, TerrainType.SMALL_HILLS,
                new BiomeBuilder()
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                        .category(Category.PLAINS)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "low_grassland_smallhill");
    }
}
