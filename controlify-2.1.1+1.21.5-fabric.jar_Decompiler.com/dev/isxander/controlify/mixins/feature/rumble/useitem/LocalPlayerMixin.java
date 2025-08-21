package dev.isxander.controlify.mixins.feature.rumble.useitem;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.rumble.ContinuousRumbleEffect;
import dev.isxander.controlify.rumble.RumbleSource;
import dev.isxander.controlify.rumble.RumbleState;
import dev.isxander.controlify.rumble.effects.UseItemEffectHolder;
import net.minecraft.class_1268;
import net.minecraft.class_1753;
import net.minecraft.class_1764;
import net.minecraft.class_1799;
import net.minecraft.class_746;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_746.class})
public abstract class LocalPlayerMixin extends LivingEntityMixin implements UseItemEffectHolder {
   @Unique
   private ContinuousRumbleEffect useItemRumble;

   protected void onStartUsingItem(class_1268 hand, CallbackInfo ci, class_1799 stack) {
      switch(stack.method_7976()) {
      case field_8953:
         this.startRumble(ContinuousRumbleEffect.builder().byTick((tick) -> {
            return new RumbleState(tick % 7 <= 3 && tick > 20 ? 0.1F : 0.0F, class_1753.method_7722(tick));
         }).build());
         break;
      case field_8947:
         int chargeDuration = class_1764.method_7775(stack, (class_746)this);
         this.startRumble(ContinuousRumbleEffect.builder().byTick((tick) -> {
            return new RumbleState(0.0F, (float)tick / (float)chargeDuration);
         }).timeout(chargeDuration).build());
         break;
      case field_8949:
      case field_27079:
         this.startRumble(ContinuousRumbleEffect.builder().byTick((tick) -> {
            return new RumbleState(0.0F, (float)(tick % 4) / 4.0F * 0.12F + 0.05F);
         }).build());
         break;
      case field_8950:
      case field_8946:
         this.startRumble(ContinuousRumbleEffect.builder().constant(0.1F, 0.2F).build());
         break;
      case field_39058:
         this.startRumble(ContinuousRumbleEffect.builder().byTick((tick) -> {
            return new RumbleState(Math.min(1.0F, (float)tick / 10.0F), 0.25F);
         }).build());
         break;
      case field_8951:
         this.startRumble(ContinuousRumbleEffect.builder().constant(0.3F, 0.3F).build());
      }

   }

   protected void onUpdateUsingItem(class_1799 stack, CallbackInfo ci) {
   }

   protected void onStopUsingItem(CallbackInfo ci) {
      if (this.useItemRumble != null) {
         this.useItemRumble.stop();
         this.useItemRumble = null;
      }

   }

   @Unique
   private void startRumble(ContinuousRumbleEffect effect) {
      ControlifyApi.get().getCurrentController().flatMap(ControllerEntity::rumble).ifPresent((controller) -> {
         controller.rumbleManager().play(RumbleSource.INTERACTION, effect);
         this.useItemRumble = effect;
      });
   }

   @Nullable
   public ContinuousRumbleEffect controlify$getUseItemEffect() {
      return this.useItemRumble;
   }
}
