package dev.isxander.splitscreen.client.ipc.packets.controllerbound.play;

import dev.isxander.splitscreen.client.host.ipc.ControllerPlayPacketListener;
import net.minecraft.class_2540;
import net.minecraft.class_2596;
import net.minecraft.class_9139;
import net.minecraft.class_9145;

public record ControllerboundKeepAlivePacket() implements ControllerboundPlayPacket {
   public static final ControllerboundKeepAlivePacket INSTANCE = new ControllerboundKeepAlivePacket();
   public static final class_9139<class_2540, ControllerboundKeepAlivePacket> CODEC;
   public static final class_9145<ControllerboundKeepAlivePacket> TYPE;

   public void handle(ControllerPlayPacketListener handler) {
      handler.handleKeepAlive(this);
   }

   public class_9145<? extends class_2596<ControllerPlayPacketListener>> method_65080() {
      return TYPE;
   }

   static {
      CODEC = class_9139.method_56431(INSTANCE);
      TYPE = ControllerboundPlayPacket.createType("keep_alive");
   }
}
