package dev.isxander.controlify.api.bind;

import dev.isxander.controlify.bindings.BindContext;
import dev.isxander.controlify.bindings.ControlifyBindApiImpl;
import dev.isxander.controlify.controller.ControllerEntity;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import net.minecraft.class_2960;
import net.minecraft.class_304;

public interface ControlifyBindApi {
   static ControlifyBindApi get() {
      return ControlifyBindApiImpl.INSTANCE;
   }

   InputBindingSupplier registerBinding(ControlifyBindApi.RegistryCallback var1);

   InputBindingSupplier registerBinding(ControlifyBindApi.RegistryCallback var1, Predicate<ControllerEntity> var2);

   List<InputBindingSupplier> getKeyCorrelation(class_304 var1);

   void registerRadialIcon(class_2960 var1, RadialIcon var2);

   void registerBindContext(BindContext var1);

   Stream<class_2960> getAllBindIds();

   @FunctionalInterface
   public interface RegistryCallback extends UnaryOperator<InputBindingBuilder> {
   }
}
