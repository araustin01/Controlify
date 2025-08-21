package dev.isxander.controlify.bindings.input;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapEncoder;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.FuzzyMapCodec;
import dev.isxander.controlify.utils.StrictEitherMapCodec;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.Stream;
import net.minecraft.class_156;
import net.minecraft.class_3542;
import net.minecraft.class_5699;
import org.jetbrains.annotations.NotNull;

public record InputType<T extends Input>(String id, MapCodec<T> codec) implements class_3542 {
   public static final InputType<ButtonInput> BUTTON;
   public static final InputType<AxisInput> AXIS;
   public static final InputType<HatInput> HAT;
   public static final InputType<EmptyInput> EMPTY;
   public static final InputType<?>[] TYPES;

   public InputType(String id, MapCodec<T> codec) {
      this.id = id;
      this.codec = codec;
   }

   public static <T extends class_3542, E> MapCodec<E> createCodec(T[] types, Function<T, MapCodec<? extends E>> codecGetter, Function<E, T> typeGetter, String typeFieldName) {
      MapCodec<E> fuzzyCodec = new FuzzyMapCodec(Stream.of(types).map(codecGetter).toList(), (obj) -> {
         return (MapEncoder)codecGetter.apply((class_3542)typeGetter.apply(obj));
      });
      Codec<T> typeCodec = class_5699.method_39512(CUtil.stringResolver(class_3542::method_15434, CUtil.createNameLookup(types, Function.identity())), class_5699.method_39511(class_156.method_43658(Arrays.asList(types)), (i) -> {
         return i >= 0 && i < types.length ? types[i] : null;
      }, -1));
      MapCodec<E> typedCodec = typeCodec.dispatchMap(typeFieldName, typeGetter, codecGetter);
      MapCodec<E> eitherCodec = new StrictEitherMapCodec(typeFieldName, typedCodec, fuzzyCodec, false);
      return CUtil.orCompressed(eitherCodec, typedCodec);
   }

   @NotNull
   public String method_15434() {
      return this.id();
   }

   public String id() {
      return this.id;
   }

   public MapCodec<T> codec() {
      return this.codec;
   }

   static {
      BUTTON = new InputType("button", ButtonInput.CODEC);
      AXIS = new InputType("axis", AxisInput.CODEC);
      HAT = new InputType("hat", HatInput.CODEC);
      EMPTY = new InputType("empty", EmptyInput.CODEC);
      TYPES = new InputType[]{BUTTON, AXIS, HAT, EMPTY};
   }
}
