package net.tablesouls.souls_message_banners.mixin;

import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.raid.Raid;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.api.MessageBannerAPI;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;
import org.objectweb.asm.Opcodes;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Raid.class)
public class RaidStatusMixin {
    private static final Logger LOGGER = LogUtils.getLogger();

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
        if (!SoulsMessageBannersConfig.RAID_STATUS.get()) return;

        Raid raid = (Raid)(Object)this;
        if (!(raid.getLevel() instanceof ServerLevel level)) return;

        ResourceLocation style;
        Component message;

        if (raid.isVictory()) {
            style = ResourceLocation.fromNamespaceAndPath(SoulsMessageBanners.MODID, "raid_victory");
            message = Component.translatable("souls_message_banners.message.raid_victory");
        } else if (raid.isLoss()) {
            style = ResourceLocation.fromNamespaceAndPath(SoulsMessageBanners.MODID, "raid_loss");
            message = Component.translatable("souls_message_banners.message.raid_loss");
        } else {
            return;
        }

        smb$handledEnd = true;

        for (ServerPlayer player : level.players()) {
            if (level.getRaidAt(player.blockPosition()) == raid) {
                MessageBannerAPI.send(player, message, style);
            }
        }
    }
}

