package net.tablesouls.souls_message_banners.mixin;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.raid.Raid;
import net.tablesouls.souls_message_banners.api.MessageBannerAPI;
import net.tablesouls.souls_message_banners.data.TriggerEntry;
import net.tablesouls.souls_message_banners.data.TriggerManager;
import net.tablesouls.souls_message_banners.data.TriggerType;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Raid.class)
public class RaidStatusMixin {
    @Unique
    private boolean smb$handledEnd;

    @Inject(
            method = "tick",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/world/entity/raid/Raid;status:Lnet/minecraft/world/entity/raid/Raid$RaidStatus;",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER
            )
    )
    private void smb$onStatusChanged(CallbackInfo ci) {
        if (smb$handledEnd) return;

        Raid raid = (Raid) (Object) this;
        if (!(raid.getLevel() instanceof ServerLevel level)) return;

        String outcome;
        if (raid.isVictory()) {
            outcome = "victory";
        } else if (raid.isLoss()) {
            outcome = "loss";
        } else {
            return;
        }

        TriggerEntry entry = TriggerManager.getSpecial(TriggerType.RAID_STATUS, outcome);
        if (entry == null) return;

        Component message = entry.message() != null
                ? Component.Serializer.fromJson(entry.message(), level.registryAccess())
                : Component.translatable("souls_message_banners.message.raid_" + outcome);

        smb$handledEnd = true;

        for (ServerPlayer player : level.players()) {
            if (level.getRaidAt(player.blockPosition()) == raid) {
                MessageBannerAPI.send(player, entry, message, outcome);
            }
        }
    }
}