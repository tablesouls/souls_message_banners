package net.tablesouls.souls_message_banners.network;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;

public record MessageBannerPacket(Component message, ResourceLocation styleId) implements CustomPacketPayload {

    public static final Type<MessageBannerPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(SoulsMessageBanners.MODID, "message_banner"));

    public static final StreamCodec<RegistryFriendlyByteBuf, MessageBannerPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ComponentSerialization.STREAM_CODEC, MessageBannerPacket::message,
                    ResourceLocation.STREAM_CODEC, MessageBannerPacket::styleId,
                    MessageBannerPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}