package tfcr.init;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDoublePlant;
import net.minecraft.block.material.Material;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;
import tfcr.TFCR;
import tfcr.blocks.BlockBranch;
import tfcr.blocks.BlockFlowerTFCR;
import tfcr.blocks.BlockSapling;
import tfcr.tileentity.TileEntityTree;

import java.util.ArrayList;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = TFCR.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
@ObjectHolder(TFCR.MODID)
public class ModBlocks {

//    public static final BlockBranch block_branch = null;
//    public static final BlockFlowerTFCR marsh_marigold = null;



//    public static final BlockBranch block_branch_8 = new BlockBranch(8);
    public static ArrayList<Block> allBlocks = new ArrayList<>();

    /**
     * A helper method that initializes all the modded Block references.
     * This sets them up for registration later.
     */
    private static void initBlocks() {
        System.out.println("Initializing block references.");

        // Add all branch variations
        //BlockBranch.init();
        allBlocks.addAll(BlockBranch.getAllBlocks());

        // Sapling variants
        //BlockSapling.init();
        allBlocks.addAll(BlockSapling.getAllBlocks());

        // Add other one-off blocks
        allBlocks.add(new BlockFlowerTFCR("marsh_marigold"));
        allBlocks.add(new BlockDoublePlant(Block.Properties.from(Blocks.TALL_GRASS)).setRegistryName(TFCR.MODID, "tall_sapling"));

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

        // Register sapling tick
        MinecraftForge.EVENT_BUS.register(ModBlocks.class);
    }

    @SubscribeEvent
    public static void registerTileEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        System.out.println("Registering tileentities.");
        TileEntityTree.registerTileEntity(event.getRegistry());
    }


    private static final UUID saplingSlowUUID = UUID.fromString("83BD3C05-50EB-460B-8961-615633A6D813");
    private static final AttributeModifier saplingSlow = new AttributeModifier(saplingSlowUUID, "SAPLING_SLOW", -0.2D, 1);

    /**
     * Handler for player tick events. Called every tick for the player.
     *
     * Currently used to tell if the player is intersecting with a TFCR block that slows them down,
     * which includes sapling blocks at the moment.
     *
     * TODO Is this the best place to put this code?
     * @param playerTickEvent A player tick event.
     */
//    @SubscribeEvent
//    public static void onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent) {
//        if (playerTickEvent.side == LogicalSide.SERVER) {
//
//            EntityPlayer player = playerTickEvent.player;
//            World world = player.getEntityWorld();
//
//            // Get all blocks that any part of the player model is colliding with.
//            Iterable<BlockPos.MutableBlockPos> playerColliding = BlockPos.getAllInBoxMutable(
//                    (int) Math.floor(player.posX - (player.width / 2)),
//                    (int) Math.floor(player.posY - (player.height / 2)),
//                    (int) Math.floor(player.posZ - (player.width / 2)),
//                    (int) Math.floor(player.posX + (player.width / 2)),
//                    (int) Math.floor(player.posY + (player.height / 2)),
//                    (int) Math.floor(player.posZ + (player.width / 2))
//            );
//
//            boolean shouldSlow = false;
//            for (BlockPos bp : playerColliding) {
//                shouldSlow |= world.getBlockState(bp).getBlock() instanceof BlockSapling;
//            }
//
//            IAttributeInstance movement = playerTickEvent.player.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
//
//            if (shouldSlow) {
//                // Add modifier, if it's not already applied
//                if (!movement.hasModifier(saplingSlow)) {
//                    movement.applyModifier(saplingSlow);
//                }
//            } else {
//                // Remove modifier, if it exists
//                if (movement.hasModifier(saplingSlow)) {
//                    movement.removeModifier(saplingSlow);
//                }
//            }
//        }
//    }
}
