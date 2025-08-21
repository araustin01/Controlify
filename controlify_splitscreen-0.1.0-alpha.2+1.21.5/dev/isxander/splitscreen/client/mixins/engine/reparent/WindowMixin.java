package dev.isxander.splitscreen.client.mixins.engine.reparent;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.splitscreen.client.ControllerBridge;
import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import dev.isxander.splitscreen.client.engine.impl.reparenting.ReparentingHostSplitscreenEngine;
import dev.isxander.splitscreen.client.engine.impl.reparenting.ReparentingSplitscreenEngine;
import dev.isxander.splitscreen.client.engine.impl.reparenting.events.VanillaWindowFocusEvent;
import dev.isxander.splitscreen.client.engine.impl.reparenting.parent.ParentWindow;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.class_10219;
import net.minecraft.class_1041;
import net.minecraft.class_319;
import net.minecraft.class_3678;
import org.lwjgl.glfw.GLFWImage.Buffer;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1041.class})
public class WindowMixin {
   @Shadow
   @Final
   private static Logger field_5178;
   @Shadow
   @Final
   private long field_5187;
   @Unique
   private boolean hasDoneInitialSetup = false;

   @Inject(
      method = {"<init>(Lnet/minecraft/class_3678;Lnet/minecraft/class_323;Lnet/minecraft/class_543;Ljava/lang/String;Ljava/lang/String;)V"},
      at = {@At("RETURN")}
   )
   private void markInitialSetup(CallbackInfo ci) {
      this.hasDoneInitialSetup = true;
   }

   @WrapOperation(
      method = {"method_4494(JZ)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_3678;method_15995(Z)V"
)}
   )
   private void transferFocusToController(class_3678 instance, boolean hasFocus, Operation<Void> original) {
      ((VanillaWindowFocusEvent)VanillaWindowFocusEvent.EVENT.invoker()).onFocus((class_1041)this, hasFocus);
   }

   @ModifyArg(
      method = {"method_4491(Lnet/minecraft/class_3262;Lnet/minecraft/class_8518;)V"},
      at = @At(
   value = "INVOKE",
   target = "Lorg/lwjgl/glfw/GLFW;glfwSetWindowIcon(JLorg/lwjgl/glfw/GLFWImage$Buffer;)V"
)
   )
   private Buffer propagateWindowIconToParent(Buffer iconBuffer) {
      SplitscreenBootstrapper.getController().flatMap(ReparentingHostSplitscreenEngine::tryGet).ifPresent((engine) -> {
         Optional.ofNullable(engine.getParentWindow()).ifPresent((win) -> {
            win.setIcon(iconBuffer);
         });
      });
      return iconBuffer;
   }

   @Inject(
      method = {"method_24286(Ljava/lang/String;)V"},
      at = {@At("HEAD")}
   )
   private void propagateTitleToParent(String title, CallbackInfo ci) {
      SplitscreenBootstrapper.getController().flatMap(ReparentingHostSplitscreenEngine::tryGet).ifPresent((engine) -> {
         Optional.ofNullable(engine.getParentWindow()).ifPresent((win) -> {
            win.setTitle(title);
         });
      });
   }

   @Inject(
      method = {"method_30132(JZ)V"},
      at = {@At("HEAD")}
   )
   private void giveSelfFocusIfForeground(long window, boolean cursorEntered, CallbackInfo ci) {
      if (window == this.field_5187 && cursorEntered) {
         SplitscreenBootstrapper.getControllerBridge().ifPresent(ControllerBridge::giveFocusToMeIfForeground);
      }

   }

   @WrapMethod(
      method = {"method_4479()V"}
   )
   private void preventModeChange(Operation<Void> original) {
      Objects.requireNonNull(original);
      this.preventIfSplitscreen(() -> {
         Void var10000 = (Void)original.call(new Object[0]);
      });
   }

   @WrapMethod(
      method = {"method_4475()V"}
   )
   private void preventChangeFullscreenVideoMode(Operation<Void> original) {
      Objects.requireNonNull(original);
      this.preventIfSplitscreen(() -> {
         Void var10000 = (Void)original.call(new Object[0]);
      });
   }

   @WrapMethod(
      method = {"method_4505(Ljava/util/Optional;)V"}
   )
   private void preventSetPreferredFullscreenVideoMode(Optional<class_319> preferredFullscreenVideoMode, Operation<Void> original) {
      this.preventIfSplitscreen(() -> {
         original.call(new Object[]{preferredFullscreenVideoMode});
      });
   }

   @WrapMethod(
      method = {"method_4500()V"}
   )
   private void preventToggleFullScreen(Operation<Void> original) {
      Optional var10000 = this.reparentingEngine().flatMap(this.filterHost()).flatMap((hostEngine) -> {
         return Optional.ofNullable(hostEngine.getParentWindow());
      });
      Consumer var10001 = ParentWindow::toggleFullscreen;
      Objects.requireNonNull(original);
      var10000.ifPresentOrElse(var10001, () -> {
         Void var10000 = (Void)original.call(new Object[0]);
      });
   }

   @WrapMethod(
      method = {"method_36813(II)V"}
   )
   private void preventSetWindowed(int windowedWidth, int windowedHeight, Operation<Void> original) {
      Optional var10000 = this.reparentingEngine().flatMap(this.filterHost()).flatMap((hostEngine) -> {
         return Optional.ofNullable(hostEngine.getParentWindow());
      });
      Consumer var10001 = (window) -> {
         window.setWindowed(windowedWidth, windowedHeight);
      };
      Objects.requireNonNull(original);
      var10000.ifPresentOrElse(var10001, () -> {
         Void var10000 = (Void)original.call(new Object[0]);
      });
   }

   @WrapMethod(
      method = {"method_4485(ZLnet/minecraft/class_10219;)V"}
   )
   private void preventUpdateFullscreen(boolean vsyncEnabled, class_10219 tracyFrameCapture, Operation<Void> original) {
      this.preventIfSplitscreen(() -> {
         original.call(new Object[]{vsyncEnabled, tracyFrameCapture});
      });
   }

   @ModifyReturnValue(
      method = {"method_4498()Z"},
      at = {@At("RETURN")}
   )
   private boolean injectParentStateToFullscreenCheck(boolean childIsFullscreen) {
      return (Boolean)this.reparentingEngine().flatMap(this.filterHost()).flatMap((hostEngine) -> {
         return Optional.ofNullable(hostEngine.getParentWindow());
      }).map(ParentWindow::isFullscreen).orElse(childIsFullscreen);
   }

   @Unique
   private Optional<ReparentingSplitscreenEngine> reparentingEngine() {
      return SplitscreenBootstrapper.getEngine().flatMap((engine) -> {
         Optional var10000;
         if (engine instanceof ReparentingSplitscreenEngine) {
            ReparentingSplitscreenEngine reparenting = (ReparentingSplitscreenEngine)engine;
            var10000 = Optional.of(reparenting);
         } else {
            var10000 = Optional.empty();
         }

         return var10000;
      });
   }

   @Unique
   private Function<ReparentingSplitscreenEngine, Optional<ReparentingHostSplitscreenEngine>> filterHost() {
      return (engine) -> {
         Optional var10000;
         if (engine instanceof ReparentingHostSplitscreenEngine) {
            ReparentingHostSplitscreenEngine reparenting = (ReparentingHostSplitscreenEngine)engine;
            var10000 = Optional.of(reparenting);
         } else {
            var10000 = Optional.empty();
         }

         return var10000;
      };
   }

   @Unique
   private void preventIfSplitscreen(Runnable originalCall) {
      if ((Boolean)SplitscreenBootstrapper.getEngine().map((engine) -> {
         return engine instanceof ReparentingSplitscreenEngine;
      }).orElse(false)) {
         if (!this.hasDoneInitialSetup) {
            originalCall.run();
         } else {
            field_5178.info("Preventing fullscreen mode change in child window");
         }
      } else {
         originalCall.run();
      }
   }
}
