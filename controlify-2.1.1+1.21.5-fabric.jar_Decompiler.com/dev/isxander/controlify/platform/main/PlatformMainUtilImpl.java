package dev.isxander.controlify.platform.main;

import dev.isxander.controlify.api.entrypoint.ControlifyEntrypoint;
import dev.isxander.controlify.platform.Environment;
import dev.isxander.controlify.platform.main.events.CommandRegistrationCallbackEvent;
import dev.isxander.controlify.platform.main.events.HandshakeCompletionEvent;
import dev.isxander.controlify.platform.main.events.PlayerJoinedEvent;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.class_2378;
import net.minecraft.class_2540;
import net.minecraft.class_2960;
import net.minecraft.class_9139;

public interface PlatformMainUtilImpl {
   void registerCommandRegistrationCallback(CommandRegistrationCallbackEvent var1);

   void registerInitPlayConnectionEvent(PlayerJoinedEvent var1);

   boolean isModLoaded(String... var1);

   Path getGameDir();

   Path getConfigDir();

   boolean isDevEnv();

   Environment getEnv();

   String getControlifyVersion();

   void applyToControlifyEntrypoint(Consumer<ControlifyEntrypoint> var1);

   <I, O> void setupServersideHandshake(class_2960 var1, class_9139<class_2540, I> var2, class_9139<class_2540, O> var3, Supplier<O> var4, HandshakeCompletionEvent<I> var5);

   <T> Supplier<T> deferredRegister(class_2378<T> var1, class_2960 var2, Supplier<? extends T> var3);
}
