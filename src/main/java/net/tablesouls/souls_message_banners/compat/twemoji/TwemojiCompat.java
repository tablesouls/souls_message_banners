package net.tablesouls.souls_message_banners.compat.twemoji;

import com.leclowndu93150.twemoji.client.registry.EmojiRegistry;
import net.minecraft.network.chat.Component;
import net.neoforged.fml.ModList;

public class TwemojiCompat {
    public static final String MODID = "twemoji";
    public static final boolean LOADED = ModList.get().isLoaded(MODID);

    public static Component rewrite(Component message) {
        String shaped = EmojiRegistry.INSTANCE.applyShortcodes(message.getString(), 0);
        return Component.literal(shaped).withStyle(message.getStyle());
    }
}