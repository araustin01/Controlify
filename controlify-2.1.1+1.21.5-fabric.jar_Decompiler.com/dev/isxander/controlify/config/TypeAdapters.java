package dev.isxander.controlify.config;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.util.function.Function;

public final class TypeAdapters {
   public static class StringDerivedTypeAdapter<T> implements JsonSerializer<T>, JsonDeserializer<T> {
      private final Function<String, T> fromString;
      private final Function<T, String> toString;

      public StringDerivedTypeAdapter(Function<String, T> fromString, Function<T, String> toString) {
         this.fromString = fromString;
         this.toString = toString;
      }

      public T deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
         return this.fromString.apply(json.getAsString());
      }

      public JsonElement serialize(T src, Type typeOfSrc, JsonSerializationContext context) {
         return new JsonPrimitive((String)this.toString.apply(src));
      }
   }

   public static class ClassTypeAdapter implements JsonSerializer<Class<?>>, JsonDeserializer<Class<?>> {
      public Class<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
         try {
            return Class.forName(json.getAsString());
         } catch (ClassNotFoundException var5) {
            throw new RuntimeException(var5);
         }
      }

      public JsonElement serialize(Class<?> src, Type typeOfSrc, JsonSerializationContext context) {
         return new JsonPrimitive(src.getName());
      }
   }
}
