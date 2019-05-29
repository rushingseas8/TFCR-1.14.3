package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraftforge.registries.IForgeRegistry;
import tfcr.TFCR;
import tfcr.data.WoodType;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;
import tfcr.init.ModTabs;

import java.util.Arrays;
import java.util.List;

public class BlockLeaves extends net.minecraft.block.BlockLeaves implements ISelfRegisterBlock, ISelfRegisterItem, IBlockWood {

    private static BlockLeaves[] allBlocks;

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
     *  there won't ever be hanging leaf blocks. If so, we can remove the BlockLeaves
     *  subclassing to minimize the number of IDs we take up.
     */
    public static final IntegerProperty NUM_TREES = IntegerProperty.create("num_trees", 0, 16);

    public BlockLeaves(WoodType woodType) {
        super(Block.Properties.from(Blocks.OAK_LEAVES));
        this.woodType = woodType;
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

    @Override
    public void registerItem(IForgeRegistry<Item> itemRegistry) {
        itemRegistry.register(new ItemBlock(this, new Item.Properties().group(ModTabs.TFCR_WOOD)).setRegistryName(TFCR.MODID, getRegistryName().getPath()));
    }

    @Override
    public WoodType getWoodType() {
        return this.woodType;
    }

    @Override
    public void fillStateContainer(StateContainer.Builder<Block, IBlockState> builder) {
        builder.add(DISTANCE).add(PERSISTENT).add(NUM_TREES);
        // TODO if we remove BlockLeaves subclass then remove distance/persistent
    }
}
