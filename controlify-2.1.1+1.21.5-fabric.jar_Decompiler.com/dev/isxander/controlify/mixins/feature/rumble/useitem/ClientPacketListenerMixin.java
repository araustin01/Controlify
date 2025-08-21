package dev.isxander.controlify.mixins.feature.rumble.useitem;

import dev.isxander.controlify.rumble.ContinuousRumbleEffect;
import dev.isxander.controlify.rumble.effects.UseItemEffectHolder;
import net.minecraft.class_2724;
import net.minecraft.class_310;
import net.minecraft.class_634;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_634.class})
public class ClientPacketListenerMixin {
   @Inject(
      method = {"method_11117(Lnet/minecraft/class_2724;)V"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/class_310;field_1724:Lnet/minecraft/class_746;",
   opcode = 181
)}
   )
   private void clearUseItemRumble(class_2724 packet, CallbackInfo ci) {
      ContinuousRumbleEffect effect = ((UseItemEffectHolder)class_310.method_1551().field_1724).controlify$getUseItemEffect();
      if (effect != null) {
         effect.stop();
      }

   }
}
