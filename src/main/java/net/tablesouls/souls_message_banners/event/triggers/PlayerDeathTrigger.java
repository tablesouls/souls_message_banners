package net.tablesouls.souls_message_banners.event.triggers;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.data.TriggerEntry;
import net.tablesouls.souls_message_banners.data.TriggerManager;
import net.tablesouls.souls_message_banners.data.TriggerType;

@Mod.EventBusSubscriber(modid = SoulsMessageBanners.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerDeathTrigger extends AbstractMessageTrigger {

    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void onPlayerDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof ServerPlayer victim)) return;

        Level level = entity.level();
        if (!(level instanceof ServerLevel serverLevel)) return;

        TriggerEntry entry = TriggerManager.getPlayerTrigger(TriggerType.PLAYED_DIED, victim);
        if (entry == null) return;

        String playerName = victim.getGameProfile().getName();
        Component message = resolveMessage(entry, "souls_message_banners.message.player_felled", playerName);
        String identifier = victim.getUUID().toString();

        Entity killCredit = entity.getKillCredit();
        Player killer = killCredit instanceof Player player ? player : null;
        triggerByKiller(entry, serverLevel, victim, killer, message, identifier);
    }
}