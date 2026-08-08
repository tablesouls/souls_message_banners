package net.tablesouls.souls_message_banners.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record MessageBannerPacket(Component message, ResourceLocation styleId, String identifier, ResourceLocation triggerId) {

    public static void encode(MessageBannerPacket packet, FriendlyByteBuf buf) {
        buf.writeComponent(packet.message());
        buf.writeResourceLocation(packet.styleId());
        buf.writeBoolean(packet.identifier() != null);
        if (packet.identifier() != null) {
            buf.writeUtf(packet.identifier());
        }
        buf.writeBoolean(packet.triggerId() != null);
        if (packet.triggerId() != null) {
            buf.writeResourceLocation(packet.triggerId());
        }
    }

    public static MessageBannerPacket decode(FriendlyByteBuf buf) {
        Component message = buf.readComponent();
        ResourceLocation styleId = buf.readResourceLocation();
        String identifier = buf.readBoolean() ? buf.readUtf() : null;
        ResourceLocation triggerId = buf.readBoolean() ? buf.readResourceLocation() : null;
        return new MessageBannerPacket(message, styleId, identifier, triggerId);
    }
}