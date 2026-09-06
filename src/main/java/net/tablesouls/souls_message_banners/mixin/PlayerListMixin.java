package net.tablesouls.souls_message_banners.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.tablesouls.souls_message_banners.event.triggers.PlayerSetSpawnTrigger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PlayerList.class)
public class PlayerListMixin {

    @Redirect(
            method = "respawn",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/server/level/ServerPlayer;copyRespawnPosition(Lnet/minecraft/server/level/ServerPlayer;)V"
            )
    )
    private void souls_message_banners$suppressRespawnCopy(ServerPlayer newPlayer, ServerPlayer oldPlayer) {
        PlayerSetSpawnTrigger.SUPPRESS_MESSAGE = true;
        try {
            newPlayer.copyRespawnPosition(oldPlayer);
        } finally {
            PlayerSetSpawnTrigger.SUPPRESS_MESSAGE = false;
        }
    }
}