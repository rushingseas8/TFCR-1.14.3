package tfcr.init;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.registries.IForgeRegistry;
import tfcr.TFCR;

public interface ISelfRegisterItem {
    default void registerItem(IForgeRegistry<Item> itemRegistry) {
        if (this instanceof Item) {
            itemRegistry.register((Item)this);
        } else if (this instanceof Block) {
            itemRegistry.register(new ItemBlock((Block)this, new Item.Properties().group(ItemGroup.BUILDING_BLOCKS)).setRegistryName(TFCR.MODID, ((Block) this).getRegistryName().getPath()));
        } else {
            // TODO make me a logging statement
            System.out.println("Failed to self-register non-item class: " + getClass() + " toString(): \"" + toString() + "\"");
        }
    }
}
