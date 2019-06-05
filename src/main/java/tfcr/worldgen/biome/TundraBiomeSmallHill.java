package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [-60--30], Precipitation: [10-30]
 */
public class TundraBiomeSmallHill extends BaseTFCRBiome {
    public TundraBiomeSmallHill() {
        super(-60, -30, 10, 30, TerrainType.SMALL_HILLS,
                (new BiomeBuilder())
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                        .category(Category.TAIGA)
                        .waterColor(4159204)
                        .waterFogColor(329011)
                );

        this.setRegistryName(TFCR.MODID, "tundra_smallhill");
    }
}
