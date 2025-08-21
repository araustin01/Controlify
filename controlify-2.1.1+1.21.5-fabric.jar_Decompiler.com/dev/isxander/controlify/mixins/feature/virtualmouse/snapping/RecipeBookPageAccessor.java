package dev.isxander.controlify.mixins.feature.virtualmouse.snapping;

import java.util.List;
import net.minecraft.class_361;
import net.minecraft.class_513;
import net.minecraft.class_514;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_513.class})
public interface RecipeBookPageAccessor {
   @Accessor("field_3131")
   List<class_514> getButtons();

   @Accessor("field_3128")
   class_361 getForwardButton();

   @Accessor("field_3130")
   class_361 getBackButton();
}
