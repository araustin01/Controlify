package dev.isxander.controlify.utils.animation.impl;

import dev.isxander.controlify.utils.animation.api.Animatable;
import dev.isxander.controlify.utils.animation.api.AnimationGroup;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.Validate;

public class AnimationGroupImpl implements AnimationGroup {
   private final Set<Animatable> animatables = new ObjectArraySet();
   private boolean started;
   private boolean done;

   public AnimationGroup add(Animatable... animation) {
      Validate.isTrue(!this.hasStarted(), "Cannot add to group that has already started.", new Object[0]);
      Animatable[] var2 = animation;
      int var3 = animation.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Animatable animatable = var2[var4];
         Validate.isTrue(!animatable.hasStarted(), "Cannot add an animation that has already started!", new Object[0]);
      }

      this.animatables.addAll(List.of(animation));
      return this;
   }

   public void tick(float tickDelta) {
      if (!this.done) {
         this.started = true;
         this.done = !this.animatables.stream().noneMatch((animatable) -> {
            animatable.tick(tickDelta);
            return animatable.isDone();
         });
      }
   }

   public void skipToEnd() {
      this.animatables.forEach(Animatable::skipToEnd);
      this.done = true;
      this.started = true;
   }

   public void abort() {
      this.animatables.forEach(Animatable::abort);
      this.done = true;
      this.started = true;
   }

   public boolean hasStarted() {
      return this.started;
   }

   public boolean isDone() {
      return this.done;
   }

   public boolean isPlaying() {
      return this.started && !this.done;
   }

   public AnimationGroup play() {
      Animator.INSTANCE.add(this);
      return this;
   }

   public AnimationGroup copy() {
      AnimationGroup group = AnimationGroup.of();
      this.animatables.forEach((animatable) -> {
         group.add(animatable.copy());
      });
      return group;
   }
}
