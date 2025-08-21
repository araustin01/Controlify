package dev.isxander.controlify.mixins.feature.rumble.fishing;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.rumble.ContinuousRumbleEffect;
import dev.isxander.controlify.rumble.RumbleSource;
import net.minecraft.class_1297;
import net.minecraft.class_1536;
import net.minecraft.class_746;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1536.class})
public class FishingHookMixin {
   @Shadow
   private boolean field_23232;
   @Unique
   private boolean isLocalPlayerHook;
   @Unique
   private ContinuousRumbleEffect bitingRumble;

   @ModifyExpressionValue(
      method = {"method_5674(Lnet/minecraft/class_2940;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_2945;method_12789(Lnet/minecraft/class_2940;)Ljava/lang/Object;",
   ordinal = 1
)}
   )
   private Object onBitingStateUpdated(Object bitingObj) {
      boolean biting = (Boolean)bitingObj;
      if (this.isLocalPlayerHook) {
         if (biting && !this.field_23232) {
            ControlifyApi.get().getCurrentController().flatMap(ControllerEntity::rumble).ifPresent((controller) -> {
               this.bitingRumble = ContinuousRumbleEffect.builder().constant(0.0F, 0.05F).build();
               controller.rumbleManager().play(RumbleSource.INTERACTION, this.bitingRumble);
            });
         } else if (!biting && this.field_23232) {
            this.stopBitingRumble();
         }
      }

      return biting;
   }

   @Inject(
      method = {"method_36209()V"},
      at = {@At("RETURN")}
   )
   private void onClientRemoval(CallbackInfo ci) {
      this.stopBitingRumble();
   }

   @Unique
   private void stopBitingRumble() {
      if (this.bitingRumble != null) {
         this.bitingRumble.stop();
         this.bitingRumble = null;
      }

   }

   @Inject(
      method = {"method_7432(Lnet/minecraft/class_1297;)V"},
      at = {@At("RETURN")}
   )
   private void onOwnerSet(@Nullable class_1297 entity, CallbackInfo ci) {
      this.isLocalPlayerHook = entity instanceof class_746;
   }
}
