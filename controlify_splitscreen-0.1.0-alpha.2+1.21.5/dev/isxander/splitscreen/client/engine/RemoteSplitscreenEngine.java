package dev.isxander.splitscreen.client.engine;

import dev.isxander.splitscreen.client.LocalSplitscreenPawn;
import dev.isxander.splitscreen.client.engine.impl.reparenting.ReparentingRemoteSplitscreenEngine;
import net.minecraft.class_310;
import net.minecraft.class_8710;

public interface RemoteSplitscreenEngine extends SplitscreenEngine {
   static RemoteSplitscreenEngine create(class_310 minecraft, SplitscreenEnginePayloadSender payloadSender, LocalSplitscreenPawn pawn) {
      return new ReparentingRemoteSplitscreenEngine(minecraft, payloadSender, pawn);
   }

   void handleInboundPayload(class_8710 var1);
}
