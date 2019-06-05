package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [15-60], Precipitation: [40-65]
 */
public class DeciduousForestBiomeSmallHill extends BaseTFCRBiome {
    public DeciduousForestBiomeSmallHill() {
        super(15, 60, 40, 65, TerrainType.SMALL_HILLS,
                new BiomeBuilder()
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                        .category(Category.FOREST)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "deciduous_forest_smallhill");
    }
}
