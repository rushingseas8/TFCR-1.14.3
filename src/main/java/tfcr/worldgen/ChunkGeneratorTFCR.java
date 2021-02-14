package tfcr.worldgen;

import it.unimi.dsi.fastutil.longs.LongIterator;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.ChunkSection;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.OctavesNoiseGenerator;
import net.minecraft.world.gen.OverworldChunkGenerator;
import net.minecraft.world.gen.OverworldGenSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.jigsaw.JigsawJunction;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import tfcr.data.TerrainType;
import tfcr.worldgen.biome.BaseTFCRBiome;

public class ChunkGeneratorTFCR extends OverworldChunkGenerator {

    // These are passed as constants from OverworldChunkGenerator to NoiseChunkGenerator's constructor
    private final int field_222563_j = 8;
    private final int field_222564_k = 4;

    // Derived values from the above + constructor params
    private final int field_222565_l = 16 / 4;
    private final int field_222566_m = 256 / 8;
    private final int field_222567_n = 16 / 4;

    // Copied from NoiseChunkGenerator (field_222561_h renamed to GAUSSIAN_BLUR)
    private static final float[] GAUSSIAN_BLUR = Util.make(new float[13824], (p_222557_0_) -> {
        for(int i = 0; i < 24; ++i) {
            for(int j = 0; j < 24; ++j) {
                for(int k = 0; k < 24; ++k) {
                    p_222557_0_[i * 24 * 24 + j * 24 + k] = (float)blurHelper(j - 12, k - 12, i - 12);
                }
            }
        }
    });

    // Copied from NoiseChunkGenerator (func_222554_b renamed to blurHelper)
    private static double blurHelper(int p_222554_0_, int p_222554_1_, int p_222554_2_) {
        double d0 = (double)(p_222554_0_ * p_222554_0_ + p_222554_2_ * p_222554_2_);
        double d1 = (double)p_222554_1_ + 0.5D;
        double d2 = d1 * d1;
        double d3 = Math.pow(Math.E, -(d2 / 16.0D + d0 / 16.0D));
        double d4 = -d1 * MathHelper.fastInvSqrt(d2 / 2.0D + d0 / 2.0D) / 2.0D;
        return d4 * d3;
    }

    // field_222562_i renamed to AIR
    private static final BlockState AIR = Blocks.AIR.getDefaultState();

    private static long biomeTime = 0;
    private static long chunkGenTime = 0;

    private static Biome[] localBiomes = new Biome[256];
    private static BlockState[] defaultBlocks = new BlockState[256];
    private static BlockState[] defaultFluids = new BlockState[256];

//    private final BlockState staticDefaultBlock = Blocks.STONE.getDefaultState();
//    private final BlockState staticDefaultFluid = Blocks.LAVA.getDefaultState();

    public ChunkGeneratorTFCR(IWorld worldIn, BiomeProvider biomeProviderIn, OverworldGenSettings settings) {
        super(worldIn, biomeProviderIn, settings);
    }

    // Copied from NoiseChunkGenerator, sets blocks in chunk
    // Some variables were renamed for readability
    @Override
    public void func_222537_b(IWorld world, IChunk chunk) {
        long start = System.nanoTime();
        int seaLevel = this.getSeaLevel();
        ObjectList<AbstractVillagePiece> objectlist = new ObjectArrayList<>(10);
        ObjectList<JigsawJunction> objectlist1 = new ObjectArrayList<>(32);
        ChunkPos chunkpos = chunk.getPos();
        int chunkX = chunkpos.x;
        int chunkZ = chunkpos.z;
        int blockX = chunkX << 4;
        int blockZ = chunkZ << 4;

        // Get the biomes for this chunk
        localBiomes = chunk.getBiomes();

        // Optimization: used to check if all default block/fluids are the same
        BlockState staticDefaultBlock = null;
        boolean allSameDefaultBlock = true;

        BlockState staticDefaultFluid = null;
        boolean allSameDefaultFluid = true;

        // Get defaults from each biome. Default to vanilla if not a TFCR biome.
        for (int i = 0; i < localBiomes.length; i++) {
            if (localBiomes[i] instanceof BaseTFCRBiome) {
                BaseTFCRBiome tfcrBiome = (BaseTFCRBiome) localBiomes[i];
                defaultBlocks[i] = tfcrBiome.DEFAULT_BLOCK;
                defaultFluids[i] = tfcrBiome.DEFAULT_FLUID;
            } else {
                defaultBlocks[i] = this.field_222559_f;
                defaultFluids[i] = this.field_222560_g;
            }

            // Optimization- check if all the default blocks/fluids are the same
            // Default to the first option, and from then on check against it.
            if (i == 0) {
                staticDefaultBlock = defaultBlocks[0];
                staticDefaultFluid = defaultFluids[0];
            } else {
                if (allSameDefaultBlock) {
                    if (defaultBlocks[i] != staticDefaultBlock) {
                        allSameDefaultBlock = false;
                    }
                }
                if (allSameDefaultFluid) {
                    if (defaultFluids[i] != staticDefaultFluid) {
                        allSameDefaultFluid = false;
                    }
                }
            }
        }

        // Feature.field_214488_aQ = ImmutableList.of(PILLAGER_OUTPOST, VILLAGE)
        for(Structure<?> structure : Feature.field_214488_aQ) {
            String s = structure.getStructureName();
            LongIterator longiterator = chunk.getStructureReferences(s).iterator();

            while(longiterator.hasNext()) {
                long j1 = longiterator.nextLong();
                ChunkPos chunkpos1 = new ChunkPos(j1);
                IChunk ichunk = world.getChunk(chunkpos1.x, chunkpos1.z);
                StructureStart structurestart = ichunk.getStructureStart(s);
                if (structurestart != null && structurestart.isValid()) {
                    for(StructurePiece structurepiece : structurestart.getComponents()) {
                        if (structurepiece.func_214810_a(chunkpos, 12) && structurepiece instanceof AbstractVillagePiece) {
                            AbstractVillagePiece abstractvillagepiece = (AbstractVillagePiece)structurepiece;
                            JigsawPattern.PlacementBehaviour jigsawpattern$placementbehaviour = abstractvillagepiece.func_214826_b().getPlacementBehaviour();
                            if (jigsawpattern$placementbehaviour == JigsawPattern.PlacementBehaviour.RIGID) {
                                objectlist.add(abstractvillagepiece);
                            }

                            for(JigsawJunction jigsawjunction : abstractvillagepiece.getJunctions()) {
                                int k1 = jigsawjunction.getSourceX();
                                int l1 = jigsawjunction.getSourceZ();
                                if (k1 > blockX - 12 && l1 > blockZ - 12 && k1 < blockX + 15 + 12 && l1 < blockZ + 15 + 12) {
                                    objectlist1.add(jigsawjunction);
                                }
                            }
                        }
                    }
                }
            }
        }

        double[][][] adouble = new double[2][this.field_222567_n + 1][this.field_222566_m + 1];

        for(int j5 = 0; j5 < this.field_222567_n + 1; ++j5) {
            adouble[0][j5] = new double[this.field_222566_m + 1];
            this.func_222548_a(adouble[0][j5], chunkX * this.field_222565_l, chunkZ * this.field_222567_n + j5);
            adouble[1][j5] = new double[this.field_222566_m + 1];
        }

        ChunkPrimer chunkprimer = (ChunkPrimer)chunk;
        Heightmap heightmap = chunkprimer.func_217303_b(Heightmap.Type.OCEAN_FLOOR_WG);
        Heightmap heightmap1 = chunkprimer.func_217303_b(Heightmap.Type.WORLD_SURFACE_WG);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        ObjectListIterator<AbstractVillagePiece> objectlistiterator = objectlist.iterator();
        ObjectListIterator<JigsawJunction> objectlistiterator1 = objectlist1.iterator();

        for(int k5 = 0; k5 < this.field_222565_l; ++k5) {
            for(int l5 = 0; l5 < this.field_222567_n + 1; ++l5) {
                this.func_222548_a(adouble[1][l5], chunkX * this.field_222565_l + k5 + 1, chunkZ * this.field_222567_n + l5);
            }

            for(int i6 = 0; i6 < this.field_222567_n; ++i6) {
                ChunkSection chunksection = chunkprimer.func_217332_a(15);
                chunksection.lock();

                for(int j6 = this.field_222566_m - 1; j6 >= 0; --j6) {
                    double d16 = adouble[0][i6][j6];
                    double d17 = adouble[0][i6 + 1][j6];
                    double d18 = adouble[1][i6][j6];
                    double d0 = adouble[1][i6 + 1][j6];
                    double d1 = adouble[0][i6][j6 + 1];
                    double d2 = adouble[0][i6 + 1][j6 + 1];
                    double d3 = adouble[1][i6][j6 + 1];
                    double d4 = adouble[1][i6 + 1][j6 + 1];

                    for(int i2 = this.field_222563_j - 1; i2 >= 0; --i2) {
                        int j2 = j6 * this.field_222563_j + i2;
                        int k2 = j2 & 15;
                        int l2 = j2 >> 4;
                        if (chunksection.getYLocation() >> 4 != l2) {
                            chunksection.unlock();
                            chunksection = chunkprimer.func_217332_a(l2);
                            chunksection.lock();
                        }

                        double d5 = (double)i2 / (double)this.field_222563_j;
                        double d6 = MathHelper.lerp(d5, d16, d1);
                        double d7 = MathHelper.lerp(d5, d18, d3);
                        double d8 = MathHelper.lerp(d5, d17, d2);
                        double d9 = MathHelper.lerp(d5, d0, d4);

                        for(int i3 = 0; i3 < this.field_222564_k; ++i3) {
                            int j3 = blockX + k5 * this.field_222564_k + i3;
                            int k3 = j3 & 15;
                            double d10 = (double)i3 / (double)this.field_222564_k;
                            double d11 = MathHelper.lerp(d10, d6, d7);
                            double d12 = MathHelper.lerp(d10, d8, d9);

                            for(int l3 = 0; l3 < this.field_222564_k; ++l3) {
                                int i4 = blockZ + i6 * this.field_222564_k + l3;
                                int j4 = i4 & 15;
                                double d13 = (double)l3 / (double)this.field_222564_k;
                                double d14 = MathHelper.lerp(d13, d11, d12);
                                double d15 = MathHelper.clamp(d14 / 200.0D, -1.0D, 1.0D);

                                int k4;
                                int l4;
                                int i5;
                                for(d15 = d15 / 2.0D - d15 * d15 * d15 / 24.0D; objectlistiterator.hasNext(); d15 += func_222556_a(k4, l4, i5) * 0.8D) {
                                    AbstractVillagePiece abstractvillagepiece1 = objectlistiterator.next();
                                    MutableBoundingBox mutableboundingbox = abstractvillagepiece1.getBoundingBox();
                                    k4 = Math.max(0, Math.max(mutableboundingbox.minX - j3, j3 - mutableboundingbox.maxX));
                                    l4 = j2 - (mutableboundingbox.minY + abstractvillagepiece1.getGroundLevelDelta());
                                    i5 = Math.max(0, Math.max(mutableboundingbox.minZ - i4, i4 - mutableboundingbox.maxZ));
                                }

                                objectlistiterator.back(objectlist.size());

                                while(objectlistiterator1.hasNext()) {
                                    JigsawJunction jigsawjunction1 = objectlistiterator1.next();
                                    int k6 = j3 - jigsawjunction1.getSourceX();
                                    k4 = j2 - jigsawjunction1.getSourceGroundY();
                                    l4 = i4 - jigsawjunction1.getSourceZ();
                                    d15 += func_222556_a(k6, k4, l4) * 0.4D;
                                }

                                objectlistiterator1.back(objectlist1.size());

                                // Check the local biome. If it's a TFCR biome, then respect its
                                // preferred default block/fluid preferences. In case we have other
                                // mods or vanilla biomes, respect the defaults, too!
//                                Biome localBiome = localBiomes[(k3 << 4) + j4];
//                                defaultBlock = this.field_222559_f;
//                                defaultFluid = this.field_222560_g;
//                                if (localBiome instanceof BaseTFCRBiome) {
//                                    BaseTFCRBiome tfcrBiome = (BaseTFCRBiome) localBiome;
//                                    defaultBlock = tfcrBiome.getDefaultBlock();
//                                    defaultFluid = tfcrBiome.getDefaultFluid();
//                                }

                                BlockState blockstate;
                                int biomeIndex = (k3 << 4) + j4;
                                if (d15 > 0.0D) {
                                    // Default solid block
                                    if (allSameDefaultBlock) {
                                        blockstate = staticDefaultBlock;
                                    } else {
                                        blockstate = defaultBlocks[biomeIndex];
                                    }
//                                    blockstate = staticDefaultBlock;
//                                    blockstate = this.field_222559_f;
                                } else if (j2 < seaLevel) {
                                    // Default fluid block
                                    if (allSameDefaultFluid) {
                                        blockstate = staticDefaultFluid;
                                    } else {
                                        blockstate = defaultFluids[biomeIndex];
                                    }
//                                    blockstate = staticDefaultFluid;

//                                    blockstate = this.field_222560_g;
                                } else {
                                    blockstate = AIR;
                                }

                                if (blockstate != AIR) {
                                    if (blockstate.getLightValue() != 0) {
                                        blockpos$mutableblockpos.setPos(j3, j2, i4);
                                        chunkprimer.addLightPosition(blockpos$mutableblockpos);
                                    }

                                    chunksection.set(k3, k2, j4, blockstate, false);
                                    heightmap.update(k3, j2, j4, blockstate);
                                    heightmap1.update(k3, j2, j4, blockstate);
                                }
                            }
                        }
                    }
                }

                chunksection.unlock();
            }

            double[][] adouble1 = adouble[0];
            adouble[0] = adouble[1];
            adouble[1] = adouble1;
        }
        chunkGenTime += (System.nanoTime() - start);
    }


    private static double func_222556_a(int p_222556_0_, int p_222556_1_, int p_222556_2_) {
        int i = p_222556_0_ + 12;
        int j = p_222556_1_ + 12;
        int k = p_222556_2_ + 12;
        if (i >= 0 && i < 24) {
            if (j >= 0 && j < 24) {
                return k >= 0 && k < 24 ? (double)GAUSSIAN_BLUR[k * 24 * 24 + i * 24 + j] : 0.0D;
            } else {
                return 0.0D;
            }
        } else {
            return 0.0D;
        }
    }
}
