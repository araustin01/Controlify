package dev.isxander.splitscreen.client.host.features.relaunch;

import dev.isxander.controlify.utils.Platform;
import dev.isxander.splitscreen.client.features.relaunch.RelaunchException;
import java.lang.management.ManagementFactory;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

public class RelaunchUtil {
   public static Path findJavaExecutable() {
      return (Path)findJavaExecutableFromProcessHandle().or(RelaunchUtil::findJavaExecutableFromSystemProperty).orElseThrow(() -> {
         return new RelaunchException("Could not find Java executable");
      });
   }

   private static Optional<Path> findJavaExecutableFromProcessHandle() {
      return ProcessHandle.current().info().command().map((x$0) -> {
         return Path.of(x$0, new String[0]);
      }).filter(Path::isAbsolute).filter(Files::isExecutable);
   }

   private static Optional<Path> findJavaExecutableFromSystemProperty() {
      String javaHome = System.getProperty("java.home");
      if (javaHome != null && !javaHome.isEmpty()) {
         Path jrePath = Path.of(javaHome, new String[0]);
         boolean isWindows = Platform.current() == Platform.WINDOWS;
         boolean consoleless = System.console() == null;
         String executableName = isWindows ? (consoleless ? "javaw.exe" : "java.exe") : "java";
         Path executablePath = jrePath.resolve(executableName);
         return Files.exists(executablePath, new LinkOption[0]) && Files.isExecutable(executablePath) ? Optional.of(executablePath.toAbsolutePath()) : Optional.empty();
      } else {
         return Optional.empty();
      }
   }

   public static List<String> findJVMArgs() {
      return new ArrayList(ManagementFactory.getRuntimeMXBean().getInputArguments());
   }

   public static String findClasspath() {
      return ManagementFactory.getRuntimeMXBean().getClassPath();
   }

   public static Path findWorkingDirectory() {
      return Path.of(System.getProperty("user.dir"), new String[0]);
   }

   public static String quoteArg(String arg) {
      String specials = " #'\"\n\r\t\f";
      if (!containsAnyChar(arg, " #'\"\n\r\t\f")) {
         return arg;
      } else {
         StringBuilder sb = new StringBuilder(arg.length() * 2);

         for(int i = 0; i < arg.length(); ++i) {
            char c = arg.charAt(i);
            switch(c) {
            case '\t':
               sb.append("\"\\t\"");
               break;
            case '\n':
               sb.append("\"\\n\"");
               break;
            case '\f':
               sb.append("\"\\f\"");
               break;
            case '\r':
               sb.append("\"\\r\"");
               break;
            case ' ':
            case '#':
            case '\'':
               sb.append('"').append(c).append('"');
               break;
            case '"':
               sb.append("\"\\\"\"");
               break;
            default:
               sb.append(c);
            }
         }

         return sb.toString();
      }
   }

   private static boolean containsAnyChar(@NotNull String value, @NotNull String chars) {
      return chars.length() > value.length() ? containsAnyChar(value, chars, 0, value.length()) : containsAnyChar(chars, value, 0, chars.length());
   }

   private static boolean containsAnyChar(@NotNull String value, @NotNull String chars, int start, int end) {
      for(int i = start; i < end; ++i) {
         if (chars.indexOf(value.charAt(i)) >= 0) {
            return true;
         }
      }

      return false;
   }
}
