package dev.isxander.controlify.gui.controllers;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.bindings.input.AxisInput;
import dev.isxander.controlify.bindings.input.ButtonInput;
import dev.isxander.controlify.bindings.input.EmptyInput;
import dev.isxander.controlify.bindings.input.HatInput;
import dev.isxander.controlify.bindings.input.Input;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.input.ControllerStateView;
import dev.isxander.controlify.controller.input.HatState;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.gui.screen.BindConsumerScreen;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.yacl3.api.Controller;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.utils.Dimension;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.controllers.ControllerWidget;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;

public class BindController implements Controller<Input> {
   private final Option<Input> option;
   public final ControllerEntity controller;
   private boolean conflicting;

   public BindController(Option<Input> option, ControllerEntity controller) {
      this.option = option;
      this.controller = controller;
   }

   public Option<Input> option() {
      return this.option;
   }

   public class_2561 formatValue() {
      return class_2561.method_43473();
   }

   public void setConflicting(boolean conflicting) {
      this.conflicting = conflicting;
   }

   public boolean getConflicting() {
      return this.conflicting;
   }

   public BindController.BindControllerElement provideWidget(YACLScreen yaclScreen, Dimension<Integer> dimension) {
      return new BindController.BindControllerElement(this, yaclScreen, dimension);
   }

   public static class BindControllerElement extends ControllerWidget<BindController> implements ComponentProcessor {
      public boolean awaitingControllerInput = false;
      private final class_2561 awaitingText;

      public BindControllerElement(BindController control, YACLScreen screen, Dimension<Integer> dim) {
         super(control, screen, dim);
         this.awaitingText = class_2561.method_43471("controlify.gui.bind_input_awaiting").method_27692(class_124.field_1056);
      }

      protected void drawValueText(class_332 graphics, int mouseX, int mouseY, float delta) {
         class_327 var10001;
         int var10003;
         float var10004;
         if (this.awaitingControllerInput) {
            var10001 = this.textRenderer;
            class_2561 var10002 = this.awaitingText;
            var10003 = (Integer)this.getDimension().xLimit() - this.textRenderer.method_27525(this.awaitingText) - this.getXPadding();
            var10004 = (float)(Integer)this.getDimension().centerY();
            Objects.requireNonNull(this.textRenderer);
            graphics.method_51439(var10001, var10002, var10003, (int)(var10004 - 9.0F / 2.0F), 16777215, true);
         } else {
            Input bind = (Input)((BindController)this.control).option().pendingValue();
            if (EmptyInput.equals(bind)) {
               return;
            }

            class_2561 text = Controlify.instance().inputFontMapper().getComponentFromBind(((BindController)this.control).controller.info().type().namespace(), bind);
            int width = this.textRenderer.method_27525(text);
            var10001 = this.textRenderer;
            var10003 = (Integer)this.getDimension().xLimit() - width - 1;
            var10004 = (float)(Integer)this.getDimension().centerY();
            Objects.requireNonNull(this.textRenderer);
            graphics.method_51439(var10001, text, var10003, (int)(var10004 - 9.0F / 2.0F + 1.0F), -1, false);
         }

      }

      public boolean method_25404(int keyCode, int scanCode, int modifiers) {
         if (this.method_25370() && keyCode == 257) {
            this.openConsumerScreen();
            return true;
         } else {
            return false;
         }
      }

      public boolean method_25402(double mouseX, double mouseY, int button) {
         if (this.getDimension().isPointInside((int)mouseX, (int)mouseY)) {
            this.openConsumerScreen();
            return true;
         } else {
            return false;
         }
      }

      private void openConsumerScreen() {
         this.awaitingControllerInput = true;
         class_310.method_1551().method_1507(new BindConsumerScreen(this::getPressedBind, ((BindController)this.control).option(), this, class_310.method_1551().field_1755));
      }

      public boolean overrideControllerButtons(ScreenProcessor<?> screen, ControllerEntity controller) {
         if (controller != ((BindController)this.control).controller) {
            return true;
         } else if (ControlifyBindings.GUI_PRESS.on(controller).justPressed()) {
            this.openConsumerScreen();
            return true;
         } else {
            return false;
         }
      }

      protected int getHoveredControlWidth() {
         return this.getUnhoveredControlWidth();
      }

      protected int getUnhoveredControlWidth() {
         if (this.awaitingControllerInput) {
            return this.textRenderer.method_27525(this.awaitingText);
         } else {
            class_2561 text = Controlify.instance().inputFontMapper().getComponentFromBind(((BindController)this.control).controller.info().type().namespace(), (Input)((BindController)this.control).option().pendingValue());
            return this.textRenderer.method_27525(text);
         }
      }

      protected int getValueColor() {
         return ((BindController)this.control).conflicting ? 16733525 : super.getValueColor();
      }

      public Optional<Input> getPressedBind() {
         InputComponent input = (InputComponent)((BindController)this.control).controller.input().orElseThrow();
         ControllerStateView state = input.stateNow();
         ControllerStateView prevState = input.stateThen();
         Iterator var4 = state.getButtons().iterator();

         class_2960 hat;
         do {
            if (!var4.hasNext()) {
               var4 = state.getAxes().iterator();

               do {
                  if (!var4.hasNext()) {
                     var4 = state.getHats().iterator();

                     HatState hatState;
                     do {
                        if (!var4.hasNext()) {
                           return Optional.empty();
                        }

                        hat = (class_2960)var4.next();
                        hatState = state.getHatState(hat);
                     } while(hatState == HatState.CENTERED || prevState.getHatState(hat) != HatState.CENTERED);

                     return Optional.of(new HatInput(hat, hatState));
                  }

                  hat = (class_2960)var4.next();
               } while(!(state.getAxisState(hat) > 0.5F) || !(prevState.getAxisState(hat) <= 0.5F));

               return Optional.of(new AxisInput(hat));
            }

            hat = (class_2960)var4.next();
         } while(!state.isButtonDown(hat) || prevState.isButtonDown(hat));

         return Optional.of(new ButtonInput(hat));
      }
   }
}
