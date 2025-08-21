package dev.isxander.splitscreen.client.ipc.packets;

import dev.isxander.splitscreen.client.host.ipc.ControllerHandshakePacketListener;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.handshake.ControllerboundHandshakePacket;
import net.minecraft.class_2539;
import net.minecraft.class_2540;
import net.minecraft.class_9127;
import net.minecraft.class_9147;

public final class HandshakeProtocols {
   public static final class_9127<ControllerHandshakePacketListener> CONTROLLERBOUND;

   static {
      CONTROLLERBOUND = class_9147.method_56451(class_2539.field_20590, (builder) -> {
         CommonProtocols.addControllerboundPackets(builder).method_56454(ControllerboundHandshakePacket.TYPE, ControllerboundHandshakePacket.CODEC);
      }).method_68874(class_2540::new);
   }
}
