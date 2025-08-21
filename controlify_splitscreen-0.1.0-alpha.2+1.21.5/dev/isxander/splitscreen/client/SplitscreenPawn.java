package dev.isxander.splitscreen.client;

import dev.isxander.controlify.controller.ControllerUID;
import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;

public interface SplitscreenPawn extends Bridge {
   int pawnIndex();

   void joinServer(String var1, int var2, @Nullable byte[] var3);

   void closeGame();

   void disconnectFromServer();

   SplitscreenPosition getWindowSplitscreenMode();

   void useController(ControllerUID var1);

   void onConfigSave(class_2960 var1);

   @Nullable
   ControllerUID getAssociatedController();
}
