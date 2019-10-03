package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowBlock;
import net.minecraft.block.SpreadableSnowyDirtBlock;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.lighting.LightEngine;
import tfcr.TFCR;
import tfcr.data.Fertility;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * TFCR grass block. Supports fertility.
 */
public class GrassBlock extends SpreadableSnowyDirtBlock {

    public Fertility fertility;

    private static GrassBlock[] allBlocks;

    protected GrassBlock(Properties builder, Fertility fertility) {
        super(builder);
        this.fertility = fertility;
        setRegistryName(TFCR.MODID, "grass/" + fertility.getName());
    }


    private static void init() {
        allBlocks = new GrassBlock[Fertility.values().length];
        for (int i = 0; i < Fertility.values().length; i++) {
            allBlocks[i] = new GrassBlock(Properties.from(Blocks.FARMLAND), Fertility.values()[i]);
        }
    }

    public static GrassBlock get(Fertility fertility) {
        if (allBlocks == null) {
            init();
        }
        return allBlocks[fertility.ordinal()];
    }

    public static List<GrassBlock> getAllBlocks() {
        if (allBlocks == null) {
            init();
        }
        return Arrays.asList(allBlocks);
    }

    /**
     * Copied from SpreadableSnowyDirtBlock. Old name: func_220257_b
     *
     * @return true if there is a 1 layer snow block above us, or the block above us blocks light.
     */
    private static boolean isSnowyOrCovered(BlockState state, IWorldReader world, BlockPos pos) {
        BlockPos upPos = pos.up();
        BlockState blockstate = world.getBlockState(upPos);

        // Return true if the block above us is a 1-layer snow block
        if (blockstate.getBlock() == Blocks.SNOW && blockstate.get(SnowBlock.LAYERS) == 1) {
            return true;
        } else {
            // I think this checks if this block is fully covered from above.
            int i = LightEngine.func_215613_a(world, state, pos, blockstate, upPos, Direction.UP, blockstate.getOpacity(world, upPos));
            return i < world.getMaxLightLevel();
        }
    }

    /**
     * Copied from SpreadableSnowyDirtBlock. Old name: func_220256_c
     *
     * @return true if #isSnowyOrCovered is true, and there isn't water above us.
     */
    private static boolean isSnowyOrCoveredNotWet(BlockState state, IWorldReader world, BlockPos pos) {
        BlockPos upPos = pos.up();
        return isSnowyOrCovered(state, world, pos) && !world.getFluidState(upPos).isTagged(FluidTags.WATER);
    }

    /**
     * An override of the default grass block's tick.
     *
     * Based on fertility, grass spreads faster or slower *from* this block.
     * (note that a dirt block's fertility does not affect its chance to receive grass)
     */
    @Override
    public void tick(@Nonnull BlockState state, @Nonnull World worldIn, @Nonnull BlockPos pos, @Nonnull Random random) {
        if (!worldIn.isRemote) {
            if (!worldIn.isAreaLoaded(pos, 3)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light and spreading
            if (!isSnowyOrCovered(state, worldIn, pos)) {
                //worldIn.setBlockState(pos, Blocks.DIRT.getDefaultState());
                worldIn.setBlockState(pos, DirtBlock.get(Fertility.NORMAL).getDefaultState());
            } else if (worldIn.getLight(pos.up()) >= 9) {
                BlockState blockstate = this.getDefaultState();

                // Chance to spread is 4 for normal grass.
                // Fertility 0 = 0 chances; 1 = 2; 2 = 4; fertility 3 = 6 chances.
                int spreadChances = 4 + (2 * (fertility.ordinal() - 2));
                int bonusChances = fertility == Fertility.FERTILE ? 2 : 0;

                for (int i = 0; i < spreadChances; ++i) {
                    BlockPos blockpos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                    BlockState targetBlockState = worldIn.getBlockState(blockpos);

                    // Default vanilla logic for spreading to vanilla dirt blocks
                    if (targetBlockState.getBlock() == Blocks.DIRT) {
                        grow(blockstate, targetBlockState, blockpos, worldIn);
                    } else if (targetBlockState.getBlock() instanceof DirtBlock) { // TFCR dirt block
                        Fertility targetFertility = ((DirtBlock) targetBlockState.getBlock()).fertility;
                        switch (targetFertility) {
                            // Barren soil cannot grow grass. Thus, we fail this attempt to grow.
                            case BARREN:
                                break;
                            // Infertile soil has a 1/4 chance of failing to grow grass without retry.
                            case INFERTILE:
                                if (random.nextInt(4) == 0) {
                                    break;
                                }
                                grow(blockstate, targetBlockState, blockpos, worldIn);
                                break;
                            // Normal soil has no changes to growth.
                            case NORMAL:
                                grow(blockstate, targetBlockState, blockpos, worldIn);
                                break;
                            // Fertile soil has a 1/2 chance to give a bonus growth attempt (up to 2 times).
                            case FERTILE:
                                grow(blockstate, targetBlockState, blockpos, worldIn);
                                if (bonusChances > 0) {
                                    if (random.nextInt(2) == 0) {
                                        bonusChances--;
                                        spreadChances++;
                                    }
                                }
                                break;
                        }
                    }
                }
            }
        }
    }

    // Helper method to set the target position to be the same grass type as this block.
    // Uses the snowy variant if applicable.
    private void grow(BlockState ourBlockState, BlockState targetBlockState, BlockPos targetPos, World world) {
        if (isSnowyOrCoveredNotWet(targetBlockState, world, targetPos)) {
            world.setBlockState(targetPos, ourBlockState.with(SNOWY, world.getBlockState(targetPos.up()).getBlock() == Blocks.SNOW));
        }
    }
}
