package dev.isxander.controlify.mixins.feature.guide.ingame;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.gui.guide.InGameButtonGuide;
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
   @Inject(
      method = {"method_11120(Lnet/minecraft/class_2678;)V"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/class_746;field_3913:Lnet/minecraft/class_744;",
   opcode = 58,
   shift = Shift.AFTER
)}
   )
   private void buttonGuideLogin(class_2678 packet, CallbackInfo ci) {
      this.initButtonGuide();
   }

   @Inject(
      method = {"method_11117(Lnet/minecraft/class_2724;)V"},
      at = {@At("RETURN")}
   )
   private void buttonGuideRespawn(class_2724 packet, CallbackInfo ci) {
      this.initButtonGuide();
   }

   @Unique
   private void initButtonGuide() {
      class_746 player = class_310.method_1551().field_1724;
      if (Controlify.instance().currentInputMode().isController() && player != null) {
         Controlify.instance().inGameButtonGuide = new InGameButtonGuide((ControllerEntity)Controlify.instance().getCurrentController().orElseThrow(), player);
      }

   }
}
