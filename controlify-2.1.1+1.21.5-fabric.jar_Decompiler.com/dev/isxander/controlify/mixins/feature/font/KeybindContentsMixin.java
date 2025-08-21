package dev.isxander.controlify.mixins.feature.font;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.font.BindingFontHelper;
import java.util.Optional;
import net.minecraft.class_2561;
import net.minecraft.class_2572;
import net.minecraft.class_2583;
import net.minecraft.class_2960;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_2572.class})
public class KeybindContentsMixin {
   @Shadow
   @Final
   private String field_11767;

   @WrapOperation(
      method = {"method_27660(Lnet/minecraft/class_5348$class_5246;Lnet/minecraft/class_2583;)Ljava/util/Optional;"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_2572;method_27691()Lnet/minecraft/class_2561;"
)}
   )
   private class_2561 testVisitWithStyle(class_2572 instance, Operation<class_2561> original, @Local(argsOnly = true) class_2583 style) {
      boolean wrapperFont = BindingFontHelper.WRAPPER_FONT.equals(style.method_27708());
      if (wrapperFont) {
         Optional<class_2561> inputText = ControlifyApi.get().getCurrentController().filter((c) -> {
            return c.input().isPresent();
         }).map((c) -> {
            return Controlify.instance().inputFontMapper().getComponentFromBinding(c.info().type().namespace(), ((InputComponent)c.input().get()).getBinding(class_2960.method_12829(this.field_11767)));
         });
         if (inputText.isPresent()) {
            return (class_2561)inputText.get();
         }
      }

      return (class_2561)original.call(new Object[]{instance});
   }
}
