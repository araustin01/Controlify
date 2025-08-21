package dev.isxander.controlify.gui.guide;

import net.minecraft.class_1735;
import net.minecraft.class_1799;
import org.jetbrains.annotations.Nullable;

public record ContainerGuideCtx(@Nullable class_1735 hoveredSlot, class_1799 holdingItem, boolean cursorOutsideContainer) {
   public ContainerGuideCtx(@Nullable class_1735 hoveredSlot, class_1799 holdingItem, boolean cursorOutsideContainer) {
      this.hoveredSlot = hoveredSlot;
      this.holdingItem = holdingItem;
      this.cursorOutsideContainer = cursorOutsideContainer;
   }

   @Nullable
   public class_1735 hoveredSlot() {
      return this.hoveredSlot;
   }

   public class_1799 holdingItem() {
      return this.holdingItem;
   }

   public boolean cursorOutsideContainer() {
      return this.cursorOutsideContainer;
   }
}
