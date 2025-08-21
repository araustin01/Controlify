package dev.isxander.splitscreen.client.ipc;

import net.minecraft.class_2561;
import net.minecraft.class_2596;

public interface ConnectionDisconnectPacketFactory {
   class_2596<?> createDisconnectPacket(Throwable var1, class_2561 var2, boolean var3);
}
