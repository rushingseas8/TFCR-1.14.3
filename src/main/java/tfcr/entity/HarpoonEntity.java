package tfcr.entity;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import tfcr.items.HarpoonItem;

public class HarpoonEntity extends AbstractArrowEntity {

    private ItemStack thrownStack = new ItemStack(HarpoonItem::new);

    protected HarpoonEntity(World p_i48546_2_) {
        super(EntityType.Builder.<HarpoonEntity>create(HarpoonEntity::new, EntityClassification.MISC).size(0.5F, 0.5F), p_i48546_2_);
    }

    @Override
    protected ItemStack getArrowStack() {
        return thrownStack.copy();
    }
}
