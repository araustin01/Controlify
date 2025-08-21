package dev.isxander.splitscreen.server.mixins.login;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Definitions;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.authlib.GameProfile;
import dev.isxander.splitscreen.server.login.LoginListenerStateHolder;
import dev.isxander.splitscreen.server.login.SplitscreenLoginFlowServer;
import java.util.Objects;
import net.minecraft.class_2535;
import net.minecraft.class_2596;
import net.minecraft.class_2905;
import net.minecraft.class_2915;
import net.minecraft.class_3248;
import net.minecraft.class_4844;
import net.minecraft.class_9812;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_3248.class})
public abstract class ServerLoginPacketListenerImplMixin implements LoginListenerStateHolder {
   @Shadow
   @Nullable
   private GameProfile field_45029;
   @Shadow
   @Final
   class_2535 field_14158;
   @Shadow
   @Final
   static Logger field_14166;
   @Shadow
   @Nullable
   String field_45028;
   @Unique
   private boolean firstFinishLoginPass = true;
   @Unique
   private boolean finishedFinishLogin = false;
   @Unique
   private boolean firstHelloPass = true;
   @Unique
   private boolean finishedHello = false;
   @Unique
   private final SplitscreenLoginFlowServer.ListenerState state = new SplitscreenLoginFlowServer.ListenerState();

   @Shadow
   protected abstract void method_52420(GameProfile var1);

   @Shadow
   abstract void method_52417(GameProfile var1);

   @ModifyExpressionValue(
      method = {"method_12641(Lnet/minecraft/class_2915;)V"},
      at = {@At("MIXINEXTRAS:EXPRESSION")}
   )
   @Definition(
      id = "ClientboundHelloPacket",
      type = {class_2905.class}
   )
   @Expression({"new ClientboundHelloPacket(?, ?, ?, @(true))"})
   private boolean modifyShouldRequestMojangAuth(boolean shouldRequestMojangAuth) {
      return shouldRequestMojangAuth && !this.state.passedSplitscreenAuth();
   }

   @Inject(
      method = {"method_12641(Lnet/minecraft/class_2915;)V"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/minecraft/class_3248;field_45028:Ljava/lang/String;",
   opcode = 181,
   shift = Shift.AFTER
)},
      cancellable = true
   )
   private void onHello(class_2915 packet, CallbackInfo ci) {
      if (this.firstHelloPass) {
         SplitscreenLoginFlowServer.startIdentifyFlow((class_3248)this, this.field_14158, packet);
         this.firstHelloPass = false;
         ci.cancel();
      }
   }

   @ModifyExpressionValue(
      method = {"method_12641(Lnet/minecraft/class_2915;)V"},
      at = {@At("MIXINEXTRAS:EXPRESSION")}
   )
   @Definitions({@Definition(
   id = "HELLO",
   field = {"Lnet/minecraft/class_3248$class_3249;field_14170:Lnet/minecraft/class_3248$class_3249;"}
), @Definition(
   id = "state",
   field = {"Lnet/minecraft/class_3248;field_14163:Lnet/minecraft/class_3248$class_3249;"}
)})
   @Expression({"this.state == HELLO"})
   private boolean shouldPassWithSecondHelloPass(boolean isHello) {
      if (!this.firstHelloPass && !this.finishedHello) {
         this.finishedHello = true;
         return true;
      } else {
         return isHello;
      }
   }

   @WrapOperation(
      method = {"method_52420(Lcom/mojang/authlib/GameProfile;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_2535;method_10743(Lnet/minecraft/class_2596;)V"
)}
   )
   private void onLoginComplete(class_2535 instance, class_2596<?> packet, Operation<Void> original, @Local(argsOnly = true) GameProfile profile) {
      class_3248 targetThis = (class_3248)this;
      if (this.firstFinishLoginPass && SplitscreenLoginFlowServer.onLoginComplete(targetThis, profile)) {
         this.firstFinishLoginPass = false;
      } else {
         this.finishedFinishLogin = true;
         original.call(new Object[]{instance, packet});
      }

   }

   @Inject(
      method = {"method_18784()V"},
      at = {@At("RETURN")}
   )
   private void checkIfLoginCanComplete(CallbackInfo ci) {
      if (!this.finishedFinishLogin && !this.firstFinishLoginPass && this.state.canFinishLogin()) {
         this.method_52420(this.field_45029);
      }

   }

   @Inject(
      method = {"method_10839(Lnet/minecraft/class_9812;)V"},
      at = {@At("HEAD")}
   )
   private void onDisconnect(class_9812 details, CallbackInfo ci) {
      if (this.field_45029 != null) {
         SplitscreenLoginFlowServer.onClientDisconnect(this.field_45029, details);
      }

   }

   @WrapOperation(
      method = {"method_12642(Lnet/minecraft/class_2917;)V"},
      at = {@At(
   value = "INVOKE",
   target = "Ljava/lang/Thread;start()V"
)}
   )
   private void wrapUserAuthThreadStart(Thread thread, Operation<Void> original) {
      if (this.state.passedSplitscreenAuth()) {
         GameProfile profile = class_4844.method_54140((String)Objects.requireNonNull(this.field_45028));
         field_14166.info("Allowing splitscreen player {} to join with UUID {}", profile.getName(), profile.getId());
         this.method_52417(profile);
      } else {
         original.call(new Object[]{thread});
      }

   }

   public SplitscreenLoginFlowServer.ListenerState splitscreen$state() {
      return this.state;
   }
}
