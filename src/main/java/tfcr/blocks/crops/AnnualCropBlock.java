package tfcr.blocks.crops;

import tfcr.blocks.CropBlock;
import tfcr.data.CropType;

/**
 * Base class for TFCR crops that are annual, meaning they have a single seed-to-seed
 * cycle per season/year.
 */
public class AnnualCropBlock extends CropBlock {
    public AnnualCropBlock(Properties properties, CropType cropType) {
        super(properties, cropType);
    }
}
