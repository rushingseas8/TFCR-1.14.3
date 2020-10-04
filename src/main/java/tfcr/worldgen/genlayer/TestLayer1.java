package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import tfcr.data.TerrainType;
import tfcr.worldgen.LayerUtilsTFCR;

public enum TestLayer1 implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(INoiseRandom rand, int south, int east, int north, int west, int center) {
        if (LayerUtilsTFCR.isOcean(south) && LayerUtilsTFCR.isOcean(east) && LayerUtilsTFCR.isOcean(north) && LayerUtilsTFCR.isOcean(west)) {
            return TerrainType.FLAT.ordinal();
        }
        return center;
    }
}
