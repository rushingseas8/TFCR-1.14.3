package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.init.Blocks;
import tfcr.TFCR;

/**
 * Generic TFCR flower block.
 */
public class BlockFlowerTFCR extends BlockFlower {

    public BlockFlowerTFCR(String name) {
        super(Block.Properties.from(Blocks.POPPY));
        this.setRegistryName(TFCR.MODID, name);
    }

}
