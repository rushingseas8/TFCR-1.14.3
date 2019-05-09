package tfcr.init;

import net.minecraft.block.Block;
import net.minecraftforge.registries.IForgeRegistry;

public interface ISelfRegisterBlock {

    /**
     * Provides a default capability for self-registering this as a Block.
     *
     * During the "initBlocks" method in ModBlocks, this method will be called
     * on every Block that implements this interface.
     * @param registry The registry we should register this Block to.
     */
    default void registerBlock(IForgeRegistry<Block> registry) {
        if (this instanceof Block) {
            registry.register((Block)this);
        } else {
            // TODO throw an exception
            System.out.println("Failed to self-register block");
        }
    }
}
