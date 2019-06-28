package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tfcr.TFCR;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;
import tfcr.init.ModItems;

import java.util.Random;

public class BlockMud extends Block implements ISelfRegisterBlock, ISelfRegisterItem {

    public BlockMud() {
        super(Block.Properties.from(Blocks.CLAY).sound(SoundType.WET_GRASS));
        setRegistryName(TFCR.MODID, "mud");
    }

    @Override
    public IItemProvider getItemDropped(IBlockState state, World worldIn, BlockPos pos, int fortune) {
        return ModItems.mud_ball;
    }

    @Override
    public int quantityDropped(IBlockState state, Random random) {
        return 4;
    }
}