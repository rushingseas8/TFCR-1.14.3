package tfcr.worldgen;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.SnowyDirtBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import tfcr.blocks.CropBlock;
import tfcr.data.CropType;

import java.util.Random;
import java.util.function.Function;

public class NaturalCropFeature extends Feature<NoFeatureConfig> {
    public static final NaturalCropFeature INSTANCE = new NaturalCropFeature(NoFeatureConfig::deserialize);

    public NaturalCropFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    private static final int NUM_ATTEMPTS = 64;

    @Override
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        // Copied from "ForestFlowerFeature", this uses the Biome Perlin noise
        // to determine which crop we should try to place here.
        double randomCropValue = MathHelper.clamp((1.0D + Biome.INFO_NOISE.getValue(pos.getX() / 48.0D, pos.getZ() / 48.0D)) / 2.0D, 0.0D, 0.9999D);
        CropType selectedCrop = CropType.values()[(int)(randomCropValue * CropType.values().length)];
        CropBlock selectedCropBlock = CropBlock.get(selectedCrop);

//        System.out.println("Trying to place natural crops. Selected crop: " + selectedCrop.getName());

        BlockPos attemptPos;
        boolean success = false;
        for (int attempt = 0; attempt < NUM_ATTEMPTS; attempt++) {
            // Generate a rough 2D gaussian centered in the middle of the chunk
            attemptPos = pos.add(
                    rand.nextInt(8) - rand.nextInt(8),
                    rand.nextInt(4) - rand.nextInt(4),
                    rand.nextInt(8) - rand.nextInt(8)
            );

//            System.out.println("Checking position: " + pos);
            // Make sure we only try to place crops in air within the world.
            if (pos.getY() < 255 && world.isAirBlock(attemptPos)) {
                // For now the valid block list is just any dirt block.
                if (world.getBlockState(attemptPos.down()).getBlock() instanceof SnowyDirtBlock) {
//                    System.out.println("Success! Placing at: " + pos);
                    world.setBlockState(attemptPos, selectedCropBlock.getDefaultState(), 2);
                    success = true;
                } else {
//                    System.out.println("Failed! Block was of type " + world.getBlockState(attemptPos.down()).getBlock());
                }
            }
        }

        return success;
    }


}
