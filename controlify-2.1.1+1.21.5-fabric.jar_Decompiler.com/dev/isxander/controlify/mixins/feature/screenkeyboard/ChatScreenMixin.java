package dev.isxander.controlify.mixins.feature.screenkeyboard;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.controller.GenericControllerConfig;
import dev.isxander.controlify.controller.keyboard.NativeKeyboardComponent;
import dev.isxander.controlify.screenkeyboard.ChatKeyboardDucky;
import dev.isxander.controlify.screenkeyboard.ChatKeyboardWidget;
import dev.isxander.controlify.screenkeyboard.KeyPressConsumer;
import java.util.Optional;
import net.minecraft.class_2561;
import net.minecraft.class_342;
import net.minecraft.class_408;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_408.class})
public abstract class ChatScreenMixin extends class_437 implements ChatKeyboardDucky {
   @Unique
   private ChatKeyboardWidget keyboard;
   @Unique
   private float shiftChatAmt = 0.0F;
   @Shadow
   protected class_342 field_2382;

   @Shadow
   public abstract boolean method_25404(int var1, int var2, int var3);

   protected ChatScreenMixin(class_2561 title) {
      super(title);
   }

   @Inject(
      method = {"method_25426()V"},
      at = {@At("HEAD")}
   )
   private void addKeyboard(CallbackInfo ci) {
      ControlifyApi.get().getCurrentController().ifPresent((c) -> {
         if (ControlifyApi.get().currentInputMode().isController()) {
            if (((GenericControllerConfig)c.genericConfig().config()).showOnScreenKeyboard) {
               Optional<NativeKeyboardComponent> nativeKeyboardOpt = c.nativeKeyboard();
               if (nativeKeyboardOpt.isPresent() && ((NativeKeyboardComponent.Config)((NativeKeyboardComponent)nativeKeyboardOpt.get()).confObj()).useNativeKeyboard) {
                  NativeKeyboardComponent nativeKeyboard = (NativeKeyboardComponent)nativeKeyboardOpt.get();
                  this.shiftChatAmt = nativeKeyboard.getKeyboardHeight();
                  nativeKeyboard.open();
               } else {
                  this.shiftChatAmt = 0.5F;
                  int keyboardHeight = (int)((float)this.field_22790 * this.shiftChatAmt);
                  this.method_37063(this.keyboard = new ChatKeyboardWidget((class_408)this, 0, this.field_22790 - keyboardHeight, this.field_22789, keyboardHeight, KeyPressConsumer.of((keycode, scancode, modifiers) -> {
                     this.field_2382.method_25404(keycode, scancode, modifiers);
                     this.method_25404(keycode, scancode, modifiers);
                  }, (codePoint, modifiers) -> {
                     this.method_25400(codePoint, modifiers);
                     this.field_2382.method_25400(codePoint, modifiers);
                  })));
               }

            }
         }
      });
   }

   @ModifyArg(
      method = {"method_25426()V"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_408$1;<init>(Lnet/minecraft/class_408;Lnet/minecraft/class_327;IIIILnet/minecraft/class_2561;)V"
),
      index = 3
   )
   private int modifyInputBoxY(int y) {
      return (int)((float)y - (float)this.field_22790 * this.shiftChatAmt);
   }

   @ModifyArg(
      method = {"method_25394(Lnet/minecraft/class_332;IIF)V"},
      at = @At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_332;method_25294(IIIII)V"
),
      index = 1
   )
   private int modifyInputBoxBackgroundY(int y) {
      return (int)((float)y - (float)this.field_22790 * this.shiftChatAmt);
   }

   @ModifyExpressionValue(
      method = {"method_25426()V"},
      at = {@At(
   value = "CONSTANT",
   args = {"intValue=10"}
)}
   )
   private int modifyMaxSuggestionCount(int count) {
      return this.shiftChatAmt > 0.0F ? 8 : count;
   }

   public float controlify$keyboardShiftAmount() {
      return this.shiftChatAmt;
   }
}
