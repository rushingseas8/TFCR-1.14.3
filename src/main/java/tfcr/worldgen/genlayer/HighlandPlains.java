package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import tfcr.worldgen.LayerUtilsTFCR;

public enum HighlandPlains implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(INoiseRandom rand, int south, int east, int north, int west, int center) {
        // Water pass-through
        if (LayerUtilsTFCR.isWater(center)) {
            return center;
        }

        // Only has a chance to affect hills
        if (center != LayerUtilsTFCR.BIG_HILLS && center != LayerUtilsTFCR.SMALL_HILLS) {
            return center;
        }

        // Check if this tile is surrounded by hills
        int count = 0;
        count += (south == LayerUtilsTFCR.BIG_HILLS || south == LayerUtilsTFCR.SMALL_HILLS) ? 1 : 0;
        count += (east == LayerUtilsTFCR.BIG_HILLS || east == LayerUtilsTFCR.SMALL_HILLS) ? 1 : 0;
        count += (north == LayerUtilsTFCR.BIG_HILLS || north == LayerUtilsTFCR.SMALL_HILLS) ? 1 : 0;
        count += (west == LayerUtilsTFCR.BIG_HILLS || west == LayerUtilsTFCR.SMALL_HILLS) ? 1 : 0;

        // If it is, then there's a small chance to turn it into flat land
        if (count == 4) {
            if (rand.random(9) == 0) {
                return LayerUtilsTFCR.FLAT;
            }
        }
        return center;
    }
}
