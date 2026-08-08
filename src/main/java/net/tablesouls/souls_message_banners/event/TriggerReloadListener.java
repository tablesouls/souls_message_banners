package net.tablesouls.souls_message_banners.event;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.data.TriggerEntry;
import net.tablesouls.souls_message_banners.data.TriggerManager;

import java.util.Map;

public class TriggerReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();

    public TriggerReloadListener() {
        super(GSON, "smb_triggers");
    }

    @Override
    protected void apply(
            Map<ResourceLocation, JsonElement> objects,
            ResourceManager resourceManager,
            ProfilerFiller profiler
    ) {
        TriggerManager.clear();

        for (var entry : objects.entrySet()) {
            ResourceLocation id = entry.getKey();
            try {
                TriggerManager.add(TriggerEntry.fromJson(id, entry.getValue().getAsJsonObject()));
            } catch (Exception e) {
                SoulsMessageBanners.LOGGER.error("Failed to load trigger '{}': {}", id, e.getMessage());
            }
        }

        TriggerManager.sort();
        SoulsMessageBanners.LOGGER.info("Loaded {} triggers", TriggerManager.size());
    }
}