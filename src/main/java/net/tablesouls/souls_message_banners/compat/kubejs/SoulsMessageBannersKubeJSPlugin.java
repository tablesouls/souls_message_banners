package net.tablesouls.souls_message_banners.compat.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.tablesouls.souls_message_banners.api.MessageBannerAPI;

public class SoulsMessageBannersKubeJSPlugin extends KubeJSPlugin {
    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("MessageBannerAPI", MessageBannerAPI.class);
    }
}
