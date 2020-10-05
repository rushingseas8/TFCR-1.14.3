package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IC1Transformer;
import tfcr.worldgen.LayerUtilsTFCR;

import static tfcr.worldgen.LayerUtilsTFCR.BIG_HILLS;
import static tfcr.worldgen.LayerUtilsTFCR.FLAT;
import static tfcr.worldgen.LayerUtilsTFCR.MOUNTAINS;

/**
 * In Vanilla, this is GenLayerAddSnow.
 *
 * At the stage this is called in worldgen, there are only ocean/plains/forest.
 * In Vanilla, GenLayerAddSnow takes land tiles and turns them into forest
 * (1/6 chance), mountains (1/6 chance), or plains (4/6 chance).
 *
 * In TFCR, plains is represented with flat land, and forest is represented by
 * small hills. This method creates big hills in place of Vanilla forests.
 */
public enum AddMountainLayer implements IC1Transformer {
    INSTANCE;

    public int apply(INoiseRandom rand, int value) {
        // Ocean is pass-through
        if (LayerUtilsTFCR.isShallowOcean(value)) {
            return value;
        }

        // Otherwise roll a six sided die
        int i = rand.random(6);
        if (i <= 1) { // 2/6 chance to turn into mountain
            return MOUNTAINS;
        }
        return FLAT;
//        return MOUNTAINS;
    }
}
