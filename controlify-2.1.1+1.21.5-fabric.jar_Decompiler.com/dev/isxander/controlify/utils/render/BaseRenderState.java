package dev.isxander.controlify.utils.render;

import dev.isxander.yacl3.gui.utils.GuiUtils;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public record BaseRenderState(class_1921 renderType, Matrix4f pose) {
   public BaseRenderState(class_1921 renderType, Matrix4f pose) {
      this.renderType = renderType;
      this.pose = pose;
   }

   public static BaseRenderState create(class_332 graphics, @Nullable class_2960 texture, int x0, int y0, int x1, int y1) {
      return create(graphics, texture);
   }

   public static BaseRenderState create(class_332 graphics, @Nullable class_2960 texture) {
      return new BaseRenderState(texture != null ? (class_1921)GuiUtils.guiTextured(false).apply(texture) : class_1921.method_51784(), graphics.method_51448().method_23760().method_23761());
   }

   public class_1921 renderType() {
      return this.renderType;
   }

   public Matrix4f pose() {
      return this.pose;
   }
}
