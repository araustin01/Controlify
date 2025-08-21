package dev.isxander.controlify.mixins.feature.rumble.blockbreak;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.rumble.ContinuousRumbleEffect;
import dev.isxander.controlify.rumble.RumbleSource;
import dev.isxander.controlify.rumble.RumbleState;
import dev.isxander.controlify.utils.Easings;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_2596;
import net.minecraft.class_2680;
import net.minecraft.class_636;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({class_636.class})
public class MultiPlayerGameModeMixin {
   @Unique
   private ContinuousRumbleEffect blockBreakRumble = null;

   @Inject(
      method = {"method_41930(Lnet/minecraft/class_2680;Lnet/minecraft/class_2338;Lnet/minecraft/class_2350;I)Lnet/minecraft/class_2596;"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_638;method_8517(ILnet/minecraft/class_2338;I)V"
)}
   )
   private void onStartBreakingBlock(CallbackInfoReturnable<class_2596<?>> cir, @Local(argsOnly = true) class_2680 state) {
      this.startRumble(state);
   }

   @Inject(
      method = {"method_41930(Lnet/minecraft/class_2680;Lnet/minecraft/class_2338;Lnet/minecraft/class_2350;I)Lnet/minecraft/class_2596;"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_636;method_2899(Lnet/minecraft/class_2338;)Z"
)}
   )
   private void onInstabreakBlockSurvival(CallbackInfoReturnable<class_2596<?>> cir, @Local(argsOnly = true) class_2680 state) {
      this.startRumble(state);
      this.stopRumble();
   }

   @Inject(
      method = {"method_2925()V"},
      at = {@At("RETURN")}
   )
   private void onStopBreakingBlock(CallbackInfo ci) {
      this.stopRumble();
   }

   @Inject(
      method = {"method_2902(Lnet/minecraft/class_2338;Lnet/minecraft/class_2350;)Z"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_636;method_41931(Lnet/minecraft/class_638;Lnet/minecraft/class_7204;)V",
   ordinal = 1
)}
   )
   private void onFinishBreakingBlock(class_2338 pos, class_2350 direction, CallbackInfoReturnable<Boolean> cir) {
      this.stopRumble();
   }

   @ModifyExpressionValue(
      method = {"method_2902(Lnet/minecraft/class_2338;Lnet/minecraft/class_2350;)Z"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_2680;method_26215()Z"
)}
   )
   private boolean onAbortBreakingBlock(boolean original) {
      if (original) {
         this.stopRumble();
      }

      return original;
   }

   @Unique
   private void startRumble(class_2680 state) {
      this.stopRumble();
      ContinuousRumbleEffect effect = ContinuousRumbleEffect.builder().byTick((tick) -> {
         return new RumbleState(0.02F + (float)Easings.easeInQuad((double)Math.min(1.0F, state.method_26204().method_36555() / 20.0F)) * 0.25F, 0.01F);
      }).minTime(1).build();
      this.blockBreakRumble = effect;
      ControlifyApi.get().getCurrentController().flatMap(ControllerEntity::rumble).ifPresent((rumble) -> {
         rumble.rumbleManager().play(RumbleSource.INTERACTION, effect);
      });
   }

   @Unique
   private void stopRumble() {
      if (this.blockBreakRumble != null) {
         this.blockBreakRumble.stop();
         this.blockBreakRumble = null;
      }

   }
}
