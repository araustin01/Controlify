package dev.isxander.splitscreen.client.ipc.packets.controllerbound.play;

import dev.isxander.splitscreen.client.host.ipc.ControllerPlayPacketListener;
import io.netty.buffer.ByteBuf;
import net.minecraft.class_9135;
import net.minecraft.class_9139;
import net.minecraft.class_9145;

public record ControllerboundSignalReadyPacket(boolean finished, float progress) implements ControllerboundPlayPacket {
   public static final class_9139<ByteBuf, ControllerboundSignalReadyPacket> CODEC;
   public static final class_9145<ControllerboundSignalReadyPacket> TYPE;

   public ControllerboundSignalReadyPacket(boolean finished, float progress) {
      this.finished = finished;
      this.progress = progress;
   }

   public void handle(ControllerPlayPacketListener handler) {
      handler.handleReadySignal(this);
   }

   public class_9145<ControllerboundSignalReadyPacket> method_65080() {
      return TYPE;
   }

   public boolean finished() {
      return this.finished;
   }

   public float progress() {
      return this.progress;
   }

   static {
      CODEC = class_9139.method_56435(class_9135.field_48547, ControllerboundSignalReadyPacket::finished, class_9135.field_48552, ControllerboundSignalReadyPacket::progress, ControllerboundSignalReadyPacket::new);
      TYPE = ControllerboundPlayPacket.createType("signal_ready");
   }
}
