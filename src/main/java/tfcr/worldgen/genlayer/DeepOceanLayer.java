package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import tfcr.data.TerrainType;
import tfcr.worldgen.LayerUtilsTFCR;

/**
 * Turns ocean tiles that are completely surrounded by other ocean tiles into
 * deep ocean tiles.
 */
public enum DeepOceanLayer implements ICastleTransformer {
    INSTANCE;

    private static final int DEEP_OCEAN = TerrainType.DEEP_OCEAN.ordinal();

    /**
     * Sets the center to be deep ocean if it is surrounded by at least 3 regular Ocean
     * terrain sections.
     *
     * @param rand
     * @param south
     * @param east
     * @param north
     * @param west
     * @param center
     * @return
     */
    public int apply(INoiseRandom rand, int south, int east, int north, int west, int center) {
        if (LayerUtilsTFCR.isShallowOcean(center)) {
            int i = 0;
            if (LayerUtilsTFCR.isShallowOcean(south)) {
                ++i;
            }

            if (LayerUtilsTFCR.isShallowOcean(east)) {
                ++i;
            }

            if (LayerUtilsTFCR.isShallowOcean(west)) {
                ++i;
            }

            if (LayerUtilsTFCR.isShallowOcean(north)) {
                ++i;
            }

            if (i > 3) {
                return DEEP_OCEAN;
            }
        }

        return center;
    }
}
