package net.tablesouls.souls_message_banners.api;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class BannerStyleManager {
    private static final Map<ResourceLocation, BannerStyle> STYLES = new HashMap<>();
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final ResourceLocation DEFAULT =
            new ResourceLocation(SoulsMessageBanners.MODID, "default");

    public static BannerStyle get(ResourceLocation id) {
        BannerStyle style = STYLES.get(id);
        if (style == null) {
            LOGGER.warn("No banner style registered for '{}' — known styles: {}", id, STYLES.keySet());
        }
        return style;
    }

    public static void register(ResourceLocation id, BannerStyle style) {
        STYLES.put(id, style);
    }

    public static void clear() {
        STYLES.clear();
    }
}

