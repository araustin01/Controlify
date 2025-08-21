package dev.isxander.controlify.platform.client;

import dev.isxander.controlify.platform.client.events.DisconnectedEvent;
import dev.isxander.controlify.platform.client.events.LifecycleEvent;
import dev.isxander.controlify.platform.client.events.ScreenRenderEvent;
import dev.isxander.controlify.platform.client.events.TickEvent;
import dev.isxander.controlify.platform.client.fabric.FabricPlatformClientImpl;
import dev.isxander.controlify.platform.client.resource.ControlifyReloadListener;
import java.util.Collection;
import java.util.function.Function;
import net.minecraft.class_2540;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_304;
import net.minecraft.class_481;
import net.minecraft.class_9139;

public final class PlatformClientUtil {
   private static final PlatformClientUtilImpl IMPL = new FabricPlatformClientImpl();

   public static void registerClientTickStarted(TickEvent event) {
      IMPL.registerClientTickStarted(event);
   }

   public static void registerClientTickEnded(TickEvent event) {
      IMPL.registerClientTickEnded(event);
   }

   public static void registerClientStopping(LifecycleEvent event) {
      IMPL.registerClientStopping(event);
   }

   public static void registerClientDisconnected(DisconnectedEvent event) {
      IMPL.registerClientDisconnected(event);
   }

   public static void registerAssetReloadListener(ControlifyReloadListener reloadListener) {
      IMPL.registerAssetReloadListener(reloadListener);
   }

   public static void registerBuiltinResourcePack(class_2960 id, class_2561 displayName) {
      IMPL.registerBuiltinResourcePack(id, displayName);
   }

   public static void registerPostScreenRender(ScreenRenderEvent event) {
      IMPL.registerPostScreenRender(event);
   }

   public static void addHudLayer(class_2960 id, HudRenderLayer layer) {
      IMPL.addHudLayer(id, layer);
   }

   public static Collection<class_304> getModdedKeyMappings() {
      return IMPL.getModdedKeyMappings();
   }

   public static <I, O> void setupClientsideHandshake(class_2960 handshakeId, class_9139<class_2540, I> clientBoundCodec, class_9139<class_2540, O> serverBoundCodec, Function<I, O> handshakeHandler) {
      IMPL.setupClientsideHandshake(handshakeId, clientBoundCodec, serverBoundCodec, handshakeHandler);
   }

   public static CreativeTabHelper createCreativeTabHelper(class_481 creativeScreen) {
      return IMPL.createCreativeTabHelper(creativeScreen);
   }

   private PlatformClientUtil() {
   }
}
