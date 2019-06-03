package tfcr.worldgen;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

import static tfcr.worldgen.LayerUtilsTFCR.hasMountain;
import static tfcr.worldgen.LayerUtilsTFCR.hasOcean;

public enum GenLayerShore implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(IContext context, int south, int east, int north, int west, int center) {
        // Surrounded by ocean and mountain, so we should be a steep beach (cliff).
        if (hasOcean(south, east, north, west) && hasMountain(south, east, north, west)) {
            return LayerUtilsTFCR.CLIFF;
        }

        // Center is not ocean or river, but we're surrounded by ocean. Return beach.
        if (!LayerUtilsTFCR.isOcean(center) && center != LayerUtilsTFCR.RIVER && hasOcean(south, east, north, west)) {
            return LayerUtilsTFCR.BEACH;
        }

        return center;
    }
}
