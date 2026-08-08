package net.tablesouls.souls_message_banners.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import net.tablesouls.souls_message_banners.compat.apotheosis.ApotheosisCompat;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public class TriggerManager {
    private static final List<TriggerEntry> ENTRIES = new ArrayList<>();

    public static void clear() {
        ENTRIES.clear();
    }

    public static void add(TriggerEntry entry) {
        ENTRIES.add(entry);
    }

    public static int size() {
        return ENTRIES.size();
    }

    public static void sort() {
        ENTRIES.sort((a, b) -> Integer.compare(b.priority(), a.priority()));
    }

    private static boolean canPlay(TriggerEntry entry) {
        if (!entry.enabled()) return true;

        BooleanSupplier gate = SoulsMessageBannersConfig.TRIGGERS.TYPE_SWITCHES.get(entry.type());
        return gate != null && !gate.getAsBoolean();
    }

    public static TriggerEntry getEntityTrigger(TriggerType type, LivingEntity entity) {
        EntityType<?> entityType = entity.getType();
        String idName = ForgeRegistries.ENTITY_TYPES.getKey(entityType).toString();

        for (TriggerEntry entry : ENTRIES) {
            if (entry.type() != type || canPlay(entry)) continue;

            boolean typeMatches = entry.matchesId(idName)
                    || entry.targetTags().stream().anyMatch(tag -> entityType.is(TagKey.create(Registries.ENTITY_TYPE, tag)));
            if (!typeMatches) continue;

            List<String> rarities = entry.apothRarity();
            if (rarities != null && !rarities.isEmpty()) {
                String rarity = ApotheosisCompat.getApothRarity(entity);
                if (rarity.isEmpty() || !rarities.contains(rarity)) continue;
            }

            return entry;
        }

        return null;
    }

    public static List<TriggerEntry> getBlockStateCandidates(BlockState state) {
        String idName = ForgeRegistries.BLOCKS.getKey(state.getBlock()).toString();
        List<TriggerEntry> matches = new ArrayList<>();

        for (TriggerEntry entry : ENTRIES) {
            if (entry.type() != TriggerType.BLOCK_STATE_CHANGED_ON_INTERACT || canPlay(entry)) continue;

            boolean blockMatches = entry.matchesId(idName)
                    || entry.targetTags().stream().anyMatch(tag -> state.is(TagKey.create(Registries.BLOCK, tag)));
            if (blockMatches) matches.add(entry);
        }

        return matches;
    }

    public static TriggerEntry getBlockTrigger(TriggerType type, BlockState state) {
        String idName = ForgeRegistries.BLOCKS.getKey(state.getBlock()).toString();

        for (TriggerEntry entry : ENTRIES) {
            if (entry.type() != type || canPlay(entry)) continue;

            boolean blockMatches = entry.matchesId(idName)
                    || entry.targetTags().stream().anyMatch(tag -> state.is(TagKey.create(Registries.BLOCK, tag)));
            if (blockMatches) return entry;
        }

        return null;
    }

    public static TriggerEntry getPlayerTrigger(TriggerType type, ServerPlayer player) {
        for (TriggerEntry entry : ENTRIES) {
            if (entry.type() != type || canPlay(entry)) continue;
            if (entry.matchesPlayer(player)) return entry;
        }
        return null;
    }

    public static TriggerEntry getSpecial(TriggerType type, String identifier) {
        for (TriggerEntry entry : ENTRIES) {
            if (entry.type() != type || canPlay(entry)) continue;
            if (entry.matchesId(identifier)) return entry;
        }
        return null;
    }
}