package dev.isxander.controlify.mixins.feature.nomousetutorial;

import dev.isxander.controlify.api.ControlifyApi;
import net.minecraft.class_1151;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1151.class})
public class MovementTutorialStepInstanceMixin {
   @Shadow
   private int field_5625;
   @Shadow
   private int field_5627;

   @Inject(
      method = {"<init>(Lnet/minecraft/class_1156;)V"},
      at = {@At("RETURN")}
   )
   private void stopLookTutorial(CallbackInfo ci) {
      if (ControlifyApi.get().currentInputMode().isController()) {
         this.field_5625 = 100;
         this.field_5627 = 41;
      }

   }
}
