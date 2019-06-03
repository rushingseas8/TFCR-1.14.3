package tfcr.worldgen;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.layer.traits.IC1Transformer;

import static tfcr.worldgen.LayerUtilsTFCR.BIG_HILLS;
import static tfcr.worldgen.LayerUtilsTFCR.FLAT;
import static tfcr.worldgen.LayerUtilsTFCR.MOUNTAINS;

public enum GenLayerAddMountain implements IC1Transformer {
    INSTANCE;

    public int apply(IContext context, int value) {
        // Ocean is pass-through
        if (LayerUtilsTFCR.isShallowOcean(value)) {
            return value;
        } else {
            // Otherwise roll a six sided die
            int i = context.random(6);
            if (i == 0) { // 1/6 chance to turn into forest in vanilla. Big hills in TFCR.
                return BIG_HILLS;
            } else {
                // 1/6 chance to turn into mountains
                // 4/6 chance to turn into flat region
                return i == 1 ? MOUNTAINS : FLAT;
            }
        }
    }
}
