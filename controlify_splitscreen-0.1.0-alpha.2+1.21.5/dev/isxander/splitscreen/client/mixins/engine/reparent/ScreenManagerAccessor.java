package dev.isxander.splitscreen.client.mixins.engine.reparent;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.minecraft.class_313;
import net.minecraft.class_323;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_323.class})
public interface ScreenManagerAccessor {
   @Accessor("field_1993")
   Long2ObjectMap<class_313> getMonitors();
}
