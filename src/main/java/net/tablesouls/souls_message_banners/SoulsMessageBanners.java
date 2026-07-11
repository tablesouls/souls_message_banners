package net.tablesouls.souls_message_banners;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.minecraft.resources.ResourceLocation;
import net.tablesouls.souls_message_banners.client.MessageBannerRenderer;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;
import net.tablesouls.souls_message_banners.integration.BonfiresCompat;
import net.tablesouls.souls_message_banners.integration.WaystonesCompat;
import net.tablesouls.souls_message_banners.sound.ModSounds;

@Mod(SoulsMessageBanners.MODID)
public class SoulsMessageBanners
{
    public static final String MODID = "souls_message_banners";

    public SoulsMessageBanners(IEventBus modEventBus, ModContainer modContainer) {
        ModSounds.SOUND_EVENTS.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, SoulsMessageBannersConfig.COMMON_SPEC);
        modContainer.registerConfig(ModConfig.Type.CLIENT, SoulsMessageBannersConfig.CLIENT_SPEC);

        if (ModList.get().isLoaded("bonfires")) {
            NeoForge.EVENT_BUS.register(BonfiresCompat.class);
        }
        if (ModList.get().isLoaded("waystones")) {
            NeoForge.EVENT_BUS.register(WaystonesCompat.class);
        }
    }

    @EventBusSubscriber(value = Dist.CLIENT, modid = SoulsMessageBanners.MODID)
    public static class ClientSetup {
        @SubscribeEvent
        public static void onRegisterLayers(RegisterGuiLayersEvent event) {
            event.registerAboveAll(
                    ResourceLocation.fromNamespaceAndPath(SoulsMessageBanners.MODID, "message_banner"),
                    new MessageBannerRenderer()
            );
        }
    }
}