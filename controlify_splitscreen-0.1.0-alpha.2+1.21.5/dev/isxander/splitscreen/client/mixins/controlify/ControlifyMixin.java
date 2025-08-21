package dev.isxander.splitscreen.client.mixins.controlify;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Definitions;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controllermanager.ControllerManager;
import dev.isxander.splitscreen.client.integrations.ControlifyExtension;
import net.minecraft.class_2561;
import net.minecraft.class_5250;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({Controlify.class})
public class ControlifyMixin {
   @ModifyExpressionValue(
      method = {"onControllerAdded(Ldev/isxander/controlify/controller/ControllerEntity;ZZ)V"},
      at = {@At("MIXINEXTRAS:EXPRESSION")}
   )
   @Definitions({@Definition(
   id = "translatable",
   method = {"Lnet/minecraft/class_2561;method_43469(Ljava/lang/String;[Ljava/lang/Object;)Lnet/minecraft/class_5250;"}
), @Definition(
   id = "sendToast",
   method = {"Ldev/isxander/controlify/utils/ToastUtils;sendToast(Lnet/minecraft/class_2561;Lnet/minecraft/class_2561;Z)V"}
)})
   @Expression({"sendToast(?, @(translatable('controlify.toast.controller_connected.description', ?)), ?)"})
   private class_5250 modifyHotplugControllerDescription(class_5250 component, @Local(argsOnly = true) ControllerEntity controller) {
      int controllersConnected = ((ControllerManager)Controlify.instance().getControllerManager().orElseThrow()).getConnectedControllers().size();
      if (controllersConnected > 1) {
         InputBinding binding = ControlifyExtension.ADD_PLAYER_BIND.on(controller);
         return class_2561.method_43470("Press ").method_10852(binding.inputIcon()).method_27693(" to start splitscreen");
      } else {
         return component;
      }
   }
}
