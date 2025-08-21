package dev.isxander.controlify.platform.network;

import net.minecraft.class_2960;
import net.minecraft.class_3222;
import net.minecraft.class_8710;

public interface C2SNetworkApi extends SidedNetworkApi {
   <T> void sendPacket(class_2960 var1, T var2);

   <T> class_8710 createPayload(class_2960 var1, T var2);

   <T> void listenForPacket(class_2960 var1, C2SNetworkApi.PacketListener<T> var2);

   @FunctionalInterface
   public interface PacketListener<T> {
      void listen(T var1, class_3222 var2);
   }
}
