package dev.isxander.controlify.screenop.compat.vanilla;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.mixins.feature.virtualmouse.snapping.RecipeBookComponentAccessor;
import dev.isxander.controlify.mixins.feature.virtualmouse.snapping.RecipeBookPageAccessor;
import dev.isxander.controlify.virtualmouse.VirtualMouseHandler;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.class_10260;
import net.minecraft.class_1735;
import net.minecraft.class_361;
import net.minecraft.class_507;
import net.minecraft.class_512;

public class RecipeBookScreenProcessor<T extends class_10260<?>> extends AbstractContainerScreenProcessor<T> {
   private final RecipeBookScreenProcessor.RecipeBookScreenAccessor recipeBookScreenAccessor;

   public RecipeBookScreenProcessor(T screen, RecipeBookScreenProcessor.RecipeBookScreenAccessor recipeBookScreenAccessor, Supplier<class_1735> hoveredSlot, AbstractContainerScreenProcessor.ClickSlotFunction clickSlotFunction, Predicate<ControllerEntity> doItemSlotActions) {
      super(screen, hoveredSlot, clickSlotFunction, doItemSlotActions);
      this.recipeBookScreenAccessor = recipeBookScreenAccessor;
   }

   protected void handleScreenVMouse(ControllerEntity controller, VirtualMouseHandler vmouse) {
      super.handleScreenVMouse(controller, vmouse);
      class_507<?> recipeBookComponent = this.recipeBookScreenAccessor.controlify$getRecipeBookComponent();
      if (recipeBookComponent.method_2605()) {
         RecipeBookComponentAccessor componentAccessor = (RecipeBookComponentAccessor)recipeBookComponent;
         RecipeBookPageAccessor pageAccessor = (RecipeBookPageAccessor)componentAccessor.getRecipeBookPage();
         List<class_512> tabs = componentAccessor.getTabButtons().stream().filter((tab) -> {
            return tab.field_22764;
         }).toList();
         class_512 selectedTab = componentAccessor.getSelectedTab();
         class_361 button;
         if (ControlifyBindings.VMOUSE_PAGE_NEXT.on(controller).justPressed()) {
            button = pageAccessor.getForwardButton();
            recipeBookComponent.method_25402((double)button.method_46426(), (double)button.method_46427(), 0);
         }

         if (ControlifyBindings.VMOUSE_PAGE_PREV.on(controller).justPressed()) {
            button = pageAccessor.getBackButton();
            recipeBookComponent.method_25402((double)button.method_46426(), (double)button.method_46427(), 0);
         }

         int index;
         if (ControlifyBindings.VMOUSE_PAGE_DOWN.on(controller).justPressed()) {
            index = tabs.indexOf(selectedTab);
            if (index != tabs.size() - 1) {
               button = (class_361)tabs.get(index + 1);
               recipeBookComponent.method_25402((double)button.method_46426(), (double)button.method_46427(), 0);
            }
         }

         if (ControlifyBindings.VMOUSE_PAGE_UP.on(controller).justPressed()) {
            index = tabs.indexOf(selectedTab);
            if (index != 0) {
               button = (class_361)tabs.get(index - 1);
               recipeBookComponent.method_25402((double)button.method_46426(), (double)button.method_46427(), 0);
            }
         }

      }
   }

   public interface RecipeBookScreenAccessor {
      class_507<?> controlify$getRecipeBookComponent();
   }
}
