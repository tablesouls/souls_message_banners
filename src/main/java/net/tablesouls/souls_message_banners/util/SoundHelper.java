package net.tablesouls.souls_message_banners.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvent;

public class SoundHelper {
    public static void playUiSound(SoundEvent sound, float volume, float pitch) {
        if (sound == null) return;
        Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(sound, pitch, volume));
    }

    public static void playUiSound(SoundEvent sound) {
        playUiSound(sound, 1.0f, 1.0f);
    }
}