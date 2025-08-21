package dev.isxander.controlify.virtualmouse;

import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import dev.isxander.controlify.mixins.feature.virtualmouse.snapping.RecipeBookComponentAccessor;
import dev.isxander.controlify.mixins.feature.virtualmouse.snapping.RecipeBookPageAccessor;
import java.util.function.Consumer;
import net.minecraft.class_361;
import net.minecraft.class_507;
import org.joml.Vector2i;

public final class SnapUtils {
   private SnapUtils() {
   }

   public static void addRecipeSnapPoints(class_507 recipeBookComponent, Consumer<SnapPoint> consumer) {
      if (recipeBookComponent.method_2605()) {
         RecipeBookComponentAccessor componentAccessor = (RecipeBookComponentAccessor)recipeBookComponent;
         componentAccessor.getTabButtons().forEach((button) -> {
            int x = button.method_46426() + button.method_25368() / 2;
            int y = button.method_46427() + button.method_25364() / 2;
            consumer.accept(new SnapPoint(new Vector2i(x, y), 20));
         });
         class_361 filterButton = componentAccessor.getFilterButton();
         if (filterButton.field_22764) {
            int x = filterButton.method_46426() + filterButton.method_25368() / 2;
            int y = filterButton.method_46427() + filterButton.method_25364() / 2;
            consumer.accept(new SnapPoint(new Vector2i(x, y), 14));
         }

         RecipeBookPageAccessor pageAccessor = (RecipeBookPageAccessor)componentAccessor.getRecipeBookPage();
         pageAccessor.getButtons().forEach((button) -> {
            int x = button.method_46426() + button.method_25368() / 2;
            int y = button.method_46427() + button.method_25364() / 2;
            consumer.accept(new SnapPoint(new Vector2i(x, y), 21));
         });
         class_361 forwardButton = pageAccessor.getForwardButton();
         int x;
         if (forwardButton.field_22764) {
            int x = forwardButton.method_46426() + forwardButton.method_25368() / 2 - 2;
            x = forwardButton.method_46427() + forwardButton.method_25364() / 2;
            consumer.accept(new SnapPoint(new Vector2i(x, x), 10));
         }

         class_361 backButton = pageAccessor.getBackButton();
         if (backButton.field_22764) {
            x = backButton.method_46426() + backButton.method_25368() / 2 + 2;
            int y = backButton.method_46427() + backButton.method_25364() / 2;
            consumer.accept(new SnapPoint(new Vector2i(x, y), 10));
         }
      }

   }
}
