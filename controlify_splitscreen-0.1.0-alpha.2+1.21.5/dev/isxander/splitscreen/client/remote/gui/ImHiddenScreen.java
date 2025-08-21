package dev.isxander.splitscreen.client.remote.gui;

import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import dev.isxander.splitscreen.client.features.screenop.ScreenSplitscreenBehaviour;
import dev.isxander.splitscreen.client.features.screenop.ScreenSplitscreenMode;
import net.minecraft.class_2561;
import net.minecraft.class_424;

public class ImHiddenScreen extends class_424 implements ScreenSplitscreenBehaviour {
   public ImHiddenScreen() {
      super(class_2561.method_43470("This Minecraft window is currently being hidden as splitscreen is in fullscreen mode and this is not the host client. If you see this, please report it as a bug."));
   }

   public void method_49589() {
      if (SplitscreenBootstrapper.getController().isPresent()) {
         throw new IllegalStateException("ImHiddenScreen should not be created on the host client.");
      }
   }

   public ScreenSplitscreenMode getSplitscreenMode() {
      return ScreenSplitscreenMode.SPLITSCREEN;
   }
}
