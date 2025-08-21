package dev.isxander.controlify.platform.network.neoforge;

import dev.isxander.controlify.platform.network.S2CNetworkApi;
import net.minecraft.class_2540;
import net.minecraft.class_2960;
import net.minecraft.class_3222;
import net.minecraft.class_9139;

public class S2CNetworkApiNeoforge implements S2CNetworkApi {
   public static final S2CNetworkApiNeoforge INSTANCE = new S2CNetworkApiNeoforge();

   public <T> void sendPacket(class_3222 recipient, class_2960 channel, T packet) {
   }

   public <T> void listenForPacket(class_2960 channel, S2CNetworkApi.PacketListener<T> listener) {
   }

   public <T> void registerPacket(class_2960 channel, class_9139<class_2540, T> handler) {
   }
}
