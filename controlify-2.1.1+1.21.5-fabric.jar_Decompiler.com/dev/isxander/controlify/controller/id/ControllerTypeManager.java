package dev.isxander.controlify.controller.id;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controllermanager.ControllerManager;
import dev.isxander.controlify.hid.ControllerHIDService;
import dev.isxander.controlify.hid.HIDDevice;
import dev.isxander.controlify.hid.HIDIdentifier;
import dev.isxander.controlify.platform.client.resource.SimpleControlifyReloadListener;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.JsonTreeParser;
import dev.isxander.controlify.utils.log.ControlifyLogger;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import net.minecraft.class_156;
import net.minecraft.class_2960;
import net.minecraft.class_3298;
import net.minecraft.class_3300;
import org.quiltmc.parsers.json.JsonReader;

public class ControllerTypeManager implements SimpleControlifyReloadListener<ControllerTypeManager.Preparations> {
   private Map<HIDIdentifier, ControllerType> typeMap = new HashMap();
   public static final class_2960 ID = CUtil.rl("controller_type");
   private static final Codec<ControllerTypeManager.ControllerTypeEntry> ENTRY_CODEC = RecordCodecBuilder.create((instance) -> {
      return instance.group(Codec.list(HIDIdentifier.LIST_CODEC).comapFlatMap((list) -> {
         return list.isEmpty() ? DataResult.error(() -> {
            return "At least one HID must be present";
         }) : DataResult.success(list);
      }, (list) -> {
         return list;
      }).fieldOf("hids").forGetter(ControllerTypeManager.ControllerTypeEntry::hid), ControllerType.CODEC.forGetter(ControllerTypeManager.ControllerTypeEntry::type)).apply(instance, ControllerTypeManager.ControllerTypeEntry::new);
   });

   public ControllerType getControllerType(HIDIdentifier hid) {
      return (ControllerType)this.typeMap.getOrDefault(hid, ControllerType.DEFAULT);
   }

   public Map<HIDIdentifier, ControllerType> getTypeMap() {
      return this.typeMap;
   }

   public CompletableFuture<ControllerTypeManager.Preparations> load(class_3300 manager, Executor executor) {
      return CompletableFuture.supplyAsync(() -> {
         return manager.method_14489(CUtil.rl("controllers/controller_identification.json5"));
      }, executor).thenCompose((resources) -> {
         List<CompletableFuture<List<Entry<HIDIdentifier, ControllerType>>>> futures = new ArrayList();
         Iterator var4 = resources.iterator();

         while(var4.hasNext()) {
            class_3298 resource = (class_3298)var4.next();
            futures.add(CompletableFuture.supplyAsync(() -> {
               return this.readIdentificationResource(resource);
            }, executor));
         }

         return class_156.method_33791(futures).thenApply((listOfEntries) -> {
            return (Map)listOfEntries.stream().flatMap(Collection::stream).collect(Collectors.toMap(Entry::getKey, Entry::getValue, (a, b) -> {
               return b;
            }));
         });
      }).thenApply(ControllerTypeManager.Preparations::new);
   }

   private List<Entry<HIDIdentifier, ControllerType>> readIdentificationResource(class_3298 resource) {
      HashMap typeMap = new HashMap();

      try {
         BufferedReader resourceReader = resource.method_43039();

         try {
            JsonReader reader = JsonReader.json5(resourceReader);
            JsonElement json = JsonTreeParser.parse(reader);
            DataResult var10000 = ENTRY_CODEC.listOf().parse(JsonOps.INSTANCE, json);
            ControlifyLogger var10001 = CUtil.LOGGER;
            Objects.requireNonNull(var10001);
            var10000.resultOrPartial(var10001::error).ifPresent((entries) -> {
               Iterator var2 = entries.iterator();

               while(var2.hasNext()) {
                  ControllerTypeManager.ControllerTypeEntry entry = (ControllerTypeManager.ControllerTypeEntry)var2.next();
                  Iterator var4 = entry.hid().iterator();

                  while(var4.hasNext()) {
                     HIDIdentifier hid = (HIDIdentifier)var4.next();
                     typeMap.put(hid, entry.type());
                  }
               }

            });
         } catch (Throwable var7) {
            if (resourceReader != null) {
               try {
                  resourceReader.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (resourceReader != null) {
            resourceReader.close();
         }
      } catch (Exception var8) {
         CUtil.LOGGER.error("Failed to read controller identification database!", (Throwable)var8);
      }

      return typeMap.entrySet().stream().toList();
   }

   public CompletableFuture<Void> apply(ControllerTypeManager.Preparations data, class_3300 manager, Executor executor) {
      return CompletableFuture.runAsync(() -> {
         this.typeMap = data.typeMap();
         this.triggerFullTypeReload();
      }, executor);
   }

   public void triggerFullTypeReload() {
      Optional<ControllerManager> controllerManagerOpt = Controlify.instance().getControllerManager();
      if (controllerManagerOpt.isPresent()) {
         ControllerManager controllerManager = (ControllerManager)controllerManagerOpt.get();
         Iterator var3 = controllerManager.getConnectedControllers().iterator();

         while(var3.hasNext()) {
            ControllerEntity controller = (ControllerEntity)var3.next();
            this.reloadTypeForController(controllerManager, controller);
         }
      }

   }

   public void reloadTypeForController(ControllerManager controllerManager, ControllerEntity controller) {
      Optional<HIDDevice> hidOpt = controller.info().hid();
      if (!hidOpt.isEmpty()) {
         HIDDevice hid = (HIDDevice)hidOpt.get();
         ControllerType newType = this.getControllerType(hid.asIdentifier());
         ControllerType oldType = controller.info().type();
         if (!newType.equals(oldType)) {
            controllerManager.reinitController(controller, new ControllerHIDService.ControllerHIDInfo(newType, controller.info().hid()));
         }

      }
   }

   public class_2960 getReloadId() {
      return ID;
   }

   public static record Preparations(Map<HIDIdentifier, ControllerType> typeMap) {
      public Preparations(Map<HIDIdentifier, ControllerType> typeMap) {
         this.typeMap = typeMap;
      }

      public Map<HIDIdentifier, ControllerType> typeMap() {
         return this.typeMap;
      }
   }

   private static record ControllerTypeEntry(List<HIDIdentifier> hid, ControllerType type) {
      private ControllerTypeEntry(List<HIDIdentifier> hid, ControllerType type) {
         this.hid = hid;
         this.type = type;
      }

      public List<HIDIdentifier> hid() {
         return this.hid;
      }

      public ControllerType type() {
         return this.type;
      }
   }
}
