package net.tablesouls.souls_message_banners.network;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.tablesouls.souls_message_banners.assets.BannerStyle;
import net.tablesouls.souls_message_banners.assets.BannerStyleManager;
import net.tablesouls.souls_message_banners.integration.TwemojiCompat;
import net.tablesouls.souls_message_banners.util.MessageBannerHelper;
import org.slf4j.Logger;

public class MessageBannerPacketHandler {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static void handle(MessageBannerPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            BannerStyle style = BannerStyleManager.get(packet.styleId());
            if (style == null) {
                LOGGER.error("Unknown banner style '{}'", packet.styleId());
                return;
            }

            Component message = packet.message();
            if (ModList.get().isLoaded("twemoji")) {
                message = TwemojiCompat.rewrite(message);
            }
            MessageBannerHelper.show(message, style);
        });
    }
}