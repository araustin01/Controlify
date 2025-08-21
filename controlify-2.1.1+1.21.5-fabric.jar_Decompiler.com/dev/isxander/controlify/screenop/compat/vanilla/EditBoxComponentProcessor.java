package dev.isxander.controlify.screenop.compat.vanilla;

import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenProcessor;

public class EditBoxComponentProcessor implements ComponentProcessor {
   public boolean shouldKeepFocusOnKeyboardMode(ScreenProcessor<?> screen) {
      return true;
   }
}
