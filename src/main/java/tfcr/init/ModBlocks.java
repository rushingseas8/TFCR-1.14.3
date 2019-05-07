package tfcr.init;

import net.minecraft.block.Block;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import tfcr.TFCR;
import tfcr.blocks.BlockBranch;

@Mod.EventBusSubscriber(modid = TFCR.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(TFCR.MODID)
public class ModBlocks {

    public static final BlockBranch block_branch = null;

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        System.out.println("Register blocks called");
        event.getRegistry().register(
                new BlockBranch()
        );
    }
}
