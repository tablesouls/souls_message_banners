package net.tablesouls.souls_message_banners.event.triggers;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerSetSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.api.MessageBannerAPI;
import net.tablesouls.souls_message_banners.data.TriggerEntry;
import net.tablesouls.souls_message_banners.data.TriggerManager;
import net.tablesouls.souls_message_banners.data.TriggerType;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = SoulsMessageBanners.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerSetSpawnTrigger extends AbstractMessageTrigger {

    public static boolean SUPPRESS_MESSAGE = false;

    @SubscribeEvent
    public static void onSetSpawn(PlayerSetSpawnEvent event) {
        if (SUPPRESS_MESSAGE) return;

        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        boolean flag = java.util.Objects.equals(player.getRespawnPosition(), event.getNewSpawn())
                && player.getRespawnDimension().equals(event.getSpawnLevel());
        if (flag) return;

        TriggerEntry entry = TriggerManager.getPlayerTrigger(TriggerType.PLAYER_SPAWN_SET, player);
        if (entry == null) return;

        Component message = resolveMessage(entry, "souls_message_banners.message.spawn_point_set");
        MessageBannerAPI.send(player, entry, message, player.getUUID().toString());
    }
}