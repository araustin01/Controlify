package dev.isxander.controlify.mixins.feature.rumble.slowblock;

import com.mojang.authlib.GameProfile;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.rumble.ContinuousRumbleEffect;
import dev.isxander.controlify.rumble.RumbleSource;
import dev.isxander.controlify.rumble.RumbleState;
import net.minecraft.class_638;
import net.minecraft.class_742;
import net.minecraft.class_744;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_746.class})
public abstract class LocalPlayerMixin extends class_742 {
   @Shadow
   public class_744 field_3913;
   @Unique
   private ContinuousRumbleEffect slowBlockRumble = null;

   @Shadow
   protected abstract boolean method_22120();

   public LocalPlayerMixin(class_638 world, GameProfile profile) {
      super(world, profile);
   }

   @Inject(
      method = {"method_6007()V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_744;method_3129()V"
)}
   )
   private void manageSlowBlockRumble(CallbackInfo ci) {
      float speed = this.method_23326();
      if (speed < 1.0F && this.method_22120()) {
         this.ensureRumbleStarted();
      } else {
         this.ensureRumbleStopped();
      }

   }

   @Unique
   private void ensureRumbleStarted() {
      if (this.slowBlockRumble != null && !this.slowBlockRumble.isFinished()) {
         this.slowBlockRumble.heartbeat();
      } else {
         this.slowBlockRumble = ContinuousRumbleEffect.builder().byTick((i) -> {
            float movementAmount = this.field_3913.method_3128().method_35584();
            return new RumbleState(0.3F * movementAmount, 0.5F * movementAmount);
         }).timeout(100).build();
         ControlifyApi.get().getCurrentController().flatMap(ControllerEntity::rumble).ifPresent((rumble) -> {
            rumble.rumbleManager().play(RumbleSource.PLAYER, this.slowBlockRumble);
         });
      }

   }

   @Unique
   private void ensureRumbleStopped() {
      if (this.slowBlockRumble != null && !this.slowBlockRumble.isFinished()) {
         this.slowBlockRumble.stop();
      }

   }
}
