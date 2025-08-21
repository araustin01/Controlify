package dev.isxander.controlify.platform.client;

import dev.isxander.controlify.platform.client.events.DisconnectedEvent;
import dev.isxander.controlify.platform.client.events.LifecycleEvent;
import dev.isxander.controlify.platform.client.events.ScreenRenderEvent;
import dev.isxander.controlify.platform.client.events.TickEvent;
import dev.isxander.controlify.platform.client.resource.ControlifyReloadListener;
import java.util.Collection;
import java.util.function.Function;
import net.minecraft.class_2540;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_304;
import net.minecraft.class_481;
import net.minecraft.class_9139;

public interface PlatformClientUtilImpl {
   void registerClientTickStarted(TickEvent var1);

   void registerClientTickEnded(TickEvent var1);

   void registerClientStopping(LifecycleEvent var1);

   void registerClientDisconnected(DisconnectedEvent var1);

   void registerAssetReloadListener(ControlifyReloadListener var1);

   void registerBuiltinResourcePack(class_2960 var1, class_2561 var2);

   void addHudLayer(class_2960 var1, HudRenderLayer var2);

   void registerPostScreenRender(ScreenRenderEvent var1);

   Collection<class_304> getModdedKeyMappings();

   <I, O> void setupClientsideHandshake(class_2960 var1, class_9139<class_2540, I> var2, class_9139<class_2540, O> var3, Function<I, O> var4);

   CreativeTabHelper createCreativeTabHelper(class_481 var1);
}
