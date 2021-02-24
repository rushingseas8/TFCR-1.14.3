package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import tfcr.worldgen.LayerUtilsTFCR;

import static tfcr.worldgen.LayerUtilsTFCR.*;

public enum AddRiverEdgeLayer implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(INoiseRandom rand, int south, int east, int north, int west, int center) {
        // Water pass-through
        if (LayerUtilsTFCR.isWater(center)) {
            return center;
        }

        // Okay, so center is a land tile. Check if we're next to river.
        if (south == RIVER || east == RIVER || north == RIVER || west == RIVER) {
            // If so, then return a river edge logical biome.
            return RIVER_EDGE;
        }

        // Spread river edge biomes further
        if (south == RIVER_EDGE || east == RIVER_EDGE || north == RIVER_EDGE || west == RIVER_EDGE) {
            return RIVER_EDGE;
        }

//        // Not next to river- check if next to river delta
//        if (south == RIVER_DELTA || east == RIVER_DELTA || north == RIVER_DELTA || west == RIVER_DELTA) {
//            return RIVER_DELTA_EDGE;
//        }
//
//        // Spread river delta edge biomes further
//        if (south == RIVER_DELTA_EDGE || east == RIVER_DELTA_EDGE || north == RIVER_DELTA_EDGE || west == RIVER_DELTA_EDGE) {
//            return RIVER_DELTA_EDGE;
//        }

        // No need to spread. Return pass-through.
        return center;
    }
}
