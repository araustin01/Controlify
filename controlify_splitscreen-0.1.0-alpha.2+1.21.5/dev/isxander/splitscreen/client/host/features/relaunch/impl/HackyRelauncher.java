package dev.isxander.splitscreen.client.host.features.relaunch.impl;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.isxander.splitscreen.client.features.relaunch.RelaunchException;
import dev.isxander.splitscreen.client.host.features.relaunch.LaunchInfo;
import dev.isxander.splitscreen.client.host.features.relaunch.RelaunchUtil;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class HackyRelauncher {
   private static LaunchInfo launchInfo = null;
   private static final Pattern FLAG_PATTERN = Pattern.compile("(--\\S+)\\s+(.+?)(?=(?:\\s+--\\S+)|$)");

   public static LaunchInfo getLaunchInfo() {
      if (launchInfo == null) {
         launchInfo = createLaunchInfo();
      }

      return launchInfo;
   }

   private static LaunchInfo createLaunchInfo() {
      Path javaExecutable = RelaunchUtil.findJavaExecutable();
      String mainClass = findEntrypointMainClassFromStacktrace();
      String programArgs = System.getProperty("sun.java.command");
      if (programArgs == null) {
         throw new RelaunchException("Could not find program arguments");
      } else if (!programArgs.startsWith(mainClass)) {
         throw new RelaunchException("Program arguments do not start with main class");
      } else {
         String joinedGameArgs = programArgs.substring(mainClass.length()).trim();
         List<String> gameArgs = splitGameArgs(joinedGameArgs);
         int accessTokenIndex = gameArgs.indexOf("--accessToken");
         if (accessTokenIndex != -1) {
            gameArgs.remove(accessTokenIndex);
            gameArgs.remove(accessTokenIndex);
         }

         List<String> jvmArgs = RelaunchUtil.findJVMArgs();
         String classpath = RelaunchUtil.findClasspath();
         Path workingDirectory = RelaunchUtil.findWorkingDirectory();
         return new LaunchInfo(javaExecutable, jvmArgs, classpath, mainClass, gameArgs, workingDirectory);
      }
   }

   private static List<String> splitGameArgs(String args) {
      return FLAG_PATTERN.matcher(args).results().flatMap((m) -> {
         return Stream.of(m.group(1), m.group(2).trim());
      }).map(RelaunchUtil::quoteArg).toList();
   }

   private static String findEntrypointMainClassFromStacktrace() {
      RenderSystem.assertOnRenderThread();
      Thread currentThread = Thread.currentThread();
      StackTraceElement[] stackTrace = currentThread.getStackTrace();
      StackTraceElement bottomFrame = stackTrace[stackTrace.length - 1];
      if (!bottomFrame.getMethodName().equals("main")) {
         throw new RelaunchException("Could not find entrypoint main class: The bottom stack frame is not main.");
      } else {
         return bottomFrame.getClassName();
      }
   }
}
