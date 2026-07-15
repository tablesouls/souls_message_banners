package net.tablesouls.souls_message_banners.data;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;
import net.tablesouls.souls_message_banners.integration.ApotheosisCompat;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class EntityBannerManager {
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final List<EntityBannerEntry> ENTRIES = new ArrayList<>();

    public static void clear() {
        ENTRIES.clear();
    }

    public static void add(EntityBannerEntry entry) {
        ENTRIES.add(entry);
    }

    public static void addAll(List<EntityBannerEntry> entries) {
        ENTRIES.addAll(entries);
    }

    public static int size() {
        return ENTRIES.size();
    }

    public static void sort() {
        ENTRIES.sort((a, b) -> Integer.compare(b.priority(), a.priority()));
    }

    public static EntityBannerEntry get(LivingEntity entity) {
        EntityType<?> type = entity.getType();
        ResourceLocation id = ForgeRegistries.ENTITY_TYPES.getKey(type);
        String idName = id.toString();

        for (EntityBannerEntry entry : ENTRIES) {
            boolean typeMatches = (entry.entity() != null && entry.entity().matcher(idName).matches())
                || (entry.tag() != null && type.is(entry.tag()));
            if (!typeMatches) continue;

            List<String> rarities = entry.apothRarity();
            if (rarities != null && !rarities.isEmpty()) {
                String rarity = ApotheosisCompat.getApothRarity(entity);
                if (rarity.isEmpty() || !rarities.contains(rarity)) {
                    continue;
                }
            }

            //LOGGER.info("Entity banner match: entity={}, chosen style={}, priority={}", idName, entry.style(), entry.priority());
            return entry;
        }

        return null;
    }
}