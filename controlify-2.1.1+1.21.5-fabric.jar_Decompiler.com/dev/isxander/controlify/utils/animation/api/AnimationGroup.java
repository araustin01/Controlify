package dev.isxander.controlify.utils.animation.api;

import dev.isxander.controlify.utils.animation.impl.AnimationGroupImpl;

public interface AnimationGroup extends Animatable {
   static AnimationGroup of() {
      return new AnimationGroupImpl();
   }

   static AnimationGroup of(Animatable... animation) {
      return of().add(animation);
   }

   AnimationGroup add(Animatable... var1);

   AnimationGroup play();

   AnimationGroup copy();
}
