package net.tablesouls.souls_message_banners.event.triggers;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.data.TriggerEntry;
import net.tablesouls.souls_message_banners.data.TriggerManager;
import net.tablesouls.souls_message_banners.data.TriggerType;

@Mod.EventBusSubscriber(modid = SoulsMessageBanners.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntitySpawnTrigger extends AbstractMessageTrigger {

    @SubscribeEvent
    public static void onEntitySpawn(EntityJoinLevelEvent event) {
        if (event.loadedFromDisk()) return;
        if (!(event.getEntity() instanceof LivingEntity entity) || entity instanceof Player) return;

        Level level = entity.level();
        if (!(level instanceof ServerLevel serverLevel)) return;

        TriggerEntry entry = TriggerManager.getEntityTrigger(TriggerType.ENTITY_SPAWNED, entity);
        if (entry == null) return;

        String entityDisplayName = entity.getDisplayName().getString().toUpperCase();
        Component message = resolveMessage(entry, "souls_message_banners.message.entity_spawned", entityDisplayName);
        String entityId = resolveEntityIdentifier(entry, entity);

        triggerByProximity(entry, serverLevel, entity, message, entityId);
    }
}