package dev.isxander.controlify.hid;

import com.sun.jna.Memory;
import dev.isxander.sdl3java.api.hidapi.SDL_hid_device;
import dev.isxander.sdl3java.api.hidapi.SdlHidApi;
import dev.isxander.sdl3java.jna.size_t;
import org.hid4java.HidDevice;

public interface HIDDevice {
   int vendorId();

   int productId();

   String path();

   boolean supportsCommunication();

   void open();

   void close();

   int read(byte[] var1);

   int write(byte[] var1, int var2, byte var3);

   default HIDIdentifier asIdentifier() {
      return new HIDIdentifier(this.vendorId(), this.productId());
   }

   public static final class SDLHidApi implements HIDDevice {
      private final int vendorId;
      private final int productId;
      private final String path;
      private SDL_hid_device device;

      public SDLHidApi(int vendorId, int productId, String path) {
         this.vendorId = vendorId;
         this.productId = productId;
         this.path = path;
         this.device = null;
      }

      public int vendorId() {
         return this.vendorId;
      }

      public int productId() {
         return this.productId;
      }

      public String path() {
         return this.path;
      }

      public boolean supportsCommunication() {
         return true;
      }

      public void open() {
         this.device = SdlHidApi.SDL_hid_open_path(this.path);
         SdlHidApi.SDL_hid_set_nonblocking(this.device, 1);
      }

      public void close() {
         SdlHidApi.SDL_hid_close(this.device);
         this.device = null;
      }

      public int read(byte[] buffer) {
         Memory memory = new Memory((long)buffer.length);

         int var4;
         try {
            int ret = SdlHidApi.SDL_hid_read(this.device, memory, new size_t((long)buffer.length));
            memory.read(0L, buffer, 0, buffer.length);
            var4 = ret;
         } catch (Throwable var6) {
            try {
               memory.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }

            throw var6;
         }

         memory.close();
         return var4;
      }

      public int write(byte[] buffer, int packetLength, byte reportId) {
         Memory memory = new Memory((long)(buffer.length + 1));

         int var5;
         try {
            memory.setByte((long)buffer.length, reportId);
            memory.write(1L, buffer, 0, buffer.length);
            var5 = SdlHidApi.SDL_hid_write(this.device, memory, new size_t((long)buffer.length));
         } catch (Throwable var8) {
            try {
               memory.close();
            } catch (Throwable var7) {
               var8.addSuppressed(var7);
            }

            throw var8;
         }

         memory.close();
         return var5;
      }

      public boolean equals(Object obj) {
         if (obj instanceof HIDDevice) {
            HIDDevice hid = (HIDDevice)obj;
            return this.asIdentifier().equals(hid.asIdentifier());
         } else {
            return false;
         }
      }
   }

   public static record IDOnly(int vendorId, int productId, String path) implements HIDDevice {
      public IDOnly(int vendorId, int productId, String path) {
         this.vendorId = vendorId;
         this.productId = productId;
         this.path = path;
      }

      public boolean supportsCommunication() {
         return false;
      }

      public void open() {
         throw new UnsupportedOperationException();
      }

      public void close() {
         throw new UnsupportedOperationException();
      }

      public int read(byte[] buffer) {
         throw new UnsupportedOperationException();
      }

      public int write(byte[] buffer, int packetLength, byte reportId) {
         throw new UnsupportedOperationException();
      }

      public boolean equals(Object obj) {
         if (obj instanceof HIDDevice) {
            HIDDevice hid = (HIDDevice)obj;
            return this.asIdentifier().equals(hid.asIdentifier());
         } else {
            return false;
         }
      }

      public int vendorId() {
         return this.vendorId;
      }

      public int productId() {
         return this.productId;
      }

      public String path() {
         return this.path;
      }
   }

   public static final class Hid4Java implements HIDDevice {
      private final HidDevice hidDevice;

      public Hid4Java(HidDevice hidDevice) {
         this.hidDevice = hidDevice;
      }

      public int vendorId() {
         return this.hidDevice.getVendorId();
      }

      public int productId() {
         return this.hidDevice.getProductId();
      }

      public String path() {
         return this.hidDevice.getPath();
      }

      public boolean supportsCommunication() {
         return true;
      }

      public void open() {
         this.hidDevice.open();
         this.hidDevice.setNonBlocking(true);
      }

      public void close() {
         this.hidDevice.close();
      }

      public int read(byte[] buffer) {
         return this.hidDevice.read(buffer);
      }

      public int write(byte[] buffer, int packetLength, byte reportId) {
         return this.hidDevice.write(buffer, packetLength, reportId);
      }

      public boolean equals(Object obj) {
         if (obj instanceof HIDDevice) {
            HIDDevice hid = (HIDDevice)obj;
            return this.asIdentifier().equals(hid.asIdentifier());
         } else {
            return false;
         }
      }
   }
}
