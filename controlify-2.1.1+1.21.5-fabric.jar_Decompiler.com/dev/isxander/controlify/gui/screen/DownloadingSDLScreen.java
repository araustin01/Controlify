package dev.isxander.controlify.gui.screen;

import dev.isxander.controlify.utils.ClientUtils;
import dev.isxander.controlify.utils.render.CGuiPose;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Objects;
import net.minecraft.class_124;
import net.minecraft.class_156;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_7077;
import net.minecraft.class_7940;

public class DownloadingSDLScreen extends class_437 implements DontInteruptScreen {
   private final class_437 screenOnFinish;
   private final Path nativePath;
   private long receivedBytes;
   private long totalBytes;
   private final DecimalFormat format = new DecimalFormat("0.00 MB");

   public DownloadingSDLScreen(class_437 screenOnFinish, long totalBytes, Path nativePath) {
      super(class_2561.method_43471("controlify.downloading_sdl.title"));
      this.screenOnFinish = screenOnFinish;
      this.nativePath = nativePath;
      this.totalBytes = totalBytes;
   }

   protected void method_25426() {
      class_2561 filePathText = class_2561.method_43470(this.nativePath.getFileName().toString()).method_27692(class_124.field_1078);
      int var10003 = this.field_22789 / 2 - this.field_22793.method_27525(filePathText) / 2;
      int var10005 = this.field_22793.method_27525(filePathText);
      Objects.requireNonNull(this.field_22793);
      this.method_37063(new class_7077(var10003, 112, var10005, 9, filePathText, (btn) -> {
         class_156.method_668().method_672(this.nativePath.toFile());
      }, this.field_22793));
      int paragraphWidth = this.field_22789 - 20;
      this.method_37063((new class_7940(this.field_22789 / 2 - paragraphWidth / 2, 139, class_2561.method_43471("controlify.downloading_sdl.info"), this.field_22793)).method_48984(paragraphWidth).method_48981(true));
   }

   public void method_25394(class_332 graphics, int mouseX, int mouseY, float delta) {
      this.method_25420(graphics, mouseX, mouseY, delta);
      super.method_25394(graphics, mouseX, mouseY, delta);
      CGuiPose pose = CGuiPose.ofPush(graphics);
      pose.translate((float)this.field_22789 / 2.0F - (float)this.field_22793.method_27525(this.method_25440()) / 2.0F * 2.5F, 30.0F);
      pose.scale(2.5F, 2.5F);
      graphics.method_27535(this.field_22793, this.method_25440(), 0, 0, -1);
      pose.pop();
      pose.push();
      pose.scale(2.0F, 2.0F);
      ClientUtils.drawBar(graphics, this.field_22789 / 2 / 2, 46, (float)this.receivedBytes / (float)this.totalBytes);
      pose.pop();
      String totalString = this.format.format((double)((float)this.totalBytes / 1024.0F / 1024.0F));
      graphics.method_25303(this.field_22793, totalString, (int)((float)this.field_22789 / 2.0F + 182.0F - (float)this.field_22793.method_1727(totalString)), 104, 11184810);
      String receivedString = this.format.format((double)((float)this.receivedBytes / 1024.0F / 1024.0F));
      graphics.method_25303(this.field_22793, receivedString, (int)((float)this.field_22789 / 2.0F - 182.0F), 104, 11184810);
   }

   public void updateDownloadProgress(long receivedBytes) {
      this.receivedBytes = receivedBytes;
   }

   public void finishDownload() {
      this.field_22787.method_1507(this.screenOnFinish);
   }

   public void failDownload(Throwable th) {
      this.finishDownload();
   }

   public void increaseTotal(long increment) {
      this.totalBytes += increment;
   }
}
