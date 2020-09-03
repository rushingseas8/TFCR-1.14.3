package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import tfcr.TFCR;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;

public class BlockLeafRoof extends Block implements ISelfRegisterBlock, ISelfRegisterItem {

    public BlockLeafRoof() {
        super(Block.Properties.from(Blocks.OAK_LEAVES));
        setRegistryName(TFCR.MODID, "leaf_roof");
    }
}
