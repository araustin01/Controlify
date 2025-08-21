package dev.isxander.controlify.platform.client;

import java.util.List;
import net.minecraft.class_1761;

public interface CreativeTabHelper {
   void setCurrentPage(int var1);

   int getCurrentPage();

   int getPageCount();

   List<class_1761> getTabsForPage(int var1);

   class_1761 getSelectedTab();

   void setSelectedTab(class_1761 var1);
}
