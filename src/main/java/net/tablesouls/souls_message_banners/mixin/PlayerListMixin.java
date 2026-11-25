package net.tablesouls.souls_message_banners.mixin;

import net.minecraft.server.players.PlayerList;
import net.tablesouls.souls_message_banners.event.triggers.PlayerSetSpawnTrigger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings({"rawtypes", "unchecked"})
@Mixin(PlayerList.class)
public class PlayerListMixin {
    @Inject(method = "respawn", at = @At("HEAD"))
    private void souls_message_banners$suppressStart(CallbackInfoReturnable cir) {
        PlayerSetSpawnTrigger.SUPPRESS_MESSAGE = true;
    }

    @Inject(method = "respawn", at = @At("RETURN"))
    private void souls_message_banners$suppressEnd(CallbackInfoReturnable cir) {
        PlayerSetSpawnTrigger.SUPPRESS_MESSAGE = false;
    }
}