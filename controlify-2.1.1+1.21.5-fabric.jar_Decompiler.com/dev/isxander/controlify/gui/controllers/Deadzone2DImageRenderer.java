package dev.isxander.controlify.gui.controllers;

import dev.isxander.controlify.controller.input.DeadzoneGroup;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.utils.render.elements.CircleElementRenderState;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.gui.image.ImageRenderer;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;

public class Deadzone2DImageRenderer implements ImageRenderer {
   private final InputComponent input;
   private final DeadzoneGroup deadzoneGroup;
   private final Supplier<Option<Float>> deadzoneOption;

   public Deadzone2DImageRenderer(InputComponent input, DeadzoneGroup deadzoneGroup, Supplier<Option<Float>> deadzoneOption) {
      this.input = input;
      this.deadzoneGroup = deadzoneGroup;
      this.deadzoneOption = deadzoneOption;
   }

   public int render(class_332 graphics, int x, int y, int renderWidth, float tickDelta) {
      float radius = (float)renderWidth / 4.0F;
      int renderHeight = (int)(radius * 2.0F);
      List<class_2960> deadzones = this.deadzoneGroup.axes();
      float up = this.input.rawStateNow().getAxisState((class_2960)deadzones.get(0));
      float down = this.input.rawStateNow().getAxisState((class_2960)deadzones.get(1));
      float left = this.input.rawStateNow().getAxisState((class_2960)deadzones.get(2));
      float right = this.input.rawStateNow().getAxisState((class_2960)deadzones.get(3));
      float currentX = right - left;
      float currentY = down - up;
      graphics.method_25292(x, (int)((float)x + radius * 2.0F), (int)((float)y + radius), -5592406);
      graphics.method_25301((int)((float)x + radius), y, (int)((float)y + radius * 2.0F), -5592406);
      CircleElementRenderState.outline(graphics, (float)x + radius, (float)y + radius, radius, 1.0F, -1).submit(graphics);
      float deadzone = (Float)((Option)this.deadzoneOption.get()).pendingValue();
      boolean aboveDeadzone = Math.abs(currentX) > deadzone || Math.abs(currentY) > deadzone;
      CircleElementRenderState.outline(graphics, (float)x + radius, (float)y + radius, deadzone * radius, 1.0F, aboveDeadzone ? -16711681 : -65536).submit(graphics);
      CircleElementRenderState.filled(graphics, (float)x + radius + currentX * radius, (float)y + radius + currentY * radius, 1.0F, -16711936).submit(graphics);
      class_327 font = class_310.method_1551().field_1772;
      DecimalFormat format = new DecimalFormat("0.000");
      graphics.method_25303(font, "X: " + format.format((double)currentX), (int)((float)x + radius * 2.0F + 5.0F), y, -1);
      String var10002 = "Y: " + format.format((double)currentY);
      int var10003 = (int)((float)x + radius * 2.0F + 5.0F);
      Objects.requireNonNull(font);
      graphics.method_25303(font, var10002, var10003, y + 9 + 1, -1);
      return renderHeight;
   }

   public void close() {
   }
}
