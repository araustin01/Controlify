package dev.isxander.controlify.mixins.feature.util;

import dev.isxander.controlify.Controlify;
import net.minecraft.class_310;
import net.minecraft.class_412;
import net.minecraft.class_639;
import net.minecraft.class_642;
import net.minecraft.class_9112;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_412.class})
public class ConnectScreenMixin {
   @Inject(
      method = {"method_2130(Lnet/minecraft/class_310;Lnet/minecraft/class_639;Lnet/minecraft/class_642;Lnet/minecraft/class_9112;)V"},
      at = {@At("HEAD")}
   )
   private void onConnect(class_310 client, class_639 address, @Nullable class_642 serverInfo, @Nullable class_9112 transferState, CallbackInfo ci) {
      Controlify.instance().notifyNewServer(serverInfo);
   }
}
