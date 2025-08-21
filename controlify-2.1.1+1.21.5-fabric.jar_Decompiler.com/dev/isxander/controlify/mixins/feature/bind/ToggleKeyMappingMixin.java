package dev.isxander.controlify.mixins.feature.bind;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.bindings.KeyMappingHandle;
import dev.isxander.controlify.controller.ControllerEntity;
import java.util.function.BooleanSupplier;
import net.minecraft.class_4666;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_4666.class})
public abstract class ToggleKeyMappingMixin extends KeyMappingMixin implements KeyMappingHandle {
   @Unique
   private BooleanSupplier controlifyToggleSupplier = () -> {
      return false;
   };

   @Shadow
   public abstract void method_23481(boolean var1);

   public void controlify$setPressed(boolean isDown) {
      if (isDown) {
         this.incClickCount();
      }

      this.method_23481(isDown);
   }

   @ModifyExpressionValue(
      method = {"method_23481(Z)V"},
      at = {@At(
   value = "INVOKE",
   target = "Ljava/util/function/BooleanSupplier;getAsBoolean()Z"
)}
   )
   private boolean modifyToggleMode(boolean vanillaToggleMode) {
      return Controlify.instance().currentInputMode().isController() ? this.controlifyToggleSupplier.getAsBoolean() : vanillaToggleMode;
   }

   public void controlify$addToggleCondition(ControllerEntity controller, BooleanSupplier condition) {
      BooleanSupplier oldCondition = this.controlifyToggleSupplier;
      this.controlifyToggleSupplier = () -> {
         boolean thisToggle = condition.getAsBoolean() && Controlify.instance().currentInputMode().isController() && (Boolean)Controlify.instance().getCurrentController().map((current) -> {
            return controller == current;
         }).orElse(false);
         return oldCondition.getAsBoolean() || thisToggle;
      };
   }
}
