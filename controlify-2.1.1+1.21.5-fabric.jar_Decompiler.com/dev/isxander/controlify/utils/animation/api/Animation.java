package dev.isxander.controlify.utils.animation.api;

import dev.isxander.controlify.utils.animation.impl.AnimationImpl;
import java.util.function.Consumer;

public interface Animation extends Animatable {
   static Animation of() {
      return new AnimationImpl();
   }

   static Animation of(int durationTicks) {
      return of().duration(durationTicks);
   }

   Animation consumerI(Consumer<Integer> var1, double var2, double var4);

   Animation consumerF(Consumer<Float> var1, double var2, double var4);

   Animation consumerD(Consumer<Double> var1, double var2, double var4);

   Animation deltaConsumerI(Consumer<Integer> var1, double var2, double var4);

   Animation deltaConsumerF(Consumer<Float> var1, double var2, double var4);

   Animation deltaConsumerD(Consumer<Double> var1, double var2, double var4);

   Animation duration(int var1);

   Animation easing(EasingFunction var1);

   Animation copy();

   Animation play();
}
