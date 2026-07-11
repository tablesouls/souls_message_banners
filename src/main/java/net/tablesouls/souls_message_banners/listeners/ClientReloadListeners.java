package net.tablesouls.souls_message_banners.listeners;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;

@EventBusSubscriber(modid = SoulsMessageBanners.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ClientReloadListeners {
    @SubscribeEvent
    public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new BannerStyleReloadListener());
    }
}