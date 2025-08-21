package dev.isxander.controlify.driver.steamdeck;

public enum CEFDebuggerFileResult {
   REQUIRES_RESTART,
   FAILED_TO_CREATE,
   PRESENT_BUT_DESKTOP,
   WORKING,
   SANDBOXED_ERROR,
   NOT_STEAM_DECK;

   // $FF: synthetic method
   private static CEFDebuggerFileResult[] $values() {
      return new CEFDebuggerFileResult[]{REQUIRES_RESTART, FAILED_TO_CREATE, PRESENT_BUT_DESKTOP, WORKING, SANDBOXED_ERROR, NOT_STEAM_DECK};
   }
}
