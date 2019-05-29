package tfcr.blocks;

import tfcr.data.WoodType;

/**
 * An interface for any TFCR Blocks that are based on a WoodType variation.
 * Allows for a simple way to access the type of wood any of the blocks map to.
 */
public interface IBlockWood {
    WoodType getWoodType();
}
