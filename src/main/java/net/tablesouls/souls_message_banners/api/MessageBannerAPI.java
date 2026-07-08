package net.tablesouls.souls_message_banners.api;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import net.tablesouls.souls_message_banners.assets.BannerStyleManager;
import net.tablesouls.souls_message_banners.network.MessageBannerPacket;
import net.tablesouls.souls_message_banners.network.NetworkHandler;

public class MessageBannerAPI {
    public static void send(Player player, MessageBanner banner) {
        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer),
                    new MessageBannerPacket(banner.message(), banner.styleId()));
        }
    }

    public static void send(Player player, Component message, ResourceLocation styleId) {
        send(player, new MessageBanner(message, styleId));
    }

    public static void send(Player player, Component message) {
        send(player, new MessageBanner(message, BannerStyleManager.DEFAULT));
    }
}
