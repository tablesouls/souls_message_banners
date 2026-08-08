package net.tablesouls.souls_message_banners.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.client.ClientBannerPlayTracker;
import net.tablesouls.souls_message_banners.config.BannerPlayMode;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;
import net.tablesouls.souls_message_banners.util.MessageBannerHelper;

@EventBusSubscriber(modid = SoulsMessageBanners.MODID)
public class ClientForgeEvents {
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        MessageBannerHelper.tick();
    }

    @SubscribeEvent
    public static void onRenderCrosshair(RenderGuiLayerEvent.Pre event) {
        if (!event.getName().equals(VanillaGuiLayers.CROSSHAIR)) return;
        if (!SoulsMessageBannersConfig.HIDE_CROSSHAIR_WHEN_BANNER.get()) return;
        if (MessageBannerHelper.getMessage() != null) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLoggedOut(ClientPlayerNetworkEvent.LoggingOut event) {
        if (SoulsMessageBannersConfig.BANNER_PLAY_MODE.get() == BannerPlayMode.ONCE_A_WORLD) {
            ClientBannerPlayTracker.clear();
        }
    }
}