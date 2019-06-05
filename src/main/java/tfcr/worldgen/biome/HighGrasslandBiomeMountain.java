package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [-30-60], Precipitation: [20-30]
 */
public class HighGrasslandBiomeMountain extends BaseTFCRBiome {
    public HighGrasslandBiomeMountain() {
        super(-30, 60, 20, 30, TerrainType.MOUNTAINS,
                new BiomeBuilder()
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                        .category(Category.PLAINS)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "high_grassland_mountain");
    }
}
