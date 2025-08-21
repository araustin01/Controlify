package dev.isxander.controlify.gui.guide;

import dev.isxander.controlify.font.BindingFontHelper;
import dev.isxander.controlify.gui.layout.RenderComponent;
import java.util.Objects;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_5348;
import org.joml.Vector2i;
import org.joml.Vector2ic;

public class GuideActionRenderer<T> implements RenderComponent {
   private final GuideAction<T> guideAction;
   private final boolean rtl;
   private final boolean textContrast;
   private class_2561 bindingText;
   private int bindingTextWidth;
   private class_2561 name = null;

   public GuideActionRenderer(GuideAction<T> action, boolean rtl, boolean textContrast) {
      this.guideAction = action;
      this.rtl = rtl;
      this.textContrast = textContrast;
      this.bindingText = action.binding().inputIcon();
      this.bindingTextWidth = class_310.method_1551().field_1772.method_27525(this.bindingText);
   }

   public void render(class_332 graphics, int x, int y, float deltaTime) {
      if (this.isVisible()) {
         class_327 font = class_310.method_1551().field_1772;
         int textWidth = font.method_27525(this.name);
         int bindingHeight = BindingFontHelper.getComponentHeight(font, (class_5348)this.bindingText);
         int var10000 = y + bindingHeight / 2;
         Objects.requireNonNull(font);
         int centeredTextY = var10000 - 9 / 2;
         if (!this.rtl) {
            graphics.method_51439(font, this.bindingText, x, centeredTextY, -1, false);
            x += this.bindingTextWidth + 4;
         }

         if (this.textContrast) {
            int var10001 = x - 1;
            int var10002 = centeredTextY - 1;
            int var10003 = x + textWidth + 1;
            Objects.requireNonNull(font);
            graphics.method_25294(var10001, var10002, var10003, centeredTextY + 9 + 1, Integer.MIN_VALUE);
         }

         graphics.method_51439(font, this.name, x, centeredTextY, -1, false);
         x += textWidth + 4;
         if (this.rtl) {
            graphics.method_51439(font, this.bindingText, x, centeredTextY, -1, false);
         }

      }
   }

   public Vector2ic size() {
      if (!this.isVisible()) {
         return new Vector2i();
      } else {
         class_327 font = class_310.method_1551().field_1772;
         int var10002 = font.method_27525(this.name) + 4 + this.bindingTextWidth;
         int var10003 = BindingFontHelper.getComponentHeight(font, (class_5348)this.bindingText);
         Objects.requireNonNull(font);
         return new Vector2i(var10002, Math.max(var10003, 9) + 2);
      }
   }

   public boolean isVisible() {
      return this.name != null && !this.guideAction.binding().isUnbound() && this.bindingText != null;
   }

   public void updateName(T ctx) {
      this.bindingText = this.guideAction.binding().inputIcon();
      this.bindingTextWidth = class_310.method_1551().field_1772.method_27525(this.bindingText);
      this.name = (class_2561)this.guideAction.name().supply(ctx).orElse((Object)null);
   }
}
