package dev.isxander.controlify.utils;

import java.util.function.UnaryOperator;
import net.minecraft.class_3532;

public class Easings {
   public static double easeInSine(double t) {
      return (double)(1.0F - class_3532.method_15362((float)(t * 3.141592653589793D / 2.0D)));
   }

   public static double easeInQuad(double t) {
      return t * t;
   }

   public static double easeOutQuad(double t) {
      return 1.0D - (1.0D - t) * (1.0D - t);
   }

   public static double easeOutExpo(double t) {
      return t == 1.0D ? 1.0D : (double)(1.0F - (float)Math.pow(2.0D, -10.0D * t));
   }

   public static UnaryOperator<Float> toFloat(UnaryOperator<Double> easing) {
      return (f) -> {
         return ((Double)easing.apply(f.doubleValue())).floatValue();
      };
   }
}
