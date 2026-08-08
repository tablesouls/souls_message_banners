package net.tablesouls.souls_message_banners.event.triggers;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.data.TriggerEntry;
import net.tablesouls.souls_message_banners.data.TriggerManager;
import net.tablesouls.souls_message_banners.data.TriggerType;

@Mod.EventBusSubscriber(modid = SoulsMessageBanners.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingDeathTrigger extends AbstractMessageTrigger {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player) return;

        Level level = entity.level();
        if (!(level instanceof ServerLevel serverLevel)) return;

        TriggerEntry entry = TriggerManager.getEntityTrigger(TriggerType.ENTITY_DIED, entity);
        if (entry == null) return;

        String entityDisplayName = entity.getDisplayName().getString().toUpperCase();
        Component message = resolveMessage(entry, "souls_message_banners.message.entity_felled", entityDisplayName);
        String entityId = resolveEntityIdentifier(entry, entity);

        Entity killCredit = entity.getKillCredit();
        Player killer = killCredit instanceof Player player ? player : null;
        triggerByKiller(entry, serverLevel, entity, killer, message, entityId);
    }
}