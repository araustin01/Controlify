package dev.isxander.splitscreen.client.ipc;

import java.nio.file.Path;

public interface IPCMethod {
   public static record Unix(String path) implements IPCMethod {
      public Unix(String path) {
         this.path = path;
      }

      public static IPCMethod.Unix inDirectory(Path path) {
         return new IPCMethod.Unix(path.resolve("controlify-splitscreen.sock").toAbsolutePath().toString());
      }

      public String path() {
         return this.path;
      }
   }

   public static record TCP(int port) implements IPCMethod {
      public static final IPCMethod.TCP DEFAULT = new IPCMethod.TCP(54321);

      public TCP(int port) {
         this.port = port;
      }

      public int port() {
         return this.port;
      }
   }
}
