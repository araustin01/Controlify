package dev.isxander.controlify.server.packets;

import dev.isxander.controlify.utils.CUtil;
import net.minecraft.class_2540;
import net.minecraft.class_2960;
import net.minecraft.class_9139;

public record ServerPolicyPacket(String id, boolean allowed) {
   public static final class_2960 CHANNEL = CUtil.rl("server_policy");
   public static final class_9139<class_2540, ServerPolicyPacket> CODEC = class_9139.method_56437((buf, packet) -> {
      buf.method_10814(packet.id());
      buf.method_52964(packet.allowed());
   }, (buf) -> {
      return new ServerPolicyPacket(buf.method_19772(), buf.readBoolean());
   });

   public ServerPolicyPacket(String id, boolean allowed) {
      this.id = id;
      this.allowed = allowed;
   }

   public String id() {
      return this.id;
   }

   public boolean allowed() {
      return this.allowed;
   }
}
