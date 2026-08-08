package net.tablesouls.souls_message_banners.event.triggers;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.data.TriggerEntry;
import net.tablesouls.souls_message_banners.data.TriggerManager;
import net.tablesouls.souls_message_banners.data.TriggerType;

@EventBusSubscriber(modid = SoulsMessageBanners.MODID)
public class PlayerJoinTrigger extends AbstractMessageTrigger {

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (!(player.level() instanceof ServerLevel serverLevel)) return;

        TriggerEntry entry = TriggerManager.getPlayerTrigger(TriggerType.PLAYER_SPAWNED, player);
        if (entry == null) return;

        String playerName = player.getGameProfile().getName();
        Component message = resolveMessage(entry, serverLevel.registryAccess(), "souls_message_banners.message.player_spawned", playerName);
        String identifier = player.getUUID().toString();

        triggerByProximity(entry, serverLevel, player, message, identifier);
    }
}