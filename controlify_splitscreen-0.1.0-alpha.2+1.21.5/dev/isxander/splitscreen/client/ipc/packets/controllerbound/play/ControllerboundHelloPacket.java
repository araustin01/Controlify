package dev.isxander.splitscreen.client.ipc.packets.controllerbound.play;

import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.splitscreen.client.host.ipc.ControllerPlayPacketListener;
import dev.isxander.splitscreen.client.ipc.utils.ExtraStreamCodecs;
import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.minecraft.class_2596;
import net.minecraft.class_9135;
import net.minecraft.class_9139;
import net.minecraft.class_9145;
import org.jetbrains.annotations.Nullable;

public record ControllerboundHelloPacket(@Nullable ControllerUID controller) implements ControllerboundPlayPacket {
   public static final class_9139<ByteBuf, ControllerboundHelloPacket> CODEC;
   public static final class_9145<ControllerboundHelloPacket> TYPE;

   public ControllerboundHelloPacket(@Nullable ControllerUID controller) {
      this.controller = controller;
   }

   public void handle(ControllerPlayPacketListener handler) {
      handler.handleHello(this);
   }

   public class_9145<? extends class_2596<ControllerPlayPacketListener>> method_65080() {
      return TYPE;
   }

   @Nullable
   public ControllerUID controller() {
      return this.controller;
   }

   static {
      CODEC = class_9135.method_56382(ExtraStreamCodecs.CONTROLLER_UID).method_56432((opt) -> {
         return new ControllerboundHelloPacket((ControllerUID)opt.orElse((Object)null));
      }, (packet) -> {
         return Optional.ofNullable(packet.controller());
      });
      TYPE = ControllerboundPlayPacket.createType("hello");
   }
}
