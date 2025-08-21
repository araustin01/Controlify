package dev.isxander.splitscreen.client.ipc.packets;

import dev.isxander.splitscreen.client.host.ipc.ControllerboundCommonPacketListener;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.common.PawnboundDisconnectPacket;
import dev.isxander.splitscreen.client.remote.ipc.PawnboundCommonPacketListener;
import net.minecraft.class_2540;
import net.minecraft.class_3902;
import net.minecraft.class_9147;

public final class CommonProtocols {
   public static <T extends ControllerboundCommonPacketListener, B extends class_2540> class_9147<T, B, class_3902> addControllerboundPackets(class_9147<T, B, class_3902> builder) {
      return builder;
   }

   public static <T extends PawnboundCommonPacketListener, B extends class_2540> class_9147<T, B, class_3902> addPawnboundPackets(class_9147<T, B, class_3902> builder) {
      return builder.method_56454(PawnboundDisconnectPacket.TYPE, PawnboundDisconnectPacket.CODEC);
   }
}
