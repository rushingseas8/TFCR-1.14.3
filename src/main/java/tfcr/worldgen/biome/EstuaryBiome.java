package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import tfcr.TFCR;
import tfcr.data.TerrainType;

public class EstuaryBiome extends BaseTFCRBiome {
    public EstuaryBiome() {
        super(-100, 100, 0, 100, TerrainType.ESTUARY,
                new Biome.Builder()
                        .surfaceBuilder(SurfaceBuilder.DEFAULT, SurfaceBuilder.GRASS_DIRT_GRAVEL_CONFIG)
                        .category(Biome.Category.RIVER)
                        .waterColor(3970006)
                        .waterFogColor(329011)
        );
        this.setRegistryName(TFCR.MODID, "estuary");
    }
}
