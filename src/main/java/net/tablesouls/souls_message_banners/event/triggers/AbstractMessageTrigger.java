package net.tablesouls.souls_message_banners.event.triggers;

import com.google.gson.JsonElement;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistries;
import net.tablesouls.souls_message_banners.api.MessageBannerAPI;
import net.tablesouls.souls_message_banners.compat.apotheosis.ApotheosisCompat;
import net.tablesouls.souls_message_banners.data.TriggerEntry;
import net.tablesouls.souls_message_banners.util.EntityProximityHelper;

import java.util.List;

public abstract class AbstractMessageTrigger {

    protected static Component resolveMessage(TriggerEntry entry, String defaultKey, Object... args) {
        JsonElement rawMessage = entry.message();
        if (rawMessage == null) {
            return Component.translatable(defaultKey, args);
        }

        Component custom = Component.Serializer.fromJson(rawMessage);
        if (args.length > 0 && custom.getContents() instanceof TranslatableContents contents) {
            return Component.translatable(contents.getKey(), args);
        }
        return custom;
    }

    protected static String resolveEntityIdentifier(TriggerEntry entry, LivingEntity entity) {
        String entityId = ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString();

        List<String> apothRarity = entry.apothRarity();
        if (apothRarity != null && !apothRarity.isEmpty()) {
            String rarity = ApotheosisCompat.getApothRarity(entity);
            if (!rarity.isEmpty()) {
                entityId = entityId + "@" + rarity;
            }
        }
        return entityId;
    }

    protected static void triggerByProximity(TriggerEntry entry, ServerLevel level, LivingEntity source,
                                             Component message, String identifier) {
        switch (entry.displayMode()) {
            case ALL_PLAYERS -> {
                for (ServerPlayer player : level.getServer().getPlayerList().getPlayers()) {
                    MessageBannerAPI.send(player, entry, message, identifier);
                }
            }
            case DIMENSION -> {
                for (ServerPlayer player : level.players()) {
                    MessageBannerAPI.send(player, entry, message, identifier);
                }
            }
            case RADIUS -> {
                for (Player player : EntityProximityHelper.getPlayersNearby(source, entry.radius())) {
                    MessageBannerAPI.send(player, entry, message, identifier);
                }
            }
        }
    }

    protected static void triggerByKiller(TriggerEntry entry, ServerLevel level, LivingEntity source,
                                          Player killCredit, Component message, String identifier) {
        if (entry.killer()) {
            if (killCredit instanceof ServerPlayer killer) {
                MessageBannerAPI.send(killer, entry, message, identifier);
            }
        } else {
            triggerByProximity(entry, level, source, message, identifier);
        }
    }
}