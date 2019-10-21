package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import tfcr.TFCR;
import tfcr.data.Fertility;
import tfcr.init.ISelfRegisterBlock;
import tfcr.init.ISelfRegisterItem;
import tfcr.tileentity.FarmlandTileEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
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
//    public static EnumProperty<Fertility> FERTILITY = TFCRBlock.FERTILITY;
    public Fertility fertility;

    private static FarmlandBlock[] allBlocks;

    public FarmlandBlock(Properties properties, Fertility fertility) {
        super(properties);
        this.setDefaultState(getDefaultState().with(MOISTURE, 0));
        this.fertility = fertility;
        setRegistryName(TFCR.MODID, "farmland/" + fertility.getName());
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        //builder.add(MOISTURE);
        super.fillStateContainer(builder);
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
        } else {
            // If the water level of this blockstate is not maximum
            if (state.get(MOISTURE) < 7) {
                // Check if it's either raining, or if we're near a water block.
                // If so, then set this block's moisture level to maximum.
                if (worldIn.isRainingAt(pos.up()) || hasWater(worldIn, pos)) {
                    worldIn.setBlockState(pos, state.with(MOISTURE, 7), 2);
                }
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
        return state.getBlock() instanceof net.minecraftforge.common.IPlantable && canSustainPlant(state, worldIn, pos, Direction.UP, (net.minecraftforge.common.IPlantable)state.getBlock());
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

    /**
     * Farmland blocks have a TileEntity- FarmlandTileEntity.
     * @param state
     * @return
     */
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    /**
     * Returns a new instance of a FarmlandTileEntity.
     * @param state
     * @param world
     * @return
     */
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new FarmlandTileEntity();
    }
}
