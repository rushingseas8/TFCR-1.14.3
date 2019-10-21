package tfcr.items;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import tfcr.blocks.FarmlandBlock;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

/**
 * A class for ceramic jugs. Currently supports empty and full.
 *
 * TODO add drinking?
 */
public class JugItem extends TFCRItem {

    private static JugItem[] allItems;

    public enum JugFluid {
        EMPTY,
        WATER
        // TODO add saltwater
    }

    private JugFluid fluid;

    public JugItem(JugFluid fluid) {
        super(new Item.Properties().group(ItemGroup.TOOLS).maxDamage(8),
                fluid == JugFluid.EMPTY ? "empty_ceramic_jug" : "filled_ceramic_jug");
        this.fluid = fluid;
    }

    private static void init() {
        allItems = new JugItem[] {
                new JugItem(JugFluid.EMPTY),
                new JugItem(JugFluid.WATER)
        };
    }

    public static List<JugItem> getAllItems() {
        if (allItems == null) {
            init();
        }
        return Arrays.asList(allItems);
    }

    public static JugItem get(JugFluid fluid) {
        if (allItems == null) {
            init();
        }
        return allItems[fluid.ordinal()];
    }

    /**
     * Called when this Jug is right clicked with.
     *
     * When empty, if this Jug is used on a water tile (source or flowing), this
     * will replace the item with a water-filled Jug.
     *
     * When full, if this Jug is used on a TFCR farmland tile, it will be watered
     * for a period of time. If used on a water tile (source or flowing), this
     * Jug will be emptied.
     *
     * @return The result of the Action, possibly modifying the ItemStack held
     *  in the player's hand.
     */
    public @Nonnull ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
        // Currently held item, to know what to return
        ItemStack heldItem = playerIn.getHeldItem(handIn);

        // Manually ray trace. If we miss, then pass-through this right-click action.
        RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.ANY);
        if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
            return new ActionResult<>(ActionResultType.PASS, heldItem);
        } else if (raytraceresult.getType() != RayTraceResult.Type.BLOCK) {
            return new ActionResult<>(ActionResultType.PASS, heldItem);
        }

        // If we hit a block, then store the result.
        BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)raytraceresult;
        BlockPos blockPos = blockraytraceresult.getPos();

        BlockState blockState = worldIn.getBlockState(blockPos);

        // Logic for trying to pick up water with empty jug
        if (this.fluid == JugFluid.EMPTY) {
            Fluid fluid = blockState.getFluidState().getFluid();
            if (fluid.isIn(FluidTags.WATER)) {
                // Play water pickup sound and return full jug
                playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                return new ActionResult<>(ActionResultType.SUCCESS, new ItemStack(get(JugFluid.WATER)));
            }
        } else if (this.fluid == JugFluid.WATER) {
            // Logic for watering TFCR farmland
            if (blockState.getBlock() instanceof FarmlandBlock) {
                if (blockState.get(FarmlandBlock.MOISTURE) == 7) {
                    return new ActionResult<>(ActionResultType.FAIL, heldItem);
                } else {
                    // Water the farmland
                    if (!worldIn.isRemote()) {
                        worldIn.setBlockState(blockPos, blockState.with(FarmlandBlock.MOISTURE, 7));
                    }

                    // Play water sploosh sound
                    playerIn.playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1.0F, 1.0F);

                    // TODO Particles?

                    // If we're at max durability, return an empty jug. Else,
                    // decrease durability by 1 and return the result.
                    if (heldItem.getDamage() == heldItem.getMaxDamage() - 1) {
                        return new ActionResult<>(ActionResultType.SUCCESS, new ItemStack(get(JugFluid.EMPTY)));
                    } else {
                        heldItem.damageItem(1, playerIn, (T) -> {});
                        return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
                    }
                }
            } else if (blockState.getFluidState().getFluid().isIn(FluidTags.WATER)) { // Logic for refilling/emptying water
                // Logic for dumping water back into a water block
                // Only occurs when we're full on water.
                if (heldItem.getDamage() == 0) {
                    playerIn.playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1.0F, 1.0F);
                    return new ActionResult<>(ActionResultType.SUCCESS, new ItemStack(get(JugFluid.EMPTY)));
                } else {
                    // If we've used some water, fill the jug back up instead.
                    playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                    return new ActionResult<>(ActionResultType.SUCCESS, new ItemStack(get(JugFluid.WATER)));
                }
            }
        }

        // Default is a no-op.
        return new ActionResult<>(ActionResultType.PASS, heldItem);
    }
}
