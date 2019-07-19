package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraftforge.registries.IForgeRegistry;
import tfcr.TFCR;
import tfcr.data.WoodType;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;
import tfcr.init.ModItems;
import tfcr.init.ModTabs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LeavesBlock extends net.minecraft.block.LeavesBlock implements ISelfRegisterBlock, ISelfRegisterItem, IBlockWood {

    private static LeavesBlock[] allBlocks;

    /**
     * What type of wood are we mapped to?
     */
    public WoodType woodType;

    /**
     * How many trees are we a part of?
     *
     * This is used to deal with multiple trees growing in close proximity to
     * one another. If two trees of the same type share a leaf canopy, then
     * this property is increased to 2. When a tree grows, instead of
     * destroying the leaf block, this property is decreased. When it hits 0,
     * it can be actually fully removed when a tree grows.
     *
     * Destroying this block by means other than growing (via Player, fire, etc.)
     * ignores this property.
     *
     * A value of 0 is not possibly naturally, and so indicates a player-placed
     * leaf block. A leaf block with this value will not decay.
     * TODO: evaluate if these leaf blocks need to decay. Player-placed leaf blocks
     *  shouldn't decay, and if we have a timber-like tree destruction system, then
     *  there won't ever be hanging leaf blocks. If so, we can remove the LeavesBlock
     *  subclassing to minimize the number of IDs we take up.
     */
    public static final IntegerProperty NUM_TREES = IntegerProperty.create("num_trees", 0, 16);

    public LeavesBlock(WoodType woodType) {
        super(Block.Properties.from(Blocks.OAK_LEAVES));
        this.woodType = woodType;
        setRegistryName(TFCR.MODID, "leaves/" + woodType.getName());
    }

    private static void init() {
        allBlocks = new LeavesBlock[WoodType.values().length];
        WoodType[] values = WoodType.values();
        for (int i = 0; i < values.length; i++) {
            WoodType type = values[i];
            allBlocks[i] = new LeavesBlock(type);
        }
    }

    public static List<LeavesBlock> getAllBlocks() {
        if (allBlocks == null) {
            init();
        }
        return Arrays.asList(allBlocks);
    }

    public static LeavesBlock get(WoodType type) {
        if (allBlocks == null) {
            init();
        }
        return allBlocks[type.ordinal()];
    }

    @Override
    public void registerItem(IForgeRegistry<Item> itemRegistry) {
        itemRegistry.register(new BlockItem(this, new Item.Properties().group(ModTabs.TFCR_WOOD)).setRegistryName(TFCR.MODID, getRegistryName().getPath()));
    }

    @Override
    public WoodType getWoodType() {
        return this.woodType;
    }

    @Override
    public void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE).add(PERSISTENT).add(NUM_TREES);
        // TODO if we remove LeavesBlock subclass then remove distance/persistent
    }

    @Override
    public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
        ArrayList<ItemStack> drops = new ArrayList<>();

        int leafCount = builder.getWorld().getRandom().nextInt(3) + 1;
        int stickCount = builder.getWorld().getRandom().nextFloat() < 0.25f ? 1 : 0;

        drops.add(new ItemStack(ModItems.leaves, leafCount));
        drops.add(new ItemStack(Items.STICK, stickCount));
        return drops;
    }
}
