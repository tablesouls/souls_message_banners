package net.tablesouls.souls_message_banners;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;
import net.tablesouls.souls_message_banners.compat.bonfires.BonfiresCompat;
import net.tablesouls.souls_message_banners.compat.waystones.WaystonesCompat;
import net.tablesouls.souls_message_banners.network.NetworkHandler;
import net.tablesouls.souls_message_banners.sound.ModSounds;
import org.slf4j.Logger;

@Mod(SoulsMessageBanners.MODID)
public class SoulsMessageBanners
{
    public static final String MODID = "souls_message_banners";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SoulsMessageBanners(FMLJavaModLoadingContext context) {
        NetworkHandler.register();
        ModSounds.SOUND_EVENTS.register(context.getModEventBus());
        context.registerConfig(ModConfig.Type.COMMON, SoulsMessageBannersConfig.COMMON_SPEC);
        context.registerConfig(ModConfig.Type.CLIENT, SoulsMessageBannersConfig.CLIENT_SPEC);

        if (ModList.get().isLoaded("bonfires")) {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
                    MinecraftForge.EVENT_BUS.register(BonfiresCompat.class));
        }
        if (ModList.get().isLoaded("waystones")) {
            MinecraftForge.EVENT_BUS.register(WaystonesCompat.class);
        }
    }
}
