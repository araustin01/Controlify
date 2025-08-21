package dev.isxander.controlify.mixins.feature.guide.ingame;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.gui.guide.InGameButtonGuide;
import net.minecraft.class_310;
import net.minecraft.class_329;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_329.class})
public class GuiMixin {
   @Shadow
   @Final
   private class_310 field_2035;

   @Inject(
      method = {"method_1748()V"},
      at = {@At("RETURN")}
   )
   private void tickButtonGuide(CallbackInfo ci) {
      if (this.field_2035.field_1687 != null) {
         Controlify.instance().inGameButtonGuide().ifPresent(InGameButtonGuide::tick);
      }
   }
}
