package tfcr.worldgen;

import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import tfcr.utils.TemplateHelper;

public class TreeStructure extends ScatteredStructure<TreeFeatureConfig> {
    public static TreeStructure INSTANCE = new TreeStructure();

    @Override
    protected StructureStart makeStart(IWorld worldIn, IChunkGenerator<?> generator, SharedSeedRandom random, int x, int z) {
        return null;
    }

    @Override
    protected String getStructureName() {
        return null;
    }

    @Override
    public int getSize() {
        return 3;
    }

    @Override
    protected int getSeedModifier() {
        return 14357811;
    }

    public static class Start extends StructureStart {
        public Start() {}

        public Start(IWorld world, IChunkGenerator<?> chunkGenerator, SharedSeedRandom random, int chunkX, int chunkZ, Biome biome) {
            TreeFeatureConfig config = (TreeFeatureConfig)chunkGenerator.getStructureConfig(biome, INSTANCE);
            int baseX = chunkX * 16;
            int baseZ = chunkZ * 16;
            BlockPos pos = new BlockPos(baseX, 90, baseZ);
            Rotation rotation = Rotation.NONE; // TODO later add rotation
            TemplateManager manager = world.getSaveHandler().getStructureTemplateManager();

        }
    }
}
