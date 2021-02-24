package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import tfcr.worldgen.LayerUtilsTFCR;

public enum SpreadEstuaryLayer implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(INoiseRandom rand, int south, int east, int north, int west, int center) {
        if (center == LayerUtilsTFCR.RIVER) {
            if (LayerUtilsTFCR.hasEstuary(south, east, north, west)) {
                return LayerUtilsTFCR.ESTUARY;
            }
        }

        return center;
    }
}
