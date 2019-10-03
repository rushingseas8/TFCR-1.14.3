package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
     * Random tick update
     * @param state
     * @param worldIn
     * @param pos
     * @param random
     */
    @Override
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
        // If the block above us is solid, then we turn into dirt no matter what.
        if (!state.isValidPosition(worldIn, pos)) {
            turnToDirt(state, worldIn, pos);
        } else {
            int i = state.get(MOISTURE);

            // If we're not irrigated..
            if (!hasWater(worldIn, pos) && !worldIn.isRainingAt(pos.up())) {
                // And we have some moisture, then decrement moisture
                if (i > 0) {
                    worldIn.setBlockState(pos, state.with(MOISTURE, i - 1), 2);
                } else if (!hasCrops(worldIn, pos)) { // If fully dry, then revert to dirt.
                    turnToDirt(state, worldIn, pos);
                }
            } else if (i < 7) { // If we're irrigated, set immediately to max moisture when ticked
                worldIn.setBlockState(pos, state.with(MOISTURE, 7), 2);
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
}
