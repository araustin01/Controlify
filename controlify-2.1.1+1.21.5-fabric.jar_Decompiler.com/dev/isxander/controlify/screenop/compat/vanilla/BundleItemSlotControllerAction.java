package dev.isxander.controlify.screenop.compat.vanilla;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import net.minecraft.class_1799;
import net.minecraft.class_3532;
import net.minecraft.class_5537;

public class BundleItemSlotControllerAction {
   public static boolean onControllerInput(class_1799 stack, int hoveredSlotIndex, ControllerEntity controller, BundleItemSlotControllerAction.SelectedBundleItemConsumer consumer) {
      int uniqueItems = class_5537.method_61645(stack);
      if (uniqueItems > 0) {
         Controlify.instance().virtualMouseHandler().preventScrollingThisTick();
         boolean up = ControlifyBindings.BUNDLE_NAVI_UP.on(controller).justPressed();
         boolean down = ControlifyBindings.BUNDLE_NAVI_DOWN.on(controller).justPressed();
         boolean left = ControlifyBindings.BUNDLE_NAVI_LEFT.on(controller).justPressed();
         boolean right = ControlifyBindings.BUNDLE_NAVI_RIGHT.on(controller).justPressed();
         int offsetX = 0;
         int offsetY = 0;
         if (up) {
            --offsetY;
         }

         if (down) {
            ++offsetY;
         }

         if (left) {
            --offsetX;
         }

         if (right) {
            ++offsetX;
         }

         if (offsetX != 0 || offsetY != 0) {
            int currentIndex = class_5537.method_61643(stack);
            if (currentIndex == -1) {
               consumer.accept(stack, hoveredSlotIndex, 0);
               return true;
            }

            int rowSize = 4;
            int colSize = Math.min(class_3532.method_15386((float)uniqueItems / (float)rowSize), 3);
            int incompleteRowSize = uniqueItems % rowSize;
            int emptySlots = (rowSize - incompleteRowSize) % rowSize;
            int gridX = (currentIndex + emptySlots) % rowSize;
            int gridY = (currentIndex + emptySlots) / rowSize;
            int newGridX = (gridX + offsetX + rowSize) % rowSize;
            int newGridY = (gridY + offsetY + colSize) % colSize;
            if (newGridY >= 0 && newGridY < colSize) {
               int newIndex = Math.max(newGridX + newGridY * rowSize - emptySlots, 0);
               consumer.accept(stack, hoveredSlotIndex, newIndex);
            }

            return true;
         }
      }

      return false;
   }

   public interface SelectedBundleItemConsumer {
      void accept(class_1799 var1, int var2, int var3);
   }
}
