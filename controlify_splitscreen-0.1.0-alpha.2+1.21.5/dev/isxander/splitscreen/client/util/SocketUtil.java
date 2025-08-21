package dev.isxander.splitscreen.client.util;

import com.mojang.logging.LogUtils;
import dev.isxander.splitscreen.client.ipc.IPCMethod;
import java.io.IOException;
import java.net.StandardProtocolFamily;
import java.nio.channels.ServerSocketChannel;
import org.slf4j.Logger;

public final class SocketUtil {
   private static final Logger LOGGER = LogUtils.getLogger();
   private static final boolean IS_AF_UNIX_SUPPORTED;

   public static boolean isSocketListening(IPCMethod method) {
      // $FF: Couldn't be decompiled
   }

   public static boolean isAfUnixSupported() {
      return IS_AF_UNIX_SUPPORTED;
   }

   static {
      boolean isAfUnixSupported;
      try {
         ServerSocketChannel ignored = ServerSocketChannel.open(StandardProtocolFamily.UNIX);

         try {
            isAfUnixSupported = true;
         } catch (Throwable var5) {
            if (ignored != null) {
               try {
                  ignored.close();
               } catch (Throwable var4) {
                  var5.addSuppressed(var4);
               }
            }

            throw var5;
         }

         if (ignored != null) {
            ignored.close();
         }
      } catch (UnsupportedOperationException var6) {
         isAfUnixSupported = false;
      } catch (IOException var7) {
         isAfUnixSupported = true;
      }

      IS_AF_UNIX_SUPPORTED = isAfUnixSupported;
   }
}
