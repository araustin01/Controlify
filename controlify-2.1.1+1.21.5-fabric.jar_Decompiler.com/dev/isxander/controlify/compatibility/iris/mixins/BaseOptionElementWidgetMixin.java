package dev.isxander.controlify.compatibility.iris.mixins;

import dev.isxander.controlify.compatibility.iris.screenop.BaseOptionElementComponentProcessor;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ComponentProcessorProvider;
import net.irisshaders.iris.gui.NavigationController;
import net.irisshaders.iris.gui.element.widget.BaseOptionElementWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({BaseOptionElementWidget.class})
public abstract class BaseOptionElementWidgetMixin implements ComponentProcessorProvider {
   @Shadow
   protected NavigationController navigation;
   @Unique
   private final BaseOptionElementComponentProcessor processor = new BaseOptionElementComponentProcessor(this::cycle);

   @Shadow
   public abstract boolean applyPreviousValue();

   @Shadow
   public abstract boolean applyNextValue();

   public ComponentProcessor componentProcessor() {
      return this.processor;
   }

   @Unique
   private void cycle(boolean reverse) {
      boolean needsUpdate = reverse ? this.applyPreviousValue() : this.applyNextValue();
      if (needsUpdate) {
         this.navigation.refresh();
      }

   }
}
