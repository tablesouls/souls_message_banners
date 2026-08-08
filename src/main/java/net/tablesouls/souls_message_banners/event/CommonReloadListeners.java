package net.tablesouls.souls_message_banners.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;

@EventBusSubscriber(modid = SoulsMessageBanners.MODID)
public class CommonReloadListeners {
    @SubscribeEvent
    public static void registerReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new TriggerReloadListener());
    }
}