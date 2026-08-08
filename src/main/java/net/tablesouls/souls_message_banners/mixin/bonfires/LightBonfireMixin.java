package net.tablesouls.souls_message_banners.mixin.bonfires;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.tablesouls.souls_message_banners.api.MessageBannerAPI;
import net.tablesouls.souls_message_banners.compat.bonfires.BonfiresCompat;
import net.tablesouls.souls_message_banners.data.TriggerEntry;
import net.tablesouls.souls_message_banners.data.TriggerManager;
import net.tablesouls.souls_message_banners.data.TriggerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.DisplayTitle;
import wehavecookies56.bonfires.packets.server.LightBonfire;

@Mixin(LightBonfire.class)
public class LightBonfireMixin {
    private static final String BONFIRE_IDENTIFIER = BonfiresCompat.BONFIRE_LIT_IDENTIFIER;

    @Redirect(
            method = "handle",
            at = @At(
                    value = "INVOKE",
                    target = "Lwehavecookies56/bonfires/packets/PacketHandler;sendTo(Lwehavecookies56/bonfires/packets/Packet;Lnet/minecraft/server/level/ServerPlayer;)V"
            )
    )
    private void souls_message_banners$onSendTo(Packet packet, ServerPlayer player) {
        if (packet instanceof DisplayTitle) {
            TriggerEntry entry = TriggerManager.getSpecial(TriggerType.BONFIRE_LIT, BONFIRE_IDENTIFIER);
            if (entry != null) {
                Component message = entry.message() != null
                        ? Component.Serializer.fromJson(entry.message(), player.registryAccess())
                        : Component.translatable("souls_message_banners.message.bonfire_lit");

                MessageBannerAPI.send(player, entry, message, BONFIRE_IDENTIFIER);
                return;
            }
        }
        PacketHandler.sendTo(packet, player);
    }
}