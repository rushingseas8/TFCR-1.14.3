package tfcr;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import tfcr.player.TFCRArmorCapability;
import tfcr.player.TFCRArmorCapabilityProvider;

import javax.annotation.Nullable;

@Mod(TFCR.MODID)
@Mod.EventBusSubscriber(modid = TFCR.MODID)
public class TFCR {
    public static final String MODID = "tfcr";
    public static final String NAME = "TerraFirmaCraft Reloaded";
    public static final String VERSION = "1.0";

    public TFCR() {
        // @Mod annotation gets this constructor to run. This explicitly calls the #onCommonSetup
        // method, so we can do common registration.
        FMLJavaModLoadingContext.get().getModEventBus().addListener(TFCR::onCommonSetup);
    }

    /**
     * General common setup event.
     *
     * Currently used to register capabilities.
     * @param event
     */
    @SubscribeEvent
    public static void onCommonSetup(FMLCommonSetupEvent event) {
        CapabilityManager.INSTANCE.register(
                TFCRArmorCapability.class,
                new Capability.IStorage<TFCRArmorCapability>() {
                    @Nullable
                    @Override
                    public INBT writeNBT(Capability<TFCRArmorCapability> capability, TFCRArmorCapability instance, Direction side) {
                        return instance.serializeNBT();
                    }

                    @Override
                    public void readNBT(Capability<TFCRArmorCapability> capability, TFCRArmorCapability instance, Direction side, INBT nbt) {
                        if (nbt instanceof CompoundNBT) {
                            instance.deserializeNBT((CompoundNBT) nbt);
                        } else {
                            throw new RuntimeException("NBT was of wrong type: " + nbt.getClass() + ". toString: " + nbt.toString());
                        }
                    }
                },
                TFCRArmorCapability::new);
        System.out.println("***Value:" + TFCRArmorCapabilityProvider.TFCR_ARMOR_CAPABILITY);
    }

    /**
     * Capability attachment event, which gives us the chance to add capabilities to Entities.
     *
     * Currently used to add the TFCR armor capability to players on start.
     * @param event
     */
    @SubscribeEvent
    public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getObject();
            event.addCapability(new ResourceLocation(TFCR.MODID, "armor_capability"), new TFCRArmorCapabilityProvider());
        }
    }

    /**
     * Entity construction event. I think this is called the first time an Entity is instantiated,
     * which usually occurs before an instance of that Entity is added to the world.
     *
     * This will be used to add RenderLayers to the player for custom TFCR armor rendering.
     * @param event
     */
    @SubscribeEvent
    public static void onEntityConstructing(EntityEvent.EntityConstructing event) {
//        System.out.println("Entity constructed event!");
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
        }
    }

    /**
     * Entity joining event. Called when an Entity is added to the world.
     *
     * This will be used to add RenderLayers to the player for custom TFCR armor rendering.
     * @param event
     */
    @SubscribeEvent
    public static void onEntityJoin(EntityJoinWorldEvent event) {
        //System.out.println("Joined!");
        if (event.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) event.getEntity();
        }
    }
}
