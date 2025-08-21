package dev.isxander.controlify.mixins.feature.guide.ingame;

import net.minecraft.class_1657;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_1657.class})
public interface PlayerAccessor {
   @Invoker("method_63628")
   boolean callCanGlide();
}
