package dev.isxander.splitscreen.client.mixins.engine.reparent;

import net.minecraft.class_323;
import net.minecraft.class_3682;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_3682.class})
public interface VirtualScreenAccessor {
   @Accessor("field_16255")
   class_323 getScreenManager();
}
