package net.tablesouls.souls_message_banners.listeners;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntityType;
import net.tablesouls.souls_message_banners.data.EntityBannerEntry;
import net.tablesouls.souls_message_banners.data.EntityBannerManager;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EntityBannerReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();
    private static final Logger LOGGER = LogUtils.getLogger();

    public EntityBannerReloadListener() {
        super(GSON, "smb_entities");
    }

    @Override
    protected void apply(
            Map<ResourceLocation, JsonElement> objects,
            ResourceManager resourceManager,
            ProfilerFiller profiler
    ) {
        EntityBannerManager.clear();

        for (var entry : objects.entrySet()) {
            ResourceLocation id = entry.getKey();
            try {
                EntityBannerManager.addAll(EntityBannerEntry.fromJson(entry.getValue().getAsJsonObject()));
            } catch (Exception e) {
                LOGGER.error("Failed to load an entity banner entry: {}", e.getMessage());
            }
        }

        EntityBannerManager.sort();
        LOGGER.info("Loaded {} entity banner entries", EntityBannerManager.size());
    }
}