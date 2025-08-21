package dev.isxander.controlify.gui.screen;

import dev.isxander.controlify.bindings.input.Input;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.gui.controllers.BindController;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import dev.isxander.controlify.utils.render.CGuiPose;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import java.util.Optional;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;

public class BindConsumerScreen extends class_437 implements ScreenProcessorProvider {
   private final BindConsumerScreen.BindConsumer bindConsumer;
   private final Option<Input> option;
   private final class_437 backgroundScreen;
   private final BindController.BindControllerElement widgetToFocus;
   private final BindConsumerScreen.ScreenProcessorImpl screenProcessor = new BindConsumerScreen.ScreenProcessorImpl(this);
   private int ticksTillClose;
   private int ticksTillInput;

   public BindConsumerScreen(BindConsumerScreen.BindConsumer bindConsumer, Option<Input> option, BindController.BindControllerElement widgetToFocus, class_437 backgroundScreen) {
      super(class_2561.method_43473());
      this.bindConsumer = bindConsumer;
      this.option = option;
      this.widgetToFocus = widgetToFocus;
      this.backgroundScreen = backgroundScreen;
      this.ticksTillInput = 5;
   }

   public void method_25394(class_332 guiGraphics, int mouseX, int mouseY, float tickDelta) {
      Dimension<Integer> dim = this.widgetToFocus.getDimension();
      CGuiPose pose = CGuiPose.ofPush(guiGraphics);
      guiGraphics.method_51448().method_46416(0.0F, 0.0F, -20.0F);
      this.backgroundScreen.method_25394(guiGraphics, (Integer)dim.centerX(), (Integer)dim.centerY(), tickDelta);
      pose.pop();
      guiGraphics.method_25294(0, 0, this.field_22789, (Integer)dim.y() - 1, Integer.MIN_VALUE);
      guiGraphics.method_25294(0, (Integer)dim.y(), (Integer)dim.x() - 1, this.field_22790, Integer.MIN_VALUE);
      guiGraphics.method_25294((Integer)dim.xLimit() + 1, (Integer)dim.y() - 1, this.field_22789, this.field_22790, Integer.MIN_VALUE);
      guiGraphics.method_25294((Integer)dim.x(), (Integer)dim.yLimit() + 1, (Integer)dim.xLimit(), this.field_22790, Integer.MIN_VALUE);
      super.method_25394(guiGraphics, mouseX, mouseY, tickDelta);
   }

   public void method_25420(class_332 guiGraphics, int i, int j, float f) {
   }

   public void method_25393() {
      if (this.ticksTillClose > 0) {
         --this.ticksTillClose;
         if (this.ticksTillClose == 0) {
            this.widgetToFocus.awaitingControllerInput = false;
            this.field_22787.field_1755 = this.backgroundScreen;
         }
      }

      if (this.ticksTillInput > 0) {
         --this.ticksTillInput;
         if (this.ticksTillInput > 0) {
            return;
         }
      }

      Optional<Input> pressedBind = this.bindConsumer.getPressedBind();
      if (pressedBind.isPresent()) {
         this.option.requestSet((Input)pressedBind.get());
         this.returnToBackground();
      }

   }

   public boolean method_25404(int keyCode, int scanCode, int modifiers) {
      boolean consumed = super.method_25404(keyCode, scanCode, modifiers);
      if (consumed) {
         return true;
      } else if (this.ticksTillInput > 0) {
         return false;
      } else {
         this.returnToBackground();
         return true;
      }
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      boolean consumed = super.method_25402(mouseX, mouseY, button);
      if (consumed) {
         return true;
      } else if (this.ticksTillInput > 0) {
         return false;
      } else {
         this.returnToBackground();
         return true;
      }
   }

   public boolean method_25403(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      boolean consumed = super.method_25403(mouseX, mouseY, button, deltaX, deltaY);
      if (consumed) {
         return true;
      } else if (this.ticksTillInput > 0) {
         return false;
      } else {
         this.returnToBackground();
         return true;
      }
   }

   public boolean method_25401(double mouseX, double mouseY, double amount, double d) {
      boolean consumed = super.method_25401(mouseX, mouseY, amount, d);
      if (consumed) {
         return true;
      } else if (this.ticksTillInput > 0) {
         return false;
      } else {
         this.returnToBackground();
         return true;
      }
   }

   private void returnToBackground() {
      this.ticksTillClose = 5;
   }

   public ScreenProcessor<?> screenProcessor() {
      return this.screenProcessor;
   }

   private static class ScreenProcessorImpl extends ScreenProcessor<BindConsumerScreen> {
      public ScreenProcessorImpl(BindConsumerScreen screen) {
         super(screen);
      }

      public void onControllerUpdate(ControllerEntity controller) {
      }
   }

   public interface BindConsumer {
      Optional<Input> getPressedBind();
   }
}
