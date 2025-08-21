package dev.isxander.controlify.mixins.feature.screenop;

import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorFactory;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import net.minecraft.class_310;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_437.class})
public class ScreenMixin implements ScreenProcessorProvider {
   @Unique
   private final ScreenProcessor<? super class_437> controlify$processor = ScreenProcessorFactory.createForScreen((class_437)this);

   public ScreenProcessor<?> screenProcessor() {
      return this.controlify$processor;
   }

   @Inject(
      method = {"method_25423(Lnet/minecraft/class_310;II)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_437;method_25426()V",
   shift = Shift.AFTER
)}
   )
   private void onScreenInitialInit(class_310 client, int width, int height, CallbackInfo ci) {
      ScreenProcessorProvider.provide((class_437)this).onWidgetRebuild();
   }

   @Inject(
      method = {"method_41843()V"},
      at = {@At("RETURN")}
   )
   private void onScreenInit(CallbackInfo ci) {
      ScreenProcessorProvider.provide((class_437)this).onWidgetRebuild();
   }
}
