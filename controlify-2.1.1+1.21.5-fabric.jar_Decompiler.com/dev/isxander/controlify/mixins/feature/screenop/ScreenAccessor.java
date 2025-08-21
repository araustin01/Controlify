package dev.isxander.controlify.mixins.feature.screenop;

import java.util.List;
import net.minecraft.class_4068;
import net.minecraft.class_437;
import net.minecraft.class_8016;
import net.minecraft.class_8028;
import net.minecraft.class_8023.class_8024;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({class_437.class})
public interface ScreenAccessor {
   @Invoker("method_48264")
   class_8024 invokeCreateArrowEvent(class_8028 var1);

   @Invoker("method_48263")
   void invokeChangeFocus(class_8016 var1);

   @Invoker("method_48267")
   void invokeClearFocus();

   @Accessor("field_33816")
   List<class_4068> getRenderables();
}
