package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.FlowerBlock;
import net.minecraft.potion.Effects;
import tfcr.TFCR;

/**
 * Generic TFCR flower block.
 */
public class TFCRFlowerBlock extends FlowerBlock {

    public TFCRFlowerBlock(String name) {
        super(Effects.SLOWNESS, 0, Block.Properties.from(Blocks.POPPY));
        this.setRegistryName(TFCR.MODID, name);
    }
}
