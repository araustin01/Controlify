package dev.isxander.controlify.platform.client.fabric;

import dev.isxander.controlify.platform.client.CreativeTabHelper;
import java.util.List;
import net.minecraft.class_1761;
import net.minecraft.class_481;

public class FAPIApiCreativeTabHelper implements CreativeTabHelper {
   private final class_481 screen;

   public FAPIApiCreativeTabHelper(class_481 screen) {
      this.screen = screen;
   }

   public void setCurrentPage(int page) {
      this.screen.switchToPage(page);
   }

   public int getCurrentPage() {
      return this.screen.getCurrentPage();
   }

   public int getPageCount() {
      return this.screen.getPageCount();
   }

   public List<class_1761> getTabsForPage(int page) {
      return this.screen.getItemGroupsOnPage(page);
   }

   public class_1761 getSelectedTab() {
      return this.screen.getSelectedItemGroup();
   }

   public void setSelectedTab(class_1761 tab) {
      this.screen.setSelectedItemGroup(tab);
   }
}
