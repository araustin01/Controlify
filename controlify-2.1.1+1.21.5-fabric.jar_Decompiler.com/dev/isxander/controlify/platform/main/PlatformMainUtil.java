package dev.isxander.controlify.platform.main;

import dev.isxander.controlify.api.entrypoint.ControlifyEntrypoint;
import dev.isxander.controlify.platform.Environment;
import dev.isxander.controlify.platform.main.events.CommandRegistrationCallbackEvent;
import dev.isxander.controlify.platform.main.events.HandshakeCompletionEvent;
import dev.isxander.controlify.platform.main.events.PlayerJoinedEvent;
import dev.isxander.controlify.platform.main.fabric.FabricPlatformMainImpl;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.class_2378;
import net.minecraft.class_2540;
import net.minecraft.class_2960;
import net.minecraft.class_9139;

public final class PlatformMainUtil {
   private static final PlatformMainUtilImpl IMPL = new FabricPlatformMainImpl();

   public static void registerCommandRegistrationCallback(CommandRegistrationCallbackEvent callback) {
      IMPL.registerCommandRegistrationCallback(callback);
   }

   public static void registerPlayerJoinedEvent(PlayerJoinedEvent event) {
      IMPL.registerInitPlayConnectionEvent(event);
   }

   public static boolean isModLoaded(String... modIds) {
      return IMPL.isModLoaded(modIds);
   }

   public static void applyToControlifyEntrypoint(Consumer<ControlifyEntrypoint> entrypointConsumer) {
      IMPL.applyToControlifyEntrypoint(entrypointConsumer);
   }

   public static <I, O> void setupServersideHandshake(class_2960 handshakeId, class_9139<class_2540, I> serverBoundCodec, class_9139<class_2540, O> clientBoundCodec, Supplier<O> packetCreator, HandshakeCompletionEvent<I> completionEvent) {
      IMPL.setupServersideHandshake(handshakeId, serverBoundCodec, clientBoundCodec, packetCreator, completionEvent);
   }

   public static <T> Supplier<T> deferredRegister(class_2378<T> registry, class_2960 id, Supplier<? extends T> registrant) {
      return IMPL.deferredRegister(registry, id, registrant);
   }

   public static Path getGameDir() {
      return IMPL.getGameDir();
   }

   public static Path getConfigDir() {
      return IMPL.getConfigDir();
   }

   public static boolean isDevEnv() {
      return IMPL.isDevEnv();
   }

   public static Environment getEnv() {
      return IMPL.getEnv();
   }

   public static String getControlifyVersion() {
      return IMPL.getControlifyVersion();
   }
}
