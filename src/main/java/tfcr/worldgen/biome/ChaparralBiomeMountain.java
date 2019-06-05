package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [30-60], Precipitation: [0-10]
 */
public class ChaparralBiomeMountain extends BaseTFCRBiome {
    public ChaparralBiomeMountain() {
        super(30, 60, 0, 10, TerrainType.MOUNTAINS,
                (new BiomeBuilder())
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                        .category(Category.PLAINS)
                        .waterColor(4159204)
                        .waterFogColor(329011)
                );
        this.setRegistryName(TFCR.MODID, "chaparral_mountain");
    }
}
