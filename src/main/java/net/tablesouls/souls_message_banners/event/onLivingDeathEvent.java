package net.tablesouls.souls_message_banners.event;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.api.MessageBannerAPI;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;
import net.tablesouls.souls_message_banners.data.EntityBannerEntry;
import net.tablesouls.souls_message_banners.data.EntityBannerManager;
import net.tablesouls.souls_message_banners.util.EntityProximityHelper;

@Mod.EventBusSubscriber(modid = SoulsMessageBanners.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class onLivingDeathEvent {

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!SoulsMessageBannersConfig.ENTITY_FELLLED.get()) return;

        LivingEntity entity = event.getEntity();
        Level level = entity.level();

        if (!(level instanceof ServerLevel serverLevel)) return;
        EntityBannerEntry entry = EntityBannerManager.get(entity.getType());
        if (entry == null) return;

        Component message = Component.translatable(
                "souls_message_banners.message.entity_felled",
                entity.getDisplayName().getString().toUpperCase()
        );

        if (entry.dimension()) {
            for (ServerPlayer player : serverLevel.players()) {
                MessageBannerAPI.send(player, message, entry.style());
            }
        } else {
            for (Player player : EntityProximityHelper.getPlayersNearby(entity, entry.radius())) {
                MessageBannerAPI.send(player, message, entry.style());
            }
        }
    };
}