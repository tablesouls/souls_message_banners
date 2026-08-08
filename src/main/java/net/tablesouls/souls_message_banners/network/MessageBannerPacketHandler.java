package net.tablesouls.souls_message_banners.network;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.assets.BannerStyle;
import net.tablesouls.souls_message_banners.assets.BannerStyleManager;
import net.tablesouls.souls_message_banners.client.ClientBannerPlayTracker;
import net.tablesouls.souls_message_banners.compat.twemoji.TwemojiCompat;
import net.tablesouls.souls_message_banners.util.MessageBannerHelper;

import java.util.function.Supplier;

public class MessageBannerPacketHandler {

    public static void handle(MessageBannerPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> onClient(packet));
    }

    private static void onClient(MessageBannerPacket packet) {
        BannerStyle style = BannerStyleManager.get(packet.styleId());
        if (style == null) {
            SoulsMessageBanners.LOGGER.error("Unknown banner style '{}'", packet.styleId());
            return;
        }

        if (!ClientBannerPlayTracker.shouldPlay(packet.styleId(), packet.identifier(), packet.triggerId())) {
            return;
        }

        Component message = packet.message();
        if (TwemojiCompat.LOADED) {
            message = TwemojiCompat.rewrite(message);
        }
        MessageBannerHelper.show(message, style);
    }
}