package tfcr.blocks;

import net.minecraft.block.Blocks;
import net.minecraft.block.ContainerBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import tfcr.TFCR;
import tfcr.tileentity.CampfireTileEntity;

import javax.annotation.Nullable;

public class CampfireBlock extends ContainerBlock {

    public CampfireBlock() {
        super(Properties.from(Blocks.CAMPFIRE));
        setRegistryName(TFCR.MODID, "campfire");
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new CampfireTileEntity();
    }
}
