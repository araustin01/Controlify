package dev.isxander.controlify.platform.fabric.mixins;

import java.util.List;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.minecraft.class_304;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({KeyBindingRegistryImpl.class})
public interface KeyBindingRegistryImplAccessor {
   @Accessor("MODDED_KEY_BINDINGS")
   static List<class_304> getCustomKeys() {
      throw new AssertionError();
   }
}
