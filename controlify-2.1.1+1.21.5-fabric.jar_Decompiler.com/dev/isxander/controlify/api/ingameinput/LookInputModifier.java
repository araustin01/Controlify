package dev.isxander.controlify.api.ingameinput;

import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.platform.EventHandler;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import org.joml.Vector2f;

public record LookInputModifier(Vector2f lookInput, ControllerEntity controller) {
   public LookInputModifier(Vector2f lookInput, ControllerEntity controller) {
      this.lookInput = lookInput;
      this.controller = controller;
   }

   public static EventHandler.Callback<LookInputModifier> functional(BiFunction<Float, ControllerEntity, Float> x, BiFunction<Float, ControllerEntity, Float> y) {
      return (event) -> {
         event.lookInput.x = (Float)x.apply(event.lookInput.x, event.controller);
         event.lookInput.y = (Float)y.apply(event.lookInput.y, event.controller);
      };
   }

   static EventHandler.Callback<LookInputModifier> zeroIf(Predicate<ControllerEntity> condition) {
      return (event) -> {
         if (condition.test(event.controller)) {
            event.lookInput.set(0.0F, 0.0F);
         }

      };
   }

   public Vector2f lookInput() {
      return this.lookInput;
   }

   public ControllerEntity controller() {
      return this.controller;
   }
}
