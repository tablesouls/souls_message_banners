package net.tablesouls.souls_message_banners.integration;

import com.leclowndu93150.twemoji.client.registry.EmojiRegistry;
import net.minecraft.network.chat.Component;

public class TwemojiCompat {
    public static Component rewrite(Component message) {
        String shaped = EmojiRegistry.INSTANCE.applyShortcodes(message.getString(), 0);
        return Component.literal(shaped).withStyle(message.getStyle());
    }
}
