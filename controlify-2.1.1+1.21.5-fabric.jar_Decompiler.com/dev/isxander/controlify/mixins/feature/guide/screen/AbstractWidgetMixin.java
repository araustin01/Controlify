package dev.isxander.controlify.mixins.feature.guide.screen;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.gui.ButtonGuideRenderer;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.class_2561;
import net.minecraft.class_339;
import net.minecraft.class_6379;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_339.class})
public abstract class AbstractWidgetMixin implements ButtonGuideRenderer<class_339>, class_6379 {
   @Unique
   private ButtonGuideRenderer.RenderData<class_339> renderData = null;
   @Unique
   private final Map<InputBinding, class_2561> controllerMessages = new Object2ObjectArrayMap(2);

   @Shadow
   public abstract int method_46426();

   @Shadow
   public abstract int method_46427();

   @Shadow
   public abstract int method_25364();

   @Shadow
   public abstract int method_25368();

   @Shadow
   public abstract class_2561 method_25369();

   @Shadow
   public abstract boolean method_37303();

   @Inject(
      method = {"method_25355(Lnet/minecraft/class_2561;)V"},
      at = {@At("RETURN")}
   )
   protected void catchMessageSet(class_2561 message, CallbackInfo ci) {
      this.controllerMessages.clear();
   }

   @ModifyExpressionValue(
      method = {"method_49604(Lnet/minecraft/class_332;Lnet/minecraft/class_327;II)V"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/class_339;method_25369()Lnet/minecraft/class_2561;"
)}
   )
   protected class_2561 modifyRenderedMessage(class_2561 actualMessage) {
      return this.getControllerMessage(actualMessage);
   }

   public void controlify$setButtonGuide(ButtonGuideRenderer.RenderData<class_339> renderData) {
      this.renderData = renderData;
      this.controllerMessages.clear();
   }

   @Unique
   private class_2561 getControllerMessage(class_2561 actualLabel) {
      return !this.shouldRender() ? actualLabel : (class_2561)this.getBind().map((bind) -> {
         return (class_2561)this.controllerMessages.computeIfAbsent(bind, (b) -> {
            return this.renderData.getControllerMessage(b, actualLabel);
         });
      }).orElse(actualLabel);
   }

   @Unique
   protected boolean shouldRender() {
      return this.renderData != null && this.method_37303() && this.renderData.shouldRender((class_339)this);
   }

   @Unique
   private Optional<InputBinding> getBind() {
      return this.renderData == null ? Optional.empty() : this.renderData.getBind();
   }
}
