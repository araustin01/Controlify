package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.api.buttonguide.ButtonGuideApi;
import dev.isxander.controlify.api.buttonguide.ButtonGuidePredicate;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import dev.isxander.controlify.screenop.compat.vanilla.SelectWorldScreenProcessor;
import net.minecraft.class_4185;
import net.minecraft.class_526;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin({class_526.class})
public class SelectWorldScreenMixin implements ScreenProcessorProvider {
   @Unique
   private final SelectWorldScreenProcessor processor = new SelectWorldScreenProcessor((class_526)this);

   public ScreenProcessor<?> screenProcessor() {
      return this.processor;
   }

   @ModifyExpressionValue(
      method = {"method_25426()V"},
      slice = {@Slice(
   from = @At(
   value = "FIELD",
   target = "Lnet/minecraft/class_5244;field_24339:Lnet/minecraft/class_2561;",
   opcode = 178,
   ordinal = 0
)
)},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_4185$class_7840;method_46431()Lnet/minecraft/class_4185;",
   ordinal = 0
)}
   )
   private class_4185 modifyCancelButton(class_4185 button) {
      ButtonGuideApi.addGuideToButton(button, (InputBindingSupplier)ControlifyBindings.GUI_BACK, ButtonGuidePredicate.always());
      return button;
   }

   @ModifyExpressionValue(
      method = {"method_25426()V"},
      slice = {@Slice(
   from = @At(
   value = "CONSTANT",
   args = {"stringValue=selectWorld.create"}
)
)},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_4185$class_7840;method_46431()Lnet/minecraft/class_4185;",
   ordinal = 0
)}
   )
   private class_4185 modifyCreateButton(class_4185 button) {
      ButtonGuideApi.addGuideToButton(button, (InputBindingSupplier)ControlifyBindings.GUI_ABSTRACT_ACTION_1, ButtonGuidePredicate.always());
      return button;
   }
}
