package net.tablesouls.souls_message_banners.integration;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.client.event.sound.PlaySoundEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;
import wehavecookies56.bonfires.client.gui.CreateBonfireScreen;

public class BonfiresCompat {
    @SubscribeEvent
    public static void onPlaySound(PlaySoundEvent event) {
        if(!SoulsMessageBannersConfig.BONFIRE_LIT.get()) return;
        if (event.getSound() == null) return;

        boolean isToastSound = event.getSound().getLocation().equals(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE.getLocation());
        boolean isBonfireCreateScreen = Minecraft.getInstance().screen instanceof CreateBonfireScreen;

        if (isToastSound && isBonfireCreateScreen) {
            event.setSound(null);
        }
    }
}