package tfcr.items;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
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
import javax.annotation.ParametersAreNonnullByDefault;
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
        super(new Item.Properties().group(ItemGroup.TOOLS).maxStackSize(1),
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
        ItemStack itemstack = playerIn.getHeldItem(handIn);

        // Manually ray trace
        RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, RayTraceContext.FluidMode.ANY);
        if (raytraceresult.getType() == RayTraceResult.Type.MISS) {
            return new ActionResult<>(ActionResultType.PASS, itemstack);
        } else if (raytraceresult.getType() != RayTraceResult.Type.BLOCK) {
            return new ActionResult<>(ActionResultType.PASS, itemstack);
        } else { // Found a block
            BlockRayTraceResult blockraytraceresult = (BlockRayTraceResult)raytraceresult;
            BlockPos blockpos = blockraytraceresult.getPos();

            BlockState blockState = worldIn.getBlockState(blockpos);

            // Logic for trying to pick up water with empty jug
            if (this.fluid == JugFluid.EMPTY) {
                Fluid fluid = blockState.getFluidState().getFluid();
                if (fluid.isIn(FluidTags.WATER)) {
                    // Play water pickup sound and return full jug
                    playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
                    return new ActionResult<>(ActionResultType.SUCCESS, new ItemStack(get(JugFluid.WATER)));
                }
            } else if (this.fluid == JugFluid.WATER) { // Logic for watering TFCR farmland
                if (blockState.getBlock() instanceof FarmlandBlock) {
                    if (blockState.get(FarmlandBlock.MOISTURE) == 7) {
                        return new ActionResult<>(ActionResultType.FAIL, itemstack);
                    } else {
                        // Water the farmland
                        // TODO make this last longer
                        if (!worldIn.isRemote()) {
                            worldIn.setBlockState(blockpos, blockState.with(FarmlandBlock.MOISTURE, 7));
                        }
                        // Play water sploosh sound
                        playerIn.playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1.0F, 1.0F);

                        // TODO Particles?

                        // Return empty jug
                        return new ActionResult<>(ActionResultType.SUCCESS, new ItemStack(get(JugFluid.EMPTY)));
                    }
                } else if (blockState.getFluidState().getFluid().isIn(FluidTags.WATER)) {
                    // Logic for dumping water back into a water block
                    playerIn.playSound(SoundEvents.ITEM_BUCKET_EMPTY, 1.0F, 1.0F);
                    return new ActionResult<>(ActionResultType.SUCCESS, new ItemStack(get(JugFluid.EMPTY)));
                } else {
                    // How did we get here?
                }
            }

        }
        return new ActionResult<>(ActionResultType.PASS, itemstack);
    }
}
