package dev.isxander.controlify.mixins.feature.rumble.waterland;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.rumble.BasicRumbleEffect;
import dev.isxander.controlify.rumble.RumbleSource;
import dev.isxander.controlify.rumble.RumbleState;
import java.util.Objects;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_746.class})
public abstract class LocalPlayerMixin extends PlayerMixin {
   protected LocalPlayerMixin(class_1299<? extends class_1309> entityType, class_1937 level) {
      super(entityType, level);
   }

   protected void splashRumble(CallbackInfo ci) {
      if (!this.method_7325()) {
         ControlifyApi.get().getCurrentController().flatMap(ControllerEntity::rumble).ifPresent((rumble) -> {
            class_1297 entity = (class_1297)Objects.requireNonNullElse(this.method_5642(), this);
            float f = entity == this ? 0.2F : 0.9F;
            class_243 vec3 = entity.method_18798();
            float impactForce = Math.min(1.0F, (float)Math.sqrt(vec3.field_1352 * vec3.field_1352 * 0.20000000298023224D + vec3.field_1351 * vec3.field_1351 + vec3.field_1350 * vec3.field_1350 * 0.20000000298023224D) * f);
            if (impactForce >= 0.05F) {
               float multiplier = Math.min(1.0F, impactForce / 0.5F);
               rumble.rumbleManager().play(RumbleSource.PLAYER, BasicRumbleEffect.byTime((t) -> {
                  return new RumbleState(multiplier * (1.0F - t), multiplier * 0.5F);
               }, impactForce < 0.25F ? 10 : 20));
            }

         });
      }
   }
}
