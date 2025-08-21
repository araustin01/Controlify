package dev.isxander.controlify.mixins.feature.hdhaptics;

import java.nio.ByteBuffer;
import javax.sound.sampled.AudioFormat;
import net.minecraft.class_4231;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_4231.class})
public interface SoundBufferAccessor {
   @Accessor("field_18916")
   @Nullable
   ByteBuffer getData();

   @Accessor("field_18917")
   AudioFormat getFormat();
}
