package dev.isxander.controlify.mixins.feature.hdhaptics;

import net.minecraft.class_1140;
import net.minecraft.class_1144;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_1144.class})
public interface SoundManagerAccessor {
   @Accessor("field_5590")
   class_1140 getSoundEngine();
}
