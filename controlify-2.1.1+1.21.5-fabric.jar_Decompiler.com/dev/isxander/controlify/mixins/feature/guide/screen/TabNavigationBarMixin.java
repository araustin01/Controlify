package dev.isxander.controlify.mixins.feature.guide.screen;

import com.google.common.collect.ImmutableList;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.GenericControllerConfig;
import java.util.Objects;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_8089;
import net.minecraft.class_8209;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_8089.class})
public class TabNavigationBarMixin {
   @Shadow
   @Final
   private ImmutableList<class_8209> field_42148;

   @Inject(
      method = {"method_25394(Lnet/minecraft/class_332;IIF)V"},
      at = {@At("RETURN")}
   )
   private void renderControllerButtonOverlay(class_332 graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
      if (Controlify.instance().currentInputMode().isController()) {
         Controlify.instance().getCurrentController().ifPresent((c) -> {
            if (((GenericControllerConfig)c.genericConfig().config()).showScreenGuides) {
               this.renderControllerButtonOverlay(graphics, c);
            }

         });
      }

   }

   @Unique
   private void renderControllerButtonOverlay(class_332 graphics, ControllerEntity controller) {
      if (this.field_42148.size() > 1) {
         class_8209 firstTab = (class_8209)this.field_42148.get(0);
         class_8209 lastTab = (class_8209)this.field_42148.get(this.field_42148.size() - 1);
         class_327 font = class_310.method_1551().field_1772;
         class_2561 prevTabText = ControlifyBindings.GUI_PREV_TAB.on(controller).inputIcon();
         int prevTabTextWidth = font.method_27525(prevTabText);
         int var10003 = firstTab.method_46426() - 2 - prevTabTextWidth;
         int var10004 = firstTab.method_46427() / 2;
         Objects.requireNonNull(font);
         graphics.method_27535(font, prevTabText, var10003, var10004 + 9 / 2, 16777215);
         class_2561 nextTabText = ControlifyBindings.GUI_NEXT_TAB.on(controller).inputIcon();
         var10003 = lastTab.method_46426() + lastTab.method_25368() + 2;
         var10004 = lastTab.method_46427() / 2;
         Objects.requireNonNull(font);
         graphics.method_27535(font, nextTabText, var10003, var10004 + 9 / 2, 16777215);
      }
   }
}
