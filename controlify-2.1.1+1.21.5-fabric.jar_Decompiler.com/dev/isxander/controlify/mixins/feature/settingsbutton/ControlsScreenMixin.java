package dev.isxander.controlify.mixins.feature.settingsbutton;

import dev.isxander.controlify.gui.screen.ControllerCarouselScreen;
import net.minecraft.class_2561;
import net.minecraft.class_315;
import net.minecraft.class_339;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_458;
import net.minecraft.class_4667;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_458.class})
public abstract class ControlsScreenMixin extends class_4667 {
   public ControlsScreenMixin(class_437 parent, class_315 gameOptions, class_2561 title) {
      super(parent, gameOptions, title);
   }

   @Inject(
      method = {"method_60325()V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_353;method_20408([Lnet/minecraft/class_7172;)V",
   shift = Shift.AFTER
)}
   )
   private void addControllerSettings(CallbackInfo ci) {
      this.field_51824.method_20407(class_4185.method_46430(class_2561.method_43471("controlify.gui.button"), (btn) -> {
         this.openControllerSettings();
      }).method_46431(), (class_339)null);
   }

   @Unique
   private void openControllerSettings() {
      ControllerCarouselScreen.openConfigScreen(this);
   }
}
