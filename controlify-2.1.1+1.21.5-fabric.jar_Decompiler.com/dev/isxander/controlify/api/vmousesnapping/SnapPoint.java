package dev.isxander.controlify.api.vmousesnapping;

import org.joml.Vector2i;
import org.joml.Vector2ic;

public record SnapPoint(Vector2ic position, int range) {
   public SnapPoint(int x, int y, int range) {
      this(new Vector2i(x, y), range);
   }

   public SnapPoint(Vector2ic position, int range) {
      this.position = position;
      this.range = range;
   }

   public Vector2ic position() {
      return this.position;
   }

   public int range() {
      return this.range;
   }
}
