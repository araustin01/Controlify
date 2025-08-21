package dev.isxander.controlify.utils.animation.api;

import dev.isxander.controlify.utils.animation.impl.AnimationSequenceImpl;

public interface AnimationSequence extends Animatable {
   static AnimationSequence of() {
      return new AnimationSequenceImpl();
   }

   static AnimationSequence of(Animatable... animatables) {
      return of().push(animatables);
   }

   AnimationSequence push(Animatable... var1);

   AnimationSequence play();

   AnimationSequence copy();
}
