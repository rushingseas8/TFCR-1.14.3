package tfcr.init;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import tfcr.blocks.LogBlock;
import tfcr.blocks.IBlockWood;
import tfcr.data.WoodType;

import javax.annotation.Nonnull;

public class ModTabs {
    public static final ItemGroup TFCR_WOOD = new ItemGroup("tfcrWood") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(LogBlock.get(WoodType.OAK));
        }

        @Override
        public boolean hasScrollbar() {
            return true;
        }

        @Override
        public void fill(@Nonnull NonNullList<ItemStack> items) {
            super.fill(items);

            items.sort((a, b) -> {
                Block blockA = ((ItemBlock)a.getItem()).getBlock();
                if (!(blockA instanceof IBlockWood)) {
                    throw new IllegalArgumentException("Found non-wood item in list: " + a.toString());
                }
                Block blockB = ((ItemBlock)b.getItem()).getBlock();
                if (!(blockB instanceof IBlockWood)) {
                    throw new IllegalArgumentException("Found non-wood item in list: " + b.toString());
                }

                // TODO implement an order based on the block type. Blocks of a given wood type
                //  are not guaranteed to be in any particular order.
                int woodOrder = ((IBlockWood)blockA).getWoodType().ordinal() - ((IBlockWood)blockB).getWoodType().ordinal();
                if (woodOrder != 0) {
                    return woodOrder;
                }

                return 0;
            });
        }
    };
}
