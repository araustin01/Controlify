package dev.isxander.controlify.compatibility.yacl.mixins;

import dev.isxander.yacl3.gui.OptionListWidget;
import dev.isxander.yacl3.gui.YACLScreen.CategoryTab;
import dev.isxander.yacl3.gui.tab.ListHolderWidget;
import net.minecraft.class_4185;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({CategoryTab.class})
public interface YACLScreenCategoryTabAccessor {
   @Accessor("saveFinishedButton")
   class_4185 getSaveFinishedButton();

   @Accessor("optionList")
   ListHolderWidget<OptionListWidget> getOptionList();
}
