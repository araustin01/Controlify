package dev.isxander.controlify.mixins.feature.input;

import net.minecraft.class_340;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_340.class})
public interface DebugScreenOverlayAccessor {
   @Accessor("field_45988")
   boolean isRenderDebug();
}
