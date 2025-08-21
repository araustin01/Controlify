package dev.isxander.controlify.utils.render;

import net.minecraft.class_332;
import net.minecraft.class_4588;
import org.joml.Matrix4f;

public interface CGuiElementRenderState {
   BaseRenderState baseState();

   void buildVertices(class_4588 var1, float var2);

   default class_4588 add2DVertex(class_4588 vertexConsumer, float x, float y, float z) {
      return this.add2DVertex(vertexConsumer, this.baseState().pose(), x, y, z);
   }

   default class_4588 add2DVertex(class_4588 vertexConsumer, Matrix4f pose, float x, float y, float z) {
      return vertexConsumer.method_22918(pose, x, y, z);
   }

   default void submit(class_332 graphics) {
      class_4588 vertexConsumer = GuiRenderStateSink.bufferSource(graphics).getBuffer(this.baseState().renderType());
      this.buildVertices(vertexConsumer, 0.0F);
   }
}
