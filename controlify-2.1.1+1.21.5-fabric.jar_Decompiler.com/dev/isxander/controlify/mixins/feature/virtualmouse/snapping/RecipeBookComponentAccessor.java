package dev.isxander.controlify.mixins.feature.virtualmouse.snapping;

import java.util.List;
import net.minecraft.class_361;
import net.minecraft.class_507;
import net.minecraft.class_512;
import net.minecraft.class_513;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_507.class})
public interface RecipeBookComponentAccessor {
   @Accessor("field_3086")
   class_513 getRecipeBookPage();

   @Accessor("field_3094")
   List<class_512> getTabButtons();

   @Accessor("field_3098")
   class_512 getSelectedTab();

   @Accessor("field_3088")
   class_361 getFilterButton();
}
