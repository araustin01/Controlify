package dev.isxander.splitscreen.server.login;

import dev.isxander.splitscreen.config.SplitscreenServerSharedConfig;
import java.util.Objects;
import java.util.UUID;
import net.minecraft.class_2540;
import net.minecraft.class_4844;
import net.minecraft.class_9135;
import net.minecraft.class_9139;
import org.apache.commons.codec.binary.Hex;
import org.jetbrains.annotations.NotNull;

public interface ClientIdentification {
   class_9139<class_2540, ClientIdentification> STREAM_CODEC = class_9139.method_56437((buf, id) -> {
      Objects.requireNonNull(id);
      int index$1 = 0;
      switch(id.typeSwitch<invokedynamic>(id, index$1)) {
      case 0:
         ClientIdentification.Controller c = (ClientIdentification.Controller)id;
         buf.method_52997(0);
         ClientIdentification.Controller.STREAM_CODEC.encode(buf, c);
         break;
      case 1:
         ClientIdentification.Pawn p = (ClientIdentification.Pawn)id;
         buf.method_52997(1);
         ClientIdentification.Pawn.STREAM_CODEC.encode(buf, p);
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

   }, (buf) -> {
      byte type = buf.readByte();
      Object var10000;
      switch(type) {
      case 0:
         var10000 = (ClientIdentification.Controller)ClientIdentification.Controller.STREAM_CODEC.decode(buf);
         break;
      case 1:
         var10000 = (ClientIdentification.Pawn)ClientIdentification.Pawn.STREAM_CODEC.decode(buf);
         break;
      default:
         throw new IllegalStateException("Unexpected value: " + type);
      }

      return (ClientIdentification)var10000;
   });

   public static record Controller(int subPlayerCount, SplitscreenServerSharedConfig config) implements ClientIdentification {
      public static final class_9139<class_2540, ClientIdentification.Controller> STREAM_CODEC;

      public Controller(int subPlayerCount, SplitscreenServerSharedConfig config) {
         this.subPlayerCount = subPlayerCount;
         this.config = config;
      }

      public int subPlayerCount() {
         return this.subPlayerCount;
      }

      public SplitscreenServerSharedConfig config() {
         return this.config;
      }

      static {
         STREAM_CODEC = class_9139.method_56435(class_9135.field_48550, ClientIdentification.Controller::subPlayerCount, class_9135.method_56368(SplitscreenServerSharedConfig.CODEC), ClientIdentification.Controller::config, ClientIdentification.Controller::new);
      }
   }

   public static record Pawn(UUID controllerUuid, byte[] hmac, int subPlayerIndex) implements ClientIdentification {
      public static final int HMAC_SIZE_BITS = 256;
      public static final int HMAC_SIZE_BYTES = 32;
      public static final class_9139<class_2540, ClientIdentification.Pawn> STREAM_CODEC;

      public Pawn(UUID controllerUuid, byte[] hmac, int subPlayerIndex) {
         this.controllerUuid = controllerUuid;
         this.hmac = hmac;
         this.subPlayerIndex = subPlayerIndex;
      }

      @NotNull
      public String toString() {
         return "Pawn{hmac=" + Hex.encodeHexString(this.hmac) + "}";
      }

      public UUID controllerUuid() {
         return this.controllerUuid;
      }

      public byte[] hmac() {
         return this.hmac;
      }

      public int subPlayerIndex() {
         return this.subPlayerIndex;
      }

      static {
         STREAM_CODEC = class_9139.method_56436(class_4844.field_48453, ClientIdentification.Pawn::controllerUuid, class_9135.method_56895(32), ClientIdentification.Pawn::hmac, class_9135.field_48550, ClientIdentification.Pawn::subPlayerIndex, ClientIdentification.Pawn::new);
      }
   }
}
