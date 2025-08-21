package dev.isxander.splitscreen.client;

import net.minecraft.class_2561;
import net.minecraft.class_5195;
import org.jetbrains.annotations.Nullable;

public interface ControllerBridge extends Bridge {
   void giveFocusToMeIfForeground();

   void signalImReady(boolean var1, float var2);

   void serverDisconnected(class_2561 var1);

   void requestPlayMusic(@Nullable class_5195 var1, float var2);
}
