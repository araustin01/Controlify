package dev.isxander.controlify.font;

import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.mixins.feature.font.FontAccessor;
import dev.isxander.controlify.utils.CUtil;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_2960;
import net.minecraft.class_327;
import net.minecraft.class_377;
import net.minecraft.class_379;
import net.minecraft.class_5223;
import net.minecraft.class_5348;
import net.minecraft.class_5481;
import org.apache.commons.lang3.mutable.MutableInt;

public final class BindingFontHelper {
   public static final class_2960 WRAPPER_FONT = CUtil.rl("inputs");
   public static final String PLACEHOLDER_KEY = "controlify.placeholder";
   public static final String PLACEHOLDER_CONTROLLER_ACTIVE_KEY = "controlify.placeholder.controller_active";

   public static class_2561 bindingWithFallback(class_2960 binding, class_2561 fallback) {
      return class_2561.method_48322("controlify.placeholder", "%2$s", new Object[]{binding(binding), fallback});
   }

   public static class_2561 bindingWithFallback(InputBinding binding, class_2561 fallback) {
      return bindingWithFallback(binding.id(), fallback);
   }

   public static class_2561 binding(class_2960 binding) {
      return class_2561.method_43472(binding.toString()).method_27694((style) -> {
         return style.method_27704(WRAPPER_FONT);
      });
   }

   public static class_2561 binding(InputBinding binding) {
      return binding(binding.id());
   }

   public static int getComponentHeight(class_327 font, class_5481 text) {
      MutableInt mutableInt = new MutableInt();
      text.accept((index, style, codePoint) -> {
         mutableInt.setValue(Math.max(mutableInt.intValue(), getHeight(font, codePoint, style)));
         return true;
      });
      return mutableInt.intValue();
   }

   public static int getComponentHeight(class_327 font, class_5348 text) {
      MutableInt mutableInt = new MutableInt();
      class_5223.method_27476(text, class_2583.field_24360, (index, style, codePoint) -> {
         mutableInt.setValue(Math.max(mutableInt.intValue(), getHeight(font, codePoint, style)));
         return true;
      });
      return mutableInt.intValue();
   }

   private static int getHeight(class_327 font, int codepoint, class_2583 style) {
      class_377 fontSet = ((FontAccessor)font).invokeGetFontSet(style.method_27708());
      class_379 glyphInfo = fontSet.method_2011(codepoint, false);
      MutableInt f = new MutableInt(0);
      glyphInfo.bake((sheetGlyphInfo) -> {
         f.setValue(sheetGlyphInfo.method_2032());
         return null;
      });
      return f.intValue();
   }
}
