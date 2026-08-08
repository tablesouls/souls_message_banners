package net.tablesouls.souls_message_banners.data;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public record TriggerEntry(
        ResourceLocation id,
        TriggerType type,
        List<Pattern> targetPatterns,
        List<ResourceLocation> targetTags,
        List<String> targetPlayers,
        ResourceLocation style,
        JsonElement message,
        int priority,
        boolean enabled,

        // entity/player-only
        List<String> apothRarity,
        boolean killer,
        DisplayMode displayMode,
        int radius,

        // block_state-only
        Map<String, Boolean> oldBlockState,
        Map<String, Boolean> newBlockState
) {
    public static TriggerEntry fromJson(ResourceLocation id, JsonObject json) {
        TriggerType type = TriggerType.fromId(GsonHelper.getAsString(json, "type"));
        boolean enabled = GsonHelper.getAsBoolean(json, "enabled", true);

        List<Pattern> patterns = new ArrayList<>();
        List<ResourceLocation> tags = new ArrayList<>();
        List<String> players = new ArrayList<>();

        switch (type) {
            case ENTITY_DIED, ENTITY_SPAWNED ->
                    parseIdTargets(id, json, "target_entities", type, patterns, tags);
            case BLOCK_STATE_CHANGED_ON_INTERACT, WAYSTONE_ACTIVATION ->
                    parseIdTargets(id, json, "target_blocks", type, patterns, tags);
            case PLAYER_SPAWNED, PLAYED_DIED, PLAYER_SPAWN_SET ->
                    parsePlayerTargets(json, players);
            default -> {
                if (json.has("targets")) {
                    for (JsonElement targetElement : GsonHelper.getAsJsonArray(json, "targets")) {
                        patterns.add(Pattern.compile(targetElement.getAsString()));
                    }
                }
            }
        }

        ResourceLocation style = ResourceLocation.parse(GsonHelper.getAsString(json, "style"));
        JsonElement message = json.has("message") ? json.get("message") : null;
        int priority = GsonHelper.getAsInt(json, "priority", 0);

        JsonObject conditions = json.has("conditions") ? GsonHelper.getAsJsonObject(json, "conditions") : new JsonObject();

        List<String> apothRarities = null;
        if (conditions.has("apotheosis_rarities")) {
            apothRarities = new ArrayList<>();
            for (JsonElement rarityElement : GsonHelper.getAsJsonArray(conditions, "apotheosis_rarities")) {
                apothRarities.add(rarityElement.getAsString());
            }
        }
        boolean killer = GsonHelper.getAsBoolean(conditions, "killer", false);
        DisplayMode displayMode = json.has("display_mode")
                ? DisplayMode.fromId(GsonHelper.getAsString(json, "display_mode"))
                : DisplayMode.RADIUS;
        int radius = GsonHelper.getAsInt(conditions, "radius", 64);

        Map<String, Boolean> oldBlockState = parseBlockState(conditions, "old_blockstate");
        Map<String, Boolean> newBlockState = parseBlockState(conditions, "new_blockstate");

        if (type == TriggerType.BLOCK_STATE_CHANGED_ON_INTERACT && newBlockState.isEmpty()) {
            throw new IllegalArgumentException(
                    "trigger \"" + id + "\" has type=block_state but no (or an empty) \"conditions.new_blockstate\" field");
        }

        return new TriggerEntry(id, type, patterns, tags, players, style, message, priority, enabled,
                apothRarities, killer, displayMode, radius, oldBlockState, newBlockState);
    }

    private static void parseIdTargets(ResourceLocation id, JsonObject json, String key, TriggerType type,
                                       List<Pattern> patterns, List<ResourceLocation> tags) {
        if (!json.has(key)) {
            throw new IllegalArgumentException(
                    "trigger \"" + id + "\" (type=" + type.id() + ") requires a \"" + key + "\" field");
        }
        for (JsonElement targetElement : GsonHelper.getAsJsonArray(json, key)) {
            String target = targetElement.getAsString();
            if (target.startsWith("#")) {
                tags.add(ResourceLocation.parse(target.substring(1)));
            } else {
                patterns.add(Pattern.compile(target));
            }
        }
    }

    private static void parsePlayerTargets(JsonObject json, List<String> players) {
        // Optional: empty/omitted "target_players" means "any player".
        if (!json.has("target_players")) return;
        for (JsonElement playerElement : GsonHelper.getAsJsonArray(json, "target_players")) {
            players.add(playerElement.getAsString());
        }
    }

    private static Map<String, Boolean> parseBlockState(JsonObject json, String key) {
        Map<String, Boolean> result = new LinkedHashMap<>();
        if (!json.has(key)) return result;

        JsonObject stateObject = GsonHelper.getAsJsonObject(json, key);
        for (Map.Entry<String, JsonElement> propertyEntry : stateObject.entrySet()) {
            result.put(propertyEntry.getKey(), propertyEntry.getValue().getAsBoolean());
        }
        return result;
    }

    public boolean matchesId(String identifier) {
        for (Pattern pattern : targetPatterns) {
            if (pattern.matcher(identifier).matches()) return true;
        }
        return false;
    }

    public boolean matchesPlayer(ServerPlayer player) {
        if (targetPlayers.isEmpty()) return true;

        String uuid = player.getUUID().toString();
        String name = player.getGameProfile().getName();
        for (String target : targetPlayers) {
            if (target.equalsIgnoreCase(uuid) || target.equalsIgnoreCase(name)) return true;
        }
        return false;
    }
}