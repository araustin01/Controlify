package dev.isxander.splitscreen.server.play.sound;

import dev.isxander.splitscreen.server.BundledPacketInfo;
import dev.isxander.splitscreen.util.CSUtil;
import net.minecraft.class_2767;
import net.minecraft.class_8710;
import net.minecraft.class_9129;
import net.minecraft.class_9139;
import net.minecraft.class_8710.class_9154;
import org.jetbrains.annotations.NotNull;

public record ClientboundBundledSoundPacket(BundledPacketInfo bundleInfo, class_2767 packet) implements class_8710 {
   public static final class_9139<class_9129, ClientboundBundledSoundPacket> STREAM_CODEC;
   public static final class_9154<ClientboundBundledSoundPacket> TYPE;

   public ClientboundBundledSoundPacket(BundledPacketInfo bundleInfo, class_2767 packet) {
      this.bundleInfo = bundleInfo;
      this.packet = packet;
   }

   @NotNull
   public class_9154<ClientboundBundledSoundPacket> method_56479() {
      return TYPE;
   }

   public BundledPacketInfo bundleInfo() {
      return this.bundleInfo;
   }

   public class_2767 packet() {
      return this.packet;
   }

   static {
      STREAM_CODEC = class_9139.method_56435(BundledPacketInfo.STREAM_CODEC, ClientboundBundledSoundPacket::bundleInfo, class_2767.field_47995, ClientboundBundledSoundPacket::packet, ClientboundBundledSoundPacket::new);
      TYPE = new class_9154(CSUtil.rl("bundled_sound"));
   }
}
