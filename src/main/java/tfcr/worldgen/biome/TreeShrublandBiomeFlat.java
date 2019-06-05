package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [60-100], Precipitation: [30-65]
 */
public class TreeShrublandBiomeFlat extends BaseTFCRBiome {
    public TreeShrublandBiomeFlat() {
        super(60, 100, 30, 65, TerrainType.FLAT,
                new BiomeBuilder()
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                        .category(Category.FOREST)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "tree_shrubland_flat");
    }
}
