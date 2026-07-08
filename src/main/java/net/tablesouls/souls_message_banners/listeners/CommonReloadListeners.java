package net.tablesouls.souls_message_banners.listeners;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;

@Mod.EventBusSubscriber(modid = SoulsMessageBanners.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CommonReloadListeners {
    @SubscribeEvent
    public static void registerReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new EntityBannerReloadListener());
    }
}
