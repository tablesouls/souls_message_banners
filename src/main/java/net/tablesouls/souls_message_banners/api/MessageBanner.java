package net.tablesouls.souls_message_banners.api;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class MessageBanner {
    private final Component message;
    private final ResourceLocation styleId;
    private final String identifier;
    private final ResourceLocation triggerId;

    public MessageBanner(Component message, ResourceLocation styleId, String identifier, ResourceLocation triggerId) {
        this.message = message;
        this.styleId = styleId;
        this.identifier = identifier;
        this.triggerId = triggerId;
    }

    public MessageBanner(Component message, ResourceLocation styleId, String identifier) {
        this(message, styleId, identifier, null);
    }

    public MessageBanner(Component message, ResourceLocation styleId) {
        this(message, styleId, null, null);
    }

    public Component message() {
        return message;
    }

    public ResourceLocation styleId() {
        return styleId;
    }

    public String identifier() {
        return identifier;
    }

    public ResourceLocation triggerId() {
        return triggerId;
    }
}
