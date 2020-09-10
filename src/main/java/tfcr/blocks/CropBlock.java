package tfcr.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import tfcr.data.CropType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class CropBlock extends TFCRBlock implements IPlantable {

    // Needed for compatibility with IPlantable.
    public static final PlantType TFCRCrop = PlantType.create("TFCRCrop");

    public static final int MAX_AGE = 15;
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, MAX_AGE);

    public CropType cropType;

    private static CropBlock[] allBlocks;

    public CropBlock(Properties properties, CropType cropType) {
        super(properties, "plants/crops/" + cropType.getName());
        this.cropType = cropType;
    }

    private static void init() {
        allBlocks = new CropBlock[CropType.values().length];
        for (int i = 0; i < CropType.values().length; i++) {
            allBlocks[i] = new CropBlock(Properties.from(Blocks.WHEAT), CropType.values()[i]);
        }
    }

    public static List<CropBlock> getAllBlocks() {
        if (allBlocks == null) {
            init();
        }
        return Arrays.asList(allBlocks);
    }

    public static CropBlock get(CropType type) {
        if (allBlocks == null) {
            init();
        }
        return allBlocks[type.ordinal()];
    }

    /**
     * Register the different states this block can take
     */
    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public int getOpacity(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return 1;
    }

    public boolean isSolid(BlockState state) {
        return false;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        // This is a somewhat indirect way of preventing the player from placing this block on non-farmland blocks.
        if (!(worldIn.getBlockState(pos.down()).getBlock() instanceof FarmlandBlock)) {
            worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
        }
    }

    @Override
    public void tick(BlockState state, World worldIn, BlockPos pos, Random random) {
        super.tick(state, worldIn, pos, random);
        System.out.println("TICK!");

        // Make sure we don't load unloaded chunks when doing a light check
//        if (!worldIn.isAreaLoaded(pos, 1)) {
//            return;
//        }

        int age = state.get(AGE);
        // Don't need to grow if we're at max growth
        // TODO later implement crop death after enough time
        if (age >= MAX_AGE) {
            return;
        }

        // Otherwise, if we pass growth checks, increment our age
        if (shouldGrow(worldIn, pos, random)) {
            worldIn.setBlockState(pos, state.with(AGE, age + 1), 2);
        }
    }

    private boolean shouldGrow(World worldIn, BlockPos pos, Random random) {
        // This is the chance this plant should grow, bounded between [0, 1].
        float growthChance = 1.0f;

        // Check sunlight first
        // Penalty is 0 if above minimum sun level. Penalty increases linearly
        // from direct sunlight, approaching 0 growth chance at minSunLevel - 1.
        int skylight = worldIn.getLightFor(LightType.SKY, pos.up());
        growthChance -= (15 - skylight) / (15 - (cropType.minSunLevel + 1));
        if (growthChance <= 0) {
            return false;
        }

        // Check water and fertility levels
        BlockState rawState = worldIn.getBlockState(pos.down());
        // Make sure we're on farmland. Immediately fail if we're not.
        if (!(rawState.getBlock() instanceof FarmlandBlock)) {
            return false;
        }

        // If the farmland is not watered, we can't grow this time.
        if (rawState.get(FarmlandBlock.MOISTURE) == 0) {
            return false;
        }

        // Adjust growth chance based on the fertility level.
        FarmlandBlock soil = ((FarmlandBlock) rawState.getBlock());
        switch (soil.fertility) {
            case BARREN:    return false;
            case INFERTILE: growthChance -= 0.5f; break;
            case NORMAL:    growthChance -= 0.1f; break;
            case FERTILE:   // fall-through
            default:        growthChance -= 0.0f; break;
        }

        // Check temperature. TODO add temperature.
        System.out.println("Tick called. Final growth chance: " + growthChance + " * " + cropType.idealGrowthChance);

        // Finally, check the chance to grow this time.
        // We multiply the growth chance by the base probability, which is defined by
        // the number of growth stages and average growth time of this crop.
        return random.nextFloat() < (growthChance * cropType.idealGrowthChance);
    }

    /**
     * Gets the render layer this block will render on. SOLID for solid blocks, CUTOUT or CUTOUT_MIPPED for on-off
     * transparency (glass, reeds), TRANSLUCENT for fully blended transparency (stained glass)
     */
    public BlockRenderLayer getRenderLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    /**
     * Implements IPlantable interface.
     * @return A custom TFCR crop type
     */
    @Override
    public PlantType getPlantType(IBlockReader world, BlockPos pos) {
        return TFCRCrop;
    }

    /**
     * Implements IPlantable interface.
     * @return This block's default state.
     */
    @Override
    public BlockState getPlant(IBlockReader world, BlockPos pos) {
        return this.getDefaultState();
    }
}
