package dev.isxander.controlify.compatibility.yacl.screenop;

import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.compatibility.yacl.mixins.YACLScreenCategoryTabAccessor;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.yacl3.gui.OptionListWidget;
import dev.isxander.yacl3.gui.YACLScreen;
import dev.isxander.yacl3.gui.OptionListWidget.Entry;
import dev.isxander.yacl3.gui.YACLScreen.CategoryTab;
import dev.isxander.yacl3.gui.tab.ListHolderWidget;
import java.util.Iterator;
import net.minecraft.class_364;
import net.minecraft.class_8087;

public class YACLScreenProcessor extends ScreenProcessor<YACLScreen> {
   public YACLScreenProcessor(YACLScreen screen) {
      super(screen);
   }

   protected void handleButtons(ControllerEntity controller) {
      if (ControlifyBindings.GUI_ABSTRACT_ACTION_1.on(controller).justPressed()) {
         class_8087 var3 = ((YACLScreen)this.screen).tabManager.method_48614();
         if (var3 instanceof CategoryTab) {
            CategoryTab categoryTab = (CategoryTab)var3;
            ((YACLScreenCategoryTabAccessor)categoryTab).getSaveFinishedButton().method_25306();
         }

         playClackSound();
      }

      super.handleButtons(controller);
   }

   protected void onTabChanged(ControllerEntity controller) {
      class_8087 var3 = ((YACLScreen)this.screen).tabManager.method_48614();
      if (var3 instanceof CategoryTab) {
         CategoryTab categoryTab = (CategoryTab)var3;
         ListHolderWidget<OptionListWidget> optionListHolder = ((YACLScreenCategoryTabAccessor)categoryTab).getOptionList();
         OptionListWidget optionList = (OptionListWidget)optionListHolder.getList();
         optionList.method_44382(0.0D);
         ((YACLScreen)this.screen).method_25395(optionListHolder);
         if (optionList.method_25396().isEmpty()) {
            return;
         }

         Iterator var5 = optionList.method_25396().iterator();

         while(var5.hasNext()) {
            Entry entry = (Entry)var5.next();
            entry.method_25365(false);
            entry.method_25395((class_364)null);
         }

         Entry firstEntry = (Entry)optionList.method_25396().get(0);
         optionList.method_25395(firstEntry);
         firstEntry.method_25395((class_364)firstEntry.method_25396().get(0));
      }

   }
}
