package tfcr.worldgen;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoublePlantBlock;
import net.minecraft.block.SnowyDirtBlock;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import tfcr.blocks.MudBlock;

import java.util.Random;
import java.util.function.Function;

/**
 * A chunk-level feature that converts dirt to mud, if it is adjacent to a
 * water block.
 */
public class TallGrassFeature extends Feature<NoFeatureConfig> {
    public static final TallGrassFeature INSTANCE = new TallGrassFeature(NoFeatureConfig::deserialize);

    private static final int NUM_ATTEMPTS = 64;

    private static final float COVERAGE = 0.5f;
    private static final float RANDOM_MIXIN = 0.75f;
    private static final float TALLER_PERCENTAGE = 0.25f;

    private static final Block[] GRASS_BLOCKS = new Block[] {
            Blocks.GRASS,
            Blocks.FERN
    };

    private static final Block[] TALL_GRASS_BLOCKS = new Block[] {
            Blocks.TALL_GRASS,
            Blocks.LARGE_FERN
    };

    public TallGrassFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config) {

        boolean success = false;
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                if (rand.nextFloat() >= COVERAGE) {
                    continue;
                }

                BlockPos surfaceBlock = world.getHeight(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, pos.add(x, 0, z));
//                System.out.println("Testing surface block at pos: " + surfaceBlock + ". Found: " + world.getBlockState(surfaceBlock).getBlock());
                if (world.getBlockState(surfaceBlock.down()).getBlock() instanceof SnowyDirtBlock && world.isAirBlock(surfaceBlock)) {
//                    Block selectedGrass;
                    double randomGrassValue;
                    if (rand.nextFloat() < RANDOM_MIXIN) {
                        randomGrassValue = rand.nextFloat();
                    } else {
                        randomGrassValue = MathHelper.clamp((1.0D + Biome.INFO_NOISE.getValue(pos.getX() / 48.0D, pos.getZ() / 48.0D)) / 2.0D, 0.0D, 0.9999D);
                    }


//                    double randomGrassValue = MathHelper.clamp((1.0D + Biome.INFO_NOISE.getValue(pos.getX() / 48.0D, pos.getZ() / 48.0D)) / 2.0D, 0.0D, 0.9999D);
                    Block selectedGrass;
                    if (rand.nextFloat() < TALLER_PERCENTAGE) {
                        selectedGrass = TALL_GRASS_BLOCKS[(int)(randomGrassValue * TALL_GRASS_BLOCKS.length)];
                    } else {
                        selectedGrass = GRASS_BLOCKS[(int)(randomGrassValue * GRASS_BLOCKS.length)];
                    }

                    if (selectedGrass instanceof DoublePlantBlock) {
                        // Double plant blocks have a custom place method to place both halves
                        ((DoublePlantBlock) selectedGrass).placeAt(world, surfaceBlock, 2);
                    } else {
                        world.setBlockState(surfaceBlock, selectedGrass.getDefaultState(), 2);
                    }
                    success = true;
                }


            }
        }

        return success;



//        double randomGrassValue = MathHelper.clamp((1.0D + Biome.INFO_NOISE.getValue(pos.getX() / 48.0D, pos.getZ() / 48.0D)) / 2.0D, 0.0D, 0.9999D);
//        Block selectedGrass = TALL_GRASS_BLOCKS[(int)(randomGrassValue * TALL_GRASS_BLOCKS.length)];
//
//        BlockPos attemptPos;
//        boolean success = false;
//        for (int attempt = 0; attempt < NUM_ATTEMPTS; attempt++) {
//            // Generate a rough 2D gaussian centered in the middle of the chunk
//            attemptPos = pos.add(
//                    rand.nextInt(8) - rand.nextInt(8),
//                    rand.nextInt(4) - rand.nextInt(4),
//                    rand.nextInt(8) - rand.nextInt(8)
//            );
//
//            // Make sure we only try to place crops in air within the world.
//            if (pos.getY() < 255 && world.isAirBlock(attemptPos)) {
//                // For now the valid block list is just any dirt block.
//                if (world.getBlockState(attemptPos.down()).getBlock() instanceof SnowyDirtBlock) {
//                    world.setBlockState(attemptPos, selectedGrass.getDefaultState(), 2);
//
//                    success = true;
//                }
//            }
//        }

//        return success;
    }
}
