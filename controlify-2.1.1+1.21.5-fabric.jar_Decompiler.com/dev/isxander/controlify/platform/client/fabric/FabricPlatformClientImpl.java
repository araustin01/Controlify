package dev.isxander.controlify.platform.client.fabric;

import dev.isxander.controlify.platform.client.CreativeTabHelper;
import dev.isxander.controlify.platform.client.HudRenderLayer;
import dev.isxander.controlify.platform.client.PlatformClientUtilImpl;
import dev.isxander.controlify.platform.client.events.DisconnectedEvent;
import dev.isxander.controlify.platform.client.events.LifecycleEvent;
import dev.isxander.controlify.platform.client.events.ScreenRenderEvent;
import dev.isxander.controlify.platform.client.events.TickEvent;
import dev.isxander.controlify.platform.client.resource.ControlifyReloadListener;
import dev.isxander.controlify.platform.fabric.mixins.KeyBindingRegistryImplAccessor;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientLoginNetworking;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.ResourcePackActivationType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.class_2540;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_304;
import net.minecraft.class_3264;
import net.minecraft.class_481;
import net.minecraft.class_9139;

public class FabricPlatformClientImpl implements PlatformClientUtilImpl {
   public void registerClientTickStarted(TickEvent event) {
      Event var10000 = ClientTickEvents.START_CLIENT_TICK;
      Objects.requireNonNull(event);
      var10000.register(event::onTick);
   }

   public void registerClientTickEnded(TickEvent event) {
      Event var10000 = ClientTickEvents.END_CLIENT_TICK;
      Objects.requireNonNull(event);
      var10000.register(event::onTick);
   }

   public void registerClientStopping(LifecycleEvent event) {
      Event var10000 = ClientLifecycleEvents.CLIENT_STOPPING;
      Objects.requireNonNull(event);
      var10000.register(event::onLifecycle);
   }

   public void registerClientDisconnected(DisconnectedEvent event) {
      ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
         event.onDisconnected(client);
      });
   }

   public void registerAssetReloadListener(ControlifyReloadListener reloadListener) {
      ResourceManagerHelper.get(class_3264.field_14188).registerReloadListener(reloadListener);
   }

   public void registerBuiltinResourcePack(class_2960 id, class_2561 displayName) {
      ResourceManagerHelper.registerBuiltinResourcePack(id, (ModContainer)FabricLoader.getInstance().getModContainer("controlify").orElseThrow(), displayName, ResourcePackActivationType.NORMAL);
   }

   public void registerPostScreenRender(ScreenRenderEvent event) {
      ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
         ScreenEvents.afterRender(screen).register((unused, graphics, mouseX, mouseY, tickDelta) -> {
            event.onRender(screen, graphics, mouseX, mouseY, tickDelta);
         });
      });
   }

   public void addHudLayer(class_2960 id, HudRenderLayer renderLayer) {
      HudLayerRegistrationCallback.EVENT.register((layeredDrawer) -> {
         Objects.requireNonNull(renderLayer);
         layeredDrawer.addLayer(IdentifiedLayer.of(id, renderLayer::render));
      });
   }

   public Collection<class_304> getModdedKeyMappings() {
      return KeyBindingRegistryImplAccessor.getCustomKeys();
   }

   public <I, O> void setupClientsideHandshake(class_2960 handshakeId, class_9139<class_2540, I> clientBoundCodec, class_9139<class_2540, O> serverBoundCodec, Function<I, O> handshakeHandler) {
      ClientLoginNetworking.registerGlobalReceiver(handshakeId, (client, handler, buf, listenerAdder) -> {
         I decodedInput = clientBoundCodec.decode(buf);
         O decodedOutput = handshakeHandler.apply(decodedInput);
         class_2540 encodedOutput = PacketByteBufs.create();
         serverBoundCodec.encode(encodedOutput, decodedOutput);
         return CompletableFuture.completedFuture(encodedOutput);
      });
   }

   public CreativeTabHelper createCreativeTabHelper(class_481 creativeScreen) {
      return new FAPIApiCreativeTabHelper(creativeScreen);
   }
}
