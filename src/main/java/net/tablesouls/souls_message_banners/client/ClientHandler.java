package net.tablesouls.souls_message_banners.client;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.util.MessageBannerHelper;

@EventBusSubscriber(modid = SoulsMessageBanners.MODID)
public class ClientHandler {
    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        MessageBannerHelper.tick();
    }
}