package dev.isxander.controlify.gui.screen;

import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_437;

public class ModConfigOpenerScreen extends class_437 {
   private final class_437 lastScreen;

   public ModConfigOpenerScreen(class_437 lastScreen) {
      super(class_2561.method_43473());
      this.lastScreen = lastScreen;
   }

   public void method_49589() {
      class_310 minecraft = class_310.method_1551();
      this.method_25423(minecraft, minecraft.method_22683().method_4486(), minecraft.method_22683().method_4502());
      ControllerCarouselScreen.openConfigScreen(this.lastScreen);
   }

   public void method_37064(boolean useTranslationsCache) {
   }
}
