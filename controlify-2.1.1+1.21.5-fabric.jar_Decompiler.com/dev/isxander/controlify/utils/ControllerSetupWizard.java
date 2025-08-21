package dev.isxander.controlify.utils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.class_310;
import net.minecraft.class_437;

public class ControllerSetupWizard {
   private final Deque<ControllerSetupWizard.Stage> stages = new ArrayDeque();
   private final List<class_437> screens = new ArrayList();

   public void addStage(Supplier<Boolean> enabled, ControllerSetupWizard.ScreenCreator screenCreator) {
      this.stages.add(new ControllerSetupWizard.Stage(enabled, screenCreator));
   }

   public void addStage(ControllerSetupWizard.ScreenCreator screenCreator) {
      this.addStage(() -> {
         return true;
      }, screenCreator);
   }

   public class_437 start(class_437 resultantScreen) {
      class_437 prevScreen = resultantScreen;

      while(!this.stages.isEmpty()) {
         ControllerSetupWizard.Stage stage = (ControllerSetupWizard.Stage)this.stages.pollLast();
         if ((Boolean)stage.enabled().get()) {
            class_437 screen = stage.screenCreator().createWizardScreen(prevScreen);
            if (screen != null) {
               prevScreen = screen;
               this.screens.add(screen);
            }
         }
      }

      return prevScreen;
   }

   public boolean isDone() {
      class_437 screen = class_310.method_1551().field_1755;
      return screen == null || this.screens.stream().noneMatch((resultantScreen) -> {
         return resultantScreen == screen;
      });
   }

   private static record Stage(Supplier<Boolean> enabled, ControllerSetupWizard.ScreenCreator screenCreator) {
      private Stage(Supplier<Boolean> enabled, ControllerSetupWizard.ScreenCreator screenCreator) {
         this.enabled = enabled;
         this.screenCreator = screenCreator;
      }

      public Supplier<Boolean> enabled() {
         return this.enabled;
      }

      public ControllerSetupWizard.ScreenCreator screenCreator() {
         return this.screenCreator;
      }
   }

   public interface ScreenCreator {
      class_437 createWizardScreen(class_437 var1);
   }
}
