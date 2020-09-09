package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.INoiseRandom;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.layer.traits.IAreaTransformer2;
import net.minecraft.world.gen.layer.traits.IDimOffset0Transformer;
import tfcr.worldgen.LayerUtilsTFCR;

/**
 * Based on GenLayerRiverMix. If a river is present in the added river mask, then it will
 * be mixed into the first area. Else, returns the first area.
 */
public enum RiverMaskLayer implements IAreaTransformer2, IDimOffset0Transformer {
    INSTANCE;

    @Override
    public int apply(INoiseRandom rand, IArea landArea, IArea riverMask, int x, int z) {
        int i = landArea.getValue(x, z);
        int j = riverMask.getValue(x, z);

        if (LayerUtilsTFCR.isOcean(i)) {
            // Pass-through ocean
            return i;
        } else if (j > 0) {
            // If it's a river, then take the river
            return j;
        } else {
            // Else take the land
            return i;
        }
    }
}
