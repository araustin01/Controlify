package dev.isxander.controlify.rumble;

public interface RumbleEffect extends Comparable<RumbleEffect> {
   void tick();

   RumbleState currentState();

   boolean isFinished();

   int priority();

   int age();

   default int compareTo(RumbleEffect o) {
      int priorityCompare = Integer.compare(o.priority(), this.priority());
      return priorityCompare != 0 ? priorityCompare : Integer.compare(this.age(), o.age());
   }
}
