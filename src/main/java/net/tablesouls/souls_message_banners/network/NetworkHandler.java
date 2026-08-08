package net.tablesouls.souls_message_banners.network;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;

@EventBusSubscriber(modid = SoulsMessageBanners.MODID)
public class NetworkHandler {
    @SubscribeEvent
    public static void register(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");

        registrar.playToClient(
                MessageBannerPacket.TYPE,
                MessageBannerPacket.STREAM_CODEC,
                MessageBannerPacketHandler::handle
        );
    }
}