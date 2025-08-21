package dev.isxander.splitscreen.client.host.features.relaunch;

import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.splitscreen.client.features.relaunch.RelaunchArguments;
import dev.isxander.splitscreen.client.features.relaunch.RelaunchException;
import dev.isxander.splitscreen.client.features.relaunch.RelaunchQuickPlayFormat;
import dev.isxander.splitscreen.client.host.RemoteSplitscreenPawn;
import dev.isxander.splitscreen.client.host.SplitscreenController;
import dev.isxander.splitscreen.client.host.features.relaunch.impl.FabricLoaderRelauncher;
import dev.isxander.splitscreen.client.host.features.relaunch.impl.HackyRelauncher;
import dev.isxander.splitscreen.client.host.features.relaunch.impl.PrismRelauncher;
import dev.isxander.splitscreen.client.host.util.LANUtil;
import dev.isxander.splitscreen.client.ipc.IPCMethod;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.ProcessBuilder.Redirect;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_310;
import net.minecraft.class_639;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RelaunchProcessHandler {
   private final Process process;
   private final int pawnIndex;
   private final Logger logger;
   @Nullable
   private RemoteSplitscreenPawn pawn;

   public static RelaunchProcessHandler createProcess(class_310 minecraft, ControllerUID controller, SplitscreenController splitscreenController, int pawnIndex, IPCMethod ipcMethod) {
      boolean isDevLaunchInjector = FabricLoader.getInstance().isDevelopmentEnvironment();
      LaunchInfo launchInfo = !isDevLaunchInjector ? FabricLoaderRelauncher.getLaunchInfo() : (PrismRelauncher.isPrism() ? PrismRelauncher.getLaunchInfo() : HackyRelauncher.getLaunchInfo());

      Path argFile;
      try {
         argFile = launchInfo.workingDirectory().relativize(Files.createTempFile(launchInfo.workingDirectory(), "pawn-args", ".txt"));
      } catch (IOException var15) {
         throw new RelaunchException("Failed to create arg file", var15);
      }

      label48: {
         launchInfo.jvmArgs().add(RelaunchArguments.RELAUNCHED.asArgument(true));
         launchInfo.jvmArgs().add(RelaunchArguments.CONTROLLER.asArgument(controller));
         launchInfo.jvmArgs().add(RelaunchArguments.PAWN_INDEX.asArgument(pawnIndex));
         launchInfo.jvmArgs().add(RelaunchArguments.HOST_UUID.asArgument(minecraft.method_1548().method_44717()));
         launchInfo.jvmArgs().add(RelaunchArguments.ARGFILE_PATH.asArgument(argFile.toString()));
         Objects.requireNonNull(ipcMethod);
         byte var9 = 0;
         boolean var10001;
         Throwable var21;
         switch(ipcMethod.typeSwitch<invokedynamic>(ipcMethod, var9)) {
         case 0:
            IPCMethod.TCP var10 = (IPCMethod.TCP)ipcMethod;
            IPCMethod.TCP var23 = var10;

            int var24;
            try {
               var24 = var23.port();
            } catch (Throwable var17) {
               var21 = var17;
               var10001 = false;
               break;
            }

            int var20 = var24;
            launchInfo.jvmArgs().add(RelaunchArguments.IPC_TCP_PORT.asArgument(var20));
            break label48;
         case 1:
            IPCMethod.Unix var12 = (IPCMethod.Unix)ipcMethod;
            IPCMethod.Unix var10000 = var12;

            String var22;
            try {
               var22 = var10000.path();
            } catch (Throwable var16) {
               var21 = var16;
               var10001 = false;
               break;
            }

            String var14 = var22;
            launchInfo.jvmArgs().add(RelaunchArguments.IPC_SOCKET_PATH.asArgument(var14));
            break label48;
         default:
            throw new UnsupportedOperationException("Unrecognized IPC method: " + String.valueOf(ipcMethod));
         }

         Throwable var19 = var21;
         throw new MatchException(var19.toString(), var19);
      }

      String username = minecraft.method_1548().method_1676();
      String pawnUsername = username + "." + pawnIndex;
      launchInfo.jvmArgs().add(RelaunchArguments.USERNAME.asArgument(pawnUsername));
      LANUtil.getOrPublishLANServer().or(() -> {
         return Optional.ofNullable(minecraft.method_1558()).map((data) -> {
            return class_639.method_2950(data.field_3761);
         });
      }).ifPresent((address) -> {
         String quickPlayFormat = RelaunchQuickPlayFormat.asString(address.toString(), Hex.encodeHexString(splitscreenController.getLocalPawn().getLastLoginNonce()));
         launchInfo.jvmArgs().add(RelaunchArguments.LAN_GAME.asArgument(quickPlayFormat));
      });
      return new RelaunchProcessHandler(launchInfo, pawnIndex, argFile);
   }

   private RelaunchProcessHandler(LaunchInfo launchInfo, int pawnIndex, Path argfilePath) {
      this.pawnIndex = pawnIndex;
      this.logger = LoggerFactory.getLogger("RelaunchProcessHandler#" + pawnIndex);
      this.process = this.handleProcess(launchInfo, argfilePath);
   }

   public boolean isAlive() {
      return this.process.isAlive();
   }

   public CompletableFuture<RelaunchProcessHandler> onExit() {
      return this.process.onExit().thenApply((proc) -> {
         return this;
      });
   }

   private Process startProcess(LaunchInfo launchInfo, Path argfilePath) {
      try {
         String argFileContent = launchInfo.buildArgfile();
         Files.writeString(argfilePath, argFileContent, new OpenOption[0]);
         ProcessBuilder processBuilder = launchInfo.buildProcessWithArgfile(argfilePath).redirectOutput(Redirect.PIPE).redirectError(Redirect.PIPE).redirectInput(Redirect.INHERIT);

         try {
            return processBuilder.start();
         } catch (IOException var6) {
            throw new RelaunchException("Failed to start new pawn process", var6);
         }
      } catch (Exception var7) {
         throw new RuntimeException(var7);
      }
   }

   private Process handleProcess(LaunchInfo launchInfo, Path argfilePath) {
      Thread[] readerThreads = new Thread[2];
      Process process = this.startProcess(launchInfo, argfilePath);
      readerThreads[0] = Thread.ofVirtual().name("stdout-reader").start(this.pipeStream(process.getInputStream(), System.out));
      readerThreads[1] = Thread.ofVirtual().name("stderr-reader").start(this.pipeStream(process.getErrorStream(), System.err));
      this.logger.info("Pawn #{} started with PID {}", this.pawnIndex, process.pid());
      CompletableFuture<Process> onExit = process.onExit();
      onExit.thenAcceptAsync((exitedProcess) -> {
         this.logger.info("Pawn #{} exited with code {}", this.pawnIndex, exitedProcess.exitValue());

         try {
            Thread[] var3 = readerThreads;
            int var4 = readerThreads.length;

            for(int var5 = 0; var5 < var4; ++var5) {
               Thread thread = var3[var5];
               thread.join(1000L);
            }

            this.logger.info("All reader threads joined successfully for Pawn #{}", this.pawnIndex);
         } catch (InterruptedException var7) {
            this.logger.error("Async callback interrupted while joining reader threads for Pawn #{}", this.pawnIndex, var7);
            Thread.currentThread().interrupt();
         }

      });
      onExit.exceptionally((throwable) -> {
         this.logger.error("An exception occurred in the async completion stage", throwable);
         return null;
      });
      return process;
   }

   public void setPawn(@Nullable RemoteSplitscreenPawn pawn) {
      if (this.pawn != null && this.pawn != pawn) {
         throw new IllegalStateException("Pawn already set to " + String.valueOf(this.pawn));
      } else {
         this.pawn = pawn;
      }
   }

   @Nullable
   public RemoteSplitscreenPawn getPawn() {
      return this.pawn;
   }

   private Runnable pipeStream(InputStream stream, PrintStream output) {
      return () -> {
         String prefix = "[Pawn #" + this.pawnIndex + "] ";

         try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            String line;
            try {
               while((line = reader.readLine()) != null) {
                  output.println(prefix + line);
               }
            } catch (Throwable var8) {
               try {
                  reader.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }

               throw var8;
            }

            reader.close();
         } catch (IOException var9) {
            this.logger.error("Failed to read from stream", var9);
         }

      };
   }
}
