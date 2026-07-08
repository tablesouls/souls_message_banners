package net.tablesouls.souls_message_banners.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;
import net.tablesouls.souls_message_banners.assets.BannerStyle;

public class ModRegistries {

    public static final DeferredRegister<BannerStyle> BANNER_STYLES =
            DeferredRegister.create(
                    new ResourceLocation(SoulsMessageBanners.MODID, "banner_styles"),
                    SoulsMessageBanners.MODID);
}
