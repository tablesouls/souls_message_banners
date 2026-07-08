package net.tablesouls.souls_message_banners.listeners;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.Registries;
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

        for (JsonElement element : objects.values()) {

            JsonObject json = element.getAsJsonObject();

            ResourceLocation style = new ResourceLocation(
                    GsonHelper.getAsString(json, "style")
            );

            int radius = GsonHelper.getAsInt(json, "radius", 32);
            boolean dimension = GsonHelper.getAsBoolean(json, "dimension", false);

            JsonArray targets = GsonHelper.getAsJsonArray(json, "targets");

            for (JsonElement targetElement : targets) {

                String target = targetElement.getAsString();

                if (target.startsWith("#")) {
                    TagKey<EntityType<?>> tag = TagKey.create(
                            Registries.ENTITY_TYPE,
                            new ResourceLocation(target.substring(1))
                    );

                    EntityBannerManager.add(new EntityBannerEntry(
                            null,
                            tag,
                            style,
                            dimension,
                            radius
                    ));

                } else {

                    EntityBannerManager.add(new EntityBannerEntry(
                            new ResourceLocation(target),
                            null,
                            style,
                            dimension,
                            radius
                    ));
                }
            }
        }

        LOGGER.info("Loaded {} entity banner entries", EntityBannerManager.size());
    }
}