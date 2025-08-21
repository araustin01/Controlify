package dev.isxander.splitscreen.server.mixins.login;

import net.minecraft.class_419;
import net.minecraft.class_9812;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_419.class})
public interface DisconnectedScreenAccessor {
   @Accessor("field_52131")
   class_9812 getDetails();
}
