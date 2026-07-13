package net.tablesouls.souls_message_banners.data;

import com.google.gson.JsonElement;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

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