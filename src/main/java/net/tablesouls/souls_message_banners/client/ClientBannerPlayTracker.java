package net.tablesouls.souls_message_banners.client;

import net.minecraft.resources.ResourceLocation;
import net.tablesouls.souls_message_banners.config.BannerPlayMode;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;

import java.util.HashSet;
import java.util.Set;

public class ClientBannerPlayTracker {
    private static final Set<String> SHOWN = new HashSet<>();

    public static boolean shouldPlay(ResourceLocation styleId, String identifier, ResourceLocation triggerId) {
        BannerPlayMode mode = SoulsMessageBannersConfig.BANNER_PLAY_MODE.get();
        if (mode == BannerPlayMode.NONE) return false;

        if (isBlacklisted(triggerId)) return false;

        if (mode == BannerPlayMode.EVERYTIME) return true;

        if (identifier == null) return true;

        String key = styleId + "|" + identifier;
        return SHOWN.add(key);
    }

    private static boolean isBlacklisted(ResourceLocation triggerId) {
        if (triggerId == null) return false;
        String id = triggerId.toString();
        return SoulsMessageBannersConfig.TRIGGER_BLACKLIST.get().stream().anyMatch(id::equals);
    }

    public static void clear() {
        SHOWN.clear();
    }
}
