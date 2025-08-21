package dev.isxander.splitscreen.client.ipc.packets;

import dev.isxander.splitscreen.client.host.ipc.ControllerPlayPacketListener;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundEngineCustomPayloadPacket;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundGiveMeFocusIfForegroundPacket;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundHelloPacket;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundKeepAlivePacket;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundRequestPlayMusicPacket;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundServerDisconnectedPacket;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundSignalReadyPacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundCloseGamePacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundEngineCustomPayloadPacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundJoinServerPacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundKeepAlivePacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundLoadConfigPacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundServerDisconnectPacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundUseControllerPacket;
import dev.isxander.splitscreen.client.remote.ipc.PawnPlayPacketListener;
import net.minecraft.class_2539;
import net.minecraft.class_2540;
import net.minecraft.class_9127;
import net.minecraft.class_9139;
import net.minecraft.class_9147;

public final class PlayProtocols {
   public static class_9127<ControllerPlayPacketListener> controllerbound(class_9139<class_2540, ControllerboundEngineCustomPayloadPacket> engineCodec) {
      return class_9147.method_56451(class_2539.field_20591, (builder) -> {
         CommonProtocols.addControllerboundPackets(builder).method_56454(ControllerboundHelloPacket.TYPE, ControllerboundHelloPacket.CODEC).method_56454(ControllerboundKeepAlivePacket.TYPE, ControllerboundKeepAlivePacket.CODEC).method_56454(ControllerboundGiveMeFocusIfForegroundPacket.TYPE, ControllerboundGiveMeFocusIfForegroundPacket.CODEC).method_56454(ControllerboundSignalReadyPacket.TYPE, ControllerboundSignalReadyPacket.CODEC).method_56454(ControllerboundServerDisconnectedPacket.TYPE, ControllerboundServerDisconnectedPacket.CODEC).method_56454(ControllerboundRequestPlayMusicPacket.TYPE, ControllerboundRequestPlayMusicPacket.CODEC).method_56454(ControllerboundEngineCustomPayloadPacket.TYPE, engineCodec);
      }).method_68874(class_2540::new);
   }

   public static class_9127<PawnPlayPacketListener> pawnbound(class_9139<class_2540, PawnboundEngineCustomPayloadPacket> engineCodec) {
      return class_9147.method_56455(class_2539.field_20591, (builder) -> {
         CommonProtocols.addPawnboundPackets(builder).method_56454(PawnboundKeepAlivePacket.TYPE, PawnboundKeepAlivePacket.CODEC).method_56454(PawnboundJoinServerPacket.TYPE, PawnboundJoinServerPacket.CODEC).method_56454(PawnboundCloseGamePacket.TYPE, PawnboundCloseGamePacket.CODEC).method_56454(PawnboundUseControllerPacket.TYPE, PawnboundUseControllerPacket.CODEC).method_56454(PawnboundServerDisconnectPacket.TYPE, PawnboundServerDisconnectPacket.CODEC).method_56454(PawnboundLoadConfigPacket.TYPE, PawnboundLoadConfigPacket.CODEC).method_56454(PawnboundEngineCustomPayloadPacket.TYPE, engineCodec);
      }).method_68874(class_2540::new);
   }
}
