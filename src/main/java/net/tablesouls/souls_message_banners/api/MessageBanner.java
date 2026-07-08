package net.tablesouls.souls_message_banners.api;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MessageBanner {
    private final Component message;
    private final ResourceLocation styleId;

    public MessageBanner(Component message, ResourceLocation styleId) {
        this.message = message;
        this.styleId = styleId;
    }

    public Component message() {
        return message;
    }

    public ResourceLocation styleId() {
        return styleId;
    }
}
