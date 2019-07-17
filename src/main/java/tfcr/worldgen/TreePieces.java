package tfcr.worldgen;

import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.TemplateStructurePiece;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.TemplateManager;

import java.util.List;
import java.util.Random;

public class TreePieces {

    public static void initPieces(TemplateManager manager, BlockPos pos, Rotation rotation, List<StructurePiece> pieces, Random random, TreeFeatureConfig config) {

    }

    public static class Piece extends TemplateStructurePiece {
        private ResourceLocation location;
        private Rotation rotation;

        public Piece() {
            super(null, 0);
        }

        public Piece(TemplateManager manager, ResourceLocation location, BlockPos pos, Rotation rotation) {
//            super(0); // TODO unsure what this type represents
            super(null, 0); // TODO i just did this to prevent compiler errors, this is wrong
            this.location = location;
            this.rotation = rotation;
            this.templatePosition = pos;

            // setup/init
            setup(manager);
        }

        private void setup(TemplateManager manager) {
            Template template = manager.getTemplateDefaulted(location);
            BlockPos templateSize = template.getSize();
            BlockPos offset = new BlockPos(-templateSize.getX() / 2, 0, -templateSize.getZ() / 2);
            PlacementSettings settings = new PlacementSettings().setRotation(rotation).setMirror(Mirror.NONE).setCenterOffset(offset);
            this.setup(template, this.templatePosition, settings);
        }

        // TODO find out how this is supposed to work in 1.14
//        /**
//         * (abstract) Helper method to write subclass data to NBT
//         */
//        @Override
//        protected void writeStructureToNBT(CompoundNBT tagCompound) {
//            super.writeStructureToNBT(tagCompound);
//            tagCompound.setString("Location", this.location.toString());
//            tagCompound.setString("Rotation", this.rotation.name());
//        }
//
//        /**
//         * (abstract) Helper method to read subclass data from NBT
//         */
//        protected void readStructureFromNBT(CompoundNBT tagCompound, TemplateManager manager) {
//            super.readStructureFromNBT(tagCompound, manager);
//            this.location = new ResourceLocation(tagCompound.getString("Location"));
//            this.rotation = Rotation.valueOf(tagCompound.getString("Rotation"));
//            this.setup(manager);
//        }

        @Override
        protected void handleDataMarker(String function, BlockPos pos, IWorld worldIn, Random rand, MutableBoundingBox sbb) {

        }
    }
}
