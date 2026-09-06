package net.tablesouls.souls_message_banners.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.level.Level;
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
                    target = "Lnet/minecraft/server/level/ServerPlayer;setRespawnPosition(Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/core/BlockPos;FZZ)V"
            )
    )
    private void souls_message_banners$respawnCopy(
            ServerPlayer player, ResourceKey<Level> dimension, BlockPos pos, float angle, boolean forced, boolean sendMessage) {
        PlayerSetSpawnTrigger.SUPPRESS_MESSAGE = true;
        try {
            player.setRespawnPosition(dimension, pos, angle, forced, sendMessage);
        } finally {
            PlayerSetSpawnTrigger.SUPPRESS_MESSAGE = false;
        }
    }
}