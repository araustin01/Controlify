package dev.isxander.controlify.platform.network;

import dev.isxander.controlify.platform.network.fabric.C2SNetworkApiFabric;
import dev.isxander.controlify.platform.network.fabric.S2CNetworkApiFabric;
import net.minecraft.class_2540;
import net.minecraft.class_2960;
import net.minecraft.class_9139;

public interface SidedNetworkApi {
   static C2SNetworkApi C2S() {
      return C2SNetworkApiFabric.INSTANCE;
   }

   static S2CNetworkApi S2C() {
      return S2CNetworkApiFabric.INSTANCE;
   }

   <T> void registerPacket(class_2960 var1, class_9139<class_2540, T> var2);
}
