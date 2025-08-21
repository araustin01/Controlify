package dev.isxander.splitscreen.client.engine.impl.fboshare.b3dext;

import io.netty.buffer.ByteBuf;
import java.util.Objects;
import net.minecraft.class_2540;
import net.minecraft.class_9135;
import net.minecraft.class_9139;

public interface ShareHandle {
   class_9139<class_2540, ShareHandle> CODEC = class_9139.method_56437((buf, handle) -> {
      Objects.requireNonNull(handle);
      int index$1 = 0;
      switch(handle.typeSwitch<invokedynamic>(handle, index$1)) {
      case 0:
         ShareHandle.Fd fd = (ShareHandle.Fd)handle;
         buf.method_52997(0);
         ShareHandle.Fd.STREAM_CODEC.encode(buf, fd);
         break;
      case 1:
         ShareHandle.Win32 win32 = (ShareHandle.Win32)handle;
         buf.method_52997(1);
         ShareHandle.Win32.STREAM_CODEC.encode(buf, win32);
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

   }, (buf) -> {
      byte type = buf.readByte();
      if (type == 0) {
         return (ShareHandle)ShareHandle.Fd.STREAM_CODEC.decode(buf);
      } else if (type == 1) {
         return (ShareHandle)ShareHandle.Win32.STREAM_CODEC.decode(buf);
      } else {
         throw new IllegalArgumentException("Unknown share handle type: " + type);
      }
   });

   public static record Fd(int fd) implements ShareHandle {
      public static final class_9139<ByteBuf, ShareHandle.Fd> STREAM_CODEC;

      public Fd(int fd) {
         this.fd = fd;
      }

      public int fd() {
         return this.fd;
      }

      static {
         STREAM_CODEC = class_9135.field_49675.method_56432(ShareHandle.Fd::new, ShareHandle.Fd::fd);
      }
   }

   public static record Win32(long handle) implements ShareHandle {
      public static final class_9139<ByteBuf, ShareHandle.Win32> STREAM_CODEC;

      public Win32(long handle) {
         this.handle = handle;
      }

      public long handle() {
         return this.handle;
      }

      static {
         STREAM_CODEC = class_9135.field_54505.method_56432(ShareHandle.Win32::new, ShareHandle.Win32::handle);
      }
   }
}
