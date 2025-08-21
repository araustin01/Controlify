package dev.isxander.controlify.mixins.feature.input;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.ingame.PickBlockAccessor;
import net.minecraft.class_310;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_310.class})
public abstract class MinecraftMixin implements PickBlockAccessor {
   @Unique
   private boolean useNbtPick;

   @Shadow
   protected abstract void method_1511();

   public void controlify$pickBlock() {
      this.useNbtPick = false;
      this.method_1511();
   }

   public void controlify$pickBlockWithNbt() {
      this.useNbtPick = true;
      this.method_1511();
   }

   @ModifyExpressionValue(
      method = {"method_1511()V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_437;method_25441()Z"
)}
   )
   private boolean shouldUseNbtPick(boolean hasControlDown) {
      if (this.useNbtPick) {
         this.useNbtPick = false;
         return true;
      } else {
         return hasControlDown;
      }
   }
}
