package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import tfcr.worldgen.LayerUtilsTFCR;

public enum AddEstuaryLayer implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(INoiseRandom rand, int south, int east, int north, int west, int center) {
        if (center == LayerUtilsTFCR.OCEAN) {
            return center;
        } else if (center <= 300000) {
            if (LayerUtilsTFCR.hasOcean(south, east, north, west)) {
                return center + 300000;
            }
        }

//        if (center == LayerUtilsTFCR.RIVER) {
//            if (LayerUtilsTFCR.hasOcean(south, east, north, west)) {
//                return LayerUtilsTFCR.ESTUARY;
//            }
//        }

        return center;
    }
}
