package tfcr.blocks;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import tfcr.TFCR;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;
import tfcr.init.ModItems;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// TODO make it so dirt will turn into mud if near water (water/dirt onplace?)
public class MudBlock extends Block implements ISelfRegisterBlock, ISelfRegisterItem {

    private static MudBlock INSTANCE;

    public MudBlock() {
        super(Properties.from(Blocks.CLAY).sound(SoundType.WET_GRASS));
        setRegistryName(TFCR.MODID, "mud");
    }

    // TODO check if this works; not sure how loot tables differ from old system
    @Nonnull
    @Override
    public List<ItemStack> getDrops(@Nonnull BlockState state, @Nonnull LootContext.Builder builder) {
        List<ItemStack> toReturn = new ArrayList<>();
        toReturn.add(new ItemStack(ModItems.mud_ball, 4));
        return toReturn;
    }

    private static void init() {
        INSTANCE = new MudBlock();
    }

    public static MudBlock get() {
        if (INSTANCE == null) {
            init();
        }
        return INSTANCE;
    }
}
