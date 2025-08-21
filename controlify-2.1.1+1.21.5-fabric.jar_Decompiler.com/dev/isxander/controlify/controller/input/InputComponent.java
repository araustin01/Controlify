package dev.isxander.controlify.controller.input;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.bindings.ControlifyBindApiImpl;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.bindings.input.Input;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.ECSComponent;
import dev.isxander.controlify.controller.impl.ConfigImpl;
import dev.isxander.controlify.controller.input.mapping.ControllerMapping;
import dev.isxander.controlify.controller.input.mapping.ControllerMappingStorage;
import dev.isxander.controlify.controller.serialization.ConfigClass;
import dev.isxander.controlify.controller.serialization.ConfigHolder;
import dev.isxander.controlify.controller.serialization.CustomSaveLoadConfig;
import dev.isxander.controlify.controller.serialization.IConfig;
import dev.isxander.controlify.gui.screen.RadialMenuScreen;
import dev.isxander.controlify.utils.CUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;

public class InputComponent implements ECSComponent, ConfigHolder<InputComponent.Config>, CustomSaveLoadConfig {
   public static final class_2960 ID = CUtil.rl("input");
   private final ControllerEntity controller;
   private ControllerState stateNow;
   private ControllerState stateThen;
   private DeadzoneControllerStateView deadzoneStateNow;
   private DeadzoneControllerStateView deadzoneStateThen;
   private final int buttonCount;
   private final int axisCount;
   private final int hatCount;
   private final Map<class_2960, DeadzoneGroup> deadzoneAxes;
   private final boolean definitelyGamepad;
   private final Map<class_2960, InputBinding> inputBindings;
   private final IConfig<InputComponent.Config> config;

   public InputComponent(ControllerEntity controller, int buttonCount, int axisCount, int hatCount, boolean definitelyGamepad, Set<DeadzoneGroup> deadzoneAxes, String mappingId) {
      this.stateNow = ControllerState.EMPTY;
      this.stateThen = ControllerState.EMPTY;
      this.controller = controller;
      this.buttonCount = buttonCount;
      this.axisCount = axisCount;
      this.hatCount = hatCount;
      this.config = new ConfigImpl(() -> {
         return new InputComponent.Config(ControllerMappingStorage.get(mappingId));
      }, InputComponent.Config.class, this);
      this.definitelyGamepad = definitelyGamepad;
      this.deadzoneAxes = (Map)deadzoneAxes.stream().collect(Collectors.toMap(DeadzoneGroup::name, Function.identity(), (x, y) -> {
         return y;
      }, LinkedHashMap::new));
      this.inputBindings = new LinkedHashMap();
      this.updateDeadzoneView();
   }

   public ControllerStateView stateNow() {
      return this.deadzoneStateNow;
   }

   public ControllerStateView stateThen() {
      return this.deadzoneStateThen;
   }

   public ControllerState rawStateNow() {
      return this.stateNow;
   }

   public ControllerState rawStateThen() {
      return this.stateThen;
   }

   public void pushState(ControllerState state) {
      ControllerMapping mapping = ((InputComponent.Config)this.confObj()).mapping;
      if (mapping != null) {
         state = mapping.mapState(state);
      }

      this.stateThen = this.stateNow;
      this.stateNow = state;
      this.updateDeadzoneView();
      Iterator var3 = this.inputBindings.values().iterator();

      while(var3.hasNext()) {
         InputBinding binding = (InputBinding)var3.next();
         binding.pushState(this.deadzoneStateNow);
      }

   }

   @Nullable
   public InputBinding getBinding(class_2960 id) {
      return (InputBinding)this.inputBindings.get(id);
   }

   public Collection<InputBinding> getAllBindings() {
      return this.inputBindings.values();
   }

   public void notifyGuiPressOutputsOfNavigate() {
      Iterator var1 = this.inputBindings.values().iterator();

      while(var1.hasNext()) {
         InputBinding binding = (InputBinding)var1.next();
         binding.guiPressed().onNavigate();
      }

   }

   public void finalise() {
      Iterator var1 = ControlifyBindApiImpl.INSTANCE.provideBindsForController(this.controller).iterator();

      while(var1.hasNext()) {
         InputBinding binding = (InputBinding)var1.next();
         this.inputBindings.put(binding.id(), binding);
      }

   }

   public int buttonCount() {
      return this.buttonCount;
   }

   public int axisCount() {
      return this.axisCount;
   }

   public int hatCount() {
      return this.hatCount;
   }

   public boolean isDefinitelyGamepad() {
      return this.definitelyGamepad;
   }

   public Map<class_2960, DeadzoneGroup> getDeadzoneGroups() {
      ControllerMapping mapping = ((InputComponent.Config)this.confObj()).mapping;
      return (Map)(mapping != null ? mapping.deadzones() : this.deadzoneAxes);
   }

   public IConfig<InputComponent.Config> config() {
      return this.config;
   }

   public class_2960 id() {
      return ID;
   }

   private void updateDeadzoneView() {
      this.deadzoneStateNow = new DeadzoneControllerStateView(this.stateNow, this);
      this.deadzoneStateThen = new DeadzoneControllerStateView(this.stateThen, this);
   }

   Optional<class_2960> getDeadzoneForAxis(class_2960 axis) {
      Iterator var2 = this.getDeadzoneGroups().values().iterator();

      DeadzoneGroup group;
      do {
         if (!var2.hasNext()) {
            return Optional.empty();
         }

         group = (DeadzoneGroup)var2.next();
      } while(!group.axes().contains(axis));

      return Optional.of(group.name());
   }

   public void toJson(JsonObject json) {
      JsonObject innerJson = new JsonObject();
      Iterator var3 = this.inputBindings.values().iterator();

      while(true) {
         InputBinding binding;
         do {
            if (!var3.hasNext()) {
               json.add("bindings", innerJson);
               return;
            }

            binding = (InputBinding)var3.next();
         } while(!((InputComponent.Config)this.confObj()).keepDefaultBindings && binding.boundInput().equals(binding.defaultInput()));

         try {
            innerJson.add(binding.id().toString(), (JsonElement)Input.CODEC.encodeStart(JsonOps.INSTANCE, binding.boundInput()).result().orElseThrow());
         } catch (Exception var6) {
            CUtil.LOGGER.error("Failed to serialize input binding {}", binding.id(), var6);
         }
      }
   }

   public void fromJson(JsonObject json) {
      if (!json.has("bindings")) {
         CUtil.LOGGER.warn("Could not find bindings in json, upgrading from older version?");
      } else {
         JsonObject innerJson = json.getAsJsonObject("bindings");
         Iterator var3 = this.inputBindings.values().iterator();

         while(var3.hasNext()) {
            InputBinding binding = (InputBinding)var3.next();
            JsonElement element = innerJson.get(binding.id().toString());
            if (element != null) {
               try {
                  Input input = (Input)Input.CODEC.parse(JsonOps.INSTANCE, element).result().orElseThrow();
                  binding.setBoundInput(input);
               } catch (Exception var7) {
                  CUtil.LOGGER.error("Failed to deserialize input binding {}. Using default.", binding.id(), var7);
               }
            }
         }

      }
   }

   public static class Config implements ConfigClass {
      public float hLookSensitivity = 1.0F;
      public float vLookSensitivity = 0.9F;
      public boolean vLookInvert = false;
      public float virtualMouseSensitivity = 1.0F;
      public boolean reduceAimingSensitivity = true;
      public float buttonActivationThreshold = 0.5F;
      public boolean isLCE = false;
      public Map<class_2960, Float> deadzones = new Object2ObjectOpenHashMap();
      public boolean deadzonesCalibrated = false;
      public boolean delayedCalibration = false;
      public boolean mixedInput = false;
      public boolean keepDefaultBindings = false;
      public class_2960[] radialActions = new class_2960[8];
      public int radialButtonFocusTimeoutTicks = 20;
      @Nullable
      public ControllerMapping mapping = null;

      public Config() {
      }

      public Config(@Nullable ControllerMapping typeProvidedMapping) {
         this.mapping = typeProvidedMapping;
      }

      public void onConfigSaveLoad(ControllerEntity controller) {
         this.validateRadialActions(controller);
         if (!this.deadzonesCalibrated) {
            this.deadzonesCalibrated = true;
            this.deadzones.put(CUtil.rl("left_stick"), 0.1F);
            this.deadzones.put(CUtil.rl("right_stick"), 0.1F);
         }

      }

      private void validateRadialActions(ControllerEntity controller) {
         boolean changed = false;

         for(int i = 0; i < this.radialActions.length; ++i) {
            class_2960 action = this.radialActions[i];
            InputBinding radialBinding = action != null ? ((InputComponent)controller.input().orElseThrow()).getBinding(action) : null;
            if (!RadialMenuScreen.EMPTY_ACTION.equals(action) && (radialBinding == null || radialBinding.radialIcon().isEmpty())) {
               this.setDefaultRadialAction(i);
               changed = true;
            }
         }

         if (changed) {
            Controlify.instance().config().setDirty();
         }

      }

      private void setDefaultRadialAction(int index) {
         class_2960[] var10000 = this.radialActions;
         class_2960 var10002;
         switch(index) {
         case 0:
            var10002 = ControlifyBindings.TOGGLE_HUD_VISIBILITY.bindId();
            break;
         case 1:
            var10002 = ControlifyBindings.CHANGE_PERSPECTIVE.bindId();
            break;
         case 2:
            var10002 = ControlifyBindings.DROP_STACK.bindId();
            break;
         case 3:
            var10002 = ControlifyBindings.OPEN_CHAT.bindId();
            break;
         case 4:
            var10002 = ControlifyBindings.SWAP_HANDS.bindId();
            break;
         case 5:
            var10002 = ControlifyBindings.PICK_BLOCK.bindId();
            break;
         case 6:
            var10002 = ControlifyBindings.TAKE_SCREENSHOT.bindId();
            break;
         case 7:
            var10002 = ControlifyBindings.SHOW_PLAYER_LIST.bindId();
            break;
         default:
            var10002 = RadialMenuScreen.EMPTY_ACTION;
         }

         var10000[index] = var10002;
      }
   }
}
