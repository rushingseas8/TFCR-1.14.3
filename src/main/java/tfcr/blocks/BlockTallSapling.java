package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import tfcr.TFCR;
import tfcr.data.WoodType;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;
import tfcr.tileentity.TileEntityTree;

import javax.annotation.Nullable;

public class BlockTallSapling extends BlockDoublePlant implements ISelfRegisterBlock, ISelfRegisterItem {

    // TODO implement multiple wood type saplings
    private WoodType woodType;

    public BlockTallSapling() {
        super(Block.Properties.from(Blocks.TALL_GRASS));
        setRegistryName(TFCR.MODID, "tall_sapling");
    }

    @Override
    public boolean hasTileEntity(IBlockState state) {
        return state.get(HALF) == DoubleBlockHalf.LOWER;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(IBlockState state, IBlockReader world) {
        return new TileEntityTree();
    }
}
