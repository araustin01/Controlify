package dev.isxander.controlify.mixins.feature.virtualmouse;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import net.minecraft.class_310;
import net.minecraft.class_3675;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_3675.class})
public class InputConstantsMixin {
   @ModifyReturnValue(
      method = {"method_15987(JI)Z"},
      at = {@At("RETURN")}
   )
   private static boolean modifyIsKeyDown(boolean keyDown, long window, int key) {
      if (key == 340 && window == class_310.method_1551().method_22683().method_4490()) {
         ControllerEntity controller = (ControllerEntity)Controlify.instance().getCurrentController().orElse((Object)null);
         if (controller == null) {
            return keyDown;
         } else {
            return keyDown || ControlifyBindings.VMOUSE_SHIFT_CLICK.on(controller).digitalNow() || ControlifyBindings.VMOUSE_SHIFT.on(controller).digitalNow();
         }
      } else {
         return keyDown;
      }
   }
}
