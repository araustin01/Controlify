package dev.isxander.controlify.platform.neoforge.mixins;

import dev.isxander.controlify.platform.neoforge.VanillaKeyMappingHolder;
import java.util.Arrays;
import net.minecraft.class_304;
import net.minecraft.class_315;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_315.class})
public class OptionsMixin implements VanillaKeyMappingHolder {
   @Shadow
   public class_304[] field_1839;
   @Unique
   private class_304[] controlify$vanillaKeyMappings;

   @Inject(
      method = {"<init>(Lnet/minecraft/class_310;Ljava/io/File;)V"},
      at = {@At("RETURN")}
   )
   private void storeVanillaKeybindsBeforeModification(CallbackInfo ci) {
      this.controlify$vanillaKeyMappings = (class_304[])Arrays.copyOf(this.field_1839, this.field_1839.length);
   }

   public class_304[] controlify$getVanillaKeys() {
      return this.controlify$vanillaKeyMappings;
   }
}
