package dev.isxander.splitscreen.server.mixins.play;

import com.google.common.collect.Lists;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import dev.isxander.splitscreen.server.PacketBundler;
import dev.isxander.splitscreen.server.SplitscreenPlayerInfo;
import dev.isxander.splitscreen.server.SplitscreenSSServer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_2596;
import net.minecraft.class_3222;
import net.minecraft.class_3244;
import net.minecraft.class_3324;
import net.minecraft.class_5321;
import net.minecraft.class_8710;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   value = {class_3324.class},
   priority = 2000
)
public class PlayerListMixin {
   @Inject(
      method = {"method_14605(Lnet/minecraft/class_1657;DDDDLnet/minecraft/class_5321;Lnet/minecraft/class_2596;)V"},
      at = {@At("HEAD")}
   )
   private void insertListAtHead(CallbackInfo ci, @Share("players_to_send") LocalRef<List<class_3222>> playersToSendRef, @Share("packet_bundler") LocalRef<PacketBundler<?, ?>> packetBundlerRef, @Local(argsOnly = true) class_2596<?> packet) {
      SplitscreenSSServer.getBundler(packet.getClass()).ifPresent((bundler) -> {
         packetBundlerRef.set(bundler);
         playersToSendRef.set(new ArrayList());
      });
   }

   @WrapOperation(
      method = {"method_14605(Lnet/minecraft/class_1657;DDDDLnet/minecraft/class_5321;Lnet/minecraft/class_2596;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_3244;method_14364(Lnet/minecraft/class_2596;)V"
)}
   )
   private void collectSends(class_3244 instance, class_2596<?> packet, Operation<Void> original, @Local class_3222 player, @Share("players_to_send") LocalRef<List<class_3222>> playersToSendRef) {
      if (playersToSendRef.get() == null) {
         original.call(new Object[]{instance, packet});
      } else {
         SplitscreenPlayerInfo.get(player).ifPresentOrElse((info) -> {
            ((List)playersToSendRef.get()).add(player);
         }, () -> {
            original.call(new Object[]{instance, packet});
         });
      }
   }

   @Inject(
      method = {"method_14605(Lnet/minecraft/class_1657;DDDDLnet/minecraft/class_5321;Lnet/minecraft/class_2596;)V"},
      at = {@At("RETURN")}
   )
   private void bundlePackets(class_1657 except, double x, double y, double z, double radius, class_5321<class_1937> dimension, class_2596<?> packet, CallbackInfo ci, @Share("players_to_send") LocalRef<List<class_3222>> playersToSendRef, @Share("packet_bundler") LocalRef<PacketBundler<?, ?>> packetBundlerRef) {
      if (playersToSendRef.get() != null) {
         HashMap<SplitscreenPlayerInfo.Controller, List<class_3222>> map = new HashMap();
         Iterator var16 = ((List)playersToSendRef.get()).iterator();

         while(var16.hasNext()) {
            class_3222 player = (class_3222)var16.next();
            SplitscreenPlayerInfo var10000 = (SplitscreenPlayerInfo)SplitscreenPlayerInfo.get(player).orElseThrow();
            Objects.requireNonNull(var10000);
            SplitscreenPlayerInfo var18 = var10000;
            byte var19 = 0;
            switch(var18.typeSwitch<invokedynamic>(var18, var19)) {
            case 0:
               SplitscreenPlayerInfo.Controller controller = (SplitscreenPlayerInfo.Controller)var18;
               map.put(controller, Lists.newArrayList(new class_3222[]{player}));
               break;
            case 1:
               SplitscreenPlayerInfo.SubPlayer subPlayer = (SplitscreenPlayerInfo.SubPlayer)var18;
               ((List)map.computeIfAbsent(subPlayer.controller(), (c) -> {
                  return new ArrayList();
               })).add(player);
               break;
            default:
               throw new MatchException((String)null, (Throwable)null);
            }
         }

         map.forEach((controllerx, players) -> {
            class_8710 payload = this.doBundle(packet, (PacketBundler)packetBundlerRef.get(), x, y, z, radius, dimension, controllerx, players);
            ServerPlayNetworking.send(controllerx.player(), payload);
         });
      }
   }

   @Unique
   private <T extends class_2596<?>> class_8710 doBundle(class_2596<?> packet, PacketBundler<T, ?> bundler, double x, double y, double z, double radius, class_5321<class_1937> dimension, SplitscreenPlayerInfo.Controller controller, List<class_3222> players) {
      return bundler.bundle(packet, x, y, z, radius, dimension, controller, players);
   }
}
