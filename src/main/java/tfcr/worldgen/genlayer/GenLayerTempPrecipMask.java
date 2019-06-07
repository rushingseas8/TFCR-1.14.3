package tfcr.worldgen.genlayer;

import net.minecraft.world.gen.IContext;
import net.minecraft.world.gen.area.AreaDimension;
import net.minecraft.world.gen.area.IArea;
import net.minecraft.world.gen.layer.traits.IAreaTransformer2;
import net.minecraft.world.gen.layer.traits.IDimOffset1Transformer;
import tfcr.data.TerrainType;
import tfcr.worldgen.BiomeProviderTFCR;
import tfcr.worldgen.LayerUtilsTFCR;
import tfcr.worldgen.biome.BaseTFCRBiome;

/**
 * Handles combining the Temp/Precip layer with the structural worldgen.
 */
public enum GenLayerTempPrecipMask implements IAreaTransformer2, IDimOffset1Transformer {
    INSTANCE;


    // TODO currently mixing indices for placeholder biomes and concrete biomes. Should only
    //  return concrete biomes, since we'll soon get rid of placeholder biomes entirely.
    @Override
    public int apply(IContext context, AreaDimension dimensionIn, IArea placeholderArea, IArea tempPrecipArea, int x, int z) {
        int placeholderBiome = placeholderArea.getValue(x, z);
        int rawTempPrecip = tempPrecipArea.getValue(x, z);

        // Water pass-through
        if (LayerUtilsTFCR.isWater(placeholderBiome) ||
                placeholderBiome == LayerUtilsTFCR.CLIFF) {
            return placeholderBiome;
        }

        // Else, get temp/precip
        int precip = rawTempPrecip & 0xff; // Take the bottom 8 bits for precip
        int temp = (rawTempPrecip >> 8) - 100; // Take the next 8 bits for temp & normalize

        // Go through all the biomes
        for (BaseTFCRBiome biome : BiomeProviderTFCR.biomes) {
            // If any match the requested range..
            if (biome.matchesRange(temp, precip, TerrainType.values()[placeholderBiome])) {
                // Return their index
                return BiomeProviderTFCR.biomeClassToIndexLookup.get(biome.getClass());
            }
        }

        // Error. This shouldn't ever be hit.
        // TODO put a good default value here
        return placeholderBiome;
    }

}
