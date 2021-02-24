package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import tfcr.worldgen.LayerUtilsTFCR;

import static tfcr.worldgen.LayerUtilsTFCR.*;

public enum AddRiverDeltaEdgeLayer implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(INoiseRandom rand, int south, int east, int north, int west, int center) {
        // Water pass-through
        if (LayerUtilsTFCR.isWater(center)) {
            return center;
        }

        if (center == RIVER_DELTA || center == RIVER_DELTA_EDGE) {
            return center;
        }

        // Okay, so center is a land tile. Check if we're next to river.
        // Not next to river- check if next to river delta
        if (south == RIVER_DELTA || east == RIVER_DELTA || north == RIVER_DELTA || west == RIVER_DELTA) {
            // Make sure we're not in the center, so that the edge doesn't spread inwards
            if (south != RIVER_DELTA || east != RIVER_DELTA || north != RIVER_DELTA || west != RIVER_DELTA) {
                return RIVER_DELTA_EDGE;
            }
        }

        // Spread river delta edge biomes further
        if (south == RIVER_DELTA_EDGE || east == RIVER_DELTA_EDGE || north == RIVER_DELTA_EDGE || west == RIVER_DELTA_EDGE) {
            return RIVER_DELTA_EDGE;
        }

        // No need to spread. Return pass-through.
        return center;
    }
}
