package dev.isxander.controlify.mixins.feature.steamdeck;

import java.io.File;
import net.minecraft.class_318;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_318.class})
public interface ScreenshotAccessor {
   @Invoker("method_1660")
   static File invokeGetFile(File file) {
      throw new AssertionError();
   }
}
