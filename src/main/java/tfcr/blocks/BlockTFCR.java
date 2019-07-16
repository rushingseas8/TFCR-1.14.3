package tfcr.blocks;

import net.minecraft.block.Block;
import tfcr.TFCR;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;

/**
 * Generic TFCR block template. Has no special properties, sets its registry name,
 * and self-registers a Block and ItemBlock.
 */
public class BlockTFCR extends Block implements ISelfRegisterItem, ISelfRegisterBlock {
    public BlockTFCR(Properties properties, String name) {
        super(properties);
        setRegistryName(TFCR.MODID, name);
    }
}
