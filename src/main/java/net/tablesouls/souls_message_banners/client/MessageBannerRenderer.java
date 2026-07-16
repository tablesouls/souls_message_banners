package net.tablesouls.souls_message_banners.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.tablesouls.souls_message_banners.assets.BannerStyle;
import net.tablesouls.souls_message_banners.util.MessageBannerHelper;

public class MessageBannerRenderer implements LayeredDraw.Layer {
    private static final int TRANSPARENT_COLOR = 0x00000000;

    private static final int SOLID_HALF = 20;
    private static final int FADE_HEIGHT = 15;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Component message = MessageBannerHelper.getMessage();
        if (message == null) return;

        float partialTick = deltaTracker.getGameTimeDeltaPartialTick(false);
        float smoothTicks = MessageBannerHelper.getTicksAlive() + partialTick;
        float alpha = MessageBannerHelper.getAlpha(smoothTicks);

        BannerStyle style = MessageBannerHelper.getStyle();

        Font font = Minecraft.getInstance().font;

        int screenWidth = guiGraphics.guiWidth();
        int screenHeight = guiGraphics.guiHeight();

        int x = screenWidth / 2;
        int y = screenHeight / 2 - style.yOffset();

        int alphaInt = (int) (alpha * 255f);
        if (alpha <= 0f || alphaInt < 5) return;

        RenderSystem.enableBlend();

        // general text properties
        float textScale = style.textScale();
        boolean textAutoScale = style.textAutoScale();

        int textWidth = font.width(message);

        if (textAutoScale) {
            float maxWidth = screenWidth * 0.9f;
            float scaledWidth = textWidth * textScale;

            if (scaledWidth > maxWidth) {
                textScale = maxWidth / textWidth;
            }
        }

        int textX = -(int) (textWidth / 2f);
        int textY = -(int) (font.lineHeight / 2f);

        // ghost text properties
        int ghostTextAlpha = (int) (alphaInt * style.ghostTextOpacity());
        if (ghostTextAlpha < 4) ghostTextAlpha = 4;

        int ghostTextRgb = (style.ghostTextRed() << 16) | (style.ghostTextGreen() << 8) | style.ghostTextBlue();
        int ghostTextColor = (ghostTextAlpha << 24) | ghostTextRgb;
        float spacingMultiplier = style.spacingModifier();

        float ghostTextScale = MessageBannerHelper.getGhostTextScale(smoothTicks, style.ghostTextStartScale(), style.ghostTextEndScale());
        float ghostTextSpacing = MessageBannerHelper.getSpacingAnimation(smoothTicks, spacingMultiplier);
        float ghostTextScaling = textScale * ghostTextScale;

        // animates the ghost text
        RenderSystem.setShaderColor(1f, 1f, 1f, ghostTextAlpha  / 255f);
        pushTextTransform(guiGraphics, style, x, y, style.ghostTextOffsetX(), style.ghostTextOffsetY(), ghostTextScaling, ghostTextScaling);
        drawSpacedText(guiGraphics, font, message, 0,  textY, ghostTextRgb, ghostTextSpacing);
        guiGraphics.pose().popPose(); // ghost text transform
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        //banner properties
        int baseAlpha = (int) (alphaInt * style.bannerOpacity());
        int solidColor = (baseAlpha << 24);

        //banner rendering
        guiGraphics.fillGradient(0, y - SOLID_HALF - FADE_HEIGHT, screenWidth, y - SOLID_HALF, TRANSPARENT_COLOR, solidColor);
        guiGraphics.fill(0, y - SOLID_HALF, screenWidth, y + SOLID_HALF, solidColor);
        guiGraphics.fillGradient(0, y + SOLID_HALF, screenWidth, y + SOLID_HALF + FADE_HEIGHT, solidColor, TRANSPARENT_COLOR);

        //main text properties
        int textAlpha = (int) (alphaInt * style.textOpacity());
        if (textAlpha < 4) textAlpha = 4;

        int textRgb = (style.textRed() << 16) | (style.textGreen() << 8) | style.textBlue();
        int textColor = (textAlpha << 24) | textRgb;

        // animates the main text
        RenderSystem.setShaderColor(1f, 1f, 1f, textAlpha / 255f);
        pushTextTransform(guiGraphics, style, x, y, style.textOffsetX(), style.textOffsetY(), textScale, textScale);
        guiGraphics.drawString(font, message, textX, textY, textRgb, false);
        guiGraphics.pose().popPose(); // main text transform
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

        RenderSystem.disableBlend();
    }

    private void pushTextTransform(
            GuiGraphics guiGraphics,
            BannerStyle style,
            float x,
            float y,
            float offsetX,
            float offsetY,
            float w,
            float h
    ) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(x + offsetX, y - offsetY, 0);
        guiGraphics.pose().scale(w, h, 1f);
    }

    private void drawSpacedText(GuiGraphics guiGraphics, Font font, Component message, float x, float y, int color, float spacingMultiplier) {
        if (MessageBannerHelper.getCachedChars() == null) {MessageBannerHelper.computeCharCache(font, message);}

        FormattedCharSequence[] chars = MessageBannerHelper.getCachedChars();
        float[] charWidths = MessageBannerHelper.getCachedCharWidths();

        int length = chars.length;
        if (length == 0) return;

        float totalWidth = 0;
        for (int i = 0; i < length; i++) {
            totalWidth += charWidths[i] + (charWidths[i] * spacingMultiplier);
        }
        totalWidth -= (charWidths[length - 1] * spacingMultiplier);

        float x_c = x - totalWidth / 2f;
        for (int i = 0; i < length; i++) {
            guiGraphics.drawString(font, chars[i], x_c, y, color, false);
            x_c += charWidths[i] + (charWidths[i] * spacingMultiplier);
        }
    }
}