package dev.isxander.controlify.controller.battery;

public interface PowerState {
   default int percent() {
      return -1;
   }

   public static record Unknown() implements PowerState {
   }

   public static record WiredOnly() implements PowerState {
   }

   public static record Full() implements PowerState {
      public int percent() {
         return 100;
      }
   }

   public static record Charging(int percent) implements PowerState {
      public Charging(int percent) {
         this.percent = percent;
      }

      public int percent() {
         return this.percent;
      }
   }

   public static record Depleting(int percent) implements PowerState {
      public Depleting(int percent) {
         this.percent = percent;
      }

      public int percent() {
         return this.percent;
      }
   }
}
