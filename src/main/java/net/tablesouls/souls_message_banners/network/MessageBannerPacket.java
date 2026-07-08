package net.tablesouls.souls_message_banners.network;

import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tablesouls.souls_message_banners.assets.BannerStyle;
import net.tablesouls.souls_message_banners.assets.BannerStyleManager;
import net.tablesouls.souls_message_banners.util.MessageBannerHelper;
import org.slf4j.Logger;

import java.util.function.Supplier;

public class MessageBannerPacket {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Component message;
    private final ResourceLocation styleId;

    public MessageBannerPacket(Component message, ResourceLocation styleId) {
        this.message = message;
        this.styleId = styleId;
    }

    public static void encode(MessageBannerPacket packet, FriendlyByteBuf buf) {
        buf.writeComponent(packet.message);
        buf.writeResourceLocation(packet.styleId);
    }

    public static MessageBannerPacket decode(FriendlyByteBuf buf) {
        return new MessageBannerPacket(buf.readComponent(), buf.readResourceLocation());
    }

    public static void handle(MessageBannerPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().setPacketHandled(true);
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            BannerStyle style = BannerStyleManager.get(packet.styleId);
            if (style == null) {
                LOGGER.error("Unknown banner style '{}'", packet.styleId);
                return;
            }

            MessageBannerHelper.show(packet.message, style);
        });
    }
}
