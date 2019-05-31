package tfcr.worldgen;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.layer.LayerUtil;
import net.minecraft.world.gen.layer.traits.IC1Transformer;
import tfcr.data.TerrainType;

public enum GenLayerAddMountain implements IC1Transformer {
    INSTANCE;

    private static final int FLAT = TerrainType.FLAT.ordinal();
    private static final int BIG_HILLS = TerrainType.BIG_HILLS.ordinal();
    private static final int MOUNTAINS = TerrainType.MOUNTAINS.ordinal();

    public int apply(IContext context, int value) {
        // Ocean is pass-through
        if (LayerUtilsTFCR.isShallowOcean(value)) {
            return value;
        } else {
            // Otherwise roll a six sided die
            int i = context.random(6);
            if (i == 0) { // 1/6 chance to turn into small hills in vanilla. Big hills in TFCR.
                return BIG_HILLS;
            } else {
                // 1/6 chance to turn into mountains
                // 4/6 chance to turn into flat region
                return i == 1 ? MOUNTAINS : FLAT;
            }
        }
    }
}
