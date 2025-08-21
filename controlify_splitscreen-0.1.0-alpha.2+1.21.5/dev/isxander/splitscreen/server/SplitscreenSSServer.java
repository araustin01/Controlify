package dev.isxander.splitscreen.server;

import dev.isxander.splitscreen.server.login.SplitscreenLoginFlowServer;
import dev.isxander.splitscreen.server.play.sound.ClientboundBundledSoundEntityPacket;
import dev.isxander.splitscreen.server.play.sound.ClientboundBundledSoundPacket;
import dev.isxander.splitscreen.server.play.sound.ClientboundSetBundleStatePacket;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.class_2765;
import net.minecraft.class_2767;
import net.minecraft.class_9129;

public class SplitscreenSSServer {
   private static final Map<Class<?>, PacketBundler<?, ?>> BUNDLERS = new HashMap();

   public static void init() {
      registerPackets();
      SplitscreenLoginFlowServer.init();
   }

   private static void registerPackets() {
      PayloadTypeRegistry<class_9129> clientbound = PayloadTypeRegistry.playS2C();
      PayloadTypeRegistry<class_9129> serverbound = PayloadTypeRegistry.playC2S();
      clientbound.register(ClientboundSetBundleStatePacket.TYPE, ClientboundSetBundleStatePacket.STREAM_CODEC);
      clientbound.register(ClientboundBundledSoundPacket.TYPE, ClientboundBundledSoundPacket.STREAM_CODEC);
      registerBundler(new PacketBundler.Simple(class_2767.class, ClientboundBundledSoundPacket::new));
      clientbound.register(ClientboundBundledSoundEntityPacket.TYPE, ClientboundBundledSoundEntityPacket.STREAM_CODEC);
      registerBundler(new PacketBundler.Simple(class_2765.class, ClientboundBundledSoundEntityPacket::new));
   }

   public static Optional<PacketBundler<?, ?>> getBundler(Class<?> clazz) {
      return Optional.ofNullable((PacketBundler)BUNDLERS.get(clazz));
   }

   private static void registerBundler(PacketBundler<?, ?> bundler) {
      BUNDLERS.put(bundler.packetClass(), bundler);
   }
}
