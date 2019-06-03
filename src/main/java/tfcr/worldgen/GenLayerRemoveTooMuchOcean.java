package tfcr.worldgen;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.layer.traits.ICastleTransformer;
import tfcr.data.TerrainType;

import static tfcr.worldgen.LayerUtilsTFCR.FLAT;

public enum GenLayerRemoveTooMuchOcean implements ICastleTransformer {
    INSTANCE;

    /**
     * If a region is completely regular Ocean (NSEW + C), then flip a coin.
     * There's a 50% chance of turning it into a single region of Flat terrain.
     * Otherwise, it remains Ocean.
     *
     * @param context
     * @param south
     * @param east
     * @param north
     * @param west
     * @param center
     * @return
     */
    public int apply(IContext context, int south, int east, int north, int west, int center) {
        return LayerUtilsTFCR.isShallowOcean(center) && LayerUtilsTFCR.isShallowOcean(south) && LayerUtilsTFCR.isShallowOcean(east) && LayerUtilsTFCR.isShallowOcean(west) && LayerUtilsTFCR.isShallowOcean(north) && context.random(2) == 0 ? FLAT : center;
    }
}
