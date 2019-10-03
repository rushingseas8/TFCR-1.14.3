package tfcr.events;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import tfcr.blocks.DirtBlock;
import tfcr.blocks.FarmlandBlock;
import tfcr.data.Fertility;

import java.util.Map;

@Mod.EventBusSubscriber
public class FarmingHooks {

    // Copied from ItemHoe. Map of what blocks can be hoed, and what the result will be.
    // Modified to return TFCR farmland instead.
    private static final Map<Block, BlockState> HOE_LOOKUP = Maps.newHashMap(new ImmutableMap.Builder<Block, BlockState>()
            // Vanilla blocks --> TFCR blocks
            .put(Blocks.GRASS_BLOCK, FarmlandBlock.get(Fertility.NORMAL).getDefaultState())
            .put(Blocks.GRASS_PATH, FarmlandBlock.get(Fertility.NORMAL).getDefaultState())
            .put(Blocks.DIRT, FarmlandBlock.get(Fertility.NORMAL).getDefaultState())
            .put(Blocks.COARSE_DIRT, DirtBlock.get(Fertility.INFERTILE).getDefaultState())

            // TFCR --> TFCR blocks
            .put(DirtBlock.get(Fertility.BARREN), FarmlandBlock.get(Fertility.BARREN).getDefaultState())
            .put(DirtBlock.get(Fertility.INFERTILE), FarmlandBlock.get(Fertility.INFERTILE).getDefaultState())
            .put(DirtBlock.get(Fertility.NORMAL), FarmlandBlock.get(Fertility.NORMAL).getDefaultState())
            .put(DirtBlock.get(Fertility.FERTILE), FarmlandBlock.get(Fertility.FERTILE).getDefaultState())
            .build()
    );

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onHoeUse(UseHoeEvent useHoeEvent) {
        System.out.println("On hoe used called!");

        ItemUseContext context = useHoeEvent.getContext();
        BlockPos pos = context.getPos();
        World world = context.getWorld();

        // Ensure we can't till the ceiling, and that the targeted block has air above it
        if (context.getFace() != Direction.DOWN && world.isAirBlock(pos.up())) {
            // Lookup the targeted block
            BlockState result = HOE_LOOKUP.get(world.getBlockState(pos).getBlock());
            if (result != null) {
                // Ok, successful tilling of soil. Set the state, play a sound, and reduce hoe durability.
                PlayerEntity playerentity = context.getPlayer();
                world.playSound(playerentity, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                if (!world.isRemote) {
                    world.setBlockState(pos, result, 11);
                    if (playerentity != null) {
                        context.getItem().damageItem(1, playerentity, (p_220043_1_) -> {
                            p_220043_1_.sendBreakAnimation(context.getHand());
                        });
                    }
                }

                //return ActionResultType.SUCCESS;
                // Mark this as a successful action. This stops additional processing.
                useHoeEvent.setResult(Event.Result.ALLOW);
                useHoeEvent.setCanceled(true);
            }
        }

        // I don't think this is needed, but pass-through processing if we failed to till.
        useHoeEvent.setResult(Event.Result.DEFAULT);
    }
}
