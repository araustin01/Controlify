package dev.isxander.controlify.gui.screen;

import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_410;
import net.minecraft.class_437;

public final class SteamDeckAlerts {
   public static class_437 createDesktopModeWarning(Runnable callback) {
      return createQuitOrContinueScreen(callback, class_2561.method_43471("controlify.steam_deck.desktop_mode_warning.title"), class_2561.method_43471("controlify.steam_deck.desktop_mode_warning.message"));
   }

   public static class_437 createDeckyRequiredWarning(Runnable callback) {
      return createQuitOrContinueScreen(callback, class_2561.method_43471("controlify.steam_deck.decky_required_warning.title"), class_2561.method_43469("controlify.steam_deck.decky_required_warning.message", new Object[]{class_2561.method_43470("https://short.isxander.dev/decky-install").method_27695(new class_124[]{class_124.field_1075, class_124.field_1073})}));
   }

   private static class_437 createQuitOrContinueScreen(Runnable callback, class_2561 title, class_2561 message) {
      return new class_410((yes) -> {
         if (yes) {
            class_310.method_1551().method_1592();
         } else {
            callback.run();
         }

      }, title, message, class_2561.method_43471("menu.quit"), class_2561.method_43471("gui.continue"));
   }
}
