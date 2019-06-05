package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [60-100], Precipitation: [20-30]
 */
public class ShrublandBiomeSmallHill extends BaseTFCRBiome {
    public ShrublandBiomeSmallHill() {
        super(60, 100, 20, 30, TerrainType.SMALL_HILLS,
                new Biome.BiomeBuilder()
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                        .category(Category.PLAINS)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "shrubland_smallhill");
    }
}
