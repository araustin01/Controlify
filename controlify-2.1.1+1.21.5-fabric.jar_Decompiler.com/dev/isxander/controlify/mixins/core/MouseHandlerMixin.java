package dev.isxander.controlify.mixins.core;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.InputMode;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.utils.MouseMinecraftCallNotifier;
import net.minecraft.class_310;
import net.minecraft.class_312;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_312.class})
public class MouseHandlerMixin implements MouseMinecraftCallNotifier {
   @Shadow
   @Final
   private class_310 field_1779;
   @Unique
   private boolean controlify$calledFromMinecraftSetScreen = false;

   @Inject(
      method = {"method_22686(JIII)V", "/lambda\\$setup\\$\\d+/"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_312;method_1601(JIII)V"
)}
   )
   private void onMouseClickInput(long window, int button, int action, int modifiers, CallbackInfo ci) {
      this.onMouse(window);
   }

   @Inject(
      method = {"method_22689(JDD)V", "/lambda\\$setup\\$\\d+/"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_312;method_1600(JDD)V"
)}
   )
   private void onMouseMoveInput(long window, double x, double y, CallbackInfo ci) {
      this.onMouse(window);
   }

   @Inject(
      method = {"method_22687(JDD)V", "/lambda\\$setup\\$\\d+/"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_312;method_1598(JDD)V"
)}
   )
   private void onMouseScrollInput(long window, double scrollDeltaX, double scrollDeltaY, CallbackInfo ci) {
      this.onMouse(window);
   }

   @Unique
   private void onMouse(long window) {
      if (window == this.field_1779.method_22683().method_4490()) {
         if (Controlify.instance().currentInputMode() != InputMode.MIXED) {
            Controlify.instance().setInputMode(InputMode.KEYBOARD_MOUSE);
         } else {
            Controlify.instance().showCursorTemporarily();
         }
      }

   }

   @Inject(
      method = {"method_1610()V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_3675;method_15984(JIDD)V"
)}
   )
   private void moveMouseIfNecessary(CallbackInfo ci) {
      if (!this.controlify$calledFromMinecraftSetScreen && ControlifyApi.get().currentInputMode().isController()) {
         Controlify.instance().hideMouse(true, true);
      }

   }

   @Inject(
      method = {"method_1610()V"},
      at = {@At("RETURN")}
   )
   private void resetCalledFromMinecraftSetScreen(CallbackInfo ci) {
      this.controlify$calledFromMinecraftSetScreen = false;
   }

   @ModifyExpressionValue(
      method = {"method_1612()V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_310;method_1569()Z"
)}
   )
   private boolean passWindowActiveCheckIfOOFInputIsOn(boolean isWindowActive) {
      return isWindowActive || ControlifyApi.get().currentInputMode().isController() && Controlify.instance().config().globalSettings().outOfFocusInput;
   }

   public void imFromMinecraftSetScreen() {
      this.controlify$calledFromMinecraftSetScreen = true;
   }
}
