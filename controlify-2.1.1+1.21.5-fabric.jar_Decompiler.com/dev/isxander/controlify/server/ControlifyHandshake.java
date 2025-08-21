package dev.isxander.controlify.server;

import com.mojang.logging.LogUtils;
import dev.isxander.controlify.platform.client.PlatformClientUtil;
import dev.isxander.controlify.platform.main.PlatformMainUtil;
import dev.isxander.controlify.utils.CUtil;
import net.minecraft.class_124;
import net.minecraft.class_2540;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_9139;
import org.slf4j.Logger;

public class ControlifyHandshake {
   public static final Logger LOGGER = LogUtils.getLogger();
   public static final int PROTOCOL_VERSION = 1;
   public static final class_2960 HANDSHAKE_CHANNEL = CUtil.rl("handshake");
   private static final class_9139<class_2540, ControlifyHandshake.HandshakePacket> handshakePacketCodec = class_9139.method_56437((buf, packet) -> {
      buf.method_53002(packet.protocolVersion());
   }, (buf) -> {
      return new ControlifyHandshake.HandshakePacket(buf.readInt());
   });

   public static void setupOnServer() {
      PlatformMainUtil.setupServersideHandshake(HANDSHAKE_CHANNEL, handshakePacketCodec, handshakePacketCodec, () -> {
         return new ControlifyHandshake.HandshakePacket(1);
      }, (packet, handler) -> {
         if (packet != null) {
            if (packet.protocolVersion() > 1) {
               handler.method_14380(class_2561.method_43470("Server has an old version of Controlify installed and is incompatible with this client.").method_27692(class_124.field_1061));
            } else if (packet.protocolVersion() < 1) {
               handler.method_14380(class_2561.method_43470("Client has an old version of Controlify installed and is incompatible with this server.").method_27692(class_124.field_1061));
            }

         }
      });
   }

   public static void setupOnClient() {
      PlatformClientUtil.setupClientsideHandshake(HANDSHAKE_CHANNEL, handshakePacketCodec, handshakePacketCodec, (inboundHandshake) -> {
         return new ControlifyHandshake.HandshakePacket(1);
      });
   }

   private static record HandshakePacket(int protocolVersion) {
      private HandshakePacket(int protocolVersion) {
         this.protocolVersion = protocolVersion;
      }

      public int protocolVersion() {
         return this.protocolVersion;
      }
   }
}
