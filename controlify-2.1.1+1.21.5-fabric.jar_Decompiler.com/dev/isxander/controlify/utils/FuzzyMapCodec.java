package dev.isxander.controlify.utils;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapDecoder;
import com.mojang.serialization.MapEncoder;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

public class FuzzyMapCodec<T> extends MapCodec<T> {
   private final List<MapCodec<? extends T>> codecs;
   private final Function<T, MapEncoder<? extends T>> encoderGetter;

   public FuzzyMapCodec(List<MapCodec<? extends T>> codecs, Function<T, MapEncoder<? extends T>> encoderGetter) {
      this.codecs = codecs;
      this.encoderGetter = encoderGetter;
   }

   public <T1> DataResult<T> decode(DynamicOps<T1> ops, MapLike<T1> input) {
      Iterator var3 = this.codecs.iterator();

      DataResult result;
      do {
         if (!var3.hasNext()) {
            return DataResult.error(() -> {
               return "No matching codec found.";
            });
         }

         MapDecoder<? extends T> decoder = (MapDecoder)var3.next();
         result = decoder.decode(ops, input);
      } while(!result.result().isPresent());

      return result;
   }

   public <T1> RecordBuilder<T1> encode(T input, DynamicOps<T1> ops, RecordBuilder<T1> prefix) {
      MapEncoder<T> encoder = (MapEncoder)this.encoderGetter.apply(input);
      return encoder.encode(input, ops, prefix);
   }

   public <T1> Stream<T1> keys(DynamicOps<T1> ops) {
      return this.codecs.stream().flatMap((codec) -> {
         return codec.keys(ops);
      }).distinct();
   }
}
