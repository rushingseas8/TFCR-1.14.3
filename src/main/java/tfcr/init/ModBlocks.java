package tfcr.init;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import tfcr.TFCR;
import tfcr.blocks.BlockBranch;
import tfcr.blocks.BlockFlowerTFCR;
import tfcr.blocks.BlockSapling;
import tfcr.tileentity.TileEntityTree;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = TFCR.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(TFCR.MODID)
public class ModBlocks {

//    public static final BlockBranch block_branch = null;
//    public static final BlockFlowerTFCR marsh_marigold = null;

    public static final BlockBranch block_branch_8 = new BlockBranch(8);
    public static ArrayList<Block> allBlocks = new ArrayList<>();

    /**
     * A helper method that initializes all the modded Block references.
     * This sets them up for registration later.
     */
    private static void initBlocks() {
        System.out.println("Initializing block references.");

        // Add all branch variations
        for (int i = 2; i <= 14; i += 2) {
            if (i == 8) {
                allBlocks.add(block_branch_8);
                continue;
            }
            allBlocks.add(new BlockBranch(i));
        }

        // Add other one-off blocks
        allBlocks.add(new BlockFlowerTFCR("marsh_marigold"));
        allBlocks.add(new BlockSapling());

        System.out.println("Done initializing.");
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        initBlocks();

        IForgeRegistry<Block> registry = event.getRegistry();

        System.out.println("Registering blocks.");
        for (int i = 0; i < allBlocks.size(); i++) {
            Block b = allBlocks.get(i);
            if (b instanceof ISelfRegisterBlock) {
                ((ISelfRegisterBlock) b).registerBlock(registry);
            } else {
                event.getRegistry().register(b);
            }
        }

//        System.out.println("Register blocks called");
//        event.getRegistry().registerAll(
//                new BlockBranch(2),
//                new BlockFlowerTFCR("marsh_marigold")
//        );
    }

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        System.out.println("Registering tileentities.");
        TileEntityTree.registerTileEntity(event.getRegistry());
    }
}
