package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import tfcr.worldgen.LayerUtilsTFCR;

public enum RaiseInlandHillLayer implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(INoiseRandom rand, int south, int east, int north, int west, int center) {
        // Water pass-through
        if (LayerUtilsTFCR.isWater(center)) {
            return center;
        }

        // Only operate on small hill land
        if (center != LayerUtilsTFCR.SMALL_HILLS) {
            return center;
        }

        // If this land tile is adjacent to any water, then return (it's not inland!)RaiseInlandLayer
        if (LayerUtilsTFCR.isWater(south) || LayerUtilsTFCR.isWater(east) || LayerUtilsTFCR.isWater(west) || LayerUtilsTFCR.isWater(north)) {
            return center;
        }

        // Else, count up small hill land tiles
        int count = 0;
        count += south == LayerUtilsTFCR.SMALL_HILLS ? 1 : 0;
        count += east == LayerUtilsTFCR.SMALL_HILLS ? 1 : 0;
        count += north == LayerUtilsTFCR.SMALL_HILLS ? 1 : 0;
        count += west == LayerUtilsTFCR.SMALL_HILLS ? 1 : 0;

        // If count >= 3, then 2/3rd chance to convert small hills -> big hills
        if (count >= 3 && rand.random(3) < 2) {
            return LayerUtilsTFCR.BIG_HILLS;
        }
        return center;
    }
}
