package dev.isxander.controlify.utils;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import dev.isxander.controlify.utils.log.ControlifyLogger;
import java.io.IOException;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.class_156;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_2960;
import net.minecraft.class_3532;
import net.minecraft.class_3542;
import net.minecraft.class_156.class_158;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.LoggerFactory;

public class CUtil {
   public static final ControlifyLogger LOGGER = ControlifyLogger.createMasterLogger(LoggerFactory.getLogger("Controlify"));
   public static final boolean IS_POJAV_LAUNCHER = System.getenv("POJAV_NATIVEDIR") != null;

   public static class_2960 rl(String path) {
      return class_2960.method_60655("controlify", path);
   }

   public static class_287 beginBuffer(class_5596 mode, VertexFormat format) {
      return class_289.method_1348().method_60827(mode, format);
   }

   public static void openUri(String uri) {
      try {
         String[] command = CUtil.URIOpener.get().openArguments(URI.create(uri));
         Process process = Runtime.getRuntime().exec(command);
         process.getInputStream().close();
         process.getOutputStream().close();
         process.getErrorStream().close();
      } catch (SecurityException | IOException var3) {
         LOGGER.error("Failed to open URI: {}", uri, var3);
      }

   }

   public static <T> Supplier<T> lazyInit(Supplier<T> supplier) {
      return new Supplier<T>() {
         private T created = null;

         public T get() {
            if (this.created == null) {
               this.created = supplier.get();
            }

            return this.created;
         }
      };
   }

   public static String createUIDFromBytes(byte[]... bytes) {
      MessageDigest md;
      try {
         md = MessageDigest.getInstance("MD5");
      } catch (NoSuchAlgorithmException var6) {
         throw new IllegalStateException("Could not get MD5 hash.", var6);
      }

      byte[][] var2 = bytes;
      int var3 = bytes.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         byte[] b = var2[var4];
         md.update(b);
      }

      byte[] digest = md.digest();
      return Hex.encodeHexString(digest);
   }

   public static void sleepChecked(long millis) {
      try {
         Thread.sleep(millis);
      } catch (InterruptedException var3) {
         LOGGER.error("Failed to sleep for {}ms", var3, millis);
      }

   }

   public static <E> Codec<E> stringResolver(Function<E, String> toString, Function<String, E> fromString) {
      return Codec.STRING.flatXmap((name) -> {
         return (DataResult)Optional.ofNullable(fromString.apply(name)).map(DataResult::success).orElseGet(() -> {
            return DataResult.error(() -> {
               return "Unknown element name:" + name;
            });
         });
      }, (e) -> {
         return (DataResult)Optional.ofNullable((String)toString.apply(e)).map(DataResult::success).orElseGet(() -> {
            return DataResult.error(() -> {
               return "Element with unknown name: " + String.valueOf(e);
            });
         });
      });
   }

   public static <T extends class_3542> Function<String, T> createNameLookup(T[] values, Function<String, String> keyFunction) {
      Map<String, T> map = (Map)Arrays.stream(values).collect(Collectors.toMap((stringRepresentable) -> {
         return (String)keyFunction.apply(stringRepresentable.method_15434());
      }, (stringRepresentable) -> {
         return stringRepresentable;
      }));
      return (string) -> {
         return string == null ? null : (class_3542)map.get(string);
      };
   }

   public static <E> MapCodec<E> orCompressed(MapCodec<E> first, MapCodec<E> second) {
      return new MapCodec<E>() {
         public <T> RecordBuilder<T> encode(E object, DynamicOps<T> dynamicOps, RecordBuilder<T> recordBuilder) {
            return dynamicOps.compressMaps() ? second.encode(object, dynamicOps, recordBuilder) : first.encode(object, dynamicOps, recordBuilder);
         }

         public <T> DataResult<E> decode(DynamicOps<T> dynamicOps, MapLike<T> mapLike) {
            return dynamicOps.compressMaps() ? second.decode(dynamicOps, mapLike) : first.decode(dynamicOps, mapLike);
         }

         public <T> Stream<T> keys(DynamicOps<T> dynamicOps) {
            return second.keys(dynamicOps);
         }

         public String toString() {
            String var10000 = String.valueOf(first);
            return var10000 + " orCompressed " + String.valueOf(second);
         }
      };
   }

   public static float positiveAxis(float value) {
      return value < 0.0F ? 0.0F : value;
   }

   public static float negativeAxis(float value) {
      return value > 0.0F ? 0.0F : -value;
   }

   public static float mapShortToFloat(short value) {
      return class_3532.method_37958((float)value, -32768.0F, 0.0F, -1.0F, 0.0F) + class_3532.method_37958((float)value, 0.0F, 32767.0F, 0.0F, 1.0F);
   }

   private static enum URIOpener {
      WINDOWS(class_158.field_1133),
      OSX(class_158.field_1137),
      LINUX(class_158.field_1135),
      SOLARIS(class_158.field_1134);

      private final class_158 mcOS;

      private URIOpener(class_158 mcOS) {
         this.mcOS = mcOS;
      }

      public String[] openArguments(URI uri) {
         String[] var10000;
         switch(this.mcOS) {
         case field_1133:
            var10000 = new String[]{"rundll32", "url.dll,FileProtocolHandler", uri.toString()};
            break;
         case field_1137:
            var10000 = new String[]{"open", uri.toString()};
            break;
         case field_1135:
         case field_1134:
            var10000 = new String[]{"xdg-open", uri.toString()};
            break;
         default:
            throw new UnsupportedOperationException("Unsupported OS: " + String.valueOf(this.mcOS));
         }

         return var10000;
      }

      public static CUtil.URIOpener get() {
         CUtil.URIOpener var10000;
         switch(class_156.method_668()) {
         case field_1133:
            var10000 = WINDOWS;
            break;
         case field_1137:
            var10000 = OSX;
            break;
         case field_1135:
            var10000 = LINUX;
            break;
         case field_1134:
            var10000 = SOLARIS;
            break;
         default:
            throw new UnsupportedOperationException("Unsupported OS: " + String.valueOf(class_156.method_668()));
         }

         return var10000;
      }

      // $FF: synthetic method
      private static CUtil.URIOpener[] $values() {
         return new CUtil.URIOpener[]{WINDOWS, OSX, LINUX, SOLARIS};
      }
   }
}
