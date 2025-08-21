package dev.isxander.splitscreen.client.host.features.relaunch;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record LaunchInfo(
        Path javaExecutable,
        List<String> jvmArgs,
        String classpath,
        String mainClass,
        List<String> gameArgs,
        Path workingDirectory
) {
    public List<String> buildCommand(boolean includeExecutable) {
        List<String> command = new ArrayList<>();
        if (includeExecutable) {
            command.add(javaExecutable.toString());
        }
        command.addAll(jvmArgs);
        command.add("-cp");
        command.add(classpath);
        command.add(mainClass);
        command.addAll(gameArgs);
        return command;
    }

    public List<String> buildCommandWithArgfile(Path argFile) {
        return List.of(
                javaExecutable.toString(),
                "@" + argFile.toString()
        );
    }

    public String buildArgfile() {
        // Quote JVM args and classpath for argfile usage to preserve spaces on Windows.
        List<String> lines = new ArrayList<>();
        for (String jvmArg : jvmArgs) {
            lines.add(RelaunchUtil.quoteArg(jvmArg));
        }
        lines.add("-cp");
        lines.add(RelaunchUtil.quoteArg(classpath));
        lines.add(mainClass);
        // gameArgs are already quoted at source (Fabric/Prism/Hacky relaunchers)
        lines.addAll(gameArgs);
        return String.join("\n", lines);
    }

    public ProcessBuilder buildProcess() {
        return new ProcessBuilder()
                .command(buildCommand(true))
                .directory(workingDirectory.toFile());
    }

    public ProcessBuilder buildProcessWithArgfile(Path argFile) {
        System.out.println(buildCommandWithArgfile(argFile));
        return new ProcessBuilder()
                .command(buildCommandWithArgfile(argFile))
                .directory(workingDirectory.toFile());
    }

    @Override
    public @NotNull String toString() {
        throw new UnsupportedOperationException("LaunchInfo may contain sensitive information that should not be printed to the console.");
    }
}
