package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import tfcr.worldgen.LayerUtilsTFCR;

/**
 * Modified heavily from Vanilla code.
 *
 * In Vanilla my best guess is that there's a 1/3 chance that fully enclosed
 * biomes will be turned into a "hills" variation. For example, BirchForest
 * can turn into BirchForestHills. There's a bunch of other checks, however,
 * that I didn't look into.
 *
 * In TFCR, this method does the above logic: if a biome is surrounded by at
 * least 3/4 biomes of the same height, then there's a further 1/3 chance that
 * the center biome will have its height raised by 1. This turns flat land
 * into hills, and hills into mountains.
 */
public enum GenLayerHills implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(IContext context, int south, int east, int north, int west, int center) {
        // Ignore ocean
        if (LayerUtilsTFCR.isOcean(center)) {
            return center;
        }

        // Check if we're surrounded by similar height placeholderBiomes
        int countSimilar = 0;
        if (south == center) {
            countSimilar++;
        }
        if (east == center) {
            countSimilar++;
        }
        if (north == center) {
            countSimilar++;
        }
        if (west == center) {
            countSimilar++;
        }

        // If 3/4 are the same height as the center..
        if (countSimilar >= 3 && context.random(3) == 0) {
            // Then roll a 3-sided die. 1/3 chance to raise the land in the center by 1.
            // TODO next: If it's max height (mountain), instead chance to lower by 1?
            if (center != LayerUtilsTFCR.MOUNTAINS) {
                return center + 1;
            }
        }

        // TODO also check for ocean- chance to raise it into an island as well
        return center;
    }
}
