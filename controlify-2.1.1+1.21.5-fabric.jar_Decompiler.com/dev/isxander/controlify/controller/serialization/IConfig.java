package dev.isxander.controlify.controller.serialization;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.ECSComponent;
import org.apache.commons.lang3.SerializationException;

public interface IConfig<T extends ConfigClass> extends ECSComponent {
   T config();

   T defaultConfig();

   JsonElement serialize(Gson var1, ControllerEntity var2) throws SerializationException;

   void deserialize(JsonElement var1, Gson var2, ControllerEntity var3) throws SerializationException;

   void resetToDefault();
}
