package net.tablesouls.souls_message_banners.data;

import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

import java.awt.*;

public record EntityBannerEntry(
        ResourceLocation entity,
        TagKey<EntityType<?>> tag,
        ResourceLocation style,
        JsonElement message,
        int priority,
        boolean killer,
        boolean dimension,
        int radius
) {}