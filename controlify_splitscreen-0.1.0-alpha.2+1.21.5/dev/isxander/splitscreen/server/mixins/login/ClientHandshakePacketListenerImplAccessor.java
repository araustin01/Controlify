package dev.isxander.splitscreen.server.mixins.login;

import net.minecraft.class_635;
import net.minecraft.class_642;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_635.class})
public interface ClientHandshakePacketListenerImplAccessor {
   @Accessor("field_40481")
   class_642 getServerData();
}
