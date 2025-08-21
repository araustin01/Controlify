package dev.isxander.controlify.api.entrypoint;

import dev.isxander.controlify.api.ControlifyApi;

public interface ControlifyEntrypoint {
   void onControllersDiscovered(ControlifyApi var1);

   default void onControlifyInit(ControlifyApi controlify) {
      this.onControlifyPreInit(controlify);
   }

   /** @deprecated */
   @Deprecated
   default void onControlifyPreInit(ControlifyApi controlify) {
   }
}
