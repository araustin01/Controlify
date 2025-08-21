package dev.isxander.controlify.server;

public enum ServerPolicy {
   ALLOWED,
   DISALLOWED,
   UNSET;

   public boolean isAllowed() {
      return this != DISALLOWED;
   }

   public static ServerPolicy fromBoolean(boolean value) {
      return value ? ALLOWED : DISALLOWED;
   }

   // $FF: synthetic method
   private static ServerPolicy[] $values() {
      return new ServerPolicy[]{ALLOWED, DISALLOWED, UNSET};
   }
}
