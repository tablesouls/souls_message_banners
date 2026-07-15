package net.tablesouls.souls_message_banners.event;

import com.google.gson.JsonObject;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.api.MessageBannerAPI;
import net.tablesouls.souls_message_banners.assets.BannerStyle;
import net.tablesouls.souls_message_banners.assets.BannerStyleManager;
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
        Entity killCredit = entity.getKillCredit();
        String entityDisplayName = entity.getDisplayName().getString().toUpperCase();
        Level level = entity.level();

        if (!(level instanceof ServerLevel serverLevel)) return;

        EntityBannerEntry entry = EntityBannerManager.get(entity);

        Component message = Component.translatable(
                "souls_message_banners.message.entity_felled",
                entityDisplayName
        );

        if (entry == null) return;
        Component entryMessage = Component.empty();
        ResourceLocation entryStyle = entry.style();

        if (entry.message() != null) {
            entryMessage = Component.Serializer.fromJson(
                    entry.message()
            );
        }

        if (!entryMessage.equals(Component.empty())) {
            if (entryMessage.getContents() instanceof TranslatableContents contents) {
                message = Component.translatable(
                        contents.getKey(),
                        entityDisplayName
                );
            } else {
                message = entryMessage;
            }
        }

        if (entry.killer()) {
            if (killCredit instanceof ServerPlayer player) {
                MessageBannerAPI.send(player, message, entryStyle);
            }
        } else if (entry.dimension()) {
            for (ServerPlayer player : serverLevel.players()) {
                MessageBannerAPI.send(player, message, entryStyle);
            }
        } else {
            for (Player player : EntityProximityHelper.getPlayersNearby(entity, entry.radius())) {
                MessageBannerAPI.send(player, message, entryStyle);
            }
        }
    };
}