package dev.isxander.controlify.server;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum ServerPolicies {
   REACH_AROUND("reachAround", true),
   DISABLE_FLY_DRIFTING("disableFlyDrifting", false),
   KEYBOARD_LIKE_MOVEMENT("keyboardLikeMovement", false);

   private static final Map<String, ServerPolicies> BY_ID = (Map)Arrays.stream(values()).collect(Collectors.toMap(ServerPolicies::getId, (e) -> {
      return e;
   }));
   private final String id;
   private ServerPolicy value;
   private final boolean unsetValue;

   private ServerPolicies(String id, boolean unsetValue) {
      this.id = id;
      this.value = ServerPolicy.UNSET;
      this.unsetValue = unsetValue;
   }

   public boolean get() {
      boolean var10000;
      switch(this.value) {
      case ALLOWED:
         var10000 = true;
         break;
      case DISALLOWED:
         var10000 = false;
         break;
      case UNSET:
         var10000 = this.unsetValue;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   }

   public boolean getUnsetValue() {
      return this.unsetValue;
   }

   public boolean isUnset() {
      return this.value == ServerPolicy.UNSET;
   }

   public ServerPolicy getPolicy() {
      return this.value;
   }

   public void set(ServerPolicy value) {
      this.value = value;
   }

   public String getId() {
      return this.id;
   }

   public static ServerPolicies getById(String id) {
      return (ServerPolicies)BY_ID.get(id);
   }

   public static void unsetAll() {
      ServerPolicies[] var0 = values();
      int var1 = var0.length;

      for(int var2 = 0; var2 < var1; ++var2) {
         ServerPolicies policy = var0[var2];
         policy.set(ServerPolicy.UNSET);
      }

   }

   // $FF: synthetic method
   private static ServerPolicies[] $values() {
      return new ServerPolicies[]{REACH_AROUND, DISABLE_FLY_DRIFTING, KEYBOARD_LIKE_MOVEMENT};
   }
}
