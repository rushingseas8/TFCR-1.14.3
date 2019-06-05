package tfcr.worldgen.biome;

import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;
import tfcr.worldgen.biome.BaseTFCRBiome;

public class RiverBiome extends BaseTFCRBiome {
    public RiverBiome() {
        super(-100, 100, 0, 100, TerrainType.RIVER,
                new BiomeBuilder()
                        .surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE))
                        .category(Category.RIVER)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "river");
    }
}
