package dev.isxander.controlify.controller.touchpad;

import dev.isxander.controlify.controller.ECSComponent;
import dev.isxander.controlify.utils.CUtil;
import net.minecraft.class_2960;

public class TouchpadComponent implements ECSComponent {
   public static final class_2960 ID = CUtil.rl("touchpad");
   private final Touchpads touchpads;

   public TouchpadComponent(Touchpads touchpads) {
      this.touchpads = touchpads;
   }

   public Touchpads.Touchpad[] touchpads() {
      return this.touchpads.touchpads();
   }

   public class_2960 id() {
      return ID;
   }
}
