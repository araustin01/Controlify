package dev.isxander.controlify.screenop.compat.vanilla;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.platform.client.CreativeTabHelper;
import dev.isxander.controlify.platform.client.PlatformClientUtil;
import dev.isxander.controlify.virtualmouse.VirtualMouseHandler;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.class_1735;
import net.minecraft.class_1761;
import net.minecraft.class_481;

public class CreativeModeInventoryScreenProcessor extends AbstractContainerScreenProcessor<class_481> {
   private final CreativeTabHelper tabHelper;

   public CreativeModeInventoryScreenProcessor(class_481 screen, Supplier<class_1735> hoveredSlot, AbstractContainerScreenProcessor.ClickSlotFunction clickSlotFunction, Predicate<ControllerEntity> itemSlotAction) {
      super(screen, hoveredSlot, clickSlotFunction, itemSlotAction);
      this.tabHelper = PlatformClientUtil.createCreativeTabHelper(screen);
   }

   public void onWidgetRebuild() {
      super.onWidgetRebuild();
      Controlify.instance().virtualMouseHandler().snapToClosestPoint();
   }

   protected void handleScreenVMouse(ControllerEntity controller, VirtualMouseHandler vmouse) {
      List<class_1761> tabs = this.tabHelper.getTabsForPage(this.tabHelper.getCurrentPage());
      int newIndex;
      int newPage;
      if (ControlifyBindings.GUI_NEXT_TAB.on(controller).justPressed()) {
         newIndex = tabs.indexOf(this.tabHelper.getSelectedTab()) + 1;
         if (newIndex >= tabs.size()) {
            newIndex = 0;
            newPage = this.tabHelper.getCurrentPage() + 1;
            if (newPage >= this.tabHelper.getPageCount()) {
               newPage = 0;
            }

            this.tabHelper.setCurrentPage(newPage);
            tabs = this.tabHelper.getTabsForPage(newPage);
         }

         this.tabHelper.setSelectedTab((class_1761)tabs.get(newIndex));
      }

      if (ControlifyBindings.GUI_PREV_TAB.on(controller).justPressed()) {
         newIndex = tabs.indexOf(this.tabHelper.getSelectedTab()) - 1;
         if (newIndex < 0) {
            newPage = this.tabHelper.getCurrentPage() - 1;
            if (newPage < 0) {
               newPage = this.tabHelper.getPageCount() - 1;
            }

            this.tabHelper.setCurrentPage(newPage);
            tabs = this.tabHelper.getTabsForPage(newPage);
            newIndex = tabs.size() - 1;
         }

         this.tabHelper.setSelectedTab((class_1761)tabs.get(newIndex));
      }

      super.handleScreenVMouse(controller, vmouse);
   }
}
