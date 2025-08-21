package dev.isxander.splitscreen.client.ipc;

import dev.isxander.splitscreen.client.ipc.packets.pawnbound.common.PawnboundDisconnectPacket;
import net.minecraft.class_2535;
import net.minecraft.class_2561;
import net.minecraft.class_2596;
import net.minecraft.class_2598;

public class SplitscreenConnection extends class_2535 implements ConnectionDisconnectPacketFactory {
   public SplitscreenConnection(class_2598 receiving) {
      super(receiving);
   }

   public class_2596<?> createDisconnectPacket(Throwable throwable, class_2561 reason, boolean login) {
      throwable.printStackTrace();
      return new PawnboundDisconnectPacket(reason);
   }
}
