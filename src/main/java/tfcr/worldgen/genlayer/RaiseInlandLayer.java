package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import tfcr.worldgen.LayerUtilsTFCR;

public enum RaiseInlandLayer implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(INoiseRandom rand, int south, int east, int north, int west, int center) {
        // Water pass-through
        if (LayerUtilsTFCR.isWater(center)) {
            return center;
        }

        // Only operate on flat land; existing small hills are unaffected
        if (center != LayerUtilsTFCR.FLAT) {
            return center;
        }

        // If this land tile is adjacent to any water, then return (it's not inland!)RaiseInlandLayer
        if (LayerUtilsTFCR.isWater(south) || LayerUtilsTFCR.isWater(east) || LayerUtilsTFCR.isWater(west) || LayerUtilsTFCR.isWater(north)) {
            return center;
        }

        // Else, count up land tiles
        int count = 0;
        count += south == LayerUtilsTFCR.FLAT ? 1 : 0;
        count += east == LayerUtilsTFCR.FLAT ? 1 : 0;
        count += north == LayerUtilsTFCR.FLAT ? 1 : 0;
        count += west == LayerUtilsTFCR.FLAT ? 1 : 0;

        // If count >= 3, then convert flat -> small hills
        if (count >= 3) {
            return LayerUtilsTFCR.SMALL_HILLS;
        }
        return center;
    }
}
