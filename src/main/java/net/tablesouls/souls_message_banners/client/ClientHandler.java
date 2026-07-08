package net.tablesouls.souls_message_banners.client;

import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.util.MessageBannerHelper;

@Mod.EventBusSubscriber(modid = SoulsMessageBanners.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ClientHandler {
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            MessageBannerHelper.tick();
        }
    }
}