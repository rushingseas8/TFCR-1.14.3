package tfcr.init;

import com.sun.javafx.binding.Logging;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.FoliageColors;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeColors;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import tfcr.TFCR;
import tfcr.blocks.*;
import tfcr.tileentity.TileEntityTree;

import java.util.ArrayList;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = TFCR.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(TFCR.MODID)
public class ModBlocks {


    public static final Block tall_sapling = new BlockDoublePlant(Block.Properties.from(Blocks.TALL_GRASS)).setRegistryName(TFCR.MODID, "tall_sapling");
    public static ArrayList<Block> allBlocks = new ArrayList<>();

    /**
     * A helper method that initializes all the modded Block references.
     * This sets them up for registration later.
     */
    private static void initBlocks() {
        System.out.println("Initializing block references.");

        // Full block wooden log variations.
        allBlocks.addAll(BlockLog.getAllBlocks());

        // Tree leaves
        allBlocks.addAll(BlockLeaves.getAllBlocks());

        // Add all branch variations
        allBlocks.addAll(BlockBranch.getAllBlocks());

        // Sapling variants
        allBlocks.addAll(BlockSapling.getAllBlocks());

        // Add other one-off blocks
        allBlocks.add(new BlockFlowerTFCR("marsh_marigold"));
        allBlocks.add(tall_sapling);

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
        TileEntityTree.registerTileEntity(event.getRegistry());
    }
}
