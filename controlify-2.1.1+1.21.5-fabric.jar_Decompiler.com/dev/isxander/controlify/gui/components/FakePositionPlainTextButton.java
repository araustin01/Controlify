package dev.isxander.controlify.gui.components;

import java.util.Objects;
import net.minecraft.class_2561;
import net.minecraft.class_327;
import net.minecraft.class_7077;
import net.minecraft.class_8030;
import net.minecraft.class_4185.class_4241;
import org.jetbrains.annotations.NotNull;

public class FakePositionPlainTextButton extends class_7077 {
   private class_8030 fakePosition;

   public FakePositionPlainTextButton(int x, int y, int width, int height, class_2561 content, class_4241 empty, class_327 font) {
      super(x, y, width, height, content, empty, font);
   }

   public FakePositionPlainTextButton(class_2561 text, class_327 font, int x, int y, class_4241 onPress) {
      int var10003 = font.method_30880(text.method_30937());
      Objects.requireNonNull(font);
      this(x, y, var10003, 9, text, onPress, font);
   }

   public void setFakePosition(class_8030 fakePosition) {
      this.fakePosition = fakePosition;
   }

   @NotNull
   public class_8030 method_48202() {
      return this.method_25370() ? super.method_48202() : this.fakePosition;
   }
}
