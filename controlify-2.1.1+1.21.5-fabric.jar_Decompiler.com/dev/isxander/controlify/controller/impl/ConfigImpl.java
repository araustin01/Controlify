package dev.isxander.controlify.controller.impl;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.GenericControllerConfig;
import dev.isxander.controlify.controller.serialization.ConfigClass;
import dev.isxander.controlify.controller.serialization.CustomSaveLoadConfig;
import dev.isxander.controlify.controller.serialization.IConfig;
import java.util.function.Supplier;
import net.minecraft.class_2960;
import org.apache.commons.lang3.SerializationException;
import org.jetbrains.annotations.Nullable;

public class ConfigImpl<T extends ConfigClass> implements IConfig<T> {
   private final Supplier<T> defaultFactory;
   private final Class<T> classOfT;
   private final T defaultInstance;
   private T instance;
   @Nullable
   private CustomSaveLoadConfig customSaveLoadConfig;

   public ConfigImpl(Supplier<T> defaultFactory, Class<T> configClass, @Nullable CustomSaveLoadConfig customSaveLoadConfig) {
      this.defaultFactory = defaultFactory;
      this.classOfT = configClass;
      this.defaultInstance = (ConfigClass)this.defaultFactory.get();
      this.instance = (ConfigClass)this.defaultFactory.get();
      this.customSaveLoadConfig = customSaveLoadConfig;
   }

   public ConfigImpl(Supplier<T> defaultFactory, Class<T> configClass) {
      this(defaultFactory, configClass, (CustomSaveLoadConfig)null);
   }

   public T config() {
      return this.instance;
   }

   public T defaultConfig() {
      return this.defaultInstance;
   }

   public JsonElement serialize(Gson gson, ControllerEntity controller) throws SerializationException {
      try {
         this.config().onConfigSaveLoad(controller);
         JsonObject json = gson.toJsonTree(this.config(), this.classOfT).getAsJsonObject();
         if (this.customSaveLoadConfig != null) {
            this.customSaveLoadConfig.toJson(json);
         }

         return json;
      } catch (Exception var4) {
         throw new SerializationException("Failed to serialize config type " + this.classOfT.getTypeName(), var4);
      }
   }

   public void deserialize(JsonElement element, Gson gson, ControllerEntity controller) throws SerializationException {
      try {
         this.instance = (ConfigClass)gson.fromJson(element, this.classOfT);
         if (this.instance == null) {
            throw new IllegalStateException("Deserialized config returned null.");
         }

         if (this.customSaveLoadConfig != null) {
            this.customSaveLoadConfig.fromJson(element.getAsJsonObject());
         }
      } catch (Throwable var5) {
         this.instance = (ConfigClass)this.defaultFactory.get();
         throw new SerializationException("Failed to deserialize type " + this.classOfT.getTypeName() + ". Resetting to default.", var5);
      }

      this.instance.onConfigSaveLoad(controller);
   }

   public void resetToDefault() {
      this.instance = (ConfigClass)this.defaultFactory.get();
   }

   public class_2960 id() {
      return GenericControllerConfig.ID;
   }
}
