package dev.isxander.splitscreen.client.host.gui;

import java.util.Optional;
import java.util.function.Consumer;
import net.minecraft.class_156;
import net.minecraft.class_1921;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4071;
import net.minecraft.class_9848;

public class SplitscreenLoadingOverlay extends class_4071 {
   private static final int BACKGROUND_COLOUR = class_9848.method_61323(250, 254, 140);
   private static final int FOREGROUND_COLOUR = class_9848.method_61323(35, 35, 35);
   private static final long FADE_OUT_TIME = 1000L;
   private static final long FADE_IN_TIME = 500L;
   private final class_310 minecraft;
   private final SplitscreenFakeReloadInstance status;
   private float progress;
   private final boolean fadeIn;
   private long fadeOutStart = -1L;
   private long fadeInStart = -1L;
   private final Consumer<Optional<Throwable>> onFinish;

   public SplitscreenLoadingOverlay(class_310 minecraft, SplitscreenFakeReloadInstance status, Consumer<Optional<Throwable>> onFinish, boolean fadeIn) {
      this.minecraft = minecraft;
      this.status = status;
      this.onFinish = onFinish;
      this.fadeIn = fadeIn;
   }

   public void method_25394(class_332 guiGraphics, int mouseX, int mouseY, float partialTick) {
      guiGraphics.method_51448().method_22903();
      guiGraphics.method_51448().method_46416(0.0F, 0.0F, 1000.0F);
      int width = guiGraphics.method_51421();
      int height = guiGraphics.method_51443();
      long time = class_156.method_658();
      if (this.fadeIn && this.fadeInStart == -1L) {
         this.fadeInStart = time;
      }

      float fadeOutProgress = this.fadeOutStart > 1L ? (float)(time - this.fadeOutStart) / 1000.0F : -1.0F;
      float fadeInProgress = this.fadeInStart > 1L ? (float)(time - this.fadeInStart) / 500.0F : -1.0F;
      float logoFade;
      int centerX;
      if (fadeOutProgress >= 1.0F) {
         this.renderScreen(guiGraphics, partialTick);
         logoFade = 1.0F - class_3532.method_15363(fadeOutProgress - 1.0F, 0.0F, 1.0F);
         centerX = class_3532.method_15386(logoFade * 255.0F);
         guiGraphics.method_51739(class_1921.method_51785(), 0, 0, width, height, class_9848.method_61330(centerX, BACKGROUND_COLOUR));
      } else if (this.fadeIn) {
         this.renderScreen(guiGraphics, partialTick);
         centerX = class_3532.method_15384(class_3532.method_15350((double)fadeInProgress, 0.15D, 1.0D) * 255.0D);
         guiGraphics.method_51739(class_1921.method_51785(), 0, 0, width, height, class_9848.method_61330(centerX, BACKGROUND_COLOUR));
         logoFade = class_3532.method_15363(fadeInProgress, 0.0F, 1.0F);
      } else {
         centerX = BACKGROUND_COLOUR;
         guiGraphics.method_51739(class_1921.method_51785(), 0, 0, width, height, centerX);
         logoFade = 1.0F;
      }

      centerX = width / 2;
      int centerY = height / 2;
      double d = Math.min((double)width * 0.75D, (double)height) * 0.25D;
      double barWidth = d * 4.0D;
      int halfBarWidth = (int)(barWidth / 2.0D);
      int barY = (int)((float)height * 0.8325F);
      guiGraphics.method_51433(this.minecraft.field_1772, "Loading Splitscreen", centerX, centerY - 10, FOREGROUND_COLOUR, false);
      float progress = this.status.method_18229();
      this.progress = class_3532.method_15363(this.progress * 0.95F + progress * 0.05F, 0.0F, 1.0F);
      if (fadeOutProgress < 1.0F) {
         this.drawProgressBar(guiGraphics, centerX - halfBarWidth, barY - 5, centerX + halfBarWidth, barY + 5, 1.0F - class_3532.method_15363(fadeOutProgress, 0.0F, 1.0F));
      }

      if (fadeOutProgress >= 2.0F) {
         this.minecraft.method_18502((class_4071)null);
      }

      if (this.fadeOutStart == -1L && this.status.method_18787() && (!this.fadeIn || fadeInProgress >= 2.0F)) {
         try {
            this.status.method_18849();
            this.onFinish.accept(Optional.empty());
         } catch (Throwable var22) {
            this.onFinish.accept(Optional.of(var22));
         }

         this.fadeOutStart = class_156.method_658();
         if (this.minecraft.field_1755 != null) {
            this.minecraft.field_1755.method_25423(this.minecraft, guiGraphics.method_51421(), guiGraphics.method_51443());
         }
      }

      guiGraphics.method_51448().method_22909();
   }

   private void renderScreen(class_332 guiGraphics, float partialTick) {
      if (this.minecraft.field_1755 != null) {
         this.minecraft.field_1755.method_25394(guiGraphics, 0, 0, partialTick);
      }

   }

   private void drawProgressBar(class_332 guiGraphics, int minX, int minY, int maxX, int maxY, float partialTick) {
      int i = class_3532.method_15386((float)(maxX - minX - 2) * this.progress);
      int j = Math.round(partialTick * 255.0F);
      int k = class_9848.method_61330(j, FOREGROUND_COLOUR);
      guiGraphics.method_25294(minX + 2, minY + 2, minX + i, maxY - 2, k);
      guiGraphics.method_25294(minX + 1, minY, maxX - 1, minY + 1, k);
      guiGraphics.method_25294(minX + 1, maxY, maxX - 1, maxY - 1, k);
      guiGraphics.method_25294(minX, minY, minX + 1, maxY, k);
      guiGraphics.method_25294(maxX, minY, maxX - 1, maxY, k);
   }
}
