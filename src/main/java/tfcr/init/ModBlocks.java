package tfcr.init;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import tfcr.TFCR;
import tfcr.blocks.BranchBlock;
import tfcr.blocks.MudBlock;
import tfcr.blocks.SmallRockBlock;
import tfcr.blocks.TFCRFlowerBlock;
import tfcr.blocks.LeavesBlock;
import tfcr.blocks.LogBlock;
import tfcr.blocks.SaplingBlock;
import tfcr.blocks.TallSaplingBlock;
import tfcr.blocks.WattleBlock;
import tfcr.tileentity.TreeTileEntity;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = TFCR.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(TFCR.MODID)
public class ModBlocks {

    public static ArrayList<Block> allBlocks = new ArrayList<>();

    /**
     * A helper method that initializes all the modded Block references.
     * This sets them up for registration later.
     */
    private static void initBlocks() {
        System.out.println("Initializing block references.");

        // Full block wooden log variations.
        allBlocks.addAll(LogBlock.getAllBlocks());

        // Tree leaves
        allBlocks.addAll(LeavesBlock.getAllBlocks());

        // Add all branch variations
        allBlocks.addAll(BranchBlock.getAllBlocks());

        // Sapling variants
        allBlocks.addAll(SaplingBlock.getAllBlocks());

        // Add other one-off blocks
        allBlocks.add(new TFCRFlowerBlock("marsh_marigold"));
        allBlocks.add(new TallSaplingBlock());
        allBlocks.add(MudBlock.get());
        allBlocks.add(WattleBlock.get());
        allBlocks.add(new SmallRockBlock(Block.Properties.from(Blocks.STONE).hardnessAndResistance(0f), "small_rock_block"));

        System.out.println("Done initializing.");
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        initBlocks();

        IForgeRegistry<Block> registry = event.getRegistry();

        System.out.println("Registering blocks.");
        System.out.println("Found " + allBlocks.size() + " blocks to register.");
        for (int i = 0; i < allBlocks.size(); i++) {
            Block b = allBlocks.get(i);
            System.out.println("[" + i + "] Registering block: " + b.getRegistryName());
            if (b instanceof ISelfRegisterBlock) {
                ((ISelfRegisterBlock) b).registerBlock(registry);
            } else {
//                Logging.getLogger().warning("Found non self-registering block: \"" + b + "\". Bug?");
                event.getRegistry().register(b);
            }
        }
    }

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        System.out.println("Registering tileentities.");
        TreeTileEntity.registerTileEntity(event.getRegistry());
    }
}
