package dev.isxander.controlify.driver.sdl;

import dev.isxander.sdl3java.api.guid.SDL_GUID;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.Nullable;

public record DecodedGUID(short bus, short crc, short vendor, @Nullable Short product, @Nullable Short version, @Nullable Byte driverSignature, @Nullable Byte driverData, @Nullable String productName) {
   private static final Map<Byte, String> driverSigToName = Map.of((byte)120, "windows/XInput", (byte)119, "windows/Windows Gaming Input", (byte)114, "windows/Raw Input", (byte)103, "gdk/GameInput", (byte)104, "HIDAPI", (byte)109, "apple/MFI", (byte)118, "Virtual");

   public DecodedGUID(short bus, short crc, short vendor, @Nullable Short product, @Nullable Short version, @Nullable Byte driverSignature, @Nullable Byte driverData, @Nullable String productName) {
      this.bus = bus;
      this.crc = crc;
      this.vendor = vendor;
      this.product = product;
      this.version = version;
      this.driverSignature = driverSignature;
      this.driverData = driverData;
      this.productName = productName;
   }

   public static DecodedGUID fromGUID(SDL_GUID guid) {
      return fromBytes(guid.data);
   }

   public static DecodedGUID fromString(String guid) {
      guid = guid.replace("-", "");
      return fromBytes(guid.getBytes(StandardCharsets.UTF_8));
   }

   public static DecodedGUID fromBytes(byte[] guid) {
      Validate.isTrue(guid.length == 16, "GUID must be 16 bytes long", new Object[0]);
      short bus = readShortLE(guid, 0);
      short crc = readShortLE(guid, 2);
      short vendor = readShortLE(guid, 4);
      Short product = null;
      Short version = null;
      Byte driverSignature = null;
      Byte driverData = null;
      String productName = null;
      if (vendor != 0) {
         product = readShortLE(guid, 8);
         version = readShortLE(guid, 12);
         driverSignature = guid[14];
         driverData = guid[15];
      } else {
         byte driverSignatureRaw = guid[14];
         byte driverDataRaw = guid[15];
         if (driverSignatureRaw != 0) {
            driverSignature = driverSignatureRaw;
            driverData = driverDataRaw;
            productName = readCString(guid, 4, 10);
         } else {
            productName = readCString(guid, 4, 12);
         }
      }

      return new DecodedGUID(bus, crc, vendor, product, version, driverSignature, driverData, productName);
   }

   public static String getDriverHint(byte driverSignature) {
      return (String)driverSigToName.getOrDefault(driverSignature, "Unknown");
   }

   public String getDriverHint() {
      return this.driverSignature == null ? "Unknown" : getDriverHint(this.driverSignature);
   }

   private static short readShortLE(byte[] data, int offset) {
      int low = data[offset] & 255;
      int high = (data[offset + 1] & 255) << 8;
      return (short)(low | high);
   }

   private static String readCString(byte[] data, int offset, int length) {
      int end = offset;

      for(int max = Math.min(offset + length, data.length); end < max && data[end] != 0; ++end) {
      }

      return new String(data, offset, end - offset, StandardCharsets.UTF_8);
   }

   public short bus() {
      return this.bus;
   }

   public short crc() {
      return this.crc;
   }

   public short vendor() {
      return this.vendor;
   }

   @Nullable
   public Short product() {
      return this.product;
   }

   @Nullable
   public Short version() {
      return this.version;
   }

   @Nullable
   public Byte driverSignature() {
      return this.driverSignature;
   }

   @Nullable
   public Byte driverData() {
      return this.driverData;
   }

   @Nullable
   public String productName() {
      return this.productName;
   }
}
