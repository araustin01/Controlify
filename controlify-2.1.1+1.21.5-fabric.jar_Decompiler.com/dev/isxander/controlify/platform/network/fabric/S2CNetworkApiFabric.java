package dev.isxander.controlify.platform.network.fabric;

import dev.isxander.controlify.platform.network.S2CNetworkApi;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.class_2540;
import net.minecraft.class_2960;
import net.minecraft.class_3222;
import net.minecraft.class_9139;

public final class S2CNetworkApiFabric implements S2CNetworkApi {
   public static final S2CNetworkApiFabric INSTANCE = new S2CNetworkApiFabric();
   private final Map<class_2960, FabricPacketWrapper<?>> packets = new HashMap();

   private S2CNetworkApiFabric() {
   }

   public <T> void registerPacket(class_2960 channel, class_9139<class_2540, T> codec) {
      this.packets.put(channel, new FabricPacketWrapper(channel, codec, PayloadTypeRegistry.playS2C()));
   }

   public <T> void sendPacket(class_3222 recipient, class_2960 channel, T packet) {
      FabricPacketWrapper<T> packetWrapper = this.getWrapper(channel);
      Objects.requireNonNull(packetWrapper);
      ServerPlayNetworking.send(recipient, packetWrapper.new FabricPacketPayloadWrapper(packet));
   }

   public <T> void listenForPacket(class_2960 channel, S2CNetworkApi.PacketListener<T> listener) {
      FabricPacketWrapper<T> packetWrapper = this.getWrapper(channel);
      ClientPlayNetworking.registerGlobalReceiver(packetWrapper.type, (packet, context) -> {
         listener.listen(packet.payload);
      });
   }

   private <T> FabricPacketWrapper<T> getWrapper(class_2960 channel) {
      return (FabricPacketWrapper)this.packets.get(channel);
   }
}
