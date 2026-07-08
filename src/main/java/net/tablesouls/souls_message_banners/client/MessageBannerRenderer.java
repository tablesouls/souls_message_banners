package net.tablesouls.souls_message_banners.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.tablesouls.souls_message_banners.assets.BannerStyle;
import net.tablesouls.souls_message_banners.util.MessageBannerHelper;

public class MessageBannerRenderer implements IGuiOverlay {
    private static final int TRANSPARENT_COLOR = 0x00000000;

    private static final int SOLID_HALF = 20;
    private static final int FADE_HEIGHT = 15;

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Component message = MessageBannerHelper.getMessage();
        if (message == null) return;

        float smoothTicks = MessageBannerHelper.getTicksAlive() + partialTick;
        float alpha = MessageBannerHelper.getAlpha(smoothTicks);

        BannerStyle style = MessageBannerHelper.getStyle();

        var font = gui.getFont();

        int x = screenWidth/2;
        int y = screenHeight/2;

        int alphaInt = (int) (alpha * 255f);
        if (alpha <= 0f || alphaInt < 5) return;

        RenderSystem.enableBlend();

        // general text properties
        int textWidth = font.width(message);
        int textX = -(int)(textWidth / 2f) ;
        int textY = -(int)(font.lineHeight / 2f);
        int textRgb = (style.textRed() << 16) | (style.textGreen() << 8) | style.textBlue();

        // ghost text properties
        int ghostTextAlpha = (int)  (alphaInt * style.ghostTextOpacity());
        if (ghostTextAlpha < 4) ghostTextAlpha = 4;

        int ghostTextColor = (ghostTextAlpha << 24) | textRgb;
        float spacingMultiplier = style.spacingModifier();
        float holdSpacingMultiplier = style.holdSpacingModifier();

        float ghostTextScale = MessageBannerHelper.getGhostTextScale(smoothTicks, style.ghostTextStartScale(), style.ghostTextEndScale());
        float ghostTextSpacing = MessageBannerHelper.getSpacingAnimation(smoothTicks, spacingMultiplier, holdSpacingMultiplier);

        // animates the ghost text
        pushTextTransform(guiGraphics, style, x, y);
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(ghostTextScale, ghostTextScale, 1f);

        drawSpacedText(guiGraphics, font, message, 0,  textY, ghostTextColor, ghostTextSpacing);

        guiGraphics.pose().popPose(); // ghost scaling
        guiGraphics.pose().popPose(); // ghost text transform

        //banner properties
        int baseAlpha = (int) (alphaInt * style.bannerOpacity());
        int solidColor = (baseAlpha << 24);

        //banner rendering
        guiGraphics.fillGradient(0, y - SOLID_HALF - FADE_HEIGHT, screenWidth, y - SOLID_HALF, TRANSPARENT_COLOR, solidColor);
        guiGraphics.fill(0, y - SOLID_HALF, screenWidth, y + SOLID_HALF, solidColor);
        guiGraphics.fillGradient(0, y + SOLID_HALF, screenWidth, y + SOLID_HALF + FADE_HEIGHT, solidColor, TRANSPARENT_COLOR);

        //main text properties
        int textAlpha = (int)  (alphaInt * style.textOpacity());
        if (textAlpha < 4) textAlpha = 4;
        int textColor = (textAlpha << 24) | textRgb;

        // animates the main text
        pushTextTransform(guiGraphics, style, x, y);
        guiGraphics.drawString(font, message, textX, textY, textColor, false);
        guiGraphics.pose().popPose(); // main text transform

        RenderSystem.disableBlend();
    }

    private void pushTextTransform(
            GuiGraphics guiGraphics,
            BannerStyle style,
            int x,
            int y
    ) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(
                x + style.textOffsetX(),
                y + style.textOffsetY(),
                0
        );
        guiGraphics.pose().scale(
                style.textScale(),
                style.textScale(),
                1f
        );
    }

    private void drawSpacedText(GuiGraphics guiGraphics, Font font, Component message, int x, int y, int color, float spacingMultiplier) {
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
