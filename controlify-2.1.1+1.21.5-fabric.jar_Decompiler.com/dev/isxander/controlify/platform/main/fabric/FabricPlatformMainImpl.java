package dev.isxander.controlify.platform.main.fabric;

import dev.isxander.controlify.api.entrypoint.ControlifyEntrypoint;
import dev.isxander.controlify.platform.Environment;
import dev.isxander.controlify.platform.main.PlatformMainUtilImpl;
import dev.isxander.controlify.platform.main.events.CommandRegistrationCallbackEvent;
import dev.isxander.controlify.platform.main.events.HandshakeCompletionEvent;
import dev.isxander.controlify.platform.main.events.PlayerJoinedEvent;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerLoginConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerLoginNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.class_2378;
import net.minecraft.class_2540;
import net.minecraft.class_2960;
import net.minecraft.class_9139;

public class FabricPlatformMainImpl implements PlatformMainUtilImpl {
   public void registerCommandRegistrationCallback(CommandRegistrationCallbackEvent callback) {
      Event var10000 = CommandRegistrationCallback.EVENT;
      Objects.requireNonNull(callback);
      var10000.register(callback::onRegister);
   }

   public void registerInitPlayConnectionEvent(PlayerJoinedEvent event) {
      ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
         event.onInit(handler.method_32311());
      });
   }

   public boolean isModLoaded(String... modIds) {
      Stream var10000 = Arrays.stream(modIds);
      FabricLoader var10001 = FabricLoader.getInstance();
      Objects.requireNonNull(var10001);
      return var10000.anyMatch(var10001::isModLoaded);
   }

   public void applyToControlifyEntrypoint(Consumer<ControlifyEntrypoint> entrypointConsumer) {
      FabricLoader.getInstance().getEntrypoints("controlify", ControlifyEntrypoint.class).forEach(entrypointConsumer);
   }

   public <I, O> void setupServersideHandshake(class_2960 handshakeId, class_9139<class_2540, I> serverBoundCodec, class_9139<class_2540, O> clientBoundCodec, Supplier<O> packetCreator, HandshakeCompletionEvent<I> completionEvent) {
      ServerLoginConnectionEvents.QUERY_START.register((handler, server, sender, synchronizer) -> {
         O decodedPacket = packetCreator.get();
         class_2540 encodedPacket = PacketByteBufs.create();
         clientBoundCodec.encode(encodedPacket, decodedPacket);
         sender.sendPacket(handshakeId, encodedPacket);
      });
      ServerLoginNetworking.registerGlobalReceiver(handshakeId, (server, handler, understood, buf, synchronizer, responseSender) -> {
         I decodedPacket = understood ? serverBoundCodec.decode(buf) : null;
         completionEvent.onCompletion(decodedPacket, handler);
      });
   }

   public <T> Supplier<T> deferredRegister(class_2378<T> registry, class_2960 id, Supplier<? extends T> registrant) {
      T registered = class_2378.method_10230(registry, id, registrant.get());
      return () -> {
         return registered;
      };
   }

   public Path getGameDir() {
      return FabricLoader.getInstance().getGameDir();
   }

   public Path getConfigDir() {
      return FabricLoader.getInstance().getConfigDir();
   }

   public boolean isDevEnv() {
      return FabricLoader.getInstance().isDevelopmentEnvironment();
   }

   public Environment getEnv() {
      Environment var10000;
      switch(FabricLoader.getInstance().getEnvironmentType()) {
      case CLIENT:
         var10000 = Environment.CLIENT;
         break;
      case SERVER:
         var10000 = Environment.SERVER;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public String getControlifyVersion() {
      return ((ModContainer)FabricLoader.getInstance().getModContainer("controlify").orElseThrow()).getMetadata().getVersion().getFriendlyString();
   }
}
