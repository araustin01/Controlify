package dev.isxander.controlify.utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import java.lang.reflect.Type;

public record GsonCodecAdapter<T>(Codec<T> codec) implements JsonSerializer<T>, JsonDeserializer<T> {
   public GsonCodecAdapter(Codec<T> codec) {
      this.codec = codec;
   }

   public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
      return (JsonElement)this.codec.encodeStart(JsonOps.INSTANCE, src).getOrThrow();
   }

   public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      return this.codec.parse(JsonOps.INSTANCE, json).getOrThrow();
   }

   public Codec<T> codec() {
      return this.codec;
   }
}
