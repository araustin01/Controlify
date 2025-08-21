package dev.isxander.controlify.platform.client.events;

import net.minecraft.class_332;
import net.minecraft.class_437;

@FunctionalInterface
public interface ScreenRenderEvent {
   void onRender(class_437 var1, class_332 var2, int var3, int var4, float var5);
}
