package dev.isxander.controlify.controller.input;

import com.google.common.collect.Sets;
import dev.isxander.controlify.bindings.input.AxisInput;
import dev.isxander.controlify.bindings.input.ButtonInput;
import dev.isxander.controlify.bindings.input.Input;
import dev.isxander.controlify.utils.CUtil;
import java.util.List;
import java.util.Set;
import net.minecraft.class_2960;

public final class GamepadInputs {
   public static final class_2960 SOUTH_BUTTON = button("south");
   public static final class_2960 EAST_BUTTON = button("east");
   public static final class_2960 WEST_BUTTON = button("west");
   public static final class_2960 NORTH_BUTTON = button("north");
   public static final class_2960 LEFT_SHOULDER_BUTTON = button("left_shoulder");
   public static final class_2960 RIGHT_SHOULDER_BUTTON = button("right_shoulder");
   public static final class_2960 LEFT_STICK_BUTTON = button("left_stick");
   public static final class_2960 RIGHT_STICK_BUTTON = button("right_stick");
   public static final class_2960 BACK_BUTTON = button("back");
   public static final class_2960 START_BUTTON = button("start");
   public static final class_2960 GUIDE_BUTTON = button("guide");
   public static final class_2960 DPAD_UP_BUTTON = button("dpad_up");
   public static final class_2960 DPAD_DOWN_BUTTON = button("dpad_down");
   public static final class_2960 DPAD_LEFT_BUTTON = button("dpad_left");
   public static final class_2960 DPAD_RIGHT_BUTTON = button("dpad_right");
   public static final class_2960 LEFT_TRIGGER_AXIS = axis("left_trigger");
   public static final class_2960 RIGHT_TRIGGER_AXIS = axis("right_trigger");
   public static final class_2960 LEFT_STICK_AXIS_UP = axis("left_stick_up");
   public static final class_2960 LEFT_STICK_AXIS_DOWN = axis("left_stick_down");
   public static final class_2960 LEFT_STICK_AXIS_LEFT = axis("left_stick_left");
   public static final class_2960 LEFT_STICK_AXIS_RIGHT = axis("left_stick_right");
   public static final class_2960 RIGHT_STICK_AXIS_UP = axis("right_stick_up");
   public static final class_2960 RIGHT_STICK_AXIS_DOWN = axis("right_stick_down");
   public static final class_2960 RIGHT_STICK_AXIS_LEFT = axis("right_stick_left");
   public static final class_2960 RIGHT_STICK_AXIS_RIGHT = axis("right_stick_right");
   public static final class_2960 MISC_1_BUTTON = button("misc_1");
   public static final class_2960 MISC_2_BUTTON = button("misc_2");
   public static final class_2960 MISC_3_BUTTON = button("misc_3");
   public static final class_2960 MISC_4_BUTTON = button("misc_4");
   public static final class_2960 MISC_5_BUTTON = button("misc_5");
   public static final class_2960 MISC_6_BUTTON = button("misc_6");
   public static final class_2960 RIGHT_PADDLE_1_BUTTON = button("right_paddle_1");
   public static final class_2960 RIGHT_PADDLE_2_BUTTON = button("right_paddle_2");
   public static final class_2960 LEFT_PADDLE_1_BUTTON = button("left_paddle_1");
   public static final class_2960 LEFT_PADDLE_2_BUTTON = button("left_paddle_2");
   public static final class_2960 TOUCHPAD_1_BUTTON = button("touchpad_1");
   public static final class_2960 TOUCHPAD_2_BUTTON = button("touchpad_2");
   public static final Set<DeadzoneGroup> DEADZONE_GROUPS;

   private GamepadInputs() {
   }

   public static Input getBind(class_2960 id) {
      String var1 = id.method_12832().split("/")[0];
      byte var2 = -1;
      switch(var1.hashCode()) {
      case -1377687758:
         if (var1.equals("button")) {
            var2 = 0;
         }
         break;
      case 103067:
         if (var1.equals("hat")) {
            var2 = 2;
         }
         break;
      case 3008417:
         if (var1.equals("axis")) {
            var2 = 1;
         }
      }

      Object var10000;
      switch(var2) {
      case 0:
         var10000 = new ButtonInput(id);
         break;
      case 1:
         var10000 = new AxisInput(id);
         break;
      case 2:
         throw new IllegalArgumentException("Gamepad does not have hat inputs.");
      default:
         throw new IllegalArgumentException("Unknown bind type: " + String.valueOf(id));
      }

      return (Input)var10000;
   }

   private static class_2960 button(String id) {
      return CUtil.rl("button/" + id);
   }

   private static class_2960 axis(String id) {
      return CUtil.rl("axis/" + id);
   }

   private static class_2960 hat(String id) {
      return CUtil.rl("hat/" + id);
   }

   static {
      DEADZONE_GROUPS = Sets.newLinkedHashSet(List.of(new DeadzoneGroup(CUtil.rl("left_stick"), List.of(LEFT_STICK_AXIS_UP, LEFT_STICK_AXIS_DOWN, LEFT_STICK_AXIS_LEFT, LEFT_STICK_AXIS_RIGHT)), new DeadzoneGroup(CUtil.rl("right_stick"), List.of(RIGHT_STICK_AXIS_UP, RIGHT_STICK_AXIS_DOWN, RIGHT_STICK_AXIS_LEFT, RIGHT_STICK_AXIS_RIGHT))));
   }
}
