package dev.isxander.splitscreen.server.login.packets;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.class_2540;
import net.minecraft.class_9139;

public interface CodedPacket<T> {
   class_9139<class_2540, T> codec();

   default class_2540 encode() {
      class_2540 buf = PacketByteBufs.create();
      this.codec().encode(buf, this);
      return buf;
   }
}
