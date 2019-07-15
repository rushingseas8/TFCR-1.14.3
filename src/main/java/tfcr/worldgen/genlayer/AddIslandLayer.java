package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.IBishopTransformer;
import tfcr.worldgen.LayerUtilsTFCR;

import static tfcr.worldgen.LayerUtilsTFCR.SMALL_HILLS;

/**
 * This method, in Vanilla, adds Forest biome islands to the ocean. Here,
 * it adds the equivalent TerrainType, which is a small hill region.
 */
public enum AddIslandLayer implements IBishopTransformer {
    INSTANCE;

    /**
     * IBishopTransformer takes elements in this pattern:
     *
     * 0 - 1
     * - 4 -
     * 3 - 2
     *
     * Where 3 is where we are currently modifying.
     *
     * This method, in Vanilla, adds Forest biome islands to the ocean. Here,
     * it adds the equivalent TerrainType, which is a small hill region.
     * 
     * @param rand
     * @param northwest
     * @param northeast
     * @param southeast
     * @param southwest
     * @param center
     * @return
     */
    public int apply(INoiseRandom rand, int northwest, int northeast, int southeast, int southwest, int center) {
        if (!LayerUtilsTFCR.isShallowOcean(center) || LayerUtilsTFCR.isShallowOcean(southwest) && LayerUtilsTFCR.isShallowOcean(southeast) && LayerUtilsTFCR.isShallowOcean(northwest) && LayerUtilsTFCR.isShallowOcean(northeast)) {
            if (!LayerUtilsTFCR.isShallowOcean(center) && (LayerUtilsTFCR.isShallowOcean(southwest) || LayerUtilsTFCR.isShallowOcean(northwest) || LayerUtilsTFCR.isShallowOcean(southeast) || LayerUtilsTFCR.isShallowOcean(northeast)) && rand.random(5) == 0) {
                if (LayerUtilsTFCR.isShallowOcean(southwest)) {
                    return center == SMALL_HILLS ? SMALL_HILLS : southwest;
                }

                if (LayerUtilsTFCR.isShallowOcean(northwest)) {
                    return center == SMALL_HILLS ? SMALL_HILLS : northwest;
                }

                if (LayerUtilsTFCR.isShallowOcean(southeast)) {
                    return center == SMALL_HILLS ? SMALL_HILLS : southeast;
                }

                if (LayerUtilsTFCR.isShallowOcean(northeast)) {
                    return center == SMALL_HILLS ? SMALL_HILLS : northeast;
                }
            }

            return center;
        } else {
            int i = 1;
            int j = 1;
            if (!LayerUtilsTFCR.isShallowOcean(southwest) && rand.random(i++) == 0) {
                j = southwest;
            }

            if (!LayerUtilsTFCR.isShallowOcean(southeast) && rand.random(i++) == 0) {
                j = southeast;
            }

            if (!LayerUtilsTFCR.isShallowOcean(northwest) && rand.random(i++) == 0) {
                j = northwest;
            }

            if (!LayerUtilsTFCR.isShallowOcean(northeast) && rand.random(i++) == 0) {
                j = northeast;
            }

            if (rand.random(3) == 0) {
                return j;
            } else {
                return j == SMALL_HILLS ? SMALL_HILLS : center;
            }
        }
    }
}
