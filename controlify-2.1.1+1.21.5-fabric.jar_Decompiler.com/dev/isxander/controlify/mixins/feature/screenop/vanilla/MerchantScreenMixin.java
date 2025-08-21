package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Definitions;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.class_492;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_492.class})
public class MerchantScreenMixin {
   @Unique
   private double accumulatedScroll = 0.0D;

   @ModifyExpressionValue(
      method = {"method_25401(DDDD)Z"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_492;method_20220(I)Z"
)}
   )
   private boolean accumulateScrolling(boolean canScroll, @Local(ordinal = 3,argsOnly = true) double scrollY) {
      if (canScroll) {
         double currentScrollSign = Math.signum(scrollY);
         double accScrollSign = Math.signum(this.accumulatedScroll);
         if (currentScrollSign != accScrollSign) {
            this.accumulatedScroll = scrollY;
         } else {
            this.accumulatedScroll += scrollY;
         }
      }

      return canScroll;
   }

   @ModifyExpressionValue(
      method = {"method_25401(DDDD)Z"},
      at = {@At("MIXINEXTRAS:EXPRESSION")}
   )
   @Definitions({@Definition(
   id = "clamp",
   method = {"Lnet/minecraft/class_3532;method_15340(III)I"}
), @Definition(
   id = "scrollOff",
   field = {"Lnet/minecraft/class_492;field_19163:I"}
), @Definition(
   id = "scrollY",
   local = {@Local(
   ordinal = 3,
   argsOnly = true,
   type = double.class
)}
)})
   @Expression({"this.scrollOff = clamp((int) ((double) this.scrollOff - @(scrollY)), ?, ?)"})
   private double useAccumulatedScrollField(double scrollY) {
      double prev = this.accumulatedScroll;
      this.accumulatedScroll -= (double)((int)prev);
      return prev;
   }
}
