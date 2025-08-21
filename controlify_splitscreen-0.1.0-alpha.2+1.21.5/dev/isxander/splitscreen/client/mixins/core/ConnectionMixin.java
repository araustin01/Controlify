package dev.isxander.splitscreen.client.mixins.core;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import dev.isxander.splitscreen.client.ipc.ConnectionDisconnectPacketFactory;
import net.minecraft.class_2535;
import net.minecraft.class_2561;
import net.minecraft.class_2596;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_2535.class})
public class ConnectionMixin {
   @Shadow
   private volatile boolean field_48515;

   @ModifyExpressionValue(
      method = {"exceptionCaught(Lio/netty/channel/ChannelHandlerContext;Ljava/lang/Throwable;)V"},
      at = {@At("MIXINEXTRAS:EXPRESSION")}
   )
   @Definition(
      id = "send",
      method = {"Lnet/minecraft/class_2535;method_10752(Lnet/minecraft/class_2596;Lnet/minecraft/class_7648;)V"}
   )
   @Expression({"this.send(@(?), ?)"})
   private class_2596<?> modifyDisconnectPacket(class_2596<?> packet, @Local class_2561 reason, @Local(argsOnly = true) Throwable throwable) {
      if (this instanceof ConnectionDisconnectPacketFactory) {
         ConnectionDisconnectPacketFactory factory = (ConnectionDisconnectPacketFactory)this;
         return factory.createDisconnectPacket(throwable, reason, this.field_48515);
      } else {
         return packet;
      }
   }
}
