package dev.isxander.controlify.platform.network;

import net.minecraft.class_2960;
import net.minecraft.class_3222;

public interface S2CNetworkApi extends SidedNetworkApi {
   <T> void sendPacket(class_3222 var1, class_2960 var2, T var3);

   <T> void listenForPacket(class_2960 var1, S2CNetworkApi.PacketListener<T> var2);

   @FunctionalInterface
   public interface PacketListener<T> {
      void listen(T var1);
   }
}
