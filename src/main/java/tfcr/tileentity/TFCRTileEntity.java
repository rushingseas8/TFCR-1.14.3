package tfcr.tileentity;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

/**
 * WIP class to allow for automatic self-registration of TileEntities.
 * TODO: still need to have an internal mod registry, something like for Blocks,
 *  where we at least list each TileEntity once and assign valid Blocks for them.
 */
public abstract class TFCRTileEntity extends TileEntity {

    /**
     * Custom implementation of TileEntityType that supports being modified
     * after creation. This allows us to create a new TE Type on registration.
     */
    private static class TFCRTileEntityType extends TileEntityType {
        public Supplier tileEntityFactory;
        public Set<Block> blocks;

        public TFCRTileEntityType() {
            super(null, null, null);
        }

        @Nullable
        @Override
        public TileEntity create() {
            return (TFCRTileEntity) tileEntityFactory.get();
        }

        @Override
        public boolean isValidBlock(@Nonnull Block blockIn) {
            return blocks.contains(blockIn);
        }
    }

    private static ArrayList<TFCRTileEntityType> allModdedTileEntityTypes;

    private TFCRTileEntityType mutableType;
    private TileEntityType type;

    public TFCRTileEntity() {
        super(new TFCRTileEntityType());
        this.mutableType = (TFCRTileEntityType) getType();

        if (allModdedTileEntityTypes == null) {
            allModdedTileEntityTypes = new ArrayList<>();
        }
        allModdedTileEntityTypes.add(this.mutableType);
    }

    public abstract Block[] getValidBlocks();

    public void selfRegister(IForgeRegistry<TileEntityType<?>> tileEntityRegistry) {
        // Check if concrete type exists
        if (type != null) {
            System.out.println("TileEntityType already exists for class: " + this.getClass());
            if (tileEntityRegistry.containsValue(this.type)) {
                System.out.println("Duplicate TileEntityType registration of type: " + this.type.getClass());
            } else {
                System.out.println("TileEntityType already created, but not registered: "
                        + this.type.getClass() + ". Adding to registry.");
                tileEntityRegistry.register(this.type);
            }
            return;
        }

        // Set the concrete type, so we don't double register a new TE type
        this.type = TileEntityType.Builder.create(() -> this, getValidBlocks()).build(null);

        // Modify the already set type so its methods are now valid
        mutableType.tileEntityFactory = () -> this;
        mutableType.blocks = ImmutableSet.copyOf(getValidBlocks());
    }

    public static List<TFCRTileEntityType> getAllTypes() {
        return allModdedTileEntityTypes;
    }
}
