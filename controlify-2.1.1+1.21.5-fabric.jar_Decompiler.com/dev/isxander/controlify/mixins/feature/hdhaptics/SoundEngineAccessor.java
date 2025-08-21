package dev.isxander.controlify.mixins.feature.hdhaptics;

import net.minecraft.class_1140;
import net.minecraft.class_4237;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_1140.class})
public interface SoundEngineAccessor {
   @Accessor("field_18947")
   class_4237 getSoundBuffers();
}
