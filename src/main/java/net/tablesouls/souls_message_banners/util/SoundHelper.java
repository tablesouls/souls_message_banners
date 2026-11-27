package net.tablesouls.souls_message_banners.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;

public class SoundHelper {
    public static void playUiSound(SoundEvent sound) {
        playUiSound(sound, 1.0F, 1.0F);
    }

    public static void playUiSound(SoundEvent sound, float volume, float pitch) {
        float master = SoulsMessageBannersConfig.APPEARANCE.VOLUME.get().floatValue();
        if (master <= 0.0F) return;
        Minecraft.getInstance().getSoundManager()
                .play(SimpleSoundInstance.forUI(sound, pitch, volume * master));
    }
}