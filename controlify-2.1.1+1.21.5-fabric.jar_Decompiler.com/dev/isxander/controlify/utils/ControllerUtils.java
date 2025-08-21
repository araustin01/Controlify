package dev.isxander.controlify.utils;

import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.touchpad.Touchpads;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.class_128;
import net.minecraft.class_129;
import net.minecraft.class_148;
import net.minecraft.class_3532;
import org.joml.Vector2d;
import org.joml.Vector2f;

public class ControllerUtils {
   public static String createControllerString(ControllerEntity controller) {
      return String.format("'%s'#%s-%s (%s)", controller.name(), controller.info().ucid().toString(), controller.info().hid().map((hid) -> {
         return hid.asIdentifier().toString();
      }).orElse("hid"), controller.info().type().friendlyName());
   }

   public static void wrapControllerError(Runnable runnable, String errorTitle, ControllerEntity controller) {
      try {
         runnable.run();
      } catch (Throwable var6) {
         class_128 crashReport = class_128.method_560(var6, errorTitle);
         class_129 category = crashReport.method_562("Affected controller");
         category.method_578("Controller name", controller.name());
         category.method_578("Controller identification", controller.info().type().friendlyName());
         category.method_578("Controller type", controller.getClass().getCanonicalName());
         throw new class_148(crashReport);
      }
   }

   public static float deadzone(float value, float deadzone) {
      return (value - Math.copySign(Math.min(deadzone, Math.abs(value)), value)) / (1.0F - deadzone);
   }

   public static float applyCircularityX(float x, float y) {
      return (float)((double)x * Math.sqrt((double)(1.0F - y * y / 2.0F)));
   }

   public static float applyCircularityY(float x, float y) {
      return (float)((double)y * Math.sqrt((double)(1.0F - x * x / 2.0F)));
   }

   public static Vector2d applyEasingToLength(double x, double y, Function<Double, Double> easing) {
      double length = Math.sqrt(x * x + y * y);
      if (length == 0.0D) {
         return new Vector2d(0.0D, 0.0D);
      } else {
         double easedLength = (Double)easing.apply(length);
         double angle = class_3532.method_15349(y, x);
         return new Vector2d(Math.cos(angle) * easedLength, Math.sin(angle) * easedLength);
      }
   }

   public static Vector2d applyEasingToLength(Vector2d vec, Function<Double, Double> easing) {
      double length = vec.length();
      if (length == 0.0D) {
         return new Vector2d(0.0D, 0.0D);
      } else {
         double easedLength = (Double)easing.apply(length);
         return vec.normalize(easedLength);
      }
   }

   public static boolean shouldApplyAntiSnapBack(float x, float y, float px, float py, float threshold) {
      float dx = x - px;
      float dy = y - py;
      float distanceSquared = dx * dx + dy * dy;
      boolean isSnap = distanceSquared >= threshold * threshold;
      boolean hasCrossedOrigin = Math.signum(x) != Math.signum(px) && Math.signum(y) != Math.signum(py);
      if (isSnap && hasCrossedOrigin) {
         float t = (-x * (px - x) + -y * (py - y)) / distanceSquared;
         t = class_3532.method_15363(t, 0.0F, 1.0F);
         double distanceToMiddle = Math.sqrt(Math.pow((double)(-t * x + t * px), 2.0D) + Math.pow((double)(-t * y + t * py), 2.0D));
         return distanceToMiddle <= 0.01D;
      } else {
         return false;
      }
   }

   public static List<Touchpads.Finger> deltaFingers(List<Touchpads.Finger> now, List<Touchpads.Finger> then) {
      return now.stream().flatMap((finger) -> {
         return then.stream().anyMatch((f) -> {
            return f.id() == finger.id();
         }) ? Stream.of(finger) : Stream.empty();
      }).map((nowFinger) -> {
         Touchpads.Finger thenFinger = (Touchpads.Finger)then.stream().filter((f) -> {
            return f.id() == nowFinger.id();
         }).findFirst().orElseThrow();
         return new Touchpads.Finger(nowFinger.id(), new Vector2f(nowFinger.position().x() - thenFinger.position().x(), nowFinger.position().y() - thenFinger.position().y()), nowFinger.pressure() - thenFinger.pressure());
      }).toList();
   }
}
