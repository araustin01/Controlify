package dev.isxander.controlify.mixins.core;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.controllermanager.ControllerManager;
import dev.isxander.controlify.utils.InitialScreenRegistryDuck;
import dev.isxander.controlify.utils.MouseMinecraftCallNotifier;
import dev.isxander.controlify.utils.animation.impl.Animator;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import net.minecraft.class_128;
import net.minecraft.class_310;
import net.minecraft.class_312;
import net.minecraft.class_437;
import net.minecraft.class_9779;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_310.class})
public abstract class MinecraftMixin implements InitialScreenRegistryDuck {
   @Shadow
   @Final
   public class_312 field_1729;
   @Shadow
   @Nullable
   public class_437 field_1755;
   @Unique
   private final List<Function<Runnable, class_437>> initialScreenCallbacks = new ArrayList();
   @Unique
   private boolean initialScreensHappened = false;

   @Shadow
   public abstract void method_1507(@Nullable class_437 var1);

   @Shadow
   public abstract class_9779 method_61966();

   @Shadow
   public abstract void method_54580(class_128 var1);

   @Inject(
      method = {"method_1507(Lnet/minecraft/class_437;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_312;method_1610()V"
)}
   )
   private void notifyInjectionToNotRun(class_437 screen, CallbackInfo ci) {
      ((MouseMinecraftCallNotifier)this.field_1729).imFromMinecraftSetScreen();
   }

   @Inject(
      method = {"method_1507(Lnet/minecraft/class_437;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_437;method_25423(Lnet/minecraft/class_310;II)V",
   shift = Shift.AFTER
)}
   )
   private void hideMouseAfterRelease(class_437 screen, CallbackInfo ci) {
      if (ControlifyApi.get().currentInputMode().isController()) {
         Controlify.instance().hideMouse(true, true);
      }

   }

   @Inject(
      method = {"method_51736(Lnet/minecraft/class_310$class_8764;)V"},
      at = {@At("RETURN")}
   )
   private void initControlifyNow(CallbackInfo ci) {
      try {
         Controlify.instance().initializeControlify();
      } catch (Throwable var4) {
         class_128 report = class_128.method_560(var4, "Failed to initialize Controlify");
         this.method_54580(report);
      }

   }

   @Inject(
      method = {"method_1523(Z)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_312;method_55793()V"
)}
   )
   private void doPlayerLook(boolean tick, CallbackInfo ci) {
      Controlify.instance().inGameInputHandler().ifPresent((ih) -> {
         ih.processPlayerLook(this.getTickDelta());
      });
   }

   @Inject(
      method = {"close()V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_6628;close()V"
)}
   )
   private void onMinecraftClose(CallbackInfo ci) {
      Controlify.instance().getControllerManager().ifPresent(ControllerManager::close);
   }

   @Inject(
      method = {"method_1523(Z)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_757;method_3192(Lnet/minecraft/class_9779;Z)V"
)}
   )
   private void tickAnimator(boolean tick, CallbackInfo ci) {
      Animator.INSTANCE.tick(this.getTickDelta());
   }

   @ModifyVariable(
      method = {"method_53525(Ljava/util/List;)V"},
      at = @At("TAIL"),
      argsOnly = true
   )
   private List<Function<Runnable, class_437>> injectCustomInitialScreens(List<Function<Runnable, class_437>> output) {
      output.addAll(this.initialScreenCallbacks);
      this.initialScreensHappened = true;
      return output;
   }

   @Unique
   private float getTickDelta() {
      return this.method_61966().method_60636();
   }

   public void controlify$registerInitialScreen(Function<Runnable, class_437> screenFactory) {
      if (this.initialScreensHappened) {
         class_437 lastScreen = this.field_1755;
         this.method_1507((class_437)screenFactory.apply(() -> {
            this.method_1507(lastScreen);
         }));
      } else {
         this.initialScreenCallbacks.add(screenFactory);
      }

   }
}
