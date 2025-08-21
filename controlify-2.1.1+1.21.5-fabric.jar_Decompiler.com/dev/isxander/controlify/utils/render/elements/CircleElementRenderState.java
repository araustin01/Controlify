package dev.isxander.controlify.utils.render.elements;

import dev.isxander.controlify.utils.render.BaseRenderState;
import dev.isxander.controlify.utils.render.CGuiElementRenderState;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4588;

public record CircleElementRenderState(BaseRenderState baseState, float originX, float originY, float radius, float thickness, int color, int segments) implements CGuiElementRenderState {
   public CircleElementRenderState(BaseRenderState baseState, float originX, float originY, float radius, float thickness, int color, int segments) {
      this.baseState = baseState;
      this.originX = originX;
      this.originY = originY;
      this.radius = radius;
      this.thickness = thickness;
      this.color = color;
      this.segments = segments;
   }

   public void buildVertices(class_4588 vertexConsumer, float z) {
      float innerRadius = this.radius - this.thickness;

      for(int i = 0; i < this.segments - 1; ++i) {
         float angle = (float)i / (float)this.segments * 6.2831855F;
         float nextAngle = (float)(i + 1) / (float)this.segments * 6.2831855F;
         float xi1 = this.originX + class_3532.method_15374(angle) * innerRadius;
         float yi1 = this.originY + class_3532.method_15362(angle) * innerRadius;
         float xi2 = this.originX + class_3532.method_15374(nextAngle) * innerRadius;
         float yi2 = this.originY + class_3532.method_15362(nextAngle) * innerRadius;
         float xo1 = this.originX + class_3532.method_15374(angle) * this.radius;
         float yo1 = this.originY + class_3532.method_15362(angle) * this.radius;
         float xo2 = this.originX + class_3532.method_15374(nextAngle) * this.radius;
         float yo2 = this.originY + class_3532.method_15362(nextAngle) * this.radius;
         this.add2DVertex(vertexConsumer, xi1, yi1, z).method_39415(this.color);
         this.add2DVertex(vertexConsumer, xo1, yo1, z).method_39415(this.color);
         this.add2DVertex(vertexConsumer, xo2, yo2, z).method_39415(this.color);
         this.add2DVertex(vertexConsumer, xi2, yi2, z).method_39415(this.color);
      }

   }

   public static CircleElementRenderState outline(class_332 graphics, float originX, float originY, float radius, float thickness, int color) {
      int minX = (int)(originX - radius);
      int minY = (int)(originY - radius);
      int maxX = (int)(originX + radius);
      int maxY = (int)(originY + radius);
      return new CircleElementRenderState(BaseRenderState.create(graphics, (class_2960)null, minX, minY, maxX, maxY), originX, originY, radius, thickness, color, segmentsForRadius(radius));
   }

   public static CircleElementRenderState filled(class_332 graphics, float originX, float originY, float radius, int color) {
      return outline(graphics, originX, originY, radius, radius, color);
   }

   private static int segmentsForRadius(float radius) {
      return class_3532.method_15384(6.283185307179586D * (double)radius / 4.0D);
   }

   public BaseRenderState baseState() {
      return this.baseState;
   }

   public float originX() {
      return this.originX;
   }

   public float originY() {
      return this.originY;
   }

   public float radius() {
      return this.radius;
   }

   public float thickness() {
      return this.thickness;
   }

   public int color() {
      return this.color;
   }

   public int segments() {
      return this.segments;
   }
}
