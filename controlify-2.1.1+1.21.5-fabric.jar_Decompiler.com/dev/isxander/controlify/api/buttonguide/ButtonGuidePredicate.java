package dev.isxander.controlify.api.buttonguide;

import net.minecraft.class_339;
import net.minecraft.class_4264;

@FunctionalInterface
public interface ButtonGuidePredicate<T> {
   /** @deprecated */
   @Deprecated
   ButtonGuidePredicate<class_4264> ALWAYS = (btn) -> {
      return true;
   };
   /** @deprecated */
   @Deprecated
   ButtonGuidePredicate<class_4264> FOCUS_ONLY = class_339::method_25370;

   boolean shouldDisplay(T var1);

   static <T extends class_339> ButtonGuidePredicate<T> focusOnly() {
      return class_339::method_25370;
   }

   static <T> ButtonGuidePredicate<T> always() {
      return (btn) -> {
         return true;
      };
   }
}
