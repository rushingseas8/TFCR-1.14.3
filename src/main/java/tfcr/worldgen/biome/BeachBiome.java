package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;
import tfcr.worldgen.biome.BaseTFCRBiome;

public class BeachBiome extends BaseTFCRBiome {
    public BeachBiome() {
        super(-100, 100, 0, 100, TerrainType.BEACH,
                new BiomeBuilder()
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, SAND_SAND_GRAVEL_SURFACE))
                        .category(Category.BEACH)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "beach");
    }
}
