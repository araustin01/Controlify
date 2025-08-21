package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import dev.isxander.controlify.screenop.compat.vanilla.AbstractContainerScreenProcessor;
import dev.isxander.controlify.screenop.compat.vanilla.ItemSlotControllerAction;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_332;
import net.minecraft.class_465;
import net.minecraft.class_9930;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_465.class})
public abstract class AbstractContainerScreenMixin implements ScreenProcessorProvider {
   @Shadow
   @Nullable
   protected class_1735 field_2787;
   @Shadow
   @Final
   private List<class_9930> field_52801;
   @Unique
   protected AbstractContainerScreenProcessor<?> screenProcessor = new AbstractContainerScreenProcessor((class_465)this, () -> {
      return this.field_2787;
   }, this::method_2383, this::handleControllerItemSlotActions);

   @Shadow
   protected abstract void method_2383(class_1735 var1, int var2, int var3, class_1713 var4);

   public ScreenProcessor<?> screenProcessor() {
      return this.screenProcessor;
   }

   @Inject(
      method = {"method_25394(Lnet/minecraft/class_332;IIF)V"},
      at = {@At("HEAD")}
   )
   private void setPrevSlotShare(class_332 graphics, int mouseX, int mouseY, float delta, CallbackInfo ci, @Share("prevSlot") LocalRef<class_1735> prevSlot) {
      prevSlot.set(this.field_2787);
   }

   @Inject(
      method = {"method_25394(Lnet/minecraft/class_332;IIF)V"},
      at = {@At("RETURN")}
   )
   private void triggerSlotHovered(class_332 graphics, int mouseX, int mouseY, float delta, CallbackInfo ci, @Share("prevSlot") LocalRef<class_1735> prevSlot) {
      class_1735 oldSlot = (class_1735)prevSlot.get();
      class_1735 newSlot = this.field_2787;
      if ((oldSlot != null || newSlot != null) && (oldSlot == null || newSlot != null && newSlot.field_7874 != oldSlot.field_7874)) {
         this.screenProcessor.onHoveredSlotChanged(newSlot, oldSlot);
      }

   }

   @Unique
   protected boolean handleControllerItemSlotActions(ControllerEntity controller) {
      Iterator var2 = this.field_52801.iterator();

      while(var2.hasNext()) {
         class_9930 itemSlotMouseAction = (class_9930)var2.next();
         if (itemSlotMouseAction instanceof ItemSlotControllerAction) {
            ItemSlotControllerAction itemSlotControllerAction = (ItemSlotControllerAction)itemSlotMouseAction;
            if (itemSlotMouseAction.method_61974(this.field_2787) && itemSlotControllerAction.controlify$onControllerInput(this.field_2787.method_7677(), this.field_2787.field_7874, controller)) {
               return true;
            }
         }
      }

      return false;
   }

   @ModifyExpressionValue(
      method = {"method_25401(DDDD)Z"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_1735;method_7681()Z"
)}
   )
   private boolean allowItemSlotScrolling(boolean original) {
      return original && !Controlify.instance().currentInputMode().isController();
   }
}
