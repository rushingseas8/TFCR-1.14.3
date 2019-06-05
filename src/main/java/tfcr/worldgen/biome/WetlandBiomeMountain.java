package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [-30-15], Precipitation: [40-65]
 *
 * TODO: change worldgen to be similar to grasslands at high elevations, but keep
 *  swampy lakes at low elevation.
 */
public class WetlandBiomeMountain extends BaseTFCRBiome {
    public WetlandBiomeMountain() {
        super(-30, 15, 40, 65, TerrainType.MOUNTAINS,
                new BiomeBuilder()
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                        .category(Category.SWAMP)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "wetland_mountain");
    }
}
