package dev.isxander.splitscreen.server.login.packets;

import net.minecraft.class_2540;
import net.minecraft.class_9135;
import net.minecraft.class_9139;

public record ClientboundNoncePacket(byte[] nonce) implements CodedPacket<ClientboundNoncePacket> {
   public static final int NONCE_SIZE_BITS = 128;
   public static final int NONCE_SIZE_BYTES = 16;
   public static final class_9139<class_2540, ClientboundNoncePacket> STREAM_CODEC = class_9135.method_56895(16).method_56432(ClientboundNoncePacket::new, ClientboundNoncePacket::nonce).method_56439(class_2540::unwrap);

   public ClientboundNoncePacket(byte[] nonce) {
      this.nonce = nonce;
   }

   public class_9139<class_2540, ClientboundNoncePacket> codec() {
      return STREAM_CODEC;
   }

   public byte[] nonce() {
      return this.nonce;
   }
}
