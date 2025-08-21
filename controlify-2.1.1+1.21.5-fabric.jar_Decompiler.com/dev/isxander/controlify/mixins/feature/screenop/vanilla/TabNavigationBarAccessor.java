package dev.isxander.controlify.mixins.feature.screenop.vanilla;

import com.google.common.collect.ImmutableList;
import net.minecraft.class_8087;
import net.minecraft.class_8088;
import net.minecraft.class_8089;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({class_8089.class})
public interface TabNavigationBarAccessor {
   @Accessor("field_42147")
   ImmutableList<class_8087> getTabs();

   @Accessor("field_42146")
   class_8088 getTabManager();
}
