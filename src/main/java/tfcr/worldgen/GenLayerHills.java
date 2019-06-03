package tfcr.worldgen;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.area.AreaDimension;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.layer.traits.IAreaTransformer2;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import net.minecraft.world.gen.layer.traits.IDimOffset1Transformer;

public enum GenLayerHills implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(IContext context, int south, int east, int north, int west, int center) {
        // Ignore ocean
        if (LayerUtilsTFCR.isOcean(center)) {
            return center;
        }

        // Check if we're surrounded by similar height biomes
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
            // TODO next: If it's max height (mountain), instead chance to lower by 1.
            if (center != LayerUtilsTFCR.MOUNTAINS) {
                return center + 1;
            }
        }

        // TODO also check for ocean- chance to raise it into an island as well
        return center;
    }
}
