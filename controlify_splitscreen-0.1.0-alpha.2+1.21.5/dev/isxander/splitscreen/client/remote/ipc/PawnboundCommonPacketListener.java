package dev.isxander.splitscreen.client.remote.ipc;

import dev.isxander.splitscreen.client.ipc.packets.pawnbound.common.PawnboundDisconnectPacket;
import net.minecraft.class_8697;

public interface PawnboundCommonPacketListener extends class_8697 {
   void handleDisconnect(PawnboundDisconnectPacket var1);
}
