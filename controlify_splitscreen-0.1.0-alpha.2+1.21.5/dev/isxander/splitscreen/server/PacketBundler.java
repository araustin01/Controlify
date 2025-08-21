package dev.isxander.splitscreen.server;

import java.util.List;
import java.util.function.BiFunction;
import net.minecraft.class_1937;
import net.minecraft.class_2596;
import net.minecraft.class_3222;
import net.minecraft.class_5321;
import net.minecraft.class_8710;

public interface PacketBundler<T extends class_2596<?>, B extends class_8710> {
   Class<T> packetClass();

   B bundle(T var1, double var2, double var4, double var6, double var8, class_5321<class_1937> var10, SplitscreenPlayerInfo.Controller var11, List<class_3222> var12);

   public static record Simple<T extends class_2596<?>, B extends class_8710>(Class<? extends T> packetClassWild, BiFunction<BundledPacketInfo, T, B> bundleFunction) implements PacketBundler<T, B> {
      public Simple(Class<? extends T> packetClassWild, BiFunction<BundledPacketInfo, T, B> bundleFunction) {
         this.packetClassWild = packetClassWild;
         this.bundleFunction = bundleFunction;
      }

      public Class<T> packetClass() {
         return this.packetClassWild;
      }

      public B bundle(T packet, double x, double y, double z, double radius, class_5321<class_1937> dimension, SplitscreenPlayerInfo.Controller controller, List<class_3222> players) {
         BundledPacketInfo bundleInfo = BundledPacketInfo.create(controller, players);
         return (class_8710)this.bundleFunction.apply(bundleInfo, packet);
      }

      public Class<? extends T> packetClassWild() {
         return this.packetClassWild;
      }

      public BiFunction<BundledPacketInfo, T, B> bundleFunction() {
         return this.bundleFunction;
      }
   }
}
