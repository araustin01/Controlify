package dev.isxander.splitscreen.client.remote;

import dev.isxander.splitscreen.client.ControllerBridge;
import dev.isxander.splitscreen.client.engine.impl.reparenting.wm.WindowManager;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundEngineCustomPayloadPacket;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundGiveMeFocusIfForegroundPacket;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundRequestPlayMusicPacket;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundServerDisconnectedPacket;
import dev.isxander.splitscreen.client.ipc.packets.controllerbound.play.ControllerboundSignalReadyPacket;
import net.minecraft.class_2535;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_5195;
import net.minecraft.class_8710;
import org.jetbrains.annotations.Nullable;

public class RemoteControllerBridge implements ControllerBridge {
   private final class_310 minecraft;
   private final class_2535 connection;

   public RemoteControllerBridge(class_310 minecraft, class_2535 connection) {
      this.minecraft = minecraft;
      this.connection = connection;
   }

   public void giveFocusToMeIfForeground() {
      this.connection.method_10743(new ControllerboundGiveMeFocusIfForegroundPacket(WindowManager.get().getNativeWindowHandle(this.minecraft.method_22683().method_4490())));
   }

   public void signalImReady(boolean finished, float progress) {
      this.connection.method_10743(new ControllerboundSignalReadyPacket(finished, progress));
   }

   public void serverDisconnected(class_2561 reason) {
      this.connection.method_10743(new ControllerboundServerDisconnectedPacket(reason));
   }

   public void requestPlayMusic(@Nullable class_5195 music, float volume) {
      this.connection.method_10743(new ControllerboundRequestPlayMusicPacket(music, volume));
   }

   public void sendEnginePayload(class_8710 payload) {
      this.connection.method_10743(new ControllerboundEngineCustomPayloadPacket(payload));
   }

   public boolean isRemote() {
      return true;
   }
}
