package dev.isxander.controlify.mixins.core;

import dev.isxander.controlify.utils.render.GuiRenderStateSink;
import net.minecraft.class_332;
import net.minecraft.class_4597;
import net.minecraft.class_4597.class_4598;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({class_332.class})
public class GuiGraphicsMixin implements GuiRenderStateSink {
   @Shadow
   @Final
   private class_4598 field_44658;

   public class_4597 controlify$bufferSource() {
      return this.field_44658;
   }
}
