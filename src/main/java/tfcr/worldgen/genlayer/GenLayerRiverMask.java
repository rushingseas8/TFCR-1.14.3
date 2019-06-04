package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.area.AreaDimension;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.layer.traits.IAreaTransformer2;
import net.minecraft.world.gen.layer.traits.IDimOffset0Transformer;
import tfcr.worldgen.LayerUtilsTFCR;

/**
 * Based on GenLayerRiverMix. If a river is present in the added river mask, then it will
 * be mixed into the first area. Else, returns the first area.
 */
public enum GenLayerRiverMask implements IAreaTransformer2, IDimOffset0Transformer {
    INSTANCE;

    public int apply(IContext context, AreaDimension dimensionIn, IArea landArea, IArea riverMask, int x, int z) {
        int i = landArea.getValue(x, z);
        int j = riverMask.getValue(x, z);

        if (LayerUtilsTFCR.isOcean(i)) {
            // Pass-through ocean
            return i;
        } else if (j == LayerUtilsTFCR.RIVER) {
            // If it's a river, then take the river
            return j;
        } else {
            // Else take the land
            return i;
        }
    }
}
