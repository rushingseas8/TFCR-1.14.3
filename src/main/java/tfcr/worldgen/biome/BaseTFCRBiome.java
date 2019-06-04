package tfcr.worldgen.biome;

import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.surfacebuilders.CompositeSurfaceBuilder;
import tfcr.TFCR;

public class BaseTFCRBiome extends Biome {

    public BaseTFCRBiome() {
        super((new Biome.BiomeBuilder()).surfaceBuilder(new CompositeSurfaceBuilder<>(DEFAULT_SURFACE_BUILDER, GRASS_DIRT_GRAVEL_SURFACE)).precipitation(Biome.RainType.RAIN).category(Biome.Category.PLAINS).depth(0.125F).scale(0.05F).temperature(0.8F).downfall(0.4F).waterColor(4159204).waterFogColor(329011).parent((String)null));
        this.setRegistryName(TFCR.MODID, "biome_base");
    }

    protected BaseTFCRBiome(BiomeBuilder builder) {
        super(builder);
    }

    public int getID() {
        return -1;
    }
}
