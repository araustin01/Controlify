package dev.isxander.controlify.bindings.defaults;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.id.ControllerType;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.controllermanager.ControllerManager;
import dev.isxander.controlify.platform.client.resource.SimpleControlifyReloadListener;
import dev.isxander.controlify.utils.CUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.class_2960;
import net.minecraft.class_3298;
import net.minecraft.class_3300;
import net.minecraft.class_7654;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class DefaultBindManager implements SimpleControlifyReloadListener<DefaultBindManager.Preparations> {
   public static final String DIRECTORY = "controllers/default_bind";
   private static final class_7654 converter = class_7654.method_45114("controllers/default_bind");
   private static final Logger LOGGER = LogUtils.getLogger();
   private final Map<class_2960, DefaultBindProvider> defaultsByNamespace = new HashMap();

   public CompletableFuture<DefaultBindManager.Preparations> load(class_3300 manager, Executor executor) {
      return CompletableFuture.supplyAsync(() -> {
         Map<class_2960, List<class_3298>> defaultFiles = converter.method_45116(manager);
         Map<class_2960, DefaultBindProvider> defaultsByNamespace = new HashMap();
         class_2960 defaultNamespaceFile = converter.method_45112(ControllerType.DEFAULT.namespace());
         if (!defaultFiles.containsKey(defaultNamespaceFile)) {
            LOGGER.error("No default binds found! Everything will be unbound!");
            return null;
         } else {
            LayeredDefaultBindProvider defaultNamespaceDefaults = new LayeredDefaultBindProvider((List)this.readDefaults(defaultNamespaceFile, (List)defaultFiles.get(defaultNamespaceFile)).getSecond());
            defaultsByNamespace.put(ControllerType.DEFAULT.namespace(), defaultNamespaceDefaults);
            Iterator var6 = defaultFiles.entrySet().iterator();

            while(var6.hasNext()) {
               Entry<class_2960, List<class_3298>> stack = (Entry)var6.next();
               class_2960 id = (class_2960)stack.getKey();
               List<class_3298> files = (List)stack.getValue();
               if (!id.equals(defaultNamespaceFile)) {
                  Pair<class_2960, List<LayeredDefaultBindProvider.Layer>> defaults = this.readDefaults(id, files);
                  ((List)defaults.getSecond()).add(new LayeredDefaultBindProvider.Layer(defaultNamespaceDefaults, false));
                  LayeredDefaultBindProvider defaultBindProvider = new LayeredDefaultBindProvider((List)defaults.getSecond());
                  defaultsByNamespace.put((class_2960)defaults.getFirst(), defaultBindProvider);
               }
            }

            return new DefaultBindManager.Preparations(defaultsByNamespace);
         }
      }, executor);
   }

   private Pair<class_2960, List<LayeredDefaultBindProvider.Layer>> readDefaults(class_2960 id, List<class_3298> files) {
      List<LayeredDefaultBindProvider.Layer> defaults = new ArrayList();
      Iterator var4 = files.iterator();

      while(var4.hasNext()) {
         class_3298 resource = (class_3298)var4.next();

         try {
            BufferedReader reader = resource.method_43039();

            try {
               JsonElement json = JsonParser.parseReader(reader);
               DefaultBindManager.ControllerDefault def = (DefaultBindManager.ControllerDefault)DefaultBindManager.ControllerDefault.CODEC.parse(JsonOps.INSTANCE, json).result().orElseThrow();
               defaults.add(0, new LayeredDefaultBindProvider.Layer(def.provider(), def.clearBelow()));
            } catch (Throwable var10) {
               if (reader != null) {
                  try {
                     reader.close();
                  } catch (Throwable var9) {
                     var10.addSuppressed(var9);
                  }
               }

               throw var10;
            }

            if (reader != null) {
               reader.close();
            }
         } catch (IllegalStateException | IOException var11) {
            LOGGER.error("Failed to parse {}", id, var11);
         }
      }

      class_2960 namespace = converter.method_45115(id);
      return Pair.of(namespace, defaults);
   }

   public CompletableFuture<Void> apply(@Nullable DefaultBindManager.Preparations data, class_3300 manager, Executor executor) {
      return CompletableFuture.runAsync(() -> {
         List<InputBinding> defaultedBindings = new ArrayList();
         Iterator var3 = ((List)Controlify.instance().getControllerManager().map(ControllerManager::getConnectedControllers).orElse(List.of())).iterator();

         while(var3.hasNext()) {
            ControllerEntity controller = (ControllerEntity)var3.next();
            controller.input().ifPresent((input) -> {
               if (!((InputComponent.Config)input.confObj()).keepDefaultBindings) {
                  Iterator var2 = input.getAllBindings().iterator();

                  while(var2.hasNext()) {
                     InputBinding binding = (InputBinding)var2.next();
                     if (binding.boundInput().equals(binding.defaultInput())) {
                        defaultedBindings.add(binding);
                     }
                  }
               }

            });
         }

         this.defaultsByNamespace.clear();
         if (data != null) {
            this.defaultsByNamespace.putAll(data.map());
         }

         var3 = defaultedBindings.iterator();

         while(var3.hasNext()) {
            InputBinding binding = (InputBinding)var3.next();
            binding.setBoundInput(binding.defaultInput());
         }

      }, executor);
   }

   public DefaultBindProvider getDefaultBindProvider(class_2960 namespace) {
      DefaultBindProvider provider = (DefaultBindProvider)this.defaultsByNamespace.get(namespace);
      if (provider == null) {
         provider = (DefaultBindProvider)this.defaultsByNamespace.get(ControllerType.DEFAULT.namespace());
      }

      if (provider == null) {
         provider = DefaultBindProvider.EMPTY;
      }

      return provider;
   }

   public class_2960 getReloadId() {
      return CUtil.rl("default_binds");
   }

   private static record ControllerDefault(boolean clearBelow, MapBackedDefaultBindProvider provider) {
      public static final Codec<DefaultBindManager.ControllerDefault> CODEC = RecordCodecBuilder.create((instance) -> {
         return instance.group(Codec.BOOL.optionalFieldOf("clear_below", false).forGetter(DefaultBindManager.ControllerDefault::clearBelow), MapBackedDefaultBindProvider.MAP_CODEC.fieldOf("defaults").forGetter(DefaultBindManager.ControllerDefault::provider)).apply(instance, DefaultBindManager.ControllerDefault::new);
      });

      private ControllerDefault(boolean clearBelow, MapBackedDefaultBindProvider provider) {
         this.clearBelow = clearBelow;
         this.provider = provider;
      }

      public boolean clearBelow() {
         return this.clearBelow;
      }

      public MapBackedDefaultBindProvider provider() {
         return this.provider;
      }
   }

   public static record Preparations(Map<class_2960, DefaultBindProvider> map) {
      public Preparations(Map<class_2960, DefaultBindProvider> map) {
         this.map = map;
      }

      public Map<class_2960, DefaultBindProvider> map() {
         return this.map;
      }
   }
}
