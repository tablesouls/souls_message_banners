package net.tablesouls.souls_message_banners.util;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.tablesouls.souls_message_banners.api.BannerStyle;

public class MessageBannerHelper {
    private static Component message = null;
    private static BannerStyle style = null;
    private static int ticksAlive = 0;

    private static float[] cachedCharWidths;
    private static FormattedCharSequence[] cachedChars;

    public static void show(Component text, BannerStyle bannerStyle) {
        style = bannerStyle;
        if (!style.enabled()) return;
        message = text.copy().withStyle(s -> s.withFont(style.font()));
        SoundHelper.playUiSound(style.sound());
        ticksAlive = 0;
        cachedCharWidths = null;
        cachedChars = null;
    }

    public static void computeCharCache(Font font, Component message) {
        String text = message.getString();
        int length = text.length();

        cachedCharWidths = new float[length];
        cachedChars = new FormattedCharSequence[length];

        for (int i = 0; i < length; i++) {
            String charStr = String.valueOf(text.charAt(i));
            cachedChars[i] = Component.literal(charStr).setStyle(message.getStyle()).getVisualOrderText();
            cachedCharWidths[i] = font.width(cachedChars[i]);
        }
    }

    public static FormattedCharSequence[] getCachedChars() {
        return cachedChars;
    }

    public static float[] getCachedCharWidths() {
        return cachedCharWidths;
    }

    public static BannerStyle getStyle() {
        return style;
    }

    public static void tick() {
        if (message != null) {
            ticksAlive++;
            if (ticksAlive > style.totalDuration()) {
                message = null;
            }
        }
    }

    public static Component getMessage() {
        return message;
    }

    public static float getAlpha(float smoothTicks) {
        if (message == null) return 0f;
        int fade_in = style.fadeInTicks();
        int hold = style.holdTicks();
        int fade_out = style.fadeOutTicks();

        if (smoothTicks < fade_in) {
            return Mth.clamp(smoothTicks / (float) fade_in, 0f, 1f);
        } else if (smoothTicks < fade_in + hold) {
            return 1f;
        } else {
            float fadeTick = smoothTicks - fade_in - hold;
            return Mth.clamp(1f - (fadeTick / (float) fade_out), 0f, 1f);
        }
    }

    public static float getTicksAlive() {
        return ticksAlive;
    }

    public static float getGhostTextScale(float smoothTicks, float startScale, float endScale) {
        if (message == null) return endScale;

        int FADE_IN = style.fadeInTicks();

        if (smoothTicks < FADE_IN) {
            float progress = Mth.clamp(smoothTicks/(float) FADE_IN, 0f, 1f);
            float eased = 1f - (float)Math.pow(1f - progress, 3);

            return Mth.lerp(eased, startScale, endScale);
        }

        return endScale;
    }

    public static float getSpacingAnimation(float smoothTicks, float spacingMultiplier, float holdSpacingMultiplier) {
        if (message == null) return 0f;

        int FADE_IN = style.fadeInTicks();
        int HOLD = style.holdTicks();

        if (smoothTicks < FADE_IN) {
            return 0f;
        }

        if (smoothTicks < FADE_IN + HOLD) {
            float holdTick = smoothTicks - FADE_IN;
            float holdProgress = holdTick / (float) HOLD;
            float eased = 1f - (float)Math.pow(1f - holdProgress, 3);

            return eased * spacingMultiplier * holdSpacingMultiplier;
        }

        return spacingMultiplier * holdSpacingMultiplier;
    }

    public static float getSpacingAnimation(float smoothTicks, float spacingMultiplier){
        float holdSpacingMultiplier = 1.6f;
        return getSpacingAnimation(smoothTicks, spacingMultiplier, holdSpacingMultiplier);
    }
}
