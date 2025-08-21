package dev.isxander.splitscreen.util;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import java.util.Optional;
import java.util.function.Function;

public class CodecExtensionHelper {
   public static <V, E> Codec<V> buildExtensionCodec(Codec<V> original, Codec<Optional<E>> extensionMapOutputCodec, Function<V, Optional<E>> extensionGetter, ThreadLocal<E> extensionApplicator) {
      return new Codec<V>() {
         public <T> DataResult<Pair<V, T>> decode(DynamicOps<T> ops, T input) {
            return extensionMapOutputCodec.decode(ops, input).flatMap((extPair) -> {
               DataResult var4;
               try {
                  extensionApplicator.set(((Optional)extPair.getFirst()).orElse((Object)null));
                  var4 = original.decode(ops, extPair.getSecond());
               } finally {
                  extensionApplicator.remove();
               }

               return var4;
            });
         }

         public <T> DataResult<T> encode(V input, DynamicOps<T> ops, T prefix) {
            Optional<E> ext = (Optional)extensionGetter.apply(input);
            return original.encode(input, ops, prefix).flatMap((p) -> {
               return extensionMapOutputCodec.encode(ext, ops, p);
            });
         }
      };
   }
}
