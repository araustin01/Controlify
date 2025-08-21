package dev.isxander.splitscreen.client.engine.impl.fboshare;

import dev.isxander.splitscreen.client.engine.SplitscreenEngine;
import dev.isxander.splitscreen.client.engine.impl.fboshare.ipc.ControllerboundShareMemoryPayload;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundEngineCustomPayloadPacket;
import dev.isxander.splitscreen.client.ipc.packets.pawnbound.play.PawnboundEngineCustomPayloadPacket;
import java.util.List;
import net.minecraft.class_2540;
import net.minecraft.class_8710;
import net.minecraft.class_8711;
import net.minecraft.class_9139;
import net.minecraft.class_8710.class_9155;

public abstract class FboshareSplitscreenEngine implements SplitscreenEngine {
   private static final class_9139<class_2540, ControllerboundEngineCustomPayloadPacket> CONTROLLERBOUND_CODEC;
   private static final class_9139<class_2540, PawnboundEngineCustomPayloadPacket> PAWNBOUND_CODEC;

   public class_9139<class_2540, ControllerboundEngineCustomPayloadPacket> getControllerboundCustomPayloadCodec() {
      return CONTROLLERBOUND_CODEC;
   }

   public class_9139<class_2540, PawnboundEngineCustomPayloadPacket> getPawnboundCustomPayloadCodec() {
      return PAWNBOUND_CODEC;
   }

   static {
      CONTROLLERBOUND_CODEC = class_8710.method_56485((id) -> {
         return class_8711.method_56492(id, 1048576);
      }, List.of(new class_9155(ControllerboundShareMemoryPayload.TYPE, ControllerboundShareMemoryPayload.CODEC))).method_56432(ControllerboundEngineCustomPayloadPacket::new, ControllerboundEngineCustomPayloadPacket::payload);
      PAWNBOUND_CODEC = class_8710.method_56485((id) -> {
         return class_8711.method_56492(id, 1048576);
      }, List.of()).method_56432(PawnboundEngineCustomPayloadPacket::new, PawnboundEngineCustomPayloadPacket::payload);
   }
}
