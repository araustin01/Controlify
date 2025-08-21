package dev.isxander.splitscreen.client.engine;

import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundEngineCustomPayloadPacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundEngineCustomPayloadPacket;
import net.minecraft.class_2540;
import net.minecraft.class_9139;

public interface SplitscreenEngine {
   class_9139<class_2540, ControllerboundEngineCustomPayloadPacket> getControllerboundCustomPayloadCodec();

   class_9139<class_2540, PawnboundEngineCustomPayloadPacket> getPawnboundCustomPayloadCodec();
}
