package net.tablesouls.souls_message_banners.mixin.irons_rpg_tweaks;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.tablesouls.souls_message_banners.api.MessageBannerAPI;
import net.tablesouls.souls_message_banners.data.TriggerEntry;
import net.tablesouls.souls_message_banners.data.TriggerManager;
import net.tablesouls.souls_message_banners.data.TriggerType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "io.redspace.ironsrpgtweaks.xp_module.entity.XpCatalyst")
public abstract class XpCatalystMixin {
    private static final String XP_CATALYST_IDENTIFIER = "irons_rpg_tweaks:xp_catalyst";

    @Inject(
            method = "interact",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;giveExperiencePoints(I)V",
                    shift = At.Shift.AFTER
            )
    )
    private void souls_message_banners$onXpRetrieved(Player player, InteractionHand hand,
                                                     CallbackInfoReturnable<InteractionResult> cir) {
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        TriggerEntry entry = TriggerManager.getSpecial(TriggerType.XP_CATALYST_RETRIEVED, XP_CATALYST_IDENTIFIER);
        if (entry == null) return;

        Component message = entry.message() != null
                ? Component.Serializer.fromJson(entry.message(), player.registryAccess())
                : Component.translatable("souls_message_banners.message.experience_restored");

        MessageBannerAPI.send(serverPlayer, entry, message, XP_CATALYST_IDENTIFIER);
    }
}