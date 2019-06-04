package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import tfcr.data.TerrainType;

/**
 * Direct copy of Vanilla GenLayerRiver.
 *
 * I'm not 100% sure on why it works, but the algorithm is this:
 *
 * Check a river filter. If the biome at the given ID is >= 2, then take 2 +
 * the lowest bit of the biome ID (so return 2 or 3). Else, return the value
 * (so return 0 or 1).
 *
 * If the filter value around the center tile matches the center tile, then
 * the center tile is NOT a river. Else, it is.
 *
 * My guess is that this results in rivers that trace the outlines of biome
 * boundaries, and do not intersect themselves often based on the order in which
 * the biomes are generated.
 */
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
