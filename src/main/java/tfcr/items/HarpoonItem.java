package tfcr.items;

import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import tfcr.entity.HarpoonEntity;

public class HarpoonItem extends TFCRItem {
    public HarpoonItem() {
        super(new Item.Properties()
                .group(ItemGroup.TOOLS)
                .maxDamage(44),
                "harpoon"
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

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        if (itemstack.getDamage() >= itemstack.getMaxDamage()) {
            return new ActionResult<>(ActionResultType.FAIL, itemstack);
        } else {
            playerIn.setActiveHand(handIn);
            return new ActionResult<>(ActionResultType.SUCCESS, itemstack);
        }
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
    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
        if (!(entityLiving instanceof PlayerEntity)) {
            return;
        }

        PlayerEntity playerEntity = (PlayerEntity) entityLiving;
        // If the time used is too short (i.e., from spamming right click), do nothing
        if (getUseDuration(stack) - timeLeft < 10) {
            return;
        }

        // If we're on the server, then spawn in a Harpoon Entity
        if (!worldIn.isRemote) {
            // TODO register and setup this entity properly in mod config
            HarpoonEntity harpoonEntity = new HarpoonEntity();
            harpoonEntity.shoot(playerEntity, playerEntity.rotationPitch, playerEntity.rotationYaw, 8.0F, 2.5F, Float.POSITIVE_INFINITY);

            // Ensure creative mode pickup mirrors vanilla Trident functionality
            if (playerEntity.abilities.isCreativeMode) {
                harpoonEntity.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
            }

            // Add the entity to the world and play a throwing sound
            worldIn.addEntity(harpoonEntity);
            worldIn.playMovingSound(null, harpoonEntity, SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.PLAYERS, 1.0F, 1.0F);

            // Remove the harpoon from the player's inventory (if not in creative mode)
            if (!playerEntity.abilities.isCreativeMode) {
                playerEntity.inventory.deleteStack(stack);
            }
        }
    }
}
