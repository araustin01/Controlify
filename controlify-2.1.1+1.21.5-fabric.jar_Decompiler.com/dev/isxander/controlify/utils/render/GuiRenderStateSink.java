package dev.isxander.controlify.utils.render;

import net.minecraft.class_332;
import net.minecraft.class_4597;

public interface GuiRenderStateSink {
   class_4597 controlify$bufferSource();

   static class_4597 bufferSource(class_332 graphics) {
      return ((GuiRenderStateSink)graphics).controlify$bufferSource();
   }
}
