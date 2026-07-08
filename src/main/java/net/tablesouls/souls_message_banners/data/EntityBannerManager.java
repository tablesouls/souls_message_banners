package net.tablesouls.souls_message_banners.data;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

public class EntityBannerManager {

    private static final List<EntityBannerEntry> ENTRIES = new ArrayList<>();

    public static void clear() {
        ENTRIES.clear();
    }

    public static void add(EntityBannerEntry entry) {
        ENTRIES.add(entry);
    }

    public static int size() {
        return ENTRIES.size();
    }

    public static EntityBannerEntry get(EntityType<?> type) {
        ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(type);

        for (EntityBannerEntry entry : ENTRIES) {
            if (entry.entity() != null && entry.entity().equals(id)) {
                return entry;
            }
        }

        for (EntityBannerEntry entry : ENTRIES) {
            if (entry.tag() != null && type.is(entry.tag())) {
                return entry;
            }
        }

        return null;
    }
}