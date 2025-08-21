package dev.isxander.splitscreen.client;

import com.mojang.logging.LogUtils;
import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.splitscreen.client.config.SplitscreenConfig;
import dev.isxander.splitscreen.client.engine.SplitscreenEngine;
import dev.isxander.splitscreen.client.features.relaunch.RelaunchArguments;
import dev.isxander.splitscreen.client.features.relaunch.RelaunchException;
import dev.isxander.splitscreen.client.features.screenop.ScreenSplitscreenModeRegistry;
import dev.isxander.splitscreen.client.host.SplitscreenController;
import dev.isxander.splitscreen.client.ipc.IPCMethod;
import dev.isxander.splitscreen.client.remote.RemotePawnMain;
import dev.isxander.splitscreen.client.util.SocketUtil;
import dev.isxander.splitscreen.server.SplitscreenSSClient;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import net.minecraft.class_310;
import net.minecraft.class_3521;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class SplitscreenBootstrapper {
   private static final Logger LOGGER = LogUtils.getLogger();
   @Nullable
   private static SplitscreenController controller;
   @Nullable
   private static RemotePawnMain remotePawnMain;

   public static void bootstrap(class_310 minecraft) {
      LOGGER.info("Boostrapping Controlify splitscreen!");
      ScreenSplitscreenModeRegistry.init();
      SplitscreenConfig.INSTANCE.loadFromFile();
      boolean relaunched = (Boolean)RelaunchArguments.RELAUNCHED.get().orElse(false);
      if (relaunched) {
         RelaunchArguments.ARGFILE_PATH.get().ifPresent((pathString) -> {
            Path path = Path.of(pathString, new String[0]);

            try {
               Files.delete(path);
            } catch (Exception var3) {
               LOGGER.error("Failed to delete arg file", var3);
            }

         });
         int port = (Integer)RelaunchArguments.IPC_TCP_PORT.get().orElse(-1);
         String socketPath = (String)RelaunchArguments.IPC_SOCKET_PATH.get().orElse((Object)null);
         Object ipcMethod;
         if (socketPath != null) {
            ipcMethod = new IPCMethod.Unix(socketPath);
         } else {
            if (port == -1) {
               throw new RelaunchException("No socket path or TCP port provided");
            }

            ipcMethod = new IPCMethod.TCP(port);
         }

         int pawnIndex = (Integer)RelaunchArguments.PAWN_INDEX.get().orElseThrow();
         LOGGER.info("Detected relaunch, becoming pawn#{} and connecting to controller via TCP at port {}", pawnIndex, port);
         bootstrapAsPawn(minecraft, (IPCMethod)ipcMethod);
      } else {
         LOGGER.info("Not a relaunch, becoming controller!");
         Object ipcMethod;
         if (SocketUtil.isAfUnixSupported()) {
            ipcMethod = IPCMethod.Unix.inDirectory(Path.of(System.getProperty("user.home"), new String[0]));
         } else {
            int openPort = class_3521.method_15302();
            ipcMethod = new IPCMethod.TCP(openPort);
         }

         bootstrapAsController(minecraft, (IPCMethod)ipcMethod);
      }

      SplitscreenSSClient.init();
   }

   private static void bootstrapAsPawn(class_310 minecraft, IPCMethod connectionMethod) {
      remotePawnMain = new RemotePawnMain(minecraft, connectionMethod);
   }

   private static void bootstrapAsController(class_310 minecraft, IPCMethod connectionMethod) {
      controller = new SplitscreenController(minecraft, connectionMethod, (ControllerUID)null);
   }

   public static boolean isSplitscreen() {
      return controller != null || remotePawnMain != null;
   }

   public static Optional<ControllerBridge> getControllerBridge() {
      return getController().map(SplitscreenController::getControllerBridge).or(() -> {
         return getPawn().map(RemotePawnMain::getControllerBridge);
      });
   }

   public static Optional<SplitscreenController> getController() {
      return Optional.ofNullable(controller);
   }

   public static Optional<RemotePawnMain> getPawn() {
      return Optional.ofNullable(remotePawnMain);
   }

   public static Optional<SplitscreenEngine> getEngine() {
      return getController().map(SplitscreenController::getSplitscreenEngine).or(() -> {
         return getPawn().map(RemotePawnMain::getSplitscreenEngine);
      });
   }

   public static Optional<Side> getSide() {
      if (controller != null) {
         return Optional.of(Side.CONTROLLER);
      } else {
         return remotePawnMain != null ? Optional.of(Side.PAWN) : Optional.empty();
      }
   }
}
