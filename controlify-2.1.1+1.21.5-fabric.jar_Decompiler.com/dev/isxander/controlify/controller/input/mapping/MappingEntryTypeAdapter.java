package dev.isxander.controlify.controller.input.mapping;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;

public class MappingEntryTypeAdapter implements JsonDeserializer<MappingEntry> {
   public MappingEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
      Class var10000;
      MappingEntryTypeAdapter.InOutRecord inOut = (MappingEntryTypeAdapter.InOutRecord)context.deserialize(json, MappingEntryTypeAdapter.InOutRecord.class);
      label35:
      switch(inOut.inputType()) {
      case BUTTON:
         switch(inOut.outputType()) {
         case BUTTON:
            var10000 = MappingEntry.FromButton.ToButton.class;
            break label35;
         case AXIS:
            var10000 = MappingEntry.FromButton.ToAxis.class;
            break label35;
         case HAT:
            var10000 = MappingEntry.FromButton.ToHat.class;
            break label35;
         case NOTHING:
            throw new IllegalStateException();
         default:
            throw new MatchException((String)null, (Throwable)null);
         }
      case AXIS:
         switch(inOut.outputType()) {
         case BUTTON:
            var10000 = MappingEntry.FromAxis.ToButton.class;
            break label35;
         case AXIS:
            var10000 = MappingEntry.FromAxis.ToAxis.class;
            break label35;
         case HAT:
            var10000 = MappingEntry.FromAxis.ToHat.class;
            break label35;
         case NOTHING:
            throw new IllegalStateException();
         default:
            throw new MatchException((String)null, (Throwable)null);
         }
      case HAT:
         switch(inOut.outputType()) {
         case BUTTON:
            var10000 = MappingEntry.FromHat.ToButton.class;
            break label35;
         case AXIS:
            var10000 = MappingEntry.FromHat.ToAxis.class;
            break label35;
         case HAT:
            var10000 = MappingEntry.FromHat.ToHat.class;
            break label35;
         case NOTHING:
            throw new IllegalStateException();
         default:
            throw new MatchException((String)null, (Throwable)null);
         }
      case NOTHING:
         switch(inOut.outputType()) {
         case BUTTON:
            var10000 = MappingEntry.FromNothing.ToButton.class;
            break label35;
         case AXIS:
            var10000 = MappingEntry.FromNothing.ToAxis.class;
            break label35;
         case HAT:
            var10000 = MappingEntry.FromNothing.ToHat.class;
            break label35;
         case NOTHING:
            throw new IllegalStateException();
         default:
            throw new MatchException((String)null, (Throwable)null);
         }
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      Type type = var10000;
      return (MappingEntry)context.deserialize(json, type);
   }

   private static record InOutRecord(MapType inputType, MapType outputType) {
      private InOutRecord(MapType inputType, MapType outputType) {
         this.inputType = inputType;
         this.outputType = outputType;
      }

      public MapType inputType() {
         return this.inputType;
      }

      public MapType outputType() {
         return this.outputType;
      }
   }
}
