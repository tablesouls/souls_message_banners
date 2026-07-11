package net.tablesouls.souls_message_banners.listeners;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;

@EventBusSubscriber(modid = SoulsMessageBanners.MODID, bus = EventBusSubscriber.Bus.GAME)
public class CommonReloadListeners {
    @SubscribeEvent
    public static void registerReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new EntityBannerReloadListener());
    }
}