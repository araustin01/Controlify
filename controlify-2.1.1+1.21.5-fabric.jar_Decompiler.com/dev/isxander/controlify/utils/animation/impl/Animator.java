package dev.isxander.controlify.utils.animation.impl;

import dev.isxander.controlify.utils.animation.api.Animatable;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;

public final class Animator {
   public static final Animator INSTANCE = new Animator();
   private final List<Animatable> animatables = new ObjectArrayList();

   private Animator() {
   }

   public void add(Animatable animatable) {
      this.animatables.add(animatable);
   }

   public void tick(float tickDelta) {
      this.animatables.removeIf((animatable) -> {
         animatable.tick(tickDelta);
         return animatable.isDone();
      });
   }
}
