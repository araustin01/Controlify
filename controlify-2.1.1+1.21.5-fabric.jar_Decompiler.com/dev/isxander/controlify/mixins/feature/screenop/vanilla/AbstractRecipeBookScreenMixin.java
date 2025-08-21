package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import dev.isxander.controlify.screenop.compat.vanilla.RecipeBookScreenProcessor;
import net.minecraft.class_10260;
import net.minecraft.class_507;
import net.minecraft.class_518;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_10260.class})
public abstract class AbstractRecipeBookScreenMixin extends AbstractContainerScreenMixin implements ScreenProcessorProvider, class_518, RecipeBookScreenProcessor.RecipeBookScreenAccessor {
   @Unique
   private final RecipeBookScreenProcessor<?> processor = new RecipeBookScreenProcessor((class_10260)this, this, () -> {
      return this.field_2787;
   }, this::method_2383, this::handleControllerItemSlotActions);
   @Shadow
   @Final
   private class_507<?> field_54474;

   @Unique
   private class_507<?> getRecipeBookComponent() {
      return this.field_54474;
   }

   public class_507<?> controlify$getRecipeBookComponent() {
      return this.getRecipeBookComponent();
   }

   public ScreenProcessor<?> screenProcessor() {
      return this.processor;
   }
}
