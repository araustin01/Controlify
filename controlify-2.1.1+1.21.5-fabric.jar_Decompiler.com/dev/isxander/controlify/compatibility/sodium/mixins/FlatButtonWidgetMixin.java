package dev.isxander.controlify.compatibility.sodium.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.gui.ButtonGuideRenderer;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.Map;
import java.util.Optional;
import net.caffeinemc.mods.sodium.client.gui.widgets.FlatButtonWidget;
import net.minecraft.class_2561;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({FlatButtonWidget.class})
public class FlatButtonWidgetMixin implements ButtonGuideRenderer<FlatButtonWidget> {
   @Shadow
   private boolean enabled;
   @Shadow
   private boolean visible;
   @Unique
   private ButtonGuideRenderer.RenderData<FlatButtonWidget> renderData = null;
   @Unique
   private final Map<InputBinding, class_2561> controllerMessages = new Object2ObjectArrayMap(2);

   @ModifyExpressionValue(
      method = {"method_25394(Lnet/minecraft/class_332;IIF)V"},
      at = {@At(
   value = "FIELD",
   target = "Lnet/caffeinemc/mods/sodium/client/gui/widgets/FlatButtonWidget;label:Lnet/minecraft/class_2561;",
   opcode = 180
)}
   )
   private class_2561 modifyRenderedLabel(class_2561 actualLabel) {
      return this.getControllerMessage(actualLabel);
   }

   @Inject(
      method = {"setLabel(Lnet/minecraft/class_2561;)V"},
      at = {@At("HEAD")}
   )
   private void removeLabelCache(CallbackInfo ci) {
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
      return this.renderData != null && this.enabled && this.visible && this.renderData.shouldRender((FlatButtonWidget)this);
   }

   @Unique
   private Optional<InputBinding> getBind() {
      return this.renderData == null ? Optional.empty() : this.renderData.getBind();
   }

   public void controlify$setButtonGuide(ButtonGuideRenderer.RenderData<FlatButtonWidget> renderData) {
      this.renderData = renderData;
      this.controllerMessages.clear();
   }
}
