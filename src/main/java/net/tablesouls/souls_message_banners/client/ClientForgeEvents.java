package net.tablesouls.souls_message_banners.client;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;
import net.tablesouls.souls_message_banners.util.MessageBannerHelper;

@EventBusSubscriber(modid = SoulsMessageBanners.MODID)
public class ClientForgeEvents {
    @SubscribeEvent
    public static void onRenderCrosshair(RenderGuiLayerEvent.Pre event) {
        if (!event.getName().equals(VanillaGuiLayers.CROSSHAIR)) return;
        if (!SoulsMessageBannersConfig.HIDE_CROSSHAIR_WHEN_BANNER.get()) return;
        if (MessageBannerHelper.getMessage() != null) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        MessageBannerHelper.tick();
    }
}