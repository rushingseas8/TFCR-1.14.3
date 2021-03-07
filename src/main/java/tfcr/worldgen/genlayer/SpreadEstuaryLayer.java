package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import tfcr.worldgen.LayerUtilsTFCR;

/**
 * Spreads Estuary TerrainTypes to adjacent River or Ocean biomes.
 */
public enum SpreadEstuaryLayer implements ICastleTransformer {
    TO_RIVER,
    TO_OCEAN {
        @Override
        public int apply(INoiseRandom rand, int south, int east, int north, int west, int center) {
            if (center == LayerUtilsTFCR.OCEAN) {
                if (LayerUtilsTFCR.hasEstuary(south, east, north, west)) {
                    return LayerUtilsTFCR.ESTUARY;
                }
            }

            return center;
        }
    };

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
