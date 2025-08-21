package dev.isxander.controlify.bindings;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.bind.InputBindingBuilder;
import dev.isxander.controlify.bindings.defaults.DefaultBindProvider;
import dev.isxander.controlify.bindings.input.EmptyInput;
import dev.isxander.controlify.bindings.input.Input;
import dev.isxander.controlify.controller.ControllerEntity;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.class_2477;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_304;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InputBindingBuilderImpl implements InputBindingBuilder {
   @Nullable
   private class_2960 id;
   @Nullable
   private class_2561 category;
   @Nullable
   private class_2561 customName;
   @Nullable
   private class_2561 customDescription;
   @Nullable
   private Input defaultInput;
   private final Set<BindContext> allowedContexts = new HashSet();
   @Nullable
   private class_2960 radialCandidate;
   private final Set<class_304> keyCorrelations = new HashSet();
   private class_304 keyEmulation = null;
   private Function<ControllerEntity, Boolean> keyEmulationToggle = null;
   private boolean locked;

   public InputBindingBuilder id(@NotNull class_2960 rl) {
      this.checkLocked();
      this.id = rl;
      return this;
   }

   public InputBindingBuilder id(@NotNull String namespace, @NotNull String path) {
      return this.id(class_2960.method_60655(namespace, path));
   }

   public InputBindingBuilder category(@NotNull class_2561 text) {
      this.checkLocked();
      this.category = text;
      return this;
   }

   public InputBindingBuilder name(@NotNull class_2561 text) {
      this.checkLocked();
      this.customName = text;
      return this;
   }

   public InputBindingBuilder description(@NotNull class_2561 text) {
      this.checkLocked();
      this.customDescription = text;
      return this;
   }

   public InputBindingBuilder defaultInput(@Nullable Input input) {
      this.checkLocked();
      this.defaultInput = input;
      return this;
   }

   public InputBindingBuilder allowedContexts(@NotNull @Nullable BindContext... contexts) {
      this.checkLocked();
      if (contexts != null) {
         this.allowedContexts.addAll(List.of(contexts));
      }

      return this;
   }

   public InputBindingBuilder radialCandidate(@Nullable class_2960 icon) {
      this.checkLocked();
      this.radialCandidate = icon;
      return this;
   }

   public InputBindingBuilder addKeyCorrelation(@NotNull class_304 keyMapping) {
      this.checkLocked();
      this.keyCorrelations.add(keyMapping);
      return this;
   }

   public InputBindingBuilder keyEmulation(@NotNull class_304 keyMapping, @Nullable Function<ControllerEntity, Boolean> toggleCondition) {
      this.checkLocked();
      this.keyEmulation = keyMapping;
      this.keyEmulationToggle = toggleCondition;
      this.addKeyCorrelation(keyMapping);
      return this;
   }

   public InputBindingBuilder keyEmulation(@NotNull class_304 keyMapping) {
      return this.keyEmulation(keyMapping, (Function)null);
   }

   public InputBindingImpl build(ControllerEntity controller) {
      Validate.isTrue(this.locked, "Tried to build builder before it was locked.", new Object[0]);
      class_2561 name = this.createDefaultString(controller, (String)null, false);
      if (this.customName != null) {
         name = this.customName;
      }

      class_2561 description = this.createDefaultString(controller, "desc", true);
      if (this.customDescription != null) {
         description = this.customDescription;
      }

      if (description == null) {
         description = class_2561.method_43473();
      }

      Supplier<Input> defaultSupplier = () -> {
         DefaultBindProvider provider = Controlify.instance().defaultBindManager().getDefaultBindProvider(controller.info().type().namespace());
         Input input = provider.getDefaultBind(this.id);
         if (input == null) {
            input = this.defaultInput;
         }

         if (input == null) {
            input = EmptyInput.INSTANCE;
         }

         return (Input)input;
      };
      return new InputBindingImpl(controller, this.id, name, (class_2561)description, this.category, defaultSupplier, this.allowedContexts, this.radialCandidate);
   }

   @NotNull
   public class_2960 getIdAndLock() {
      this.checkLocked();
      this.locked = true;
      Validate.notNull(this.id, "Must call `.id(ResourceLocation)` on builder!", new Object[0]);
      Validate.notNull(this.category, "Must call `.category(Component)` on builder %s!".formatted(new Object[]{this.id}), new Object[0]);
      return this.id;
   }

   public Set<class_304> getKeyCorrelations() {
      return this.keyCorrelations;
   }

   @Nullable
   public class_304 getKeyEmulation() {
      return this.keyEmulation;
   }

   @Nullable
   public Function<ControllerEntity, Boolean> getKeyEmulationToggle() {
      return this.keyEmulationToggle;
   }

   private void checkLocked() {
      Validate.isTrue(!this.locked, "Tried to modify binding builder after is has been locked!", new Object[0]);
   }

   private class_2561 createDefaultString(ControllerEntity controller, @Nullable String suffix, boolean notExistToNull) {
      Objects.requireNonNull(this.id);
      class_2960 type = controller.info().type().namespace();
      String typeSpecificKey = type.method_48747("controlify.binding", this.id.method_42094());
      if (suffix != null) {
         typeSpecificKey = typeSpecificKey + "." + suffix;
      }

      if (class_2477.method_10517().method_4678(typeSpecificKey)) {
         return class_2561.method_43471(typeSpecificKey);
      } else {
         String genericKey = this.id.method_42093("controlify.binding");
         if (suffix != null) {
            genericKey = genericKey + "." + suffix;
         }

         return notExistToNull && !class_2477.method_10517().method_4678(genericKey) ? null : class_2561.method_43471(genericKey);
      }
   }
}
