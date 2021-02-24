package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import tfcr.data.TerrainType;
import tfcr.worldgen.LayerUtilsTFCR;

import static tfcr.worldgen.LayerUtilsTFCR.RIVER;

/**
 * Direct copy of Vanilla RiverLayer.
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
public enum RiverLayer implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(INoiseRandom rand, int south, int east, int north, int west, int center) {
        // TODO implement Vanilla RiverLayer, but first understand what that does

        int i = riverFilter(center);
        if (i == riverFilter(north) && i == riverFilter(south) && i == riverFilter(east) && i == riverFilter(west)) {
            return -1;
        } else {
            return center <= 300000 ? RIVER : LayerUtilsTFCR.ESTUARY;
        }
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
