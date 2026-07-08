package net.tablesouls.souls_message_banners.integration;

import net.blay09.mods.waystones.api.WaystoneActivatedEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.api.MessageBannerAPI;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;

public class WaystonesCompat {
    @SubscribeEvent
    public static void onWaystoneActivated(WaystoneActivatedEvent event) {
        if (!SoulsMessageBannersConfig.WAYSTONE_ACTIVATION.get()) return;
        if (!((event.getPlayer()) instanceof ServerPlayer serverPlayer)) return;

        MessageBannerAPI.send(
                serverPlayer,
                Component.translatable("souls_message_banners.message.waystone_activated"),
                new ResourceLocation(SoulsMessageBanners.MODID, "waystone_activated"));
    }
}
