package dev.isxander.controlify.mixins.feature.patches.boatfix;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.fixes.boatfix.AnalogBoatInput;
import net.minecraft.class_10255;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_10255.class})
public abstract class BoatMixin implements AnalogBoatInput {
   @Shadow
   private float field_54456;
   @Shadow
   private boolean field_54444;
   @Shadow
   private boolean field_54428;
   @Shadow
   private boolean field_54429;
   @Shadow
   private boolean field_54430;
   @Unique
   private float analogForward;
   @Unique
   private float analogRight;
   @Unique
   private boolean usingAnalogInput;
   @Unique
   private static final String TARGET_CLASS = "Lnet/minecraft/world/entity/vehicle/AbstractBoat";

   @Inject(
      method = {"method_64482()V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_10255;method_36454()F",
   ordinal = 0
)}
   )
   private void rotateBoatAnalog(CallbackInfo ci) {
      if (this.usingAnalogInput) {
         this.field_54456 += this.analogRight;
      }

   }

   @ModifyVariable(
      method = {"method_64482()V"},
      at = @At(
   value = "STORE",
   ordinal = 0
),
      ordinal = 0
   )
   private float forwardBoatAnalog(float forwardVelocity) {
      if (!this.usingAnalogInput) {
         return forwardVelocity;
      } else {
         float velocity = this.analogForward > 0.0F ? this.analogForward * 0.04F : this.analogForward * 0.005F;
         return forwardVelocity + velocity;
      }
   }

   @ModifyExpressionValue(
      method = {"method_64482()V"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/class_10255;field_54444:Z",
   opcode = 180,
   ordinal = 0
), @At(
   value = "FIELD",
   target = "Lnet/minecraft/class_10255;field_54428:Z",
   opcode = 180,
   ordinal = 0
), @At(
   value = "FIELD",
   target = "Lnet/minecraft/class_10255;field_54429:Z",
   opcode = 180,
   ordinal = 1
), @At(
   value = "FIELD",
   target = "Lnet/minecraft/class_10255;field_54430:Z",
   opcode = 180,
   ordinal = 1
)}
   )
   private boolean shouldDoDigitalInput(boolean original) {
      return !this.usingAnalogInput && original;
   }

   public void controlify$setAnalogInput(float forward, float right) {
      this.usingAnalogInput = true;
      this.analogForward = forward;
      this.analogRight = right;
      this.field_54444 = right < 0.0F;
      this.field_54428 = right > 0.0F;
      this.field_54429 = forward > 0.0F;
      this.field_54430 = forward < 0.0F;
   }

   @Inject(
      method = {"method_64487(ZZZZ)V"},
      at = {@At("HEAD")}
   )
   private void onUseDigitalInput(boolean pressingLeft, boolean pressingRight, boolean pressingForward, boolean pressingBack, CallbackInfo ci) {
      this.usingAnalogInput = false;
   }
}
