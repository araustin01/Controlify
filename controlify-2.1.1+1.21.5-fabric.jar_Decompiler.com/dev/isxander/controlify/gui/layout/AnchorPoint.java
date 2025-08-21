package dev.isxander.controlify.gui.layout;

import org.joml.Vector2i;

public enum AnchorPoint {
   TOP_LEFT(0.0F, 0.0F),
   TOP_CENTER(0.5F, 0.0F),
   TOP_RIGHT(1.0F, 0.0F),
   CENTER_LEFT(0.0F, 0.5F),
   CENTER(0.5F, 0.5F),
   CENTER_RIGHT(0.5F, 1.0F),
   BOTTOM_LEFT(0.0F, 1.0F),
   BOTTOM_CENTER(0.5F, 1.0F),
   BOTTOM_RIGHT(1.0F, 1.0F);

   public final float anchorX;
   public final float anchorY;

   private AnchorPoint(float anchorX, float anchorY) {
      this.anchorX = anchorX;
      this.anchorY = anchorY;
   }

   public Vector2i getAnchorPosition(int w, int h) {
      return new Vector2i((int)((float)w * this.anchorX), (int)((float)h * this.anchorY));
   }

   // $FF: synthetic method
   private static AnchorPoint[] $values() {
      return new AnchorPoint[]{TOP_LEFT, TOP_CENTER, TOP_RIGHT, CENTER_LEFT, CENTER, CENTER_RIGHT, BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT};
   }
}
