package net.tablesouls.souls_message_banners.assets;

import com.google.gson.JsonObject;
import com.mojang.logging.LogUtils;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.GsonHelper;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;
import org.slf4j.Logger;

public record BannerStyle(
        boolean enabled,
        ResourceLocation font,
        SoundEvent sound,
        int yOffset,
        float bannerOpacity,
        float textOpacity,
        boolean textAutoScale,
        float textScale,
        float textOffsetX,
        float textOffsetY,
        int textRed,
        int textGreen,
        int textBlue,
        float ghostTextOpacity,
        float ghostTextStartScale,
        float ghostTextEndScale,
        float ghostTextOffsetX,
        float ghostTextOffsetY,
        int ghostTextRed,
        int ghostTextGreen,
        int ghostTextBlue,
        SpacingAnimationMode spacingAnimationMode,
        float spacingModifier,
        int fadeInTicks,
        int holdTicks,
        int fadeOutTicks
) {
    private static final Logger LOGGER = LogUtils.getLogger();

    public enum SpacingAnimationMode {
        FADE_IN,
        FADE_OUT,
        FADE_IN_AND_HOLD,
        HOLD,
        HOLD_AND_FADE_OUT,
        FULL
    }

    public static BannerStyle fromJson(JsonObject json) {
        JsonObject banner = GsonHelper.getAsJsonObject(json, "banner", new JsonObject());
        JsonObject text = GsonHelper.getAsJsonObject(json, "text", new JsonObject());
        JsonObject text_color = GsonHelper.getAsJsonObject(text, "color", new JsonObject());
        JsonObject ghost_text = GsonHelper.getAsJsonObject(json, "ghost_text", new JsonObject());
        JsonObject ghost_text_color = GsonHelper.getAsJsonObject(ghost_text, "ghost_text_color", new JsonObject());
        JsonObject animation = GsonHelper.getAsJsonObject(json, "animation", new JsonObject());

        SoundEvent sound = null;
        ResourceLocation font;
        SpacingAnimationMode spacingAnimationMode;

        try {
            font = ResourceLocation.parse(
                    GsonHelper.getAsString(json, "font", SoulsMessageBannersConfig.DEFAULT_FONT.get())
            );
        } catch (Exception e) {
            LOGGER.warn("Invalid font '{}', using default.",
                    GsonHelper.getAsString(json, "font", SoulsMessageBannersConfig.DEFAULT_FONT.get()), e);
            font = ResourceLocation.parse(SoulsMessageBannersConfig.DEFAULT_FONT.get());
        }

        if (json.has("sound")) {
            String soundKey = GsonHelper.getAsString(json, "sound");

            try {
                ResourceLocation soundId = ResourceLocation.parse(soundKey);
                sound = BuiltInRegistries.SOUND_EVENT.get(soundId);

                if (sound == null) {
                    LOGGER.warn("Unknown sound '{}', using default.", soundKey);
                }
            } catch (Exception e) {
                LOGGER.warn("Invalid sound '{}', using default.", soundKey, e);
            }
        }

        if (sound == null) {
            sound = SoulsMessageBannersConfig.getSound();
        }

        String spacing_animation_mode_name = GsonHelper.getAsString(ghost_text, "spacing_animation_mode", "HOLD").toUpperCase();

        try {
            spacingAnimationMode = SpacingAnimationMode.valueOf(spacing_animation_mode_name);
        } catch (IllegalArgumentException e) {
            LOGGER.warn("Unknown spacing_animation_mode '{}', using default.", spacing_animation_mode_name);
            spacingAnimationMode = SpacingAnimationMode.HOLD;
        }

        int textRed = GsonHelper.getAsInt(text_color, "red", 255);
        int textGreen = GsonHelper.getAsInt(text_color, "green", 170);
        int textBlue = GsonHelper.getAsInt(text_color, "blue", 0);

        int ghostTextRed = GsonHelper.getAsInt(ghost_text_color, "red", textRed);
        int ghostTextGreen = GsonHelper.getAsInt(ghost_text_color, "green", textGreen);
        int ghostTextBlue = GsonHelper.getAsInt(ghost_text_color, "blue", textBlue);

        return new BannerStyle(
                GsonHelper.getAsBoolean(json, "enabled", true),
                font,
                sound,
                GsonHelper.getAsInt(json, "y_offset", SoulsMessageBannersConfig.Y_OFFSET.get()),
                GsonHelper.getAsFloat(banner, "opacity", 0.5f),
                GsonHelper.getAsFloat(text, "opacity", 0.8f),
                GsonHelper.getAsBoolean(text, "autoscale", SoulsMessageBannersConfig.TEXT_AUTOSCALE.get()),
                GsonHelper.getAsFloat(text, "scale", SoulsMessageBannersConfig.DEFAULT_TEXT_SCALE.get().floatValue()),
                GsonHelper.getAsFloat(text, "x", 0.0f),
                GsonHelper.getAsFloat(text, "y", 0.0f),
                textRed,
                textGreen,
                textBlue,

                GsonHelper.getAsFloat(ghost_text, "opacity", 0.5f),
                GsonHelper.getAsFloat(ghost_text, "start_scale", 0.5f),
                GsonHelper.getAsFloat(ghost_text, "end_scale", 1.1f),
                GsonHelper.getAsFloat(ghost_text, "x", 0.0f),
                GsonHelper.getAsFloat(ghost_text, "y", 0.0f),
                ghostTextRed,
                ghostTextGreen,
                ghostTextBlue,
                spacingAnimationMode,
                GsonHelper.getAsFloat(ghost_text, "spacing_modifier", 0.06f),

                GsonHelper.getAsInt(animation, "fade_in", 10),
                GsonHelper.getAsInt(animation, "hold", 60),
                GsonHelper.getAsInt(animation, "fade_out", 10)
        );
    }

    public int totalDuration() {
        return fadeInTicks + holdTicks + fadeOutTicks;
    }
}