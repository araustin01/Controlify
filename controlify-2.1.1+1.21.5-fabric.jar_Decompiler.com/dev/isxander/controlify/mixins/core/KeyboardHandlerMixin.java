package dev.isxander.controlify.mixins.core;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.InputMode;
import net.minecraft.class_309;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_309.class})
public class KeyboardHandlerMixin {
   @Shadow
   @Final
   private class_310 field_1678;

   @Inject(
      method = {"method_22678(JIIII)V", "/lambda\\$setup\\$\\d+/"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_309;method_1466(JIIII)V"
)}
   )
   private void onKeyboardInput(long window, int i, int j, int k, int m, CallbackInfo ci) {
      if (window == this.field_1678.method_22683().method_4490() && Controlify.instance().currentInputMode() != InputMode.MIXED) {
         Controlify.instance().setInputMode(InputMode.KEYBOARD_MOUSE);
      }

   }

   @Inject(
      method = {"method_22677(JII)V", "/lambda\\$setup\\$\\d+/"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_309;method_1457(JII)V"
)}
   )
   private void onCharInput(long window, int codePoint, int modifiers, CallbackInfo ci) {
      if (window == this.field_1678.method_22683().method_4490() && Controlify.instance().currentInputMode() != InputMode.MIXED) {
         Controlify.instance().setInputMode(InputMode.KEYBOARD_MOUSE);
      }

   }
}
