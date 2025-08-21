package dev.isxander.controlify.platform.network.fabric;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.class_2540;
import net.minecraft.class_2960;
import net.minecraft.class_8710;
import net.minecraft.class_9139;
import net.minecraft.class_8710.class_9154;
import org.jetbrains.annotations.NotNull;

public class FabricPacketWrapper<T> {
   public final class_9154<FabricPacketWrapper<T>.FabricPacketPayloadWrapper> type;

   public FabricPacketWrapper(class_2960 channel, class_9139<class_2540, T> codec, PayloadTypeRegistry<? extends class_2540> registry) {
      this.type = new class_9154(channel);
      class_9139<class_2540, FabricPacketWrapper<T>.FabricPacketPayloadWrapper> streamCodec = class_9139.method_56437((buf, wrapper) -> {
         codec.encode(buf, wrapper.payload);
      }, (buf) -> {
         return new FabricPacketWrapper.FabricPacketPayloadWrapper(codec.decode(buf));
      });
      registry.register(this.type, streamCodec);
   }

   public class FabricPacketPayloadWrapper implements class_8710 {
      public final T payload;

      public FabricPacketPayloadWrapper(T payload) {
         this.payload = payload;
      }

      @NotNull
      public class_9154<? extends class_8710> method_56479() {
         return FabricPacketWrapper.this.type;
      }
   }
}
