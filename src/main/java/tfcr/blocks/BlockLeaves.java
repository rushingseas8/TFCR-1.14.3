package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReaderBase;
import net.minecraftforge.registries.IForgeRegistry;
import tfcr.TFCR;
import tfcr.data.WoodType;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class BlockLeaves extends net.minecraft.block.BlockLeaves implements ISelfRegisterBlock, ISelfRegisterItem {

    private static BlockLeaves[] allBlocks;

    private WoodType woodType;

    public BlockLeaves(WoodType woodType) {
        super(Block.Properties.from(Blocks.OAK_LEAVES));
        setRegistryName(TFCR.MODID, "leaves/" + woodType.getName());
    }

    private static void init() {
        allBlocks = new BlockLeaves[WoodType.values().length];
        WoodType[] values = WoodType.values();
        for (int i = 0; i < values.length; i++) {
            WoodType type = values[i];
            allBlocks[i] = new BlockLeaves(type);
        }
    }

    public static List<BlockLeaves> getAllBlocks() {
        if (allBlocks == null) {
            init();
        }
        return Arrays.asList(allBlocks);
    }

    public static BlockLeaves get(WoodType type) {
        if (allBlocks == null) {
            init();
        }
        return allBlocks[type.ordinal()];
    }
}
