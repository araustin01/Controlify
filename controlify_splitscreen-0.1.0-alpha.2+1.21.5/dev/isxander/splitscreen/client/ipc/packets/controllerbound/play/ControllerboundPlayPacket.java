package dev.isxander.splitscreen.client.ipc.packets.controllerbound.play;

import dev.isxander.splitscreen.client.host.ipc.ControllerPlayPacketListener;
import dev.isxander.splitscreen.util.CSUtil;
import net.minecraft.class_2596;
import net.minecraft.class_2598;
import net.minecraft.class_9145;

public interface ControllerboundPlayPacket extends class_2596<ControllerPlayPacketListener> {
   static <T extends ControllerboundPlayPacket> class_9145<T> createType(String id) {
      return new class_9145(class_2598.field_11941, CSUtil.rl(id));
   }
}
