package tfcr.worldgen;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;

public enum GenLayerShore implements ICastleTransformer {
    INSTANCE;

    @Override
    public int apply(IContext context, int south, int east, int north, int west, int center) {
        if (hasOcean(south, east, north, west) && center == LayerUtilsTFCR.MOUNTAINS) {
            // TODO make some sort of transition biome for mountains -> ocean
            // crags?
        }

        // Center is not ocean, but we're surrounded by ocean. Return beach.
        if (hasOcean(south, east, north, west) && !LayerUtilsTFCR.isOcean(center) && center != LayerUtilsTFCR.RIVER) {
            return LayerUtilsTFCR.BEACH;
        }

        return center;
    }

    private boolean hasOcean(int south, int east, int north, int west) {
        return LayerUtilsTFCR.isOcean(south) ||
                LayerUtilsTFCR.isOcean(east) ||
                LayerUtilsTFCR.isOcean(west) ||
                LayerUtilsTFCR.isOcean(north);
    }
}
