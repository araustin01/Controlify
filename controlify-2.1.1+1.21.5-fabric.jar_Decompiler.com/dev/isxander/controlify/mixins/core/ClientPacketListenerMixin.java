package dev.isxander.controlify.mixins.core;

import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.controlify.ingame.ControllerPlayerMovement;
import net.minecraft.class_2678;
import net.minecraft.class_2724;
import net.minecraft.class_310;
import net.minecraft.class_634;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_634.class})
public class ClientPacketListenerMixin {
   @Unique
   private static final String inputFieldTarget = "Lnet/minecraft/client/player/LocalPlayer;input:Lnet/minecraft/client/player/ClientInput;";

   @Inject(
      method = {"method_11120(Lnet/minecraft/class_2678;)V"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/class_746;field_3913:Lnet/minecraft/class_744;",
   opcode = 58,
   shift = Shift.AFTER
)}
   )
   private void overrideNewPlayerInput(class_2678 packet, CallbackInfo ci) {
      ControllerPlayerMovement.updatePlayerInput(class_310.method_1551().field_1724);
   }

   @Inject(
      method = {"method_11117(Lnet/minecraft/class_2724;)V"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/class_746;field_3913:Lnet/minecraft/class_744;",
   opcode = 58,
   shift = Shift.AFTER
)}
   )
   private void overrideRespawnInput(class_2724 packet, CallbackInfo ci, @Local(ordinal = 1) class_746 newPlayer) {
      ControllerPlayerMovement.updatePlayerInput(newPlayer);
   }
}
