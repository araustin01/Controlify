package dev.isxander.splitscreen.client.engine.impl.fboshare.b3dext.vk;

import java.util.function.Supplier;

public class VkUtil {
   public static void safeVkCall(Supplier<Integer> call, Supplier<String> errorSupplier) {
      int err = (Integer)call.get();
      if (err != 0) {
         String var10002 = (String)errorSupplier.get();
         throw new RuntimeException("Vulkan call failed (" + var10002 + "): " + err);
      }
   }
}
