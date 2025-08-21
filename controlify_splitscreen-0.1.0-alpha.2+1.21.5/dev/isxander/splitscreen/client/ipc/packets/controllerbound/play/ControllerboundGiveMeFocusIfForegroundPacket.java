package dev.isxander.splitscreen.client.ipc.packets.controllerbound.play;

import dev.isxander.splitscreen.client.engine.impl.reparenting.wm.NativeWindowHandle;
import dev.isxander.splitscreen.client.host.ipc.ControllerPlayPacketListener;
import net.minecraft.class_2540;
import net.minecraft.class_9139;
import net.minecraft.class_9145;

public record ControllerboundGiveMeFocusIfForegroundPacket(NativeWindowHandle childWindow) implements ControllerboundPlayPacket {
   public static final class_9139<class_2540, ControllerboundGiveMeFocusIfForegroundPacket> CODEC;
   public static final class_9145<ControllerboundGiveMeFocusIfForegroundPacket> TYPE;

   public ControllerboundGiveMeFocusIfForegroundPacket(NativeWindowHandle childWindow) {
      this.childWindow = childWindow;
   }

   public void handle(ControllerPlayPacketListener handler) {
      handler.handleGiveChildFocusIfForeground(this);
   }

   public class_9145<ControllerboundGiveMeFocusIfForegroundPacket> method_65080() {
      return TYPE;
   }

   public NativeWindowHandle childWindow() {
      return this.childWindow;
   }

   static {
      CODEC = NativeWindowHandle.STREAM_CODEC.method_56432(ControllerboundGiveMeFocusIfForegroundPacket::new, ControllerboundGiveMeFocusIfForegroundPacket::childWindow);
      TYPE = ControllerboundPlayPacket.createType("give_me_focus_if_foreground");
   }
}
