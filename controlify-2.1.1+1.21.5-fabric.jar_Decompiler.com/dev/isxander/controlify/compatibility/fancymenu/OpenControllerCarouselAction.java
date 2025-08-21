package dev.isxander.controlify.compatibility.fancymenu;

import de.keksuccino.fancymenu.customization.action.Action;
import dev.isxander.controlify.gui.screen.ControllerCarouselScreen;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class OpenControllerCarouselAction extends Action {
   public OpenControllerCarouselAction() {
      super("controlify:open-carousel");
   }

   public boolean hasValue() {
      return false;
   }

   public void execute(@Nullable String s) {
      ControllerCarouselScreen.openConfigScreen(class_310.method_1551().field_1755);
   }

   @NotNull
   public class_2561 getActionDisplayName() {
      return class_2561.method_43471("controlify.gui.button");
   }

   @NotNull
   public class_2561[] getActionDescription() {
      return new class_2561[0];
   }

   @Nullable
   public class_2561 getValueDisplayName() {
      return null;
   }

   @Nullable
   public String getValueExample() {
      return "";
   }
}
