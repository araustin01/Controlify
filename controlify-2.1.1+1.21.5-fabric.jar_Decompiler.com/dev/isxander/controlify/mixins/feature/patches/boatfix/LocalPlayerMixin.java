package dev.isxander.controlify.mixins.feature.patches.boatfix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.fixes.boatfix.AnalogBoatInput;
import dev.isxander.controlify.ingame.InGameInputHandler;
import dev.isxander.controlify.utils.MthExt;
import net.minecraft.class_10255;
import net.minecraft.class_241;
import net.minecraft.class_744;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_746.class})
public class LocalPlayerMixin {
   @Shadow
   public class_744 field_3913;

   @WrapOperation(
      method = {"method_5842()V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_10255;method_64487(ZZZZ)V"
)}
   )
   private void useAnalogInput(class_10255 boat, boolean pressingLeft, boolean pressingRight, boolean pressingForward, boolean pressingBack, Operation<Void> original) {
      if (ControlifyApi.get().currentInputMode().isController() && !Controlify.instance().config().globalSettings().shouldUseKeyboardMovement()) {
         class_241 moveVec = InGameInputHandler.getMoveVec(this.field_3913);
         float forwardImpulse = moveVec.field_1342;
         float rightImpulse = -moveVec.field_1343;
         float deadzone = 0.1F;
         float onlyRightImpulseAbs = Math.max(0.0F, rightImpulse);
         float onlyLeftImpulseAbs = -Math.min(0.0F, rightImpulse);
         onlyRightImpulseAbs = onlyRightImpulseAbs < deadzone ? 0.0F : MthExt.remap(onlyRightImpulseAbs, deadzone, 1.0F, 0.0F, 1.0F);
         onlyLeftImpulseAbs = onlyLeftImpulseAbs < deadzone ? 0.0F : MthExt.remap(onlyLeftImpulseAbs, deadzone, 1.0F, 0.0F, 1.0F);
         rightImpulse = onlyRightImpulseAbs - onlyLeftImpulseAbs;
         ((AnalogBoatInput)boat).controlify$setAnalogInput(forwardImpulse, rightImpulse);
      } else {
         original.call(new Object[]{boat, pressingLeft, pressingRight, pressingForward, pressingBack});
      }
   }
}
