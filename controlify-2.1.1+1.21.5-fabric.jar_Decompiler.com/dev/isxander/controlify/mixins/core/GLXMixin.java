package dev.isxander.controlify.mixins.core;

import com.mojang.blaze3d.platform.GLX;
import java.util.function.LongSupplier;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({GLX.class})
public class GLXMixin {
   @Inject(
      method = {"_initGlfw()Ljava/util/function/LongSupplier;"},
      at = {@At(
   value = "INVOKE",
   target = "Lorg/lwjgl/glfw/GLFW;glfwInit()Z"
)}
   )
   private static void addInitHints(CallbackInfoReturnable<LongSupplier> cir) {
      GLFW.glfwInitHint(327681, 0);
   }
}
