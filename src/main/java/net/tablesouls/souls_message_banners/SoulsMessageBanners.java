package net.tablesouls.souls_message_banners;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.tablesouls.souls_message_banners.client.MessageBannerRenderer;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;
import net.tablesouls.souls_message_banners.integration.BonfiresCompat;
import net.tablesouls.souls_message_banners.integration.WaystonesCompat;
import net.tablesouls.souls_message_banners.network.NetworkHandler;
import net.tablesouls.souls_message_banners.sound.ModSounds;

@Mod(SoulsMessageBanners.MODID)
public class SoulsMessageBanners
{
    public static final String MODID = "souls_message_banners";

    public SoulsMessageBanners(FMLJavaModLoadingContext context) {
        NetworkHandler.register();
        ModSounds.SOUND_EVENTS.register(context.getModEventBus());
        context.registerConfig(ModConfig.Type.COMMON, SoulsMessageBannersConfig.COMMON_SPEC);
        context.registerConfig(ModConfig.Type.CLIENT, SoulsMessageBannersConfig.CLIENT_SPEC);

        if (ModList.get().isLoaded("bonfires")) {
            MinecraftForge.EVENT_BUS.register(BonfiresCompat.class);
        }
        if (ModList.get().isLoaded("waystones")) {
            MinecraftForge.EVENT_BUS.register(WaystonesCompat.class);
        }
    }

    @Mod.EventBusSubscriber(value = Dist.CLIENT, modid = SoulsMessageBanners.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public class ClientSetup {
        @SubscribeEvent
        public static void onRegisterOverlays(RegisterGuiOverlaysEvent event) {
            event.registerAboveAll(SoulsMessageBanners.MODID, new MessageBannerRenderer());
        }
    }
}
