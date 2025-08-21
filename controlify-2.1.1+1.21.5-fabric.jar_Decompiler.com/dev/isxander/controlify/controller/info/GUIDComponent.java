package dev.isxander.controlify.controller.info;

import dev.isxander.controlify.controller.SingleValueComponent;
import dev.isxander.controlify.utils.CUtil;
import net.minecraft.class_2960;

public class GUIDComponent extends SingleValueComponent<String> {
   public static final class_2960 ID = CUtil.rl("guid");

   public GUIDComponent(String value) {
      super(value, ID);
   }
}
