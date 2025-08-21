package dev.isxander.controlify.compatibility.yacl.mixins;

import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.api.buttonguide.ButtonGuideApi;
import dev.isxander.controlify.api.buttonguide.ButtonGuidePredicate;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.yacl3.gui.YACLScreen.CategoryTab;
import net.minecraft.class_4185;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({CategoryTab.class})
public class YACLScreenCategoryTabMixin {
   @Shadow
   @Final
   public class_4185 saveFinishedButton;

   @Inject(
      method = {"<init>(Ldev/isxander/yacl3/gui/YACLScreen;Ldev/isxander/yacl3/api/ConfigCategory;Lnet/minecraft/class_8030;)V"},
      at = {@At("RETURN")},
      require = 0
   )
   private void onConstructCategory(CallbackInfo ci) {
      ButtonGuideApi.addGuideToButton(this.saveFinishedButton, (InputBindingSupplier)ControlifyBindings.GUI_ABSTRACT_ACTION_1, ButtonGuidePredicate.always());
   }
}
