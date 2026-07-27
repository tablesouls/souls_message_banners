package net.tablesouls.souls_message_banners.mixin.bonfires;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.tablesouls.souls_message_banners.api.MessageBannerAPI;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import wehavecookies56.bonfires.packets.Packet;
import wehavecookies56.bonfires.packets.PacketHandler;
import wehavecookies56.bonfires.packets.client.DisplayTitle;
import wehavecookies56.bonfires.packets.server.LightBonfire;

@Mixin(LightBonfire.class)
public class LightBonfireMixin {
    @Redirect(
            method = "handle", at = @At(value = "INVOKE",
            target = "Lwehavecookies56/bonfires/packets/PacketHandler;sendTo(Lwehavecookies56/bonfires/packets/Packet;Lnet/minecraft/server/level/ServerPlayer;)V"),
            remap = false
    )
    private void souls_message_banners$onSendTo(Packet<?> packet, ServerPlayer player) {
        if (packet instanceof DisplayTitle && SoulsMessageBannersConfig.TRIGGERS.BONFIRE_LIT.get()) {
            MessageBannerAPI.send(player,
                    Component.translatable("souls_message_banners.message.bonfire_lit"),
                    ResourceLocation.fromNamespaceAndPath("souls_message_banners", "bonfire_lit"));
            return;
        }
        PacketHandler.sendTo(packet, player);
    }
}
