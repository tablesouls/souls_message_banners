package net.tablesouls.souls_message_banners.listeners;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.tablesouls.souls_message_banners.assets.BannerStyle;
import net.tablesouls.souls_message_banners.assets.BannerStyleManager;
import org.slf4j.Logger;

import java.util.Map;

public class BannerStyleReloadListener extends SimpleJsonResourceReloadListener {
    private static final Gson GSON = new Gson();
    private static final Logger LOGGER = LogUtils.getLogger();

    public BannerStyleReloadListener() {
        super(GSON, "smb_styles");
    }

    @Override
    protected void apply(
            Map<ResourceLocation, JsonElement> objects,
            ResourceManager manager,
            ProfilerFiller profiler
    ) {
        BannerStyleManager.clear();

        int loaded = 0;
        for (var entry : objects.entrySet()) {
            ResourceLocation id = entry.getKey();
            try {
                BannerStyle style = BannerStyle.fromJson(entry.getValue().getAsJsonObject());
                BannerStyleManager.register(id, style);
                loaded++;
            } catch (Exception e) {
                LOGGER.error("Failed to load banner style '{}': {}", id, e.getMessage());
            }
        }
        LOGGER.info("Loaded {} banner style(s)", loaded);

        if (BannerStyleManager.get(BannerStyleManager.DEFAULT) == null) {
            LOGGER.warn("No '{}' banner style found", BannerStyleManager.DEFAULT);
        }
    }
}