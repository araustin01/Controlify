package dev.isxander.controlify.mixins.feature.screenop.vanilla.bundle;

import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.screenop.compat.vanilla.BundleItemSlotControllerAction;
import dev.isxander.controlify.screenop.compat.vanilla.ItemSlotControllerAction;
import net.minecraft.class_1799;
import net.minecraft.class_9929;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({class_9929.class})
public abstract class BundleMouseActionsMixin implements ItemSlotControllerAction {
   @Shadow
   protected abstract void method_61976(class_1799 var1, int var2, int var3);

   public boolean controlify$onControllerInput(class_1799 stack, int hoveredSlotIndex, ControllerEntity controller) {
      return BundleItemSlotControllerAction.onControllerInput(stack, hoveredSlotIndex, controller, this::method_61976);
   }
}
