package net.tablesouls.souls_message_banners.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;

public record MessageBannerPacket(Component message, ResourceLocation styleId, String identifier, ResourceLocation triggerId) implements CustomPacketPayload {

    public static final Type<MessageBannerPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(SoulsMessageBanners.MODID, "message_banner"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageBannerPacket> STREAM_CODEC =
            StreamCodec.of(MessageBannerPacket::encode, MessageBannerPacket::decode);

    private static void encode(RegistryFriendlyByteBuf buf, MessageBannerPacket packet) {
        ComponentSerialization.STREAM_CODEC.encode(buf, packet.message());
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

    private static MessageBannerPacket decode(RegistryFriendlyByteBuf buf) {
        Component message = ComponentSerialization.STREAM_CODEC.decode(buf);
        ResourceLocation styleId = buf.readResourceLocation();
        String identifier = buf.readBoolean() ? buf.readUtf() : null;
        ResourceLocation triggerId = buf.readBoolean() ? buf.readResourceLocation() : null;
        return new MessageBannerPacket(message, styleId, identifier, triggerId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}