package net.tablesouls.souls_message_banners.data;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.EntityType;
import net.tablesouls.souls_message_banners.assets.BannerStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public record EntityBannerEntry(
        Pattern entity,
        TagKey<EntityType<?>> tag,
        List<String> apothRarity,
        ResourceLocation style,
        JsonElement message,
        int priority,
        boolean killer,
        boolean dimension,
        int radius
) {
    public static List<EntityBannerEntry> fromJson(JsonObject json) {
        List<EntityBannerEntry> entries = new ArrayList<>();
        boolean enabled = GsonHelper.getAsBoolean(json, "enabled", true);
        if (!enabled) return entries;

        JsonArray targets = json.has("targets")
                ? GsonHelper.getAsJsonArray(json, "targets")
                : null;
        List<String> apothRarities = null;
        ResourceLocation style = ResourceLocation.parse(GsonHelper.getAsString(json, "style"));
        JsonElement message = json.has("message")
                ? json.get("message")
                : null;
        int priority = GsonHelper.getAsInt(json, "priority", 0);
        boolean killer = GsonHelper.getAsBoolean(json, "killer", false);
        boolean dimension = GsonHelper.getAsBoolean(json, "dimension", false);
        int radius = GsonHelper.getAsInt(json, "radius", 32);

        if (json.has("apotheosis_rarities")) {
            JsonArray rarities = GsonHelper.getAsJsonArray(json, "apotheosis_rarities");
            apothRarities = new ArrayList<>();
            for (JsonElement rarityElement : rarities) {
                apothRarities.add(rarityElement.getAsString());
            }
        }

        for (JsonElement targetElement : targets) {
            entries.add(fromTarget(targetElement.getAsString(), apothRarities, style, message, priority, killer, dimension, radius));
        }

        return entries;
    }

    private static EntityBannerEntry fromTarget(
            String target,
            List<String> apothRarities,
            ResourceLocation style,
            JsonElement message,
            int priority,
            boolean killer,
            boolean dimension,
            int radius
    ){
        Pattern entity = null;
        TagKey<EntityType<?>> tag = null;

        if (target.startsWith("#")) {
            tag = TagKey.create(Registries.ENTITY_TYPE, ResourceLocation.parse(target.substring(1)));
        } else {
            entity = Pattern.compile(target);
        }

        return new EntityBannerEntry(entity, tag, apothRarities, style, message, priority, killer, dimension, radius);
    }
}