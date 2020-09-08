package tfcr.init;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;
import tfcr.TFCR;

import java.io.File;

public interface ISelfRegisterItem {

    /**
     * Provides a default capability for self-registering this as an Item.
     *
     * During the "registerItems" method in ModItems, this method will be called
     * on every object that implements this interface. Blocks will get registered
     * as default ItemBlocks in the Building blocks creative category.
     * @param itemRegistry The registry where we will register this Item.
     */
    default Item registerItem(IForgeRegistry<Item> itemRegistry) {
        Item toReturn;
        if (this instanceof Item) {
            toReturn = (Item)this;
        } else if (this instanceof Block) {
            toReturn = new BlockItem((Block)this, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(TFCR.MODID, ((Block) this).getRegistryName().getPath());
        } else {
            // TODO make me a logging statement
            System.out.println("Failed to self-register non-item class: " + getClass() + " toString(): \"" + toString() + "\"");
            toReturn = null;
        }
        itemRegistry.register(toReturn);
        return toReturn;
    }
}
