package dev.isxander.controlify.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.util.Objects;
import java.util.Optional;
import org.quiltmc.parsers.json.JsonReader;

public final class JsonTreeParser {
   public static JsonElement parse(JsonReader reader) throws IOException {
      switch(reader.peek()) {
      case BEGIN_OBJECT:
         reader.beginObject();
         JsonObject object = new JsonObject();

         while(reader.hasNext()) {
            String name = reader.nextName();
            JsonElement value = parse(reader);
            object.add(name, value);
         }

         reader.endObject();
         return object;
      case BEGIN_ARRAY:
         reader.beginArray();
         JsonArray array = new JsonArray();

         while(reader.hasNext()) {
            JsonElement value = parse(reader);
            array.add(value);
         }

         reader.endArray();
         return array;
      case STRING:
         return new JsonPrimitive(reader.nextString());
      case NUMBER:
         Objects.requireNonNull(reader);
         Number number = (Number)tryParseNumber(reader::nextInt).or(() -> {
            Objects.requireNonNull(reader);
            return tryParseNumber(reader::nextLong);
         }).or(() -> {
            Objects.requireNonNull(reader);
            return tryParseNumber(reader::nextDouble);
         }).orElseThrow();
         return new JsonPrimitive(number);
      case BOOLEAN:
         return new JsonPrimitive(reader.nextBoolean());
      case NULL:
         reader.nextNull();
         return JsonNull.INSTANCE;
      default:
         throw new JsonParseException("Unexpected token: " + String.valueOf(reader.peek()));
      }
   }

   private static Optional<Number> tryParseNumber(JsonTreeParser.NumberParser parser) {
      try {
         return Optional.of(parser.parse());
      } catch (IOException | NumberFormatException var2) {
         return Optional.empty();
      }
   }

   @FunctionalInterface
   private interface NumberParser {
      Number parse() throws NumberFormatException, IOException;
   }
}
