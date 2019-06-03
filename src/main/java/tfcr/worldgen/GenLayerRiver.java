package tfcr.worldgen;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import tfcr.data.TerrainType;

public enum GenLayerRiver implements ICastleTransformer {
    INSTANCE;

    private static final int RIVER = TerrainType.RIVER.ordinal();

    @Override
    public int apply(IContext context, int south, int east, int north, int west, int center) {
        // TODO implement Vanilla GenLayerRiver, but first understand what that does

        // Skip ocean
//        if (LayerUtilsTFCR.isOcean(center)) {
//            return -1;
//        }

        int i = riverFilter(center);
        return i == riverFilter(north) && i == riverFilter(south) && i == riverFilter(east) && i == riverFilter(west) ? -1 : RIVER;
    }

    private static int riverFilter(int value) {
        // If value is 0 or 1, return value.
        // If value >= 2, then take the lowest bit and add 2, so return 2 or 3.


        return value >= 2 ? 2 + (value & 1) : value;
//        if (LayerUtilsTFCR.isOcean(value)) {
//            return 1000 + (value & 1);
//        }
//        return value;
    }
}
