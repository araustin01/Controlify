package dev.isxander.splitscreen.server.play.sound;

import dev.isxander.splitscreen.server.BundledPacketInfo;
import dev.isxander.splitscreen.util.CSUtil;
import net.minecraft.class_2765;
import net.minecraft.class_8710;
import net.minecraft.class_9129;
import net.minecraft.class_9139;
import net.minecraft.class_8710.class_9154;
import org.jetbrains.annotations.NotNull;

public record ClientboundBundledSoundEntityPacket(BundledPacketInfo bundleInfo, class_2765 packet) implements class_8710 {
   public static final class_9139<class_9129, ClientboundBundledSoundEntityPacket> STREAM_CODEC;
   public static final class_9154<ClientboundBundledSoundEntityPacket> TYPE;

   public ClientboundBundledSoundEntityPacket(BundledPacketInfo bundleInfo, class_2765 packet) {
      this.bundleInfo = bundleInfo;
      this.packet = packet;
   }

   @NotNull
   public class_9154<ClientboundBundledSoundEntityPacket> method_56479() {
      return TYPE;
   }

   public BundledPacketInfo bundleInfo() {
      return this.bundleInfo;
   }

   public class_2765 packet() {
      return this.packet;
   }

   static {
      STREAM_CODEC = class_9139.method_56435(BundledPacketInfo.STREAM_CODEC, ClientboundBundledSoundEntityPacket::bundleInfo, class_2765.field_47994, ClientboundBundledSoundEntityPacket::packet, ClientboundBundledSoundEntityPacket::new);
      TYPE = new class_9154(CSUtil.rl("bundled_sound_entity"));
   }
}
