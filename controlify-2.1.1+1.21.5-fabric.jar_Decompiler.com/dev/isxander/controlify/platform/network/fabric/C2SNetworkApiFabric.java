package dev.isxander.controlify.platform.network.fabric;

import dev.isxander.controlify.platform.network.C2SNetworkApi;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.class_2540;
import net.minecraft.class_2960;
import net.minecraft.class_8710;
import net.minecraft.class_9139;

public final class C2SNetworkApiFabric implements C2SNetworkApi {
   public static final C2SNetworkApiFabric INSTANCE = new C2SNetworkApiFabric();
   private final Map<class_2960, FabricPacketWrapper<?>> packets = new HashMap();

   private C2SNetworkApiFabric() {
   }

   public <T> void registerPacket(class_2960 channel, class_9139<class_2540, T> codec) {
      this.packets.put(channel, new FabricPacketWrapper(channel, codec, PayloadTypeRegistry.playC2S()));
   }

   public <T> void sendPacket(class_2960 channel, T packet) {
      ClientPlayNetworking.send(this.createPayload(channel, packet));
   }

   public <T> class_8710 createPayload(class_2960 channel, T packet) {
      FabricPacketWrapper<T> packetWrapper = this.getWrapper(channel);
      Objects.requireNonNull(packetWrapper);
      return packetWrapper.new FabricPacketPayloadWrapper(packet);
   }

   public <T> void listenForPacket(class_2960 channel, C2SNetworkApi.PacketListener<T> listener) {
      FabricPacketWrapper<T> packetWrapper = this.getWrapper(channel);
      ServerPlayNetworking.registerGlobalReceiver(packetWrapper.type, (packet, context) -> {
         listener.listen(packet.payload, context.player());
      });
   }

   private <T> FabricPacketWrapper<T> getWrapper(class_2960 channel) {
      return (FabricPacketWrapper)this.packets.get(channel);
   }
}
