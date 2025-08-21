package dev.isxander.controlify.controller.input.mapping;

import dev.isxander.controlify.config.ControlifyConfig;
import dev.isxander.controlify.utils.CUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Map;
import net.minecraft.class_310;
import net.minecraft.class_3298;
import net.minecraft.class_3300;
import org.jetbrains.annotations.Nullable;

public class ControllerMappingStorage {
   private static final Map<String, ControllerMapping> MAPPINGS = new Object2ObjectOpenHashMap();

   @Nullable
   public static ControllerMapping get(String id) {
      return (ControllerMapping)MAPPINGS.computeIfAbsent(id, ControllerMappingStorage::resolve);
   }

   @Nullable
   private static ControllerMapping resolve(String id) {
      class_3300 resourceManager = class_310.method_1551().method_1478();
      class_3298 resource = (class_3298)resourceManager.method_14486(CUtil.rl("mappings/" + id + ".json")).orElse((Object)null);
      if (resource == null) {
         return null;
      } else {
         try {
            BufferedReader reader = resource.method_43039();

            ControllerMapping var4;
            try {
               var4 = (ControllerMapping)ControlifyConfig.GSON.fromJson(reader, ControllerMapping.class);
            } catch (Throwable var7) {
               if (reader != null) {
                  try {
                     reader.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (reader != null) {
               reader.close();
            }

            return var4;
         } catch (IOException var8) {
            throw new IllegalStateException("Failed to load controller mapping!", var8);
         }
      }
   }
}
