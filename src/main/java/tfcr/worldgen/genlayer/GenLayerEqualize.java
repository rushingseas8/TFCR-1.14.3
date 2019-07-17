package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import tfcr.worldgen.LayerUtilsTFCR;

import static tfcr.worldgen.LayerUtilsTFCR.hasBigHill;
import static tfcr.worldgen.LayerUtilsTFCR.hasMountain;
import static tfcr.worldgen.LayerUtilsTFCR.hasOcean;

/**
 * Custom TFCR code. Normalizes the heightmap.
 *
 * Tall terrains, like mountains and big hills, will have their size reduced by one
 * if they're adjacent to oceans. This is to try to normalize cliffs. Short terrains,
 * like flat and small hills, will have their size increased by one if they're next
 * to mountains or big hills, or just mountains for small hills.
 *
 * Terrains both next to mountains and oceans will later be turned into cliffs by
 * GenLayerShore.
 */
public enum GenLayerEqualize implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(IContext context, int south, int east, int north, int west, int center) {
        // Skip ocean
        if (LayerUtilsTFCR.isOcean(center)) {
            return center;
        }

        if (center == LayerUtilsTFCR.MOUNTAINS && hasOcean(south, east, north, west)) {
            return LayerUtilsTFCR.BIG_HILLS;
        }

        if (center == LayerUtilsTFCR.BIG_HILLS && hasOcean(south, east, north, west)) {
            return LayerUtilsTFCR.SMALL_HILLS;
        }

        if (center == LayerUtilsTFCR.SMALL_HILLS && hasMountain(south, east, north, west)) {
            return LayerUtilsTFCR.BIG_HILLS;
        }

        if (center == LayerUtilsTFCR.FLAT && (hasBigHill(south, east, north, west) || hasMountain(south, east, north, west))) {
            return LayerUtilsTFCR.SMALL_HILLS;
        }

        return center;
    }
}
