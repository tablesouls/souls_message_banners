package net.tablesouls.souls_message_banners.integration.kubejs;

import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingRegistry;
import net.tablesouls.souls_message_banners.api.MessageBannerAPI;

public class SoulsMessageBannersKubeJSPlugin implements KubeJSPlugin {
    @Override
    public void registerBindings(BindingRegistry registry) {
        registry.add("MessageBannerAPI", MessageBannerAPI.class);
    }
}
