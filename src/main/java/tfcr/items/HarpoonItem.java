package tfcr.items;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class HarpoonItem extends Item {
    public HarpoonItem() {
        super(new Item.Properties()
                .group(ItemGroup.TOOLS)
                .maxDamage(44)
        );
        // Copied from TridentItem
        this.addPropertyOverride(new ResourceLocation("throwing"),
                (p_210315_0_, p_210315_1_, p_210315_2_) ->
                        p_210315_2_ != null && p_210315_2_.isHandActive() && p_210315_2_.getActiveItemStack() == p_210315_0_ ? 1.0F : 0.0F);
    }

    @Override
    public boolean canPlayerBreakBlockWhileHolding(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        return !player.isCreative();
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
    public int getUseDuration(ItemStack stack) {
        return 36000; // Half the time for a trident; 1.8 seconds
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.SPEAR;
    }
    /**
     * Current implementations of this method in child classes do not use the entry argument beside ev. They just raise
     * the damage on the stack.
     */
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damageItem(1, attacker, (p_220048_0_) -> {
            p_220048_0_.sendBreakAnimation(EquipmentSlotType.MAINHAND);
        });
        return true;
    }

    @Override
    public void onUsingTick(ItemStack stack, LivingEntity player, int count) {

    }
}
