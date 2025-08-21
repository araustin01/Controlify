package dev.isxander.splitscreen.server.login.packets;

import net.minecraft.class_2540;
import net.minecraft.class_9139;

public record ServerboundNonceAckPacket() implements CodedPacket<ServerboundNonceAckPacket> {
   public static final ServerboundNonceAckPacket UNIT = new ServerboundNonceAckPacket();
   public static final class_9139<class_2540, ServerboundNonceAckPacket> STREAM_CODEC;

   public class_9139<class_2540, ServerboundNonceAckPacket> codec() {
      return STREAM_CODEC;
   }

   static {
      STREAM_CODEC = class_9139.method_56431(UNIT);
   }
}
