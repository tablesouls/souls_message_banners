package net.tablesouls.souls_message_banners.compat.bonfires;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.client.event.sound.PlaySoundEvent;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;
import wehavecookies56.bonfires.client.gui.CreateBonfireScreen;

public class BonfiresCompat {
    public static final String MODID = "bonfires";
    public static final boolean LOADED = ModList.get().isLoaded(MODID);
    public static final String BONFIRE_LIT_IDENTIFIER = "bonfires:bonfire";

    private static volatile long suppressChallengeToastUntil = 0L;

    public static void markBonfireLitBannerShown() {
        suppressChallengeToastUntil = System.currentTimeMillis() + 500L;
    }

    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent event) {
        if(!SoulsMessageBannersConfig.TRIGGERS.BONFIRE_LIT.get()) return;
        if (event.getSound() == null) return;

        boolean isToastSound = event.getSound().getLocation().equals(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE.getLocation());
        if (!isToastSound) return;

        boolean isBonfireCreateScreen = Minecraft.getInstance().screen instanceof CreateBonfireScreen;
        boolean isBonfireLitBanner = System.currentTimeMillis() < suppressChallengeToastUntil;

        if (isBonfireCreateScreen || isBonfireLitBanner) {
            event.setSound(null);
        }
    }
}