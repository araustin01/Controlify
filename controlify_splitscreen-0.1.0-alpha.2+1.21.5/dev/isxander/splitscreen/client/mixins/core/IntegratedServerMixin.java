package dev.isxander.splitscreen.client.mixins.core;

import com.mojang.datafixers.DataFixer;
import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import java.net.Proxy;
import net.minecraft.class_1132;
import net.minecraft.class_3283;
import net.minecraft.class_3950;
import net.minecraft.class_6904;
import net.minecraft.class_7497;
import net.minecraft.class_32.class_5143;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_1132.class})
public abstract class IntegratedServerMixin extends MinecraftServer {
   public IntegratedServerMixin(Thread serverThread, class_5143 storageSource, class_3283 packRepository, class_6904 worldStem, Proxy proxy, DataFixer fixerUpper, class_7497 services, class_3950 progressListenerFactory) {
      super(serverThread, storageSource, packRepository, worldStem, proxy, fixerUpper, services, progressListenerFactory);
   }

   @Inject(
      method = {"method_36439()V"},
      at = {@At("HEAD")}
   )
   private void tickConnectionWhenPaused(CallbackInfo ci) {
      if (SplitscreenBootstrapper.isSplitscreen()) {
         this.method_61254();
      }

   }
}
