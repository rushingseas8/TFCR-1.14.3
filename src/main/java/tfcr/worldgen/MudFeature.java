package tfcr.worldgen;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SnowyDirtBlock;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
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
public class MudFeature extends Feature<NoFeatureConfig> {
    public static final MudFeature INSTANCE = new MudFeature(NoFeatureConfig::deserialize);

    public MudFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> configFactoryIn) {
        super(configFactoryIn);
    }

    @Override
    public boolean place(IWorld world, ChunkGenerator<? extends GenerationSettings> generator, Random random, BlockPos blockPos, NoFeatureConfig config) {

        // Reusable blockpos
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                int worldX = blockPos.getX() + x;
                int worldZ = blockPos.getZ() + z;
                int worldY = world.getHeight(Heightmap.Type.MOTION_BLOCKING, worldX, worldZ);

                // Look up to 3 blocks under the surface
                for (int y = 0; y < 3; y++) {
                    mutableBlockPos.setPos(worldX, worldY - y - 1, worldZ);

                    // If this block is dirt, convert to mud.
                    // Grass/snow/mycelium/etc has a 1/3 chance to convert to mud.
                    Block block = world.getBlockState(mutableBlockPos).getBlock();
                    if (block == Blocks.DIRT || (block instanceof SnowyDirtBlock && random.nextInt(3) == 0)) {
                        for (Direction facing : Direction.values()) {
                            BlockPos adjacent = mutableBlockPos.add(facing.getDirectionVec());
                            // If any adjacent block has water (flowing, source, or waterlogged), then
                            // replace this position with a mud block. Stop checking adjacent blocks.
                            if (world.getBlockState(adjacent).getFluidState().getFluid() instanceof WaterFluid) {
                                world.setBlockState(mutableBlockPos, MudBlock.get().getDefaultState(), 3);
                                break;
                            }
                        }
                    }
                }
            }
        }

        return true;
    }
}
