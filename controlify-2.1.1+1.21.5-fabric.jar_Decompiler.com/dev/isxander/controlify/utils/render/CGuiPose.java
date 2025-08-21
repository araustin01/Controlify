package dev.isxander.controlify.utils.render;

import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.joml.Matrix3x2fStack;

public interface CGuiPose {
   CGuiPose push();

   CGuiPose pop();

   CGuiPose translate(float var1, float var2);

   CGuiPose scale(float var1, float var2);

   static CGuiPose of(class_332 graphics) {
      return new CGuiPose.Impl3D(graphics.method_51448());
   }

   static CGuiPose ofPush(class_332 graphics) {
      return of(graphics).push();
   }

   public static class Impl3D implements CGuiPose {
      private final class_4587 stack;

      public Impl3D(class_4587 stack) {
         this.stack = stack;
      }

      public CGuiPose push() {
         this.stack.method_22903();
         return this;
      }

      public CGuiPose pop() {
         this.stack.method_22909();
         return this;
      }

      public CGuiPose translate(float x, float y) {
         this.stack.method_46416(x, y, 0.0F);
         return this;
      }

      public CGuiPose scale(float x, float y) {
         this.stack.method_22905(x, y, 1.0F);
         return this;
      }
   }

   public static class Impl2D implements CGuiPose {
      private final Matrix3x2fStack stack;

      public Impl2D(Matrix3x2fStack stack) {
         this.stack = stack;
      }

      public CGuiPose push() {
         this.stack.pushMatrix();
         return this;
      }

      public CGuiPose pop() {
         this.stack.popMatrix();
         return this;
      }

      public CGuiPose translate(float x, float y) {
         this.stack.translate(x, y);
         return this;
      }

      public CGuiPose scale(float x, float y) {
         this.stack.scale(x, y);
         return this;
      }
   }
}
