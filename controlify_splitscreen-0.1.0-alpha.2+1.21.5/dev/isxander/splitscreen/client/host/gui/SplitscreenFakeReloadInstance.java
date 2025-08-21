package dev.isxander.splitscreen.client.host.gui;

import dev.isxander.splitscreen.client.host.features.relaunch.PendingRelaunchClientStatus;
import java.util.Collection;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_3532;
import net.minecraft.class_4011;

public class SplitscreenFakeReloadInstance implements class_4011 {
   private final CompletableFuture<?> future = new CompletableFuture();
   private final Collection<PendingRelaunchClientStatus> waitingOn;
   private int maxSeen = 0;

   public SplitscreenFakeReloadInstance(Collection<PendingRelaunchClientStatus> waitingOn) {
      this.waitingOn = waitingOn;
   }

   public CompletableFuture<?> method_18364() {
      return this.future;
   }

   public float method_18229() {
      this.maxSeen = Math.max(this.maxSeen, this.waitingOn.size());
      int finishedClients = this.maxSeen - this.waitingOn.size();
      float workingSumProgress = (float)this.waitingOn.stream().mapToDouble((status) -> {
         Objects.requireNonNull(status);
         int index$2 = 0;
         double var9;
         switch(status.typeSwitch<invokedynamic>(status, index$2)) {
         case 0:
            PendingRelaunchClientStatus.WaitingForConnection ignored = (PendingRelaunchClientStatus.WaitingForConnection)status;
            var9 = 0.05D;
            break;
         case 1:
            PendingRelaunchClientStatus.WaitingForReadySignal $b$0 = (PendingRelaunchClientStatus.WaitingForReadySignal)status;
            PendingRelaunchClientStatus.WaitingForReadySignal var10000 = $b$0;

            float var8;
            try {
               var8 = var10000.progress();
            } catch (Throwable var7) {
               throw new MatchException(var7.toString(), var7);
            }

            float patt3$temp = var8;
            var9 = 0.25D + (double)class_3532.method_15363(patt3$temp, 0.0F, 1.0F) * 0.7D;
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
         }

         return var9;
      }).sum();
      float sumProgress = workingSumProgress + (float)finishedClients;
      float progress = sumProgress / (float)this.maxSeen;
      if ((double)progress >= 1.0D) {
         this.future.complete((Object)null);
      }

      return progress;
   }
}
