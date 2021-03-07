package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import tfcr.worldgen.LayerUtilsTFCR;

import static tfcr.worldgen.LayerUtilsTFCR.*;

public enum AddRiverDeltaLayer implements ICastleTransformer {
    INSTANCE,
    SPREAD {
        @Override
        public int apply(INoiseRandom rand, int south, int east, int north, int west, int center) {
            // Water pass-through
            if (LayerUtilsTFCR.isWater(center)) {
                return center;
            }

            // Deltas don't need to spread to themselves
            if (center == RIVER_DELTA) {
                return center;
            }

            // If we're land, and next to a delta, spread delta
            if (LayerUtilsTFCR.hasTerrain(RIVER_DELTA, south, east, north, west)) {
                return RIVER_DELTA;
            }

            // Otherwise don't spread.
            return center;
        }
    };

    @Override
    public int apply(INoiseRandom rand, int south, int east, int north, int west, int center) {
        // Water pass-through
        if (LayerUtilsTFCR.isWater(center)) {
            return center;
        }

        boolean hasOcean = LayerUtilsTFCR.isOceanOrBeach(south) ||
                LayerUtilsTFCR.isOceanOrBeach(east) ||
                LayerUtilsTFCR.isOceanOrBeach(north) ||
                LayerUtilsTFCR.isOceanOrBeach(west);

        boolean hasRiver = (south == RIVER) ||
                (east == RIVER) ||
                (north == RIVER) ||
                (west == RIVER);

        // Otherwise, if we are a land tile, and are adjacent to both a river
        // and an ocean/beach tile, then convert this into a river delta.
        if (hasOcean && hasRiver) {
            return RIVER_DELTA;
        }

        // If this is a delta
        boolean hasDelta = (south == RIVER_DELTA) ||
                (east == RIVER_DELTA) ||
                (north == RIVER_DELTA) ||
                (west == RIVER_DELTA);

        // If this is adjacent to a river delta, then spread the river delta.
        if (hasDelta) {
            return RIVER_DELTA;
        }

        // If not, then pass-through
        return center;
    }
}
