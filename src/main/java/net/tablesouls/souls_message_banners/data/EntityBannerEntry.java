package net.tablesouls.souls_message_banners.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public record EntityBannerEntry(
        ResourceLocation entity,
        TagKey<EntityType<?>> tag,
        ResourceLocation style,
        boolean dimension,
        int radius
) {}