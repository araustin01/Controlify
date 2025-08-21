package dev.isxander.controlify.rumble;

import dev.isxander.controlify.controller.rumble.RumbleComponent;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Queue;
import org.jetbrains.annotations.NotNull;

public class RumbleManager {
   private final RumbleComponent controller;
   private final Queue<RumbleManager.RumbleEffectInstance> effectQueue;
   private boolean silent;
   private boolean wasSilent;

   public RumbleManager(RumbleComponent controller) {
      this.controller = controller;
      this.effectQueue = new PriorityQueue(Comparator.comparing(RumbleManager.RumbleEffectInstance::effect));
   }

   /** @deprecated */
   @Deprecated
   public void play(RumbleEffect effect) {
      this.play(RumbleSource.MASTER, effect);
   }

   public void play(RumbleSource source, RumbleEffect effect) {
      this.effectQueue.add(new RumbleManager.RumbleEffectInstance(source, effect));
   }

   public void tick() {
      this.effectQueue.removeIf((e) -> {
         return e.effect().isFinished();
      });
      this.effectQueue.forEach((e) -> {
         e.effect().tick();
      });
      if (this.effectQueue.isEmpty()) {
         this.clearRumble();
      } else {
         float strong = 0.0F;
         float weak = 0.0F;

         RumbleState effectState;
         for(Iterator var3 = this.effectQueue.iterator(); var3.hasNext(); weak = Math.max(weak, effectState.weak())) {
            RumbleManager.RumbleEffectInstance effect = (RumbleManager.RumbleEffectInstance)var3.next();
            effectState = ((RumbleComponent.Config)this.controller.config().config()).applyRumbleStrength(effect.effect().currentState(), effect.source());
            strong = Math.max(strong, effectState.strong());
         }

         RumbleState state = new RumbleState(strong, weak);
         if (state.isZero()) {
            this.clearRumble();
         } else {
            if (this.silent) {
               this.clearRumble();
            } else {
               this.controller.queueRumble(state);
               this.wasSilent = false;
            }

         }
      }
   }

   private void clearRumble() {
      if (!this.wasSilent) {
         this.controller.queueRumble(RumbleState.NONE);
         this.wasSilent = true;
      }
   }

   public void clearEffects() {
      this.effectQueue.clear();
   }

   public void setSilent(boolean silent) {
      this.silent = silent;
   }

   public boolean isPlaying() {
      return !this.effectQueue.isEmpty();
   }

   private static record RumbleEffectInstance(RumbleSource source, RumbleEffect effect) implements Comparable<RumbleManager.RumbleEffectInstance> {
      private RumbleEffectInstance(RumbleSource source, RumbleEffect effect) {
         this.source = source;
         this.effect = effect;
      }

      public int compareTo(@NotNull RumbleManager.RumbleEffectInstance o) {
         return this.effect.compareTo(o.effect);
      }

      public RumbleSource source() {
         return this.source;
      }

      public RumbleEffect effect() {
         return this.effect;
      }
   }
}
