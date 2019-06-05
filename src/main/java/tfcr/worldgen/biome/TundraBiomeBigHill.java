package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [-60--30], Precipitation: [10-30]
 */
public class TundraBiomeBigHill extends BaseTFCRBiome {
    public TundraBiomeBigHill() {
        super(-60, -30, 10, 30, TerrainType.BIG_HILLS,
                (new BiomeBuilder())
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                        .category(Category.TAIGA)
                        .waterColor(4159204)
                        .waterFogColor(329011)
                );

        this.setRegistryName(TFCR.MODID, "tundra_bighill");
    }
}
