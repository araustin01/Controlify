package dev.isxander.controlify.controller.serialization;

import com.google.gson.JsonObject;

public interface CustomSaveLoadConfig {
   void fromJson(JsonObject var1);

   void toJson(JsonObject var1);
}
