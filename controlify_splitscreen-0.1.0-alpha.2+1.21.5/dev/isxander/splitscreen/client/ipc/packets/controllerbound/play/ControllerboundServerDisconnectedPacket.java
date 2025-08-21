package dev.isxander.splitscreen.client.ipc.packets.controllerbound.play;

import dev.isxander.splitscreen.client.host.ipc.ControllerPlayPacketListener;
import net.minecraft.class_2540;
import net.minecraft.class_2561;
import net.minecraft.class_8824;
import net.minecraft.class_9139;
import net.minecraft.class_9145;

public record ControllerboundServerDisconnectedPacket(class_2561 disconnectReason) implements ControllerboundPlayPacket {
   public static final class_9139<class_2540, ControllerboundServerDisconnectedPacket> CODEC;
   public static final class_9145<ControllerboundServerDisconnectedPacket> TYPE;

   public ControllerboundServerDisconnectedPacket(class_2561 disconnectReason) {
      this.disconnectReason = disconnectReason;
   }

   public void handle(ControllerPlayPacketListener handler) {
      handler.handleServerDisconnected(this);
   }

   public class_9145<ControllerboundServerDisconnectedPacket> method_65080() {
      return TYPE;
   }

   public class_2561 disconnectReason() {
      return this.disconnectReason;
   }

   static {
      CODEC = class_9139.method_56434(class_8824.field_49668, ControllerboundServerDisconnectedPacket::disconnectReason, ControllerboundServerDisconnectedPacket::new);
      TYPE = ControllerboundPlayPacket.createType("server_disconnected");
   }
}
