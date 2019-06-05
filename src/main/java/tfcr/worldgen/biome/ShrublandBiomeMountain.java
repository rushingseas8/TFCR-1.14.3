package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

/**
 * Concrete biome implementation.
 * Temperature: [60-100], Precipitation: [20-30]
 */
public class ShrublandBiomeMountain extends BaseTFCRBiome {
    public ShrublandBiomeMountain() {
        super(60, 100, 20, 30, TerrainType.MOUNTAINS,
                new BiomeBuilder()
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                        .category(Category.PLAINS)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "shrubland_mountain");
    }

    /**
     * Overridden to return ChaparralBiomeMountain.
     * @param temp
     * @param precip
     * @param type
     * @return
     */
    @Override
    public BaseTFCRBiome provideClosest(int temp, int precip, TerrainType type) {
        if (super.provideClosest(temp, precip, type) != null) {
            return resolve(ChaparralBiomeMountain.class);
        }
        return null;
    }
}
