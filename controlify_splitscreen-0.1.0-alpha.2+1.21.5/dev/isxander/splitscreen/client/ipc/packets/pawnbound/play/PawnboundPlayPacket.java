package dev.isxander.splitscreen.client.ipc.packets.pawnbound.play;

import dev.isxander.splitscreen.client.remote.ipc.PawnPlayPacketListener;
import dev.isxander.splitscreen.util.CSUtil;
import net.minecraft.class_2596;
import net.minecraft.class_2598;
import net.minecraft.class_9145;

public interface PawnboundPlayPacket extends class_2596<PawnPlayPacketListener> {
   static <T extends PawnboundPlayPacket> class_9145<T> createType(String id) {
      return new class_9145(class_2598.field_11942, CSUtil.rl(id));
   }
}
