package dev.isxander.controlify.bindings;

import dev.isxander.controlify.api.bind.ControlifyBindApi;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.api.bind.RadialIcon;
import dev.isxander.controlify.bindings.output.KeyMappingEmulationOutput;
import dev.isxander.controlify.controller.ControllerEntity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.class_2378;
import net.minecraft.class_2960;
import net.minecraft.class_304;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ControlifyBindApiImpl implements ControlifyBindApi {
   public static final ControlifyBindApiImpl INSTANCE = new ControlifyBindApiImpl();
   private final List<ControlifyBindApiImpl.RegistryEntry> bindEntries = new ArrayList();
   private final Map<class_304, List<InputBindingSupplier>> keyMappingCorrelations = new HashMap();
   private boolean locked;

   public InputBindingSupplier registerBinding(ControlifyBindApi.RegistryCallback callback) {
      return this.registerBinding(callback, (c) -> {
         return true;
      });
   }

   public InputBindingSupplier registerBinding(ControlifyBindApi.RegistryCallback callback, Predicate<ControllerEntity> filter) {
      this.checkLocked();
      InputBindingBuilderImpl builder = new InputBindingBuilderImpl();
      callback.apply(builder);
      Objects.requireNonNull(builder);
      Function<ControllerEntity, InputBindingImpl> finaliser = builder::build;
      class_2960 bindId = builder.getIdAndLock();
      this.bindEntries.add(new ControlifyBindApiImpl.RegistryEntry(filter, finaliser, builder.getKeyEmulation(), builder.getKeyEmulationToggle(), bindId));
      Iterator var6 = builder.getKeyCorrelations().iterator();

      while(var6.hasNext()) {
         class_304 key = (class_304)var6.next();
         ((List)this.keyMappingCorrelations.computeIfAbsent(key, (k) -> {
            return new ArrayList();
         })).add(this.createSupplier(bindId));
      }

      return this.createSupplier(bindId);
   }

   public List<InputBindingSupplier> getKeyCorrelation(class_304 key) {
      return (List)Optional.ofNullable((List)this.keyMappingCorrelations.get(key)).orElse(List.of());
   }

   public void registerRadialIcon(class_2960 id, RadialIcon icon) {
      this.checkLocked();
      RadialIcons.registerIcon(id, icon);
   }

   public void registerBindContext(BindContext context) {
      class_2378.method_10230(BindContext.REGISTRY, context.id(), context);
   }

   private void checkLocked() {
      if (this.locked) {
         throw new IllegalStateException("Registry is locked. Cannot add bind now.");
      }
   }

   public List<InputBinding> provideBindsForController(ControllerEntity controller) {
      List<InputBinding> bindings = new ArrayList();
      Iterator var3 = this.bindEntries.iterator();

      while(var3.hasNext()) {
         ControlifyBindApiImpl.RegistryEntry entry = (ControlifyBindApiImpl.RegistryEntry)var3.next();
         if (entry.filter().test(controller)) {
            InputBindingImpl binding = (InputBindingImpl)entry.builder().apply(controller);
            if (entry.emulation() != null) {
               BooleanSupplier emulationToggle = null;
               if (entry.emulationToggle() != null) {
                  emulationToggle = () -> {
                     return (Boolean)entry.emulationToggle().apply(controller);
                  };
               }

               binding.addDigitalOutput(InputBinding.KEY_EMULATION, new KeyMappingEmulationOutput(controller, binding, entry.emulation(), emulationToggle));
            }

            bindings.add(binding);
         }
      }

      return bindings;
   }

   private InputBindingSupplier createSupplier(class_2960 bindingId) {
      return new InputBindingSupplier(this) {
         @Nullable
         public InputBinding onOrNull(@NotNull ControllerEntity controller) {
            return (InputBinding)controller.input().map((input) -> {
               return input.getBinding(bindingId);
            }).orElse((Object)null);
         }

         public class_2960 bindId() {
            return bindingId;
         }
      };
   }

   public void lock() {
      this.locked = true;
   }

   public Stream<class_2960> getAllBindIds() {
      return this.bindEntries.stream().map(ControlifyBindApiImpl.RegistryEntry::id);
   }

   private static record RegistryEntry(Predicate<ControllerEntity> filter, Function<ControllerEntity, InputBindingImpl> builder, class_304 emulation, Function<ControllerEntity, Boolean> emulationToggle, class_2960 id) {
      private RegistryEntry(Predicate<ControllerEntity> filter, Function<ControllerEntity, InputBindingImpl> builder, class_304 emulation, Function<ControllerEntity, Boolean> emulationToggle, class_2960 id) {
         this.filter = filter;
         this.builder = builder;
         this.emulation = emulation;
         this.emulationToggle = emulationToggle;
         this.id = id;
      }

      public Predicate<ControllerEntity> filter() {
         return this.filter;
      }

      public Function<ControllerEntity, InputBindingImpl> builder() {
         return this.builder;
      }

      public class_304 emulation() {
         return this.emulation;
      }

      public Function<ControllerEntity, Boolean> emulationToggle() {
         return this.emulationToggle;
      }

      public class_2960 id() {
         return this.id;
      }
   }
}
