package dev.isxander.controlify.utils.animation.impl;

import dev.isxander.controlify.utils.animation.api.Animatable;
import dev.isxander.controlify.utils.animation.api.AnimationSequence;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import org.apache.commons.lang3.Validate;

public class AnimationSequenceImpl implements AnimationSequence {
   private final Queue<Animatable> queue = new ArrayDeque();
   private Animatable current = null;
   private boolean started;
   private boolean done;

   public AnimationSequence push(Animatable... animatables) {
      Validate.isTrue(!this.isDone(), "Cannot add to sequence that has already completed.", new Object[0]);
      Animatable[] var2 = animatables;
      int var3 = animatables.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         Animatable animatable = var2[var4];
         Validate.isTrue(!animatable.hasStarted(), "Cannot add an animation that has already started!", new Object[0]);
      }

      this.queue.addAll(List.of(animatables));
      return this;
   }

   public void tick(float tickDelta) {
      if (this.current == null) {
         this.current = (Animatable)this.queue.poll();
         if (this.current != null) {
            this.current.play();
            this.started = true;
         } else {
            this.done = true;
         }
      } else {
         this.current.tick(tickDelta);
         if (this.current.isDone()) {
            this.current = null;
         }
      }

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

   public AnimationSequence play() {
      Animator.INSTANCE.add(this);
      return this;
   }

   public void skipToEnd() {
      while(this.current != null) {
         this.current.skipToEnd();
         this.current = (Animatable)this.queue.poll();
      }

      this.done = true;
   }

   public void abort() {
      while(this.current != null) {
         this.current.abort();
         this.current = (Animatable)this.queue.poll();
      }

      this.done = true;
   }

   public AnimationSequence copy() {
      Validate.isTrue(!this.started, "Cannot copy an animation sequence that has already started.", new Object[0]);
      AnimationSequence sequence = AnimationSequence.of();
      this.queue.forEach((animation) -> {
         sequence.push(animation.copy());
      });
      return sequence;
   }
}
