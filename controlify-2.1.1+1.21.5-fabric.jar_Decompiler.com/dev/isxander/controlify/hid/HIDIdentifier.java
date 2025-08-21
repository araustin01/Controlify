package dev.isxander.controlify.hid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import java.util.HexFormat;
import java.util.List;

public record HIDIdentifier(int vendorId, int productId) {
   public static final Codec<HIDIdentifier> LIST_CODEC;

   public HIDIdentifier(int vendorId, int productId) {
      this.vendorId = vendorId;
      this.productId = productId;
   }

   public String toString() {
      HexFormat hex = HexFormat.of();
      String var10000 = hex.toHexDigits((long)this.vendorId, 4);
      return "HID[VID=0x" + var10000 + ", PID=0x" + hex.toHexDigits((long)this.productId, 4) + "]";
   }

   public int vendorId() {
      return this.vendorId;
   }

   public int productId() {
      return this.productId;
   }

   static {
      LIST_CODEC = Codec.list(Codec.INT).comapFlatMap((parts) -> {
         return parts.size() != 2 ? DataResult.error(() -> {
            return "HID identifier list must have exactly two elements, found " + parts.size();
         }) : DataResult.success(new HIDIdentifier((Integer)parts.get(0), (Integer)parts.get(1)));
      }, (hid) -> {
         return List.of(hid.vendorId(), hid.productId());
      });
   }
}
