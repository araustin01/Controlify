package dev.isxander.splitscreen.client.mixins.engine.reparent;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import net.minecraft.class_3675;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({class_3675.class})
public class InputConstantsMixin {
   @WrapMethod(
      method = {"method_15984(JIDD)V"}
   )
   private static void shouldAllowMouseGrab(long window, int cursorValue, double xPos, double yPos, Operation<Void> original) {
      if (!SplitscreenBootstrapper.isSplitscreen()) {
         original.call(new Object[]{window, cursorValue, xPos, yPos});
      }

   }
}
