package tfcr.init;

import net.minecraft.block.Block;
import net.minecraftforge.registries.IForgeRegistry;

public interface ISelfRegisterBlock {
    default void registerBlock(IForgeRegistry<Block> registry) {
        if (this instanceof Block) {
            registry.register((Block)this);
        } else {
            System.out.println("Failed to self-register block");
        }
    }
}
