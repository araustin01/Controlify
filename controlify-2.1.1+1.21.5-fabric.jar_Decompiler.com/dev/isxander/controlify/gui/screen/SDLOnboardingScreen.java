package dev.isxander.controlify.gui.screen;

import dev.isxander.controlify.Controlify;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.class_124;
import net.minecraft.class_156;
import net.minecraft.class_2561;
import net.minecraft.class_410;
import net.minecraft.class_5250;

public class SDLOnboardingScreen extends class_410 implements DontInteruptScreen {
   public SDLOnboardingScreen(Runnable onceDecided, BooleanConsumer onAnswered) {
      super((yes) -> {
         Controlify.instance().config().globalSettings().loadVibrationNatives = yes;
         Controlify.instance().config().globalSettings().vibrationOnboarded = true;
         Controlify.instance().config().save();
         onceDecided.run();
         onAnswered.accept(yes);
      }, class_2561.method_43471("controlify.sdl3_onboarding.title").method_27692(class_124.field_1067), (class_2561)class_156.method_656(() -> {
         class_5250 message = class_2561.method_43471("controlify.sdl3_onboarding.message");
         message.method_27693("\n\n").method_10852(class_2561.method_43471("controlify.sdl3_onboarding.question"));
         return message;
      }));
   }
}
