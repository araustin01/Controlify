package dev.isxander.controlify.ingame;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.GenericControllerConfig;
import dev.isxander.controlify.controller.input.InputComponent;
import net.minecraft.class_10185;
import net.minecraft.class_241;
import net.minecraft.class_310;
import net.minecraft.class_743;
import net.minecraft.class_744;
import net.minecraft.class_746;
import org.jetbrains.annotations.Nullable;

public class ControllerPlayerMovement extends class_744 {
   private final ControllerEntity controller;
   private final class_746 player;
   private boolean wasFlying;
   private boolean wasPassenger;

   public ControllerPlayerMovement(ControllerEntity controller, class_746 player) {
      this.controller = controller;
      this.player = player;
   }

   public void method_3129() {
      if (class_310.method_1551().field_1755 == null && this.player != null) {
         float forwardImpulse = ControlifyBindings.WALK_FORWARD.on(this.controller).analogueNow() - ControlifyBindings.WALK_BACKWARD.on(this.controller).analogueNow();
         float leftImpulse = ControlifyBindings.WALK_LEFT.on(this.controller).analogueNow() - ControlifyBindings.WALK_RIGHT.on(this.controller).analogueNow();
         if (Controlify.instance().config().globalSettings().shouldUseKeyboardMovement()) {
            float threshold = ((InputComponent.Config)((InputComponent)this.controller.input().orElseThrow()).confObj()).buttonActivationThreshold;
            forwardImpulse = Math.abs(forwardImpulse) >= threshold ? Math.copySign(1.0F, forwardImpulse) : 0.0F;
            leftImpulse = Math.abs(leftImpulse) >= threshold ? Math.copySign(1.0F, leftImpulse) : 0.0F;
         }

         boolean shiftKeyDown = this.field_54155.comp_3164();
         boolean jumping = this.field_54155.comp_3163();
         boolean up = forwardImpulse > 0.0F;
         boolean down = forwardImpulse < 0.0F;
         boolean left = leftImpulse > 0.0F;
         boolean right = leftImpulse < 0.0F;
         this.setMoveVec(forwardImpulse, leftImpulse);
         InputBinding jump = ControlifyBindings.JUMP.on(this.controller);
         if (jump.justPressed()) {
            jumping = true;
         }

         if (!jump.digitalNow()) {
            jumping = false;
         }

         InputBinding sneak = ControlifyBindings.SNEAK.on(this.controller);
         if (!this.player.method_31549().field_7479 && (!this.player.method_5799() || this.player.method_24828()) && this.player.method_5854() == null && ((GenericControllerConfig)this.controller.genericConfig().config()).toggleSneak) {
            if (sneak.justPressed()) {
               shiftKeyDown = !shiftKeyDown;
            }
         } else {
            if (sneak.justPressed()) {
               shiftKeyDown = true;
            }

            if (!sneak.digitalNow()) {
               shiftKeyDown = false;
            }
         }

         if (!this.player.method_31549().field_7479 && this.wasFlying && this.player.method_24828() || !this.player.method_5765() && this.wasPassenger) {
            shiftKeyDown = false;
         }

         boolean sprinting = ControlifyBindings.SPRINT.on(this.controller).digitalNow();
         this.field_54155 = new class_10185(up, down, left, right, jumping, shiftKeyDown, sprinting);
         this.wasFlying = this.player.method_31549().field_7479;
         this.wasPassenger = this.player.method_5765();
      } else {
         this.setMoveVec(0.0F, 0.0F);
         this.field_54155 = class_10185.field_54098;
      }
   }

   private void setMoveVec(float forward, float left) {
      this.field_55868 = new class_241(left, forward);
      float length = this.field_55868.method_35584();
      if (length > 1.0F) {
         this.field_55868 = this.field_55868.method_35582(1.0F / length);
      }

   }

   public static void updatePlayerInput(@Nullable class_746 player) {
      if (player != null) {
         if (shouldBeControllerInput()) {
            player.field_3913 = new DualInput(new class_743(class_310.method_1551().field_1690), new ControllerPlayerMovement((ControllerEntity)Controlify.instance().getCurrentController().get(), player));
         } else if (!(player.field_3913 instanceof class_743)) {
            player.field_3913 = new class_743(class_310.method_1551().field_1690);
         }

      }
   }

   public static void ensureCorrectInput(@Nullable class_746 player) {
      if (player != null) {
         if (shouldBeControllerInput() && player.field_3913.getClass() == class_743.class) {
            updatePlayerInput(player);
         }

      }
   }

   public static boolean shouldBeControllerInput() {
      return Controlify.instance().getCurrentController().isPresent() && Controlify.instance().currentInputMode().isController();
   }
}
