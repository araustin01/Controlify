package dev.isxander.splitscreen.server.login.packets;

import net.minecraft.class_2540;
import net.minecraft.class_9135;
import net.minecraft.class_9139;

public record ClientboundIdentifyPacket(int protocolVersion) implements CodedPacket<ClientboundIdentifyPacket> {
   public static final class_9139<class_2540, ClientboundIdentifyPacket> STREAM_CODEC;

   public ClientboundIdentifyPacket(int protocolVersion) {
      this.protocolVersion = protocolVersion;
   }

   public class_9139<class_2540, ClientboundIdentifyPacket> codec() {
      return STREAM_CODEC;
   }

   public int protocolVersion() {
      return this.protocolVersion;
   }

   static {
      STREAM_CODEC = class_9139.method_56434(class_9135.field_48550, ClientboundIdentifyPacket::protocolVersion, ClientboundIdentifyPacket::new);
   }
}
