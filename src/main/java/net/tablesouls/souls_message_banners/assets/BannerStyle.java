package net.tablesouls.souls_message_banners.assets;

import com.google.gson.JsonObject;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraftforge.registries.ForgeRegistries;
import net.tablesouls.souls_message_banners.config.SoulsMessageBannersConfig;

public record BannerStyle(
        boolean enabled,
        ResourceLocation font,
        SoundEvent sound,
        float bannerOpacity,
        float textOpacity,
        float textScale,
        float textOffsetX,
        float textOffsetY,
        int textRed,
        int textGreen,
        int textBlue,
        float ghostTextOpacity,
        float ghostTextStartScale,
        float ghostTextEndScale,
        float spacingModifier,
        float holdSpacingModifier,
        int fadeInTicks,
        int holdTicks,
        int fadeOutTicks
) {
    public static BannerStyle fromJson(JsonObject json) {
        JsonObject banner = GsonHelper.getAsJsonObject(json, "banner", new JsonObject());
        JsonObject text = GsonHelper.getAsJsonObject(json, "text", new JsonObject());
        JsonObject text_color = GsonHelper.getAsJsonObject(text, "color", new JsonObject());
        JsonObject ghost_text = GsonHelper.getAsJsonObject(json, "ghost_text", new JsonObject());
        JsonObject animation = GsonHelper.getAsJsonObject(json, "animation", new JsonObject());

        SoundEvent sound = null;
        if (json.has("sound")) {
            String soundKey = GsonHelper.getAsString(json, "sound");
            ResourceLocation soundId = new ResourceLocation(soundKey);
            sound = ForgeRegistries.SOUND_EVENTS.getValue(soundId);
        }
        if (sound == null) {
            sound = SoulsMessageBannersConfig.getSound();
        }

        ResourceLocation font = new ResourceLocation(
          GsonHelper.getAsString(json, "font", SoulsMessageBannersConfig.DEFAULT_FONT.get())
        );

        return new BannerStyle(
                GsonHelper.getAsBoolean(json, "enabled", true),
                font,
                sound,
                GsonHelper.getAsFloat(banner, "opacity", 0.5f),

                GsonHelper.getAsFloat(text, "opacity", 0.8f),
                GsonHelper.getAsFloat(text, "scale", SoulsMessageBannersConfig.DEFAULT_TEXT_SIZE.get()),
                GsonHelper.getAsFloat(text, "x", 0f),
                GsonHelper.getAsFloat(text, "y", 0f),
                GsonHelper.getAsInt(text_color, "red", 255),
                GsonHelper.getAsInt(text_color, "green", 170),
                GsonHelper.getAsInt(text_color, "blue", 0),

                GsonHelper.getAsFloat(ghost_text, "opacity", 0.5f),
                GsonHelper.getAsFloat(ghost_text, "start_scale", 0.5f),
                GsonHelper.getAsFloat(ghost_text, "end_scale", 1.1f),
                GsonHelper.getAsFloat(ghost_text, "spacing_modifier", 0.04f),
                GsonHelper.getAsFloat(ghost_text, "hold_spacing_modifier", 1.2f),

                GsonHelper.getAsInt(animation, "fade_in", 10),
                GsonHelper.getAsInt(animation, "hold", 60),
                GsonHelper.getAsInt(animation, "fade_out", 10)
        );
    }

    public int totalDuration() {
        return fadeInTicks + holdTicks + fadeOutTicks;
    }
}