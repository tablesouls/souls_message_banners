package net.tablesouls.souls_message_banners.registry;

import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.tablesouls.souls_message_banners.SoulsMessageBanners;

public class ModFonts {
    public static final Style OPTIMUS_PRINCIPUS = Style.EMPTY.withFont(
            new ResourceLocation(SoulsMessageBanners.MODID, "optimus_principus")
    );
}
