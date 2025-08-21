package dev.isxander.splitscreen.client.host.features.relaunch;

public interface PendingRelaunchClientStatus {
   public static record WaitingForReadySignal(float progress) implements PendingRelaunchClientStatus {
      public WaitingForReadySignal(float progress) {
         this.progress = progress;
      }

      public float progress() {
         return this.progress;
      }
   }

   public static record WaitingForConnection() implements PendingRelaunchClientStatus {
   }
}
