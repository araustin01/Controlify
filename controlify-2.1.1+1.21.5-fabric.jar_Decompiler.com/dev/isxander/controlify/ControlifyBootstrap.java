package dev.isxander.controlify;

import dev.isxander.controlify.server.ControlifyServer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;

public class ControlifyBootstrap implements ClientModInitializer, ModInitializer, DedicatedServerModInitializer {
   public void onInitializeClient() {
      Controlify.instance().preInitialiseControlify();
   }

   public void onInitializeServer() {
      ControlifyServer.getInstance().onInitializeServer();
   }

   public void onInitialize() {
      ControlifyServer.getInstance().onInitialize();
   }
}
