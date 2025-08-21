package dev.isxander.controlify.mixins.feature.rumble.explosion;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.rumble.BasicRumbleEffect;
import dev.isxander.controlify.rumble.RumbleSource;
import dev.isxander.controlify.rumble.RumbleState;
import dev.isxander.controlify.utils.Easings;
import net.minecraft.class_2664;
import net.minecraft.class_310;
import net.minecraft.class_634;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_634.class})
public class ClientPacketListenerMixin {
   @Inject(
      method = {"method_11124(Lnet/minecraft/class_2664;)V"},
      at = {@At("RETURN")}
   )
   private void onClientExplosion(class_2664 packet, CallbackInfo ci) {
      float initialMagnitude = this.calculateMagnitude(packet);
      ControlifyApi.get().getCurrentController().flatMap(ControllerEntity::rumble).ifPresent((rumble) -> {
         rumble.rumbleManager().play(RumbleSource.WORLD, BasicRumbleEffect.join(BasicRumbleEffect.constant(initialMagnitude, initialMagnitude, 4), BasicRumbleEffect.byTime((t) -> {
            float magnitude = this.calculateMagnitude(packet);
            return new RumbleState(0.0F, magnitude - t * magnitude);
         }, 20)));
      });
   }

   private float calculateMagnitude(class_2664 packet) {
      double x = packet.comp_2883().method_10216();
      double y = packet.comp_2883().method_10214();
      double z = packet.comp_2883().method_10215();
      float power = 50.0F;
      float distanceSqr = Math.max((float)class_310.method_1551().field_1724.method_5649(x, y, z) - power * power, 0.0F);
      float maxDistanceSqr = 4096.0F;
      return 1.0F - (float)Easings.easeOutQuad((double)(distanceSqr / maxDistanceSqr));
   }
}
