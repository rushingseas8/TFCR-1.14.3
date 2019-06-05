package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;
import tfcr.worldgen.biome.BaseTFCRBiome;

public class CliffBiome extends BaseTFCRBiome {
    public CliffBiome() {
        super(-100, 100, 0, 100, TerrainType.CLIFF,
                new BiomeBuilder()
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, STONE_STONE_GRAVEL_SURFACE))
                        .category(Category.BEACH)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "cliff");
    }
}
