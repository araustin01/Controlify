package dev.isxander.splitscreen.client.host.gui;

import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Overlay;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

import java.util.Optional;
import java.util.function.Consumer;

public class SplitscreenLoadingOverlay extends Overlay {

    private static final int BACKGROUND_COLOUR = ARGB.color(180, 20, 20, 20); // Dark semi-transparent background
    private static final int FOREGROUND_COLOUR = ARGB.color(255, 255, 255); // White text for better contrast
    private static final int PROGRESS_BAR_COLOR = ARGB.color(255, 100, 150, 255); // Nice blue progress bar
    private static final long FADE_OUT_TIME = 1000L;
    private static final long FADE_IN_TIME = 500L;

    private final Minecraft minecraft;
    private final SplitscreenFakeReloadInstance status;

    private float progress;

    private final boolean fadeIn;
    private long fadeOutStart = -1L;
    private long fadeInStart = -1L;

    private final Consumer<Optional<Throwable>> onFinish;

    public SplitscreenLoadingOverlay(Minecraft minecraft, SplitscreenFakeReloadInstance status, Consumer<Optional<Throwable>> onFinish, boolean fadeIn) {
        this.minecraft = minecraft;
        this.status = status;
        this.onFinish = onFinish;
        this.fadeIn = fadeIn;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 1000);

        int width = guiGraphics.guiWidth();
        int height = guiGraphics.guiHeight();

        long time = Util.getMillis();
        if (this.fadeIn && this.fadeInStart == -1L) {
            this.fadeInStart = time;
        }

        float fadeOutProgress = this.fadeOutStart > 1L ? (float) (time - this.fadeOutStart) / FADE_OUT_TIME : -1.0F;
        float fadeInProgress = this.fadeInStart > 1L ? (float) (time - this.fadeInStart) / FADE_IN_TIME : -1.0F;

        float logoFade;
        if (fadeOutProgress >= 1.0F) {
            this.renderBlurredScreen(guiGraphics, partialTick);

            logoFade = 1 - Mth.clamp(fadeOutProgress - 1, 0, 1);
            int opacity = Mth.ceil(logoFade * 180); // Reduced opacity for the dark background
            guiGraphics.fill(RenderType.guiOverlay(), 0, 0, width, height, ARGB.color(opacity, 20, 20, 20));
        } else if (this.fadeIn) {
            this.renderBlurredScreen(guiGraphics, partialTick);

            int alpha = Mth.ceil(Mth.clamp(fadeInProgress, 0.15, 1.0) * 180);
            guiGraphics.fill(RenderType.guiOverlay(), 0, 0, width, height, ARGB.color(alpha, 20, 20, 20));
            logoFade = Mth.clamp(fadeInProgress, 0, 1);
        } else {
            this.renderBlurredScreen(guiGraphics, partialTick);
            guiGraphics.fill(RenderType.guiOverlay(), 0, 0, width, height, BACKGROUND_COLOUR);
            logoFade = 1;
        }

        int centerX = width / 2;
        int centerY = height / 2;

        // Center the text properly by getting its width and offsetting by half
        String loadingText = "Loading Splitscreen...";
        int textWidth = this.minecraft.font.width(loadingText);
        int textX = centerX - textWidth / 2;
        int textY = centerY - 30;

        // Draw text with shadow for better visibility
        guiGraphics.drawString(this.minecraft.font, loadingText, textX, textY, FOREGROUND_COLOUR, true);

        // Position progress bar nicely below the text
        double barWidth = Math.min(width * 0.4, 300); // Reasonable max width
        int halfBarWidth = (int) (barWidth / 2);
        int barY = textY + 40;

        float progress = this.status.getActualProgress();
        this.progress = Mth.clamp(this.progress * 0.95f + progress * 0.05f, 0, 1);
        if (fadeOutProgress < 1f) {
            this.drawProgressBar(guiGraphics, centerX - halfBarWidth, barY - 3, centerX + halfBarWidth, barY + 3, 1 - Mth.clamp(fadeOutProgress, 0, 1));
        }
        if (fadeOutProgress >= 2f) {
            this.minecraft.setOverlay(null);
        }

        if (this.fadeOutStart == -1L && this.status.isDone() && (!this.fadeIn || fadeInProgress >= 2.0F)) {
            try {
                this.status.checkExceptions();
                this.onFinish.accept(Optional.empty());
            } catch (Throwable var24) {
                this.onFinish.accept(Optional.of(var24));
            }

            this.fadeOutStart = Util.getMillis();
            if (this.minecraft.screen != null) {
                this.minecraft.screen.init(this.minecraft, guiGraphics.guiWidth(), guiGraphics.guiHeight());
            }
        }

        guiGraphics.pose().popPose();
    }

    private void renderBlurredScreen(GuiGraphics guiGraphics, float partialTick) {
        if (this.minecraft.screen != null) {
            // Render the screen behind with reduced brightness to simulate blur effect
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0, 0, -100); // Push it back
            this.minecraft.screen.render(guiGraphics, 0, 0, partialTick);
            guiGraphics.pose().popPose();

            // Add a subtle darkening overlay to enhance the blur effect
            guiGraphics.fill(RenderType.guiOverlay(), 0, 0, guiGraphics.guiWidth(), guiGraphics.guiHeight(),
                ARGB.color(120, 0, 0, 0));
        }
    }

    private void drawProgressBar(GuiGraphics guiGraphics, int minX, int minY, int maxX, int maxY, float partialTick) {
        int progressWidth = Mth.ceil((maxX - minX - 4) * this.progress);
        int alpha = Math.round(partialTick * 255.0F);

        // Draw background of progress bar
        int backgroundColor = ARGB.color(alpha / 2, 60, 60, 60);
        guiGraphics.fill(minX, minY, maxX, maxY, backgroundColor);

        // Draw progress fill with nice color
        int progressColor = ARGB.color(alpha, ARGB.red(PROGRESS_BAR_COLOR), ARGB.green(PROGRESS_BAR_COLOR), ARGB.blue(PROGRESS_BAR_COLOR));
        guiGraphics.fill(minX + 2, minY + 1, minX + 2 + progressWidth, maxY - 1, progressColor);

        // Draw subtle border
        int borderColor = ARGB.color(alpha, 120, 120, 120);
        guiGraphics.fill(minX, minY, maxX, minY + 1, borderColor); // top
        guiGraphics.fill(minX, maxY - 1, maxX, maxY, borderColor); // bottom
        guiGraphics.fill(minX, minY, minX + 1, maxY, borderColor); // left
        guiGraphics.fill(maxX - 1, minY, maxX, maxY, borderColor); // right
    }

}
