package net.tablesouls.souls_message_banners.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;

public class NetworkHandler {

    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
             ResourceLocation.fromNamespaceAndPath(SoulsMessageBanners.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;
    private static int nextId() {
        return packetId++;
    }

    public static void register() {
        INSTANCE.messageBuilder(MessageBannerPacket.class, nextId())
                .encoder(MessageBannerPacket::encode)
                .decoder(MessageBannerPacket::decode)
                .consumerMainThread(MessageBannerPacket::handle)
                .add();
    }
}
