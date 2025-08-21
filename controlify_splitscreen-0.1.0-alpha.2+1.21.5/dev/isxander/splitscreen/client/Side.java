package dev.isxander.splitscreen.client;

public enum Side {
   CONTROLLER,
   PAWN;

   public Side opposite() {
      return this == CONTROLLER ? PAWN : CONTROLLER;
   }

   // $FF: synthetic method
   private static Side[] $values() {
      return new Side[]{CONTROLLER, PAWN};
   }
}
