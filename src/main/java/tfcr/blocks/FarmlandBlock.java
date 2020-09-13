package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import tfcr.TFCR;
import tfcr.data.Fertility;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Custom TFCR implementation of a Farmland block.
 *
 * Contains 4 possible states, corresponding to the different Fertility enum values.
 *
 * When this block is randomly ticked, it checks its surroundings for irrigation
 * and other water source status (like raining). This can only increase the
 * moisture level of the soil, but not decrease it.
 *
 * Has a TileEntity: FarmlandTileEntity. This TileEntity is used for decrementing
 * moisture level on a fixed timer, exactly 8 times per day starting from the
 * moment this block is first placed or created in the world.
 */
public class FarmlandBlock extends net.minecraft.block.FarmlandBlock implements ISelfRegisterItem, ISelfRegisterBlock {

    /**
     * Fertility status. Custom to TFCR farmland.
     *
     * Increased (up to normal) by growing fixing plants, or simply waiting.
     * Increased (up to fertile) by applying fertilizer/compost.
     * Decreased by harvesting fully grown (non-fixing) plants.
     *
     * Barren indicates no plants will grow (except fixing plants).
     * Low fertility means plants will grow, albeit slowly and with lower yield.
     * Normal is the same as vanilla farmland.
     * Fertile will boost crop growth speed and yield.
     */
    public Fertility fertility;

    // TODO: Add random weed blocks on top of fallow farmland in model
    public static final int MAX_FALLOW_STATE = 7;
    public static IntegerProperty FALLOW_STATE = IntegerProperty.create("fallow", 0, MAX_FALLOW_STATE);

    /**
     * Chance to dry out one stage each random tick.
     *
     * This value is computed using the same formula as in CropType, with a mean
     * drying out time of 2 days. With 8 dryness states, 90% of farmland will dry
     * in [1.4, 3.2] days, and 99% will dry fully in [0.9, 4] days.
     */
    private static final float DEHYDRATION_CHANCE = (float)(8 / (2 * 20 * 60 / 68.27));

    /**
     * Chance to increase fallow state by one each random tick.
     *
     * TODO: Look into reducing mean fallow time to 20-25 days to make it a more
     *  consistent mechanic. Would be annoying to have 50% of land still not affected
     *  by this mechanic by 30 days.
     *
     * We expect to fully increase fallow state every 30 days. 90% in [20.64, 48.3],
     * and 99% in [13.5, 60].
     */
    private static final float FALLOW_CHANCE = (float)(8 / (30 * 20 * 60 / 68.27));

    private static FarmlandBlock[] allBlocks;

    public FarmlandBlock(Properties properties, Fertility fertility) {
        super(properties);
        this.setDefaultState(getDefaultState().with(MOISTURE, 0));
        this.fertility = fertility;
        setRegistryName(TFCR.MODID, "farmland/" + fertility.getName());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(FALLOW_STATE);
    }

    private static void init() {
        allBlocks = new FarmlandBlock[Fertility.values().length];
        for (int i = 0; i < Fertility.values().length; i++) {
            allBlocks[i] = new FarmlandBlock(Properties.from(Blocks.FARMLAND), Fertility.values()[i]);
        }
    }

    public static FarmlandBlock get(Fertility fertility) {
        if (allBlocks == null) {
            init();
        }
        return allBlocks[fertility.ordinal()];
    }

    public static List<FarmlandBlock> getAllBlocks() {
        if (allBlocks == null) {
            init();
        }
        return Arrays.asList(allBlocks);
    }

    /**
     * Random tick update. Used to check for water (in block form, or as rain),
     * and increases the moisture level of the farmland if found.
     */
    @Override
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
        // If the block above us is solid, then we turn into dirt no matter what.
        if (!state.isValidPosition(worldIn, pos)) {
            turnToDirt(state, worldIn, pos);
            return;
        }

        // If the water level of this blockstate is not maximum
        if (state.get(MOISTURE) < 7) {
            // Check if it's either raining, or if we're near a water block.
            // If so, then set this block's moisture level to maximum.
            // Note we stop processing further, since we can't dry out this tick.
            if (worldIn.isRainingAt(pos.up()) || hasWater(worldIn, pos)) {
                worldIn.setBlockState(pos, state.with(MOISTURE, 7), 2);
                return;
            }
        }

        // Logic for decreasing moisture. We expect to be fully dry in roughly 2 days.
        if (random.nextFloat() < DEHYDRATION_CHANCE) {
            worldIn.setBlockState(pos, state.with(MOISTURE, state.get(MOISTURE) - 1), 2);
        }

        // Before we check fallow logic, we return if it won't apply.
        // When fertility is high enough, leaving farmland fallow won't matter.
        if (fertility == Fertility.NORMAL || fertility == Fertility.FERTILE) {
            return;
        }

        // TODO check season- you can't leave a field fallow during winter

        // Finally, check if there's a crop above us. Return if so.
        if (hasCrops(worldIn, pos)) {
            return;
        }

        // Logic for laying fallow. If there's no crop above us, roll a chance
        // to increase the fallow state. When fallow state is max, reset it, and
        // increment the nutrient level of the soil (up to a limit of 2).
        if (random.nextFloat() < FALLOW_CHANCE) {
            int fallowState = state.get(FALLOW_STATE);
            if (fallowState < MAX_FALLOW_STATE) {
                worldIn.setBlockState(pos, state.with(FALLOW_STATE, fallowState + 1), 2);
            } else { // Fallow state is either max or about to be, so update fertility
                BlockState newState = get(Fertility.values()[fertility.ordinal() + 1]).getDefaultState()
                        .with(MOISTURE, state.get(MOISTURE))
                        .with(FALLOW_STATE, 0);
                worldIn.setBlockState(pos, newState, 2);
            }
        }
    }

    /**
     * Returns true if this farmland block has access to water (in TFCR terms,
     * if it is irrigated). Water access is defined as having any water fluid
     * within 4 blocks in the X and Z axis, and up to 1 block above us.
     *
     * Copied from Vanilla FarmlandBlock.
     *
     * @param worldIn
     * @param pos
     * @return
     */
    private static boolean hasWater(IWorldReader worldIn, BlockPos pos) {
        for(BlockPos blockpos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4))) {
            if (worldIn.getFluidState(blockpos).isTagged(FluidTags.WATER)) {
                return true;
            }
        }

        return net.minecraftforge.common.FarmlandWaterManager.hasBlockWaterTicket(worldIn, pos);
    }

    /**
     * Returns true if the block above us has a crop block on it.
     *
     * Copied from Vanilla FarmlandBlock.
     * @param worldIn
     * @param pos
     * @return
     */
    private boolean hasCrops(IBlockReader worldIn, BlockPos pos) {
        BlockState state = worldIn.getBlockState(pos.up());
        // Note: First check is for TFCR crops, second check is copied from vanilla code
        // and handles vanilla crops.
        return state.getBlock() instanceof CropBlock ||
                state.getBlock() instanceof net.minecraftforge.common.IPlantable && canSustainPlant(state, worldIn, pos, Direction.UP, (net.minecraftforge.common.IPlantable)state.getBlock());
    }


    /**
     * Block's chance to react to a living entity falling on it.
     *
     * We turn into the respective fertility dirt block on trample.
     */
    @Override
    public void onFallenUpon(World worldIn, @Nonnull BlockPos pos, Entity entityIn, float fallDistance) {
        if (!worldIn.isRemote && net.minecraftforge.common.ForgeHooks.onFarmlandTrample(worldIn, pos, DirtBlock.get(fertility).getDefaultState(), fallDistance, entityIn)) { // Forge: Move logic to Entity#canTrample
            turnToDirt(worldIn.getBlockState(pos), worldIn, pos);
        }

        // Copied from Block's onFallenUpon, since direct super() would revert block to Vanilla dirt
        entityIn.fall(fallDistance, 1.0F);
    }

    /**
     * Converts this farmland block into a TFCR dirt block. Used by #onFallenUpon.
     */
    public static void turnToDirt(BlockState state, World worldIn, @Nonnull BlockPos pos) {
        Fertility thisFertility = ((FarmlandBlock) state.getBlock()).fertility;
        worldIn.setBlockState(pos, nudgeEntitiesWithNewState(state, DirtBlock.get(thisFertility).getDefaultState(), worldIn, pos));
    }
}
