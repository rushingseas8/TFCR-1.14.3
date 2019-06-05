package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [-60--30], Precipitation: [0-10]
 */
public class DryTundraBiomeFlat extends BaseTFCRBiome {
    public DryTundraBiomeFlat() {
        super(-60, -30, 0, 10, TerrainType.FLAT,
                (new BiomeBuilder())
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                        .category(Category.ICY)
                        .waterColor(4159204)
                        .waterFogColor(329011)
                );

        this.setRegistryName(TFCR.MODID, "dry_tundra_flat");
    }
}
