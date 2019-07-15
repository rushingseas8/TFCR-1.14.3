package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import tfcr.worldgen.LayerUtilsTFCR;

import static tfcr.worldgen.LayerUtilsTFCR.FLAT;

/**
 * If a given tile is ocean, and completely surrounded by ocean, then there is
 * a 1/2 chance to turn it into flat terrain. Otherwise, it remains ocean.
 */
public enum RemoveTooMuchOceanLayer implements ICastleTransformer {
    INSTANCE;

    /**
     * If a region is completely regular Ocean (NSEW + C), then flip a coin.
     * There's a 50% chance of turning it into a single region of Flat terrain.
     * Otherwise, it remains Ocean.
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
        return LayerUtilsTFCR.isShallowOcean(center) && LayerUtilsTFCR.isShallowOcean(south) && LayerUtilsTFCR.isShallowOcean(east) && LayerUtilsTFCR.isShallowOcean(west) && LayerUtilsTFCR.isShallowOcean(north) && rand.random(2) == 0 ? FLAT : center;
    }
}
