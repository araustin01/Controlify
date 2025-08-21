package dev.isxander.controlify.mixins.feature.screenkeyboard;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.screenkeyboard.ChatKeyboardDucky;
import net.minecraft.class_310;
import net.minecraft.class_338;
import net.minecraft.class_408;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({class_338.class})
public abstract class ChatComponentMixin {
   @Shadow
   @Final
   private class_310 field_2062;
   @Unique
   private static final int VANILLA_CHAT_PADDING = 40;
   @Unique
   private static final int SHIFTED_CHAT_PADDING = 20;

   @Shadow
   public abstract double method_1814();

   @ModifyExpressionValue(
      method = {"method_1805(Lnet/minecraft/class_332;IIIZ)V"},
      at = {@At("MIXINEXTRAS:EXPRESSION")}
   )
   @Definition(
      id = "floor",
      method = {"Lnet/minecraft/class_3532;method_15375(F)I"}
   )
   @Expression({"floor((float) (@(?) - 40) / ?)"})
   private int modifyChatOffset(int y) {
      class_437 var3 = this.field_2062.field_1755;
      if (var3 instanceof class_408) {
         class_408 chat = (class_408)var3;
         return (int)((float)y * (1.0F - ChatKeyboardDucky.getKeyboardShiftAmount(chat)));
      } else {
         return y;
      }
   }

   @ModifyExpressionValue(
      method = {"method_1805(Lnet/minecraft/class_332;IIIZ)V"},
      at = {@At(
   value = "CONSTANT",
   args = {"intValue=40"}
)}
   )
   private int modifyChatToInputPadding(int padding) {
      class_437 var3 = this.field_2062.field_1755;
      if (var3 instanceof class_408) {
         class_408 chat = (class_408)var3;
         if (ChatKeyboardDucky.getKeyboardShiftAmount(chat) > 0.0F) {
            return 20;
         }
      }

      return padding;
   }

   @ModifyVariable(
      method = {"method_44724(D)D"},
      at = @At("HEAD"),
      argsOnly = true
   )
   private double modifyScreenY(double y) {
      class_437 var4 = this.field_2062.field_1755;
      if (var4 instanceof class_408) {
         class_408 chat = (class_408)var4;
         float shiftAmount = ChatKeyboardDucky.getKeyboardShiftAmount(chat);
         if (shiftAmount > 0.0F) {
            double shiftPixels = (double)(shiftAmount * (float)this.field_2062.method_22683().method_4502() - 20.0F);
            return y + shiftPixels;
         }
      }

      return y;
   }
}
