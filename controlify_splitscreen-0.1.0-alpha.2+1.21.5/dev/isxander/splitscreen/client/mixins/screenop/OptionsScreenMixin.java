package dev.isxander.splitscreen.client.mixins.screenop;

import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import dev.isxander.splitscreen.client.host.gui.SplitscreenConfigGuiFactory;
import java.util.function.Supplier;
import net.minecraft.class_2561;
import net.minecraft.class_4185;
import net.minecraft.class_429;
import net.minecraft.class_437;
import net.minecraft.class_7845.class_7939;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_429.class})
public abstract class OptionsScreenMixin {
   @Shadow
   protected abstract class_4185 method_47625(class_2561 var1, Supplier<class_437> var2);

   @Inject(
      method = {"method_25426()V"},
      at = {@At(
   value = "INVOKE:LAST",
   target = "Lnet/minecraft/class_7845$class_7939;method_47612(Lnet/minecraft/class_8021;)Lnet/minecraft/class_8021;"
)}
   )
   private void addSplitscreenConfig(CallbackInfo ci, @Local class_7939 rowHelper) {
      if (SplitscreenBootstrapper.getController().isPresent() || !SplitscreenBootstrapper.isSplitscreen()) {
         rowHelper.method_47612(this.method_47625(class_2561.method_43471("controlify.splitscreen.open_button"), () -> {
            return SplitscreenConfigGuiFactory.buildScreen((class_429)this);
         }));
      }

   }
}
