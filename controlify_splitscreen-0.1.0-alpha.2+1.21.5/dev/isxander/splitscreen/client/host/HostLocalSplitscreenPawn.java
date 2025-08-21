package dev.isxander.splitscreen.client.host;

import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.splitscreen.client.LocalSplitscreenPawn;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import org.jetbrains.annotations.Nullable;

public class HostLocalSplitscreenPawn extends LocalSplitscreenPawn {
   public HostLocalSplitscreenPawn(class_310 minecraft, @Nullable ControllerUID associatedController) {
      super(minecraft, 0, associatedController);
   }

   public void joinServer(String host, int port, @Nullable byte[] nonce) {
   }

   public void onConfigSave(class_2960 config) {
   }
}
