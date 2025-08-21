package dev.isxander.controlify.utils.animation.api;

import net.minecraft.class_3532;

public interface EasingFunction {
   EasingFunction LINEAR = (t) -> {
      return t;
   };
   EasingFunction EASE_IN_SIN = (t) -> {
      return 1.0F - class_3532.method_15362(t * 3.1415927F / 2.0F);
   };
   EasingFunction EASE_OUT_SIN = (t) -> {
      return class_3532.method_15374(t * 3.1415927F / 2.0F);
   };
   EasingFunction EASE_IN_OUT_SIN = (t) -> {
      return -(class_3532.method_15362(3.1415927F * t) - 1.0F) / 2.0F;
   };
   EasingFunction EASE_IN_QUAD = (t) -> {
      return t * t;
   };
   EasingFunction EASE_OUT_QUAD = (t) -> {
      return 1.0F - (1.0F - t) * (1.0F - t);
   };
   EasingFunction EASE_IN_OUT_QUAD = (t) -> {
      return (double)t < 0.5D ? 2.0F * t * t : 1.0F - (float)Math.pow((double)(-2.0F * t + 2.0F), 2.0D) / 2.0F;
   };
   EasingFunction EASE_IN_CUBIC = (t) -> {
      return t * t * t;
   };
   EasingFunction EASE_OUT_CUBIC = (t) -> {
      return 1.0F - (1.0F - t) * (1.0F - t) * (1.0F - t);
   };
   EasingFunction EASE_IN_OUT_CUBIC = (t) -> {
      return (double)t < 0.5D ? 4.0F * t * t * t : 1.0F - (float)Math.pow((double)(-2.0F * t + 2.0F), 3.0D) / 2.0F;
   };
   EasingFunction EASE_IN_QUART = (t) -> {
      return t * t * t * t;
   };
   EasingFunction EASE_OUT_QUART = (t) -> {
      return 1.0F - (1.0F - t) * (1.0F - t) * (1.0F - t) * (1.0F - t);
   };
   EasingFunction EASE_IN_OUT_QUART = (t) -> {
      return (double)t < 0.5D ? 8.0F * t * t * t * t : 1.0F - (float)Math.pow((double)(-2.0F * t + 2.0F), 4.0D) / 2.0F;
   };
   EasingFunction EASE_IN_QUINT = (t) -> {
      return t * t * t * t * t;
   };
   EasingFunction EASE_OUT_QUINT = (t) -> {
      return 1.0F - (1.0F - t) * (1.0F - t) * (1.0F - t) * (1.0F - t) * (1.0F - t);
   };
   EasingFunction EASE_IN_OUT_QUINT = (t) -> {
      return (double)t < 0.5D ? 16.0F * t * t * t * t * t : 1.0F - (float)Math.pow((double)(-2.0F * t + 2.0F), 5.0D) / 2.0F;
   };
   EasingFunction EASE_IN_EXPO = (t) -> {
      return t == 0.0F ? 0.0F : (float)Math.pow(2.0D, (double)(10.0F * t - 10.0F));
   };
   EasingFunction EASE_OUT_EXPO = (t) -> {
      return t == 1.0F ? 1.0F : 1.0F - (float)Math.pow(2.0D, (double)(-10.0F * t));
   };
   EasingFunction EASE_IN_OUT_EXPO = (t) -> {
      return t == 0.0F ? 0.0F : (t == 1.0F ? 1.0F : ((double)t < 0.5D ? (float)Math.pow(2.0D, (double)(20.0F * t - 10.0F)) / 2.0F : (2.0F - (float)Math.pow(2.0D, (double)(-20.0F * t + 10.0F))) / 2.0F));
   };
   EasingFunction EASE_IN_CIRC = (t) -> {
      return 1.0F - (float)Math.sqrt(1.0D - Math.pow((double)t, 2.0D));
   };
   EasingFunction EASE_OUT_CIRC = (t) -> {
      return (float)Math.sqrt(1.0D - Math.pow((double)(t - 1.0F), 2.0D));
   };
   EasingFunction EASE_IN_OUT_CIRC = (t) -> {
      return (double)t < 0.5D ? (1.0F - (float)Math.sqrt(1.0D - Math.pow((double)(2.0F * t), 2.0D))) / 2.0F : ((float)Math.sqrt(1.0D - Math.pow((double)(-2.0F * t + 2.0F), 2.0D)) + 1.0F) / 2.0F;
   };
   EasingFunction EASE_IN_BACK = (t) -> {
      return (float)((double)(t * t) * (2.70158D * (double)t - 1.70158D));
   };
   EasingFunction EASE_OUT_BACK = (t) -> {
      return 1.0F - (float)((double)((1.0F - t) * (1.0F - t)) * (-2.70158D * (double)(1.0F - t) - 1.70158D));
   };
   EasingFunction EASE_IN_OUT_BACK = (t) -> {
      return (double)t < 0.5D ? (float)((double)(2.0F * t * t) * (3.5949095D * (double)t - 2.5949095D)) : 1.0F - (float)((double)((2.0F - 2.0F * t) * (2.0F - 2.0F * t)) * (3.5949095D * (double)(2.0F - 2.0F * t) - 2.5949095D)) / 2.0F;
   };
   EasingFunction EASE_IN_ELASTIC = (t) -> {
      return t == 0.0F ? 0.0F : (t == 1.0F ? 1.0F : -((float)Math.pow(2.0D, (double)(10.0F * t - 10.0F))) * (float)Math.sin(((double)(t * 10.0F) - 10.75D) * 6.283185307179586D / 3.0D));
   };
   EasingFunction EASE_OUT_ELASTIC = (t) -> {
      return t == 0.0F ? 0.0F : (t == 1.0F ? 1.0F : (float)Math.pow(2.0D, (double)(-10.0F * t)) * (float)Math.sin(((double)(t * 10.0F) - 0.75D) * 6.283185307179586D / 3.0D) + 1.0F);
   };
   EasingFunction EASE_IN_OUT_ELASTIC = (t) -> {
      return t == 0.0F ? 0.0F : (t == 1.0F ? 1.0F : ((double)t < 0.5D ? -((float)Math.pow(2.0D, (double)(20.0F * t - 10.0F))) * (float)Math.sin(((double)(20.0F * t) - 11.125D) * 6.283185307179586D / 4.5D) / 2.0F : (float)Math.pow(2.0D, (double)(-20.0F * t + 10.0F)) * (float)Math.sin(((double)(20.0F * t) - 11.125D) * 6.283185307179586D / 4.5D) / 2.0F + 1.0F));
   };
   EasingFunction EASE_OUT_BOUNCE = (t) -> {
      if ((double)t < 0.36363636363636365D) {
         return 7.5625F * t * t;
      } else if ((double)t < 0.7272727272727273D) {
         return 7.5625F * (t -= 0.54545456F) * t + 0.75F;
      } else {
         return (double)t < 0.9090909090909091D ? 7.5625F * (t -= 0.8181818F) * t + 0.9375F : 7.5625F * (t -= 0.95454544F) * t + 0.984375F;
      }
   };
   EasingFunction EASE_IN_BOUNCE = (t) -> {
      return 1.0F - EASE_OUT_BOUNCE.ease(1.0F - t);
   };
   EasingFunction EASE_IN_OUT_BOUNCE = (t) -> {
      return (double)t < 0.5D ? (1.0F - EASE_OUT_BOUNCE.ease(1.0F - 2.0F * t)) / 2.0F : (1.0F + EASE_OUT_BOUNCE.ease(2.0F * t - 1.0F)) / 2.0F;
   };

   float ease(float var1);
}
