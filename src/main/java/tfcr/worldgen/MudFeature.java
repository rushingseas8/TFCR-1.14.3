package tfcr.worldgen;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirtSnowy;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import tfcr.blocks.BlockMud;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * A chunk-level feature that converts dirt to mud, if it is adjacent to a
 * water block.
 */
public class MudFeature extends Feature<NoFeatureConfig> {
    public static final MudFeature INSTANCE = new MudFeature();

    public boolean func_212245_a(@Nonnull IWorld world, @Nonnull IChunkGenerator<? extends IChunkGenSettings> p_212245_2_, @Nonnull Random random, @Nonnull BlockPos blockPos, @Nonnull NoFeatureConfig p_212245_5_) {

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
                    if (block == Blocks.DIRT || (block instanceof BlockDirtSnowy && random.nextInt(3) == 0)) {
                        for (EnumFacing facing : EnumFacing.values()) {
                            BlockPos adjacent = mutableBlockPos.add(facing.getDirectionVec());
                            // If any adjacent block has water (flowing, source, or waterlogged), then
                            // replace this position with a mud block. Stop checking adjacent blocks.
                            if (world.getBlockState(adjacent).getFluidState().getFluid() instanceof WaterFluid) {
                                world.setBlockState(mutableBlockPos, BlockMud.get().getDefaultState(), 3);
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
