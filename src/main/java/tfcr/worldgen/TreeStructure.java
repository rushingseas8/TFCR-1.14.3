package tfcr.worldgen;

import com.mojang.datafixers.Dynamic;
import net.minecraft.util.Rotation;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import tfcr.data.WoodType;

import java.util.function.Function;

public class TreeStructure extends ScatteredStructure<TreeFeatureConfig> {
    public static TreeStructure INSTANCE = new TreeStructure(i -> new TreeFeatureConfig(WoodType.OAK, 0));

    /**
     * @param p_i51449_1_ A deserialization function that returns a TreeFeatureConfig (I think?)
     */
    public TreeStructure(Function<Dynamic<?>, ? extends TreeFeatureConfig> p_i51449_1_) {
        super(p_i51449_1_);
    }

    @Override
    public IStartFactory getStartFactory() {
        return TreeStructure.Start::new;
    }

    @Override
    public String getStructureName() {
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
//        public Start() {}

//        public Start(IWorld world, IChunkGenerator<?> chunkGenerator, SharedSeedRandom random, int chunkX, int chunkZ, Biome biome) {
//            TreeFeatureConfig config = (TreeFeatureConfig)chunkGenerator.getStructureConfig(biome, INSTANCE);
//            int baseX = chunkX * 16;
//            int baseZ = chunkZ * 16;
//            BlockPos pos = new BlockPos(baseX, 90, baseZ);
//            Rotation rotation = Rotation.NONE; // TODO later add rotation
//            TemplateManager manager = world.getSaveHandler().getStructureTemplateManager();
//
//        }

        public Start(Structure<?> structure, int i, int i1, Biome biome, MutableBoundingBox mutableBoundingBox, int i2, long l) {
            super(structure, i, i1, biome, mutableBoundingBox, i2, l);
        }

        @Override
        public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
            TreeFeatureConfig config = (TreeFeatureConfig)generator.getStructureConfig(biomeIn, INSTANCE);
            int baseX = chunkX * 16;
            int baseZ = chunkZ * 16;
            BlockPos pos = new BlockPos(baseX, 90, baseZ);
            Rotation rotation = Rotation.NONE; // TODO later add rotation
            // replaced by templateManagerIn
//            TemplateManager manager = world.getSaveHandler().getStructureTemplateManager();

        }

    }
}
