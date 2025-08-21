package dev.isxander.splitscreen.server.login.packets;

import dev.isxander.splitscreen.server.login.ClientIdentification;
import net.minecraft.class_2540;
import net.minecraft.class_9139;

public record ServerboundIdentifyPacket(ClientIdentification identification) implements CodedPacket<ServerboundIdentifyPacket> {
   public static final class_9139<class_2540, ServerboundIdentifyPacket> STREAM_CODEC;

   public ServerboundIdentifyPacket(ClientIdentification identification) {
      this.identification = identification;
   }

   public class_9139<class_2540, ServerboundIdentifyPacket> codec() {
      return STREAM_CODEC;
   }

   public ClientIdentification identification() {
      return this.identification;
   }

   static {
      STREAM_CODEC = ClientIdentification.STREAM_CODEC.method_56432(ServerboundIdentifyPacket::new, ServerboundIdentifyPacket::identification);
   }
}
