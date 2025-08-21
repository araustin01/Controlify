package dev.isxander.splitscreen.server.mixins.login;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.class_2535;
import net.minecraft.class_2547;
import net.minecraft.class_3240;
import net.minecraft.class_9099;
import net.minecraft.class_9127;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_3240.class})
public class MemoryServerHandshakePacketListenerImplMixin {
   @WrapOperation(
      method = {"method_12576(Lnet/minecraft/class_2889;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_2535;method_56330(Lnet/minecraft/class_9127;Lnet/minecraft/class_2547;)V"
)}
   )
   private <T extends class_2547> void setOutboundBeforeInbound(class_2535 connection, class_9127<T> protocol, T packetListener, Operation<Void> original) {
      connection.method_56329(class_9099.field_48248);
      original.call(new Object[]{connection, protocol, packetListener});
   }

   @WrapWithCondition(
      method = {"method_12576(Lnet/minecraft/class_2889;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_2535;method_56329(Lnet/minecraft/class_9127;)V"
)}
   )
   private boolean shouldRunDupSetOutbound(class_2535 instance, class_9127<?> protocol) {
      return false;
   }
}
