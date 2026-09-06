package net.tablesouls.souls_message_banners.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {

    @Redirect(
            method = "setRespawnPosition",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;sendSystemMessage(Lnet/minecraft/network/chat/Component;)V"
            )
    )
    private void souls_message_banners$hideSpawnSetMessage(ServerPlayer player, Component message) {
        if (SoulsMessageBannersConfig.BLOCK_RESPAWN_POINT_SET_MESSAGE.get()) {
            return;
        }
        player.sendSystemMessage(message);
    }
}