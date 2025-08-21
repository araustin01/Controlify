package dev.isxander.splitscreen.client.host.util;

import java.net.InetAddress;
import java.util.Optional;
import net.minecraft.class_1132;
import net.minecraft.class_1934;
import net.minecraft.class_310;
import net.minecraft.class_3521;
import net.minecraft.class_639;

public class LANUtil {
   public static Optional<class_639> getOrPublishLANServer() {
      return Optional.ofNullable(class_310.method_1551().method_1576()).map(LANUtil::getOrPublishLANServer);
   }

   public static class_639 getOrPublishLANServer(class_1132 server) {
      return new class_639(getLANServerBindAddress().getHostAddress(), getPortOrPublishServer(server));
   }

   public static InetAddress getLANServerBindAddress() {
      return InetAddress.getLoopbackAddress();
   }

   public static int getPortOrPublishServer(class_1132 server) {
      int port;
      if (!server.method_3860()) {
         port = class_3521.method_15302();
         server.method_3763(class_1934.field_28045, false, port);
      } else {
         port = server.method_3756();
      }

      return port;
   }
}
