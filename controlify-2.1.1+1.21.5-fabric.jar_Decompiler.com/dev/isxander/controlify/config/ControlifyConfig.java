package dev.isxander.controlify.config;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.controlify.controller.input.mapping.MappingEntry;
import dev.isxander.controlify.controller.input.mapping.MappingEntryTypeAdapter;
import dev.isxander.controlify.controllermanager.ControllerManager;
import dev.isxander.controlify.platform.main.PlatformMainUtil;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.DebugLog;
import dev.isxander.controlify.utils.GsonCodecAdapter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.class_2960;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ControlifyConfig {
   public static final Path CONFIG_PATH = PlatformMainUtil.getConfigDir().resolve("controlify.json");
   public static final Gson GSON;
   private final Controlify controlify;
   private boolean dirty;
   private boolean firstLaunch;
   @Nullable
   private ControllerUID currentControllerUid = null;
   private final Map<ControllerUID, JsonObject> storedControllerConfig = new HashMap();
   @NotNull
   private GlobalSettings globalSettings = new GlobalSettings();

   public ControlifyConfig(Controlify controlify) {
      this.controlify = controlify;
   }

   public void save() {
      CUtil.LOGGER.log("Saving Controlify config...");

      JsonObject serialObject;
      try {
         serialObject = this.createSerialObject();
      } catch (Exception var4) {
         CUtil.LOGGER.error("Failed to serialize Controlify config. Controlify will not be saved!", (Throwable)var4);
         return;
      }

      try {
         Files.deleteIfExists(CONFIG_PATH);
         Files.writeString(CONFIG_PATH, GSON.toJson(serialObject), new OpenOption[]{StandardOpenOption.CREATE_NEW, StandardOpenOption.TRUNCATE_EXISTING});
         this.dirty = false;
      } catch (IOException var3) {
         throw new IllegalStateException("Failed to save Controlify config to file!", var3);
      }
   }

   public void saveIfDirty() {
      if (this.dirty) {
         this.save();
      }

   }

   public void load() {
      CUtil.LOGGER.log("Loading Controlify config...");
      if (!Files.exists(CONFIG_PATH, new LinkOption[0])) {
         CUtil.LOGGER.log("First launch detected. Creating initial config file!");
         this.firstLaunch = true;
         this.save();
      } else {
         try {
            this.applyConfig((JsonObject)GSON.fromJson(Files.readString(CONFIG_PATH), JsonObject.class));
         } catch (Exception var2) {
            CUtil.LOGGER.error("Failed to load Controlify config!", (Throwable)var2);
         }

         if (this.dirty) {
            DebugLog.log("Config was dirty after load, saving...");
            this.save();
         }

      }
   }

   private JsonObject createSerialObject() {
      JsonObject obj = new JsonObject();
      obj.addProperty("current_controller", ControllerUID.toNullableString(this.currentControllerUid()));
      this.controlify.getControllerManager().ifPresent(this::updateStoredControllerConfig);
      JsonObject controllersObj = new JsonObject();
      this.storedControllerConfig.forEach((uid, config) -> {
         controllersObj.add(uid.string(), config);
      });
      obj.add("controllers", controllersObj);
      JsonElement globalJson = GSON.toJsonTree(this.globalSettings);
      obj.add("global", globalJson);
      return obj;
   }

   private void updateStoredControllerConfig(ControllerManager controllerManager) {
      Iterator var2 = controllerManager.getConnectedControllers().iterator();

      while(var2.hasNext()) {
         ControllerEntity controller = (ControllerEntity)var2.next();
         JsonObject controllerObject = (JsonObject)this.storedControllerConfig.computeIfAbsent(controller.uid(), (k) -> {
            return new JsonObject();
         });
         JsonObject configObject = controllerObject.getAsJsonObject("config");
         if (configObject == null) {
            configObject = new JsonObject();
            controllerObject.add("config", configObject);
         }

         controller.serializeToObject(configObject, GSON);
         this.storedControllerConfig.put(controller.uid(), controllerObject);
      }

   }

   private void applyConfig(JsonObject json) {
      try {
         JsonElement primitive = json.get("current_controller");
         if (primitive != null) {
            this.currentControllerUid = ControllerUID.fromNullableString(primitive.isJsonNull() ? null : primitive.getAsString());
         } else {
            CUtil.LOGGER.warn("Current controller is not defined in config!");
            this.setDirty();
         }
      } catch (Exception var5) {
         CUtil.LOGGER.error("Failed to apply current controller from config file!", (Throwable)var5);
         this.setDirty();
      }

      try {
         JsonObject controllersMap = json.getAsJsonObject("controllers");
         controllersMap.asMap().forEach((uid, element) -> {
            this.storedControllerConfig.put(new ControllerUID(uid), element.getAsJsonObject());
         });
         this.controlify.getControllerManager().ifPresent(this::applyControllerConfig);
      } catch (Exception var4) {
         CUtil.LOGGER.error("Failed to apply controller config from config file!", (Throwable)var4);
         this.setDirty();
      }

      try {
         GlobalSettings newGlobalSettings = (GlobalSettings)GSON.fromJson(json.get("global"), GlobalSettings.class);
         if (newGlobalSettings != null) {
            this.globalSettings = newGlobalSettings;
         }
      } catch (Exception var3) {
         CUtil.LOGGER.error("Failed to apply global settings from config file!", (Throwable)var3);
         this.setDirty();
      }

   }

   private void applyControllerConfig(ControllerManager controllerManager) {
      Iterator var2 = controllerManager.getConnectedControllers().iterator();

      while(var2.hasNext()) {
         ControllerEntity controller = (ControllerEntity)var2.next();
         this.loadControllerConfig(controller);
      }

   }

   public boolean loadControllerConfig(ControllerEntity controller) {
      JsonObject json = (JsonObject)this.storedControllerConfig.get(controller.uid());
      if (json == null) {
         CUtil.LOGGER.warn("Controller {} has no config to load. Using defaults.", controller.uid());
         this.setDirty();
         return true;
      } else {
         JsonObject innerJson = json.getAsJsonObject("config");

         try {
            controller.deserializeFromObject(innerJson.deepCopy(), GSON);
         } catch (Exception var5) {
            CUtil.LOGGER.error("Failed to load controller {} config!", controller.uid(), var5);
            this.setDirty();
         }

         return false;
      }
   }

   @Nullable
   public ControllerUID currentControllerUid() {
      return this.currentControllerUid;
   }

   public void setCurrentControllerUid(@Nullable ControllerUID uid) {
      this.currentControllerUid = uid;
   }

   @NotNull
   public GlobalSettings globalSettings() {
      return this.globalSettings;
   }

   public boolean isFirstLaunch() {
      return this.firstLaunch;
   }

   public void setDirty() {
      this.dirty = true;
   }

   static {
      GSON = (new GsonBuilder()).serializeNulls().setPrettyPrinting().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).registerTypeHierarchyAdapter(Class.class, new TypeAdapters.ClassTypeAdapter()).registerTypeHierarchyAdapter(class_2960.class, new GsonCodecAdapter(class_2960.field_25139)).registerTypeAdapter(MappingEntry.class, new MappingEntryTypeAdapter()).registerTypeAdapter(ControllerUID.class, new TypeAdapters.StringDerivedTypeAdapter(ControllerUID::fromNullableString, ControllerUID::toNullableString)).create();
   }
}
