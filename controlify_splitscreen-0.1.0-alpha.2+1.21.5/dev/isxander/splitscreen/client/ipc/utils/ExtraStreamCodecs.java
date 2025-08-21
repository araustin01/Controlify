package dev.isxander.splitscreen.client.ipc.utils;

import dev.isxander.controlify.controller.ControllerUID;
import io.netty.buffer.ByteBuf;
import net.minecraft.class_9135;
import net.minecraft.class_9139;

public final class ExtraStreamCodecs {
   public static final class_9139<ByteBuf, ControllerUID> CONTROLLER_UID;

   private ExtraStreamCodecs() {
   }

   static {
      CONTROLLER_UID = class_9135.field_48554.method_56432(ControllerUID::new, ControllerUID::string);
   }
}
