package dev.isxander.controlify.controller.touchpad;

import java.util.ArrayList;
import java.util.List;
import org.joml.Vector2fc;

public record Touchpads(Touchpads.Touchpad[] touchpads) {
   public Touchpads(Touchpads.Touchpad[] touchpads) {
      this.touchpads = touchpads;
   }

   public Touchpads.Touchpad[] touchpads() {
      return this.touchpads;
   }

   public static final class Touchpad {
      private List<Touchpads.Finger> fingers = new ArrayList();
      private List<Touchpads.Finger> prevFingers = new ArrayList();
      private final int maxFingers;

      public Touchpad(int maxFingers) {
         this.maxFingers = maxFingers;
      }

      public List<Touchpads.Finger> fingersNow() {
         return this.fingers;
      }

      public List<Touchpads.Finger> fingersThen() {
         return this.prevFingers;
      }

      public void pushFingers(List<Touchpads.Finger> fingers) {
         this.prevFingers = this.fingers;
         this.fingers = List.copyOf(fingers);
      }

      public int maxFingers() {
         return this.maxFingers;
      }
   }

   public static record Finger(int id, Vector2fc position, float pressure) {
      public Finger(int id, Vector2fc position, float pressure) {
         this.id = id;
         this.position = position;
         this.pressure = pressure;
      }

      public int id() {
         return this.id;
      }

      public Vector2fc position() {
         return this.position;
      }

      public float pressure() {
         return this.pressure;
      }
   }
}
