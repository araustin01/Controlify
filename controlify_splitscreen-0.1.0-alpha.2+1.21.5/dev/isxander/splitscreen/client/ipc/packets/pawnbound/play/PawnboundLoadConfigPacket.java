package dev.isxander.splitscreen.client.ipc.packets.pawnbound.play;

import dev.isxander.splitscreen.client.remote.ipc.PawnPlayPacketListener;
import net.minecraft.class_2540;
import net.minecraft.class_2960;
import net.minecraft.class_9139;
import net.minecraft.class_9145;
import org.jetbrains.annotations.NotNull;

public record PawnboundLoadConfigPacket(class_2960 config) implements PawnboundPlayPacket {
   public static final class_9139<class_2540, PawnboundLoadConfigPacket> CODEC;
   public static final class_9145<PawnboundLoadConfigPacket> TYPE;

   public PawnboundLoadConfigPacket(class_2960 config) {
      this.config = config;
   }

   public void handle(PawnPlayPacketListener handler) {
      handler.handleLoadConfig(this);
   }

   @NotNull
   public class_9145<PawnboundLoadConfigPacket> method_65080() {
      return TYPE;
   }

   public class_2960 config() {
      return this.config;
   }

   static {
      CODEC = class_9139.method_56434(class_2960.field_48267, PawnboundLoadConfigPacket::config, PawnboundLoadConfigPacket::new);
      TYPE = PawnboundPlayPacket.createType("load_config");
   }
}
