package dev.isxander.controlify.utils;

import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_370;
import net.minecraft.class_370.class_9037;

public class ToastUtils {
   public static void sendToast(class_2561 title, class_2561 message, boolean longer) {
      class_370 toast = class_370.method_29047(class_310.method_1551(), longer ? class_9037.field_47589 : class_9037.field_47588, title, message);
      class_310.method_1551().method_1566().method_1999(toast);
   }
}
