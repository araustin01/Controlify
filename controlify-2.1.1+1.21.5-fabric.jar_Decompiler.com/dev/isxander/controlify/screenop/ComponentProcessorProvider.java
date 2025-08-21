package dev.isxander.controlify.screenop;

import net.minecraft.class_364;

public interface ComponentProcessorProvider {
   Registry<class_364, ComponentProcessor> REGISTRY = new Registry();

   ComponentProcessor componentProcessor();

   static ComponentProcessor provide(class_364 component) {
      if (component instanceof ComponentProcessorProvider) {
         ComponentProcessorProvider provider = (ComponentProcessorProvider)component;
         return provider.componentProcessor();
      } else {
         return (ComponentProcessor)REGISTRY.get(component).orElse(ComponentProcessor.EMPTY);
      }
   }
}
