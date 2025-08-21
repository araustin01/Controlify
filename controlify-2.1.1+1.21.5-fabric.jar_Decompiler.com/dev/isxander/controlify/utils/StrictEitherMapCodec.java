package dev.isxander.controlify.utils;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.RecordBuilder;
import java.util.stream.Stream;

public class StrictEitherMapCodec<T> extends MapCodec<T> {
   private final String typedKeyName;
   private final MapCodec<T> typed;
   private final MapCodec<T> fuzzy;
   private final boolean typedEncode;

   public StrictEitherMapCodec(String typedKeyName, MapCodec<T> typed, MapCodec<T> fuzzy, boolean typedEncode) {
      this.typedKeyName = typedKeyName;
      this.typed = typed;
      this.fuzzy = fuzzy;
      this.typedEncode = typedEncode;
   }

   public <T1> DataResult<T> decode(DynamicOps<T1> ops, MapLike<T1> input) {
      return input.get(this.typedKeyName) != null ? this.typed.decode(ops, input) : this.fuzzy.decode(ops, input);
   }

   public <T1> RecordBuilder<T1> encode(T input, DynamicOps<T1> ops, RecordBuilder<T1> prefix) {
      return this.typedEncode ? this.typed.encode(input, ops, prefix) : this.fuzzy.encode(input, ops, prefix);
   }

   public <T1> Stream<T1> keys(DynamicOps<T1> ops) {
      return Stream.concat(this.typed.keys(ops), this.fuzzy.keys(ops)).distinct();
   }
}
