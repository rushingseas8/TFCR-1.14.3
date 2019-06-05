package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [60-100], Precipitation: [0-10]
 */
public class DesertBiomeSmallHill extends BaseTFCRBiome {
    public DesertBiomeSmallHill() {
        super(60, 100, 0, 10, TerrainType.SMALL_HILLS,
                (new Biome.BiomeBuilder())
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, SAND_SAND_GRAVEL_SURFACE))
                        .category(Category.DESERT)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "desert_smallhill");
    }
}
