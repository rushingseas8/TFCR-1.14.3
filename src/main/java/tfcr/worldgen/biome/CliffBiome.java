package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

public class CliffBiome extends BaseTFCRBiome {
    public CliffBiome() {
        super(-100, 100, 0, 100, TerrainType.CLIFF,
                new Biome.Builder()
                        .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.STONE_STONE_GRAVEL_CONFIG)
                        .category(Category.BEACH)
                        .waterColor(4159204)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "cliff");
    }
}
