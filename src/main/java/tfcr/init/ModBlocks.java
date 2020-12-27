package tfcr.init;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.IUnbakedModel;
import net.minecraft.client.renderer.model.ModelBakery;
import net.minecraft.client.renderer.model.ModelResourceLocation;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.BasicState;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import tfcr.TFCR;
import tfcr.blocks.*;
import tfcr.tileentity.TreeTileEntity;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = TFCR.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(TFCR.MODID)
public class ModBlocks {

    public ModBlocks() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(ModBlocks::commonSetup);

    }

    public static ArrayList<Block> allBlocks = new ArrayList<>();

    @SubscribeEvent
    public static void commonSetup(FMLClientSetupEvent event) {
        if (FMLEnvironment.dist.isDedicatedServer()) {
            return;
        }
        System.out.println("Common setup!");
        OBJLoader.INSTANCE.addDomain(TFCR.MODID);
    }

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
        allBlocks.addAll(TallSaplingBlock.getAllBlocks());

        // Farmland, dirt, grass
        allBlocks.addAll(FarmlandBlock.getAllBlocks());
        allBlocks.addAll(DirtBlock.getAllBlocks());
        allBlocks.addAll(GrassBlock.getAllBlocks());

        // Crops
        allBlocks.addAll(CropBlock.getAllBlocks());

        // Add other one-off blocks
        allBlocks.add(new TFCRFlowerBlock("marsh_marigold"));
//        allBlocks.add(new TallSaplingBlock());
        allBlocks.add(MudBlock.get());
        allBlocks.add(WattleBlock.get());
        allBlocks.add(new SmallRockBlock(Block.Properties.from(Blocks.STONE).hardnessAndResistance(0f), "small_rock_block"));
        allBlocks.add(new LeafRoofBlock());
        allBlocks.add(new WickerBlock());

        allBlocks.add(new CampfireBlock());
//        allBlocks.add(FarmlandBlock.get());

        //allBlocks.add(new CropBlock(Block.Properties.from(Blocks.WHEAT), "tfcr_wheat"));

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

    @SubscribeEvent
    public static void onModelBakeEvent(ModelBakeEvent event) {
        System.out.println("Bake model event called!");
        try {
            IUnbakedModel model = ModelLoaderRegistry.getModelOrLogError(new ResourceLocation("tfcr:block/test.obj"), "Missing campfire model!");

            // tried:
            // position
            // item
            // block
            // POSITION_TEX_COLOR_NORMAL﻿
            // POSITION_TEX_COLOR
            // POSITION_TEX
            // OLDMODEL_POSITION_TEX_NORMAL
            if (model instanceof OBJModel) {
                System.out.println("Found campfire model!");
                IBakedModel bakedModel = model.bake(
                        event.getModelLoader(),
                        ModelLoader.defaultTextureGetter(),
                        new BasicState(model.getDefaultState(), false),
                        DefaultVertexFormats.POSITION_TEX_NORMAL
                );

                ResourceLocation campfireLocation = new ModelResourceLocation("tfcr:campfire", "");
//                System.out.println("Trying to replace model at: " + campfireLocation);
                event.getModelRegistry().put(campfireLocation, bakedModel);
                event.getModelRegistry().put(new ModelResourceLocation("stick", "inventory"), bakedModel);
            } else {
                System.out.println("Failed to find campfire model!");
            }
        } catch (Exception e) {
            System.out.println("BAKE ERROR: " + e);
            e.printStackTrace();
        }
    }
}
