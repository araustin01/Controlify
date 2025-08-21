package dev.isxander.controlify.mixins.feature.bind;

import dev.isxander.controlify.bindings.KeyMappingHandle;
import dev.isxander.controlify.controller.ControllerEntity;
import java.util.function.BooleanSupplier;
import net.minecraft.class_304;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_304.class})
public class KeyMappingMixin implements KeyMappingHandle {
   @Shadow
   private int field_1661;
   @Shadow
   private boolean field_1653;

   public void controlify$setPressed(boolean isDown) {
      if (isDown) {
         this.field_1653 = true;
         ++this.field_1661;
      } else {
         this.field_1653 = false;
      }

   }

   public void controlify$addToggleCondition(ControllerEntity controller, BooleanSupplier condition) {
   }

   @Unique
   protected void incClickCount() {
      ++this.field_1661;
   }
}
