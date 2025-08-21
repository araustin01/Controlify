package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import dev.isxander.controlify.screenop.compat.vanilla.AbstractSignEditScreenProcessor;
import net.minecraft.class_7743;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_7743.class})
public class AbstractSignEditScreenMixin implements ScreenProcessorProvider {
   @Unique
   private final AbstractSignEditScreenProcessor screenProcessor = new AbstractSignEditScreenProcessor((class_7743)this);

   public ScreenProcessor<?> screenProcessor() {
      return this.screenProcessor;
   }
}
