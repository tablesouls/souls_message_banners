package net.tablesouls.souls_message_banners.integration;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;

import java.util.List;

public class ApotheosisCompat {
    private static final String BOSS_KEY = "apoth.boss";
    private static final List<String> RARITY_KEYS = List.of(
            "apoth.boss.rarity",
            "apoth.rarity"
    );

    public static boolean isApothBoss(LivingEntity entity) {
        CompoundTag data = entity.getPersistentData();
        return data.contains(BOSS_KEY) && data.getBoolean(BOSS_KEY);
    }

    public static String getApothRarity(LivingEntity entity) {
        CompoundTag data = entity.getPersistentData();

        for (String key : RARITY_KEYS) {
            if (data.contains(key)) {
                return data.getString(key);
            }
        }
        return "";
    }
}
