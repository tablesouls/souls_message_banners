package net.tablesouls.souls_message_banners.listeners;

import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;

@Mod.EventBusSubscriber(modid = SoulsMessageBanners.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientReloadListeners {
    @SubscribeEvent
    public static void registerReloadListeners(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new BannerStyleReloadListener());
    }
}
