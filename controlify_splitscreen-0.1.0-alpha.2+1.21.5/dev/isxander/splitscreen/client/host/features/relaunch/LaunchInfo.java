package dev.isxander.splitscreen.client.host.features.relaunch;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public record LaunchInfo(Path javaExecutable, List<String> jvmArgs, String classpath, String mainClass, List<String> gameArgs, Path workingDirectory) {
   public LaunchInfo(Path javaExecutable, List<String> jvmArgs, String classpath, String mainClass, List<String> gameArgs, Path workingDirectory) {
      this.javaExecutable = javaExecutable;
      this.jvmArgs = jvmArgs;
      this.classpath = classpath;
      this.mainClass = mainClass;
      this.gameArgs = gameArgs;
      this.workingDirectory = workingDirectory;
   }

   public List<String> buildCommand(boolean includeExecutable) {
      List<String> command = new ArrayList();
      if (includeExecutable) {
         command.add(this.javaExecutable.toString());
      }

      command.addAll(this.jvmArgs);
      command.add("-cp");
      command.add(this.classpath);
      command.add(this.mainClass);
      command.addAll(this.gameArgs);
      return command;
   }

   public List<String> buildCommandWithArgfile(Path argFile) {
      return List.of(this.javaExecutable.toString(), "@" + argFile.toString());
   }

   public String buildArgfile() {
      return String.join("\n", this.buildCommand(false));
   }

   public ProcessBuilder buildProcess() {
      return (new ProcessBuilder(new String[0])).command(this.buildCommand(true)).directory(this.workingDirectory.toFile());
   }

   public ProcessBuilder buildProcessWithArgfile(Path argFile) {
      System.out.println(this.buildCommandWithArgfile(argFile));
      return (new ProcessBuilder(new String[0])).command(this.buildCommandWithArgfile(argFile)).directory(this.workingDirectory.toFile());
   }

   @NotNull
   public String toString() {
      throw new UnsupportedOperationException("LaunchInfo may contain sensitive information that should not be printed to the console.");
   }

   public Path javaExecutable() {
      return this.javaExecutable;
   }

   public List<String> jvmArgs() {
      return this.jvmArgs;
   }

   public String classpath() {
      return this.classpath;
   }

   public String mainClass() {
      return this.mainClass;
   }

   public List<String> gameArgs() {
      return this.gameArgs;
   }

   public Path workingDirectory() {
      return this.workingDirectory;
   }
}
