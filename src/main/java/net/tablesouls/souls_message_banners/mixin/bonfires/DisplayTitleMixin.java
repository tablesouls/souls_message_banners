package net.tablesouls.souls_message_banners.mixin.bonfires;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.api.BannerStyle;
import net.tablesouls.souls_message_banners.api.BannerStyleManager;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;
import net.tablesouls.souls_message_banners.util.MessageBannerHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wehavecookies56.bonfires.packets.client.DisplayTitle;

@Mixin(DisplayTitle.class)
public class DisplayTitleMixin {
    @Inject(method = "handle", at = @At("HEAD"), cancellable = true)
    private void souls_message_banners$replaceWithBanner(NetworkEvent.Context context, CallbackInfo ci) {
        if (!SoulsMessageBannersConfig.BONFIRE_LIT.get()) return;
        ci.cancel();

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () ->() -> {
            ResourceLocation styleId = new ResourceLocation(SoulsMessageBanners.MODID, "bonfire_lit");
            BannerStyle style = BannerStyleManager.get(styleId);

            MessageBannerHelper.show(Component.translatable("souls_message_banners.message.bonfire_lit"),style);
        });
    }
}
