package dev.isxander.controlify.mixins.feature.rumble.levelevents;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.rumble.BasicRumbleEffect;
import dev.isxander.controlify.rumble.RumbleEffect;
import dev.isxander.controlify.rumble.RumbleSource;
import dev.isxander.controlify.rumble.RumbleState;
import dev.isxander.controlify.utils.Easings;
import net.minecraft.class_2338;
import net.minecraft.class_9959;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_9959.class})
public class LevelRendererMixin {
   @Inject(
      method = {"method_62194(ILnet/minecraft/class_2338;I)V"},
      at = {@At("HEAD")}
   )
   private void onLevelEvent(int eventId, class_2338 pos, int data, CallbackInfo ci) {
      switch(eventId) {
      case 1030:
         this.rumble(RumbleSource.GUI, BasicRumbleEffect.join(BasicRumbleEffect.constant(1.0F, 0.5F, 2), BasicRumbleEffect.empty(5)).repeat(3));
      default:
      }
   }

   @Inject(
      method = {"method_62187(ILnet/minecraft/class_2338;I)V"},
      at = {@At("HEAD")}
   )
   private void onGlobalLevelEvent(int eventId, class_2338 pos, int data, CallbackInfo ci) {
      switch(eventId) {
      case 1023:
         this.rumble(RumbleSource.WORLD, BasicRumbleEffect.join(BasicRumbleEffect.constant(1.0F, 1.0F, 9), BasicRumbleEffect.constant(0.1F, 1.0F, 14), BasicRumbleEffect.byTime((t) -> {
            float easeOutQuad = 1.0F - (1.0F - t) * (1.0F - t);
            return new RumbleState(0.0F, 1.0F - easeOutQuad);
         }, 56)).prioritised(10));
         break;
      case 1028:
         this.rumble(RumbleSource.WORLD, BasicRumbleEffect.join(BasicRumbleEffect.constant(1.0F, 1.0F, 194), BasicRumbleEffect.byTime((t) -> {
            float easeOutQuad = (float)Easings.easeOutQuad((double)t);
            return new RumbleState(1.0F - easeOutQuad, 1.0F - easeOutQuad);
         }, 63)).prioritised(10));
      }

   }

   @Unique
   private void rumble(RumbleSource source, RumbleEffect effect) {
      ControlifyApi.get().getCurrentController().flatMap(ControllerEntity::rumble).ifPresent((rumble) -> {
         rumble.rumbleManager().play(source, effect);
      });
   }
}
