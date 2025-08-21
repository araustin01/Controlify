package dev.isxander.controlify.gui.screen;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.bindings.BindContext;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.bindings.input.EmptyInput;
import dev.isxander.controlify.bindings.input.Input;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.GenericControllerConfig;
import dev.isxander.controlify.controller.gyro.GyroButtonMode;
import dev.isxander.controlify.controller.gyro.GyroComponent;
import dev.isxander.controlify.controller.gyro.GyroYawMode;
import dev.isxander.controlify.controller.haptic.HDHapticComponent;
import dev.isxander.controlify.controller.input.DeadzoneGroup;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.controller.input.Inputs;
import dev.isxander.controlify.controller.keyboard.NativeKeyboardComponent;
import dev.isxander.controlify.controller.rumble.RumbleComponent;
import dev.isxander.controlify.controller.serialization.ConfigHolder;
import dev.isxander.controlify.gui.controllers.BindController;
import dev.isxander.controlify.gui.controllers.Deadzone2DImageRenderer;
import dev.isxander.controlify.gui.guide.InGameButtonGuide;
import dev.isxander.controlify.rumble.BasicRumbleEffect;
import dev.isxander.controlify.rumble.RumbleSource;
import dev.isxander.controlify.rumble.RumbleState;
import dev.isxander.controlify.server.ServerPolicies;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.LabelOption;
import dev.isxander.yacl3.api.NameableEnum;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionFlag;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.YetAnotherConfigLib.Builder;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.CyclingListControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import dev.isxander.yacl3.api.controller.ValueFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.class_124;
import net.minecraft.class_156;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_5244;

public class ControllerConfigScreenFactory {
   private static final ValueFormatter<Float> percentFormatter = (v) -> {
      return class_2561.method_43470(String.format("%.0f%%", v * 100.0F));
   };
   private static final ValueFormatter<Float> percentOrOffFormatter = (v) -> {
      return v == 0.0F ? class_5244.field_24333 : percentFormatter.format(v);
   };
   private static final class_2561 newOptionLabel;
   private static final ValueFormatter<Integer> ticksToMillisFormatter;
   private final List<Option<?>> newOptions = new ArrayList();

   public static class_437 generateConfigScreen(class_437 parent, ControllerEntity controller) {
      return (new ControllerConfigScreenFactory()).generateConfigScreen0(parent, controller);
   }

   private class_437 generateConfigScreen0(class_437 parent, ControllerEntity controller) {
      ConfigCategory advancedCategory = this.createAdvancedCategory(controller);
      Optional<ConfigCategory> bindsCategory = this.makeBindsCategory(controller);
      ConfigCategory basicCategory = this.createBasicCategory(controller);
      Builder yacl = YetAnotherConfigLib.createBuilder().title(class_2561.method_43470("Controlify")).category(basicCategory).category(advancedCategory).save(() -> {
         Controlify.instance().config().save();
      });
      Objects.requireNonNull(yacl);
      bindsCategory.ifPresent(yacl::category);
      return yacl.build().generateScreen(parent);
   }

   private ConfigCategory createBasicCategory(ControllerEntity controller) {
      Optional<OptionGroup> sensitivityGroup = this.makeSensitivityGroup(controller);
      Optional<OptionGroup> controlsGroup = this.makeControlsGroup(controller);
      Optional<OptionGroup> accessibilityGroup = this.makeAccessibilityGroup(controller);
      Optional<OptionGroup> deadzoneGroup = this.makeDeadzoneGroup(controller);
      GenericControllerConfig config = (GenericControllerConfig)controller.genericConfig().config();
      GenericControllerConfig def = (GenericControllerConfig)controller.genericConfig().defaultConfig();
      dev.isxander.yacl3.api.ConfigCategory.Builder builder = ConfigCategory.createBuilder().name(class_2561.method_43471("controlify.gui.config.category.basic")).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.custom_name")).description(OptionDescription.of(new class_2561[]{class_2561.method_43471("controlify.gui.custom_name.tooltip")})).binding(def.nickname == null ? "" : def.nickname, () -> {
         return config.nickname == null ? "" : config.nickname;
      }, (v) -> {
         config.nickname = v.isEmpty() ? null : v;
      }).controller(StringControllerBuilder::create).build());
      if (!this.newOptions.isEmpty()) {
         builder.group(OptionGroup.createBuilder().name(class_2561.method_43471("controlify.gui.new_options").method_27695(new class_124[]{class_124.field_1065, class_124.field_1067})).description(OptionDescription.of(new class_2561[]{class_2561.method_43471("controlify.gui.new_options.tooltip")})).options(this.newOptions).build());
      }

      Objects.requireNonNull(builder);
      sensitivityGroup.ifPresent(builder::group);
      Objects.requireNonNull(builder);
      controlsGroup.ifPresent(builder::group);
      Objects.requireNonNull(builder);
      accessibilityGroup.ifPresent(builder::group);
      Objects.requireNonNull(builder);
      deadzoneGroup.ifPresent(builder::group);
      return builder.build();
   }

   private Optional<OptionGroup> makeSensitivityGroup(ControllerEntity controller) {
      Optional<InputComponent> inputOpt = controller.input();
      if (inputOpt.isEmpty()) {
         return Optional.empty();
      } else {
         InputComponent.Config config = (InputComponent.Config)((InputComponent)inputOpt.get()).confObj();
         InputComponent.Config def = (InputComponent.Config)((InputComponent)inputOpt.get()).defObj();
         return Optional.of(OptionGroup.createBuilder().name(class_2561.method_43471("controlify.gui.config.group.sensitivity")).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.horizontal_look_sensitivity")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.horizontal_look_sensitivity.tooltip")}).build()).binding(def.hLookSensitivity, () -> {
            return config.hLookSensitivity;
         }, (v) -> {
            config.hLookSensitivity = v;
         }).controller((opt) -> {
            return ((FloatSliderControllerBuilder)((FloatSliderControllerBuilder)FloatSliderControllerBuilder.create(opt).range(0.1F, 2.0F)).step(0.05F)).formatValue(percentFormatter);
         }).build()).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.vertical_look_sensitivity")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.vertical_look_sensitivity.tooltip")}).build()).binding(def.vLookSensitivity, () -> {
            return config.vLookSensitivity;
         }, (v) -> {
            config.vLookSensitivity = v;
         }).controller((opt) -> {
            return ((FloatSliderControllerBuilder)((FloatSliderControllerBuilder)FloatSliderControllerBuilder.create(opt).range(0.1F, 2.0F)).step(0.05F)).formatValue(percentFormatter);
         }).build()).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.invert_vertical_look")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.invert_vertical_look.tooltip")}).build()).binding(def.vLookInvert, () -> {
            return config.vLookInvert;
         }, (v) -> {
            config.vLookInvert = v;
         }).controller(TickBoxControllerBuilder::create).build()).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.vmouse_sensitivity")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.vmouse_sensitivity.tooltip")}).build()).binding(def.virtualMouseSensitivity, () -> {
            return config.virtualMouseSensitivity;
         }, (v) -> {
            config.virtualMouseSensitivity = v;
         }).controller((opt) -> {
            return ((FloatSliderControllerBuilder)((FloatSliderControllerBuilder)FloatSliderControllerBuilder.create(opt).range(0.1F, 2.0F)).step(0.05F)).formatValue(percentFormatter);
         }).build()).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.reduce_aiming_sensitivity")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.reduce_aiming_sensitivity.tooltip")}).webpImage(screenshot("reduce-aim-sensitivity.webp")).build()).binding(def.reduceAimingSensitivity, () -> {
            return config.reduceAimingSensitivity;
         }, (v) -> {
            config.reduceAimingSensitivity = v;
         }).controller(TickBoxControllerBuilder::create).build()).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.is_lce")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.is_lce.tooltip")}).build()).binding(def.isLCE, () -> {
            return config.isLCE;
         }, (v) -> {
            config.isLCE = v;
         }).controller((opt) -> {
            return BooleanControllerBuilder.create(opt).onOffFormatter();
         }).build()).build());
      }
   }

   private Optional<OptionGroup> makeControlsGroup(ControllerEntity controller) {
      ValueFormatter<Boolean> holdToggleFormatter = (v) -> {
         return class_2561.method_43471("controlify.gui.format.hold_toggle." + (v ? "toggle" : "hold"));
      };
      GenericControllerConfig config = (GenericControllerConfig)controller.genericConfig().config();
      GenericControllerConfig def = (GenericControllerConfig)controller.genericConfig().defaultConfig();
      return Optional.of(OptionGroup.createBuilder().name(class_2561.method_43471("controlify.gui.config.group.controls")).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.toggle_sprint")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.toggle_sprint.tooltip")}).build()).binding(def.toggleSprint, () -> {
         return config.toggleSprint;
      }, (v) -> {
         config.toggleSprint = v;
      }).controller((opt) -> {
         return ((BooleanControllerBuilder)BooleanControllerBuilder.create(opt).formatValue(holdToggleFormatter)).coloured(false);
      }).build()).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.toggle_sneak")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.toggle_sneak.tooltip")}).build()).binding(def.toggleSneak, () -> {
         return config.toggleSneak;
      }, (v) -> {
         config.toggleSneak = v;
      }).controller((opt) -> {
         return ((BooleanControllerBuilder)BooleanControllerBuilder.create(opt).formatValue(holdToggleFormatter)).coloured(false);
      }).build()).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.auto_jump")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.auto_jump.tooltip")}).build()).binding(def.autoJump, () -> {
         return config.autoJump;
      }, (v) -> {
         config.autoJump = v;
      }).controller((opt) -> {
         return BooleanControllerBuilder.create(opt).onOffFormatter();
      }).build()).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.no_fly_drifting")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.no_fly_drifting.tooltip")}).text(new class_2561[]{ServerPolicies.DISABLE_FLY_DRIFTING.isUnset() ? class_2561.method_43471("controlify.gui.server_controlled").method_27692(class_124.field_1065) : class_2561.method_43473()}).build()).binding(def.disableFlyDrifting, () -> {
         return ServerPolicies.DISABLE_FLY_DRIFTING.isUnset() ? config.disableFlyDrifting : ServerPolicies.DISABLE_FLY_DRIFTING.get();
      }, (v) -> {
         config.disableFlyDrifting = v;
      }).controller(TickBoxControllerBuilder::create).available(ServerPolicies.DISABLE_FLY_DRIFTING.isUnset()).build()).build());
   }

   private Optional<OptionGroup> makeAccessibilityGroup(ControllerEntity controller) {
      GenericControllerConfig config = (GenericControllerConfig)controller.genericConfig().config();
      GenericControllerConfig def = (GenericControllerConfig)controller.genericConfig().defaultConfig();
      return Optional.of(OptionGroup.createBuilder().name(class_2561.method_43471("controlify.config.group.accessibility")).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.show_ingame_guide")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.show_ingame_guide.tooltip")}).image(screenshot("ingame-button-guide.png"), 961, 306).build()).binding(def.showIngameGuide, () -> {
         return config.showIngameGuide;
      }, (v) -> {
         config.showIngameGuide = v;
      }).controller(TickBoxControllerBuilder::create).build()).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.ingame_button_guide_position")).description(OptionDescription.of(new class_2561[]{class_2561.method_43471("controlify.gui.ingame_button_guide_position.tooltip")})).binding(def.ingameGuideBottom, () -> {
         return config.ingameGuideBottom;
      }, (v) -> {
         config.ingameGuideBottom = v;
      }).controller((opt) -> {
         return BooleanControllerBuilder.create(opt).formatValue((v) -> {
            return class_2561.method_43471(v ? "controlify.gui.format.bottom" : "controlify.gui.format.top");
         });
      }).flag(new OptionFlag[]{(mc) -> {
         Controlify.instance().inGameButtonGuide().ifPresent(InGameButtonGuide::refreshLayout);
      }}).build()).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.show_screen_guide")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.show_screen_guide.tooltip")}).webpImage(screenshot("screen-button-guide.webp")).build()).binding(def.showScreenGuides, () -> {
         return config.showScreenGuides;
      }, (v) -> {
         config.showScreenGuides = v;
      }).controller(TickBoxControllerBuilder::create).build()).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.show_keyboard")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.show_keyboard.tooltip")}).build()).binding(ControllerConfigScreenFactory.OnScreenKeyboardMode.getForController(controller), () -> {
         return ControllerConfigScreenFactory.OnScreenKeyboardMode.getForController(controller);
      }, (v) -> {
         v.setForController(controller);
      }).controller((opt) -> {
         return CyclingListControllerBuilder.create(opt).values(ControllerConfigScreenFactory.OnScreenKeyboardMode.valuesForController(controller)).formatValue(ControllerConfigScreenFactory.OnScreenKeyboardMode::getDisplayName);
      }).build()).build());
   }

   private Optional<OptionGroup> makeDeadzoneGroup(ControllerEntity controller) {
      Optional<InputComponent> inputOpt = controller.input();
      if (inputOpt.isEmpty()) {
         return Optional.empty();
      } else {
         InputComponent input = (InputComponent)inputOpt.get();
         InputComponent.Config config = (InputComponent.Config)input.confObj();
         InputComponent.Config def = (InputComponent.Config)input.defObj();
         ArrayList<Option<Float>> deadzoneOpts = new ArrayList();
         dev.isxander.yacl3.api.OptionGroup.Builder group = OptionGroup.createBuilder().name(class_2561.method_43471("controlify.config.group.deadzones"));
         group.option(LabelOption.create(class_2561.method_43471("controlify.gui.stickdrift_warning").method_27692(class_124.field_1061)));
         Iterator var8 = input.getDeadzoneGroups().values().iterator();

         while(var8.hasNext()) {
            DeadzoneGroup deadzoneGroup = (DeadzoneGroup)var8.next();
            class_2960 groupName = deadzoneGroup.name();
            String var10000 = groupName.method_12836();
            class_2561 name = class_2561.method_43471("controlify.deadzone_group." + var10000 + "." + groupName.method_12832());
            AtomicReference<Option<Float>> deadzoneRef = new AtomicReference();
            dev.isxander.yacl3.api.Option.Builder var14 = Option.createBuilder().name(name);
            dev.isxander.yacl3.api.OptionDescription.Builder var10001 = OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43469("controlify.gui.axis_deadzone.tooltip", new Object[]{name})});
            Optional var10002;
            if (deadzoneGroup.axes().size() == 4) {
               Objects.requireNonNull(deadzoneRef);
               var10002 = Optional.of(new Deadzone2DImageRenderer(input, deadzoneGroup, deadzoneRef::get));
            } else {
               var10002 = Optional.empty();
            }

            Option<Float> deadzoneOpt = var14.description(var10001.customImage(CompletableFuture.completedFuture(var10002)).build()).binding((Float)def.deadzones.getOrDefault(groupName, 0.0F), () -> {
               return (Float)config.deadzones.getOrDefault(groupName, 0.0F);
            }, (v) -> {
               config.deadzones.put(groupName, v);
            }).controller((opt) -> {
               return ((FloatSliderControllerBuilder)((FloatSliderControllerBuilder)FloatSliderControllerBuilder.create(opt).range(0.0F, 1.0F)).step(0.02F)).formatValue(percentFormatter);
            }).build();
            deadzoneRef.set(deadzoneOpt);
            group.option(deadzoneOpt);
            deadzoneOpts.add(deadzoneOpt);
         }

         group.option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.button_activation_threshold")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.button_activation_threshold.tooltip")}).build()).binding(def.buttonActivationThreshold, () -> {
            return config.buttonActivationThreshold;
         }, (v) -> {
            config.buttonActivationThreshold = v;
         }).controller((opt) -> {
            return ((FloatSliderControllerBuilder)((FloatSliderControllerBuilder)FloatSliderControllerBuilder.create(opt).range(0.0F, 1.0F)).step(0.01F)).formatValue(percentFormatter);
         }).build());
         group.option(ButtonOption.createBuilder().name(class_2561.method_43471("controlify.gui.auto_calibration")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.auto_calibration.tooltip")}).build()).action((screen, button) -> {
            class_310.method_1551().method_1507(new ControllerCalibrationScreen(controller, () -> {
               deadzoneOpts.forEach(Option::forgetPendingValue);
               return screen;
            }));
         }).build());
         return Optional.of(group.build());
      }
   }

   private ConfigCategory createAdvancedCategory(ControllerEntity controller) {
      Optional<InputComponent> input = controller.input();
      dev.isxander.yacl3.api.ConfigCategory.Builder builder = ConfigCategory.createBuilder().name(class_2561.method_43471("controlify.config.category.advanced"));
      input.ifPresent((inputComponent) -> {
         builder.option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.mixed_input")).description(OptionDescription.of(new class_2561[]{class_2561.method_43471("controlify.gui.mixed_input.tooltip")})).binding(((InputComponent.Config)inputComponent.defObj()).mixedInput, () -> {
            return ((InputComponent.Config)inputComponent.confObj()).mixedInput;
         }, (v) -> {
            ((InputComponent.Config)inputComponent.confObj()).mixedInput = v;
         }).controller(TickBoxControllerBuilder::create).build());
      });
      Optional var10000 = this.makeVibrationGroup(controller);
      Objects.requireNonNull(builder);
      var10000.ifPresent(builder::group);
      var10000 = this.makeGyroGroup(controller);
      Objects.requireNonNull(builder);
      var10000.ifPresent(builder::group);
      var10000 = this.makeControllerMappingGroup(controller);
      Objects.requireNonNull(builder);
      var10000.ifPresent(builder::group);
      return builder.build();
   }

   private Optional<OptionGroup> makeControllerMappingGroup(ControllerEntity controller) {
      Optional<InputComponent> inputOpt = controller.input();
      if (inputOpt.isEmpty()) {
         return Optional.empty();
      } else {
         InputComponent input = (InputComponent)inputOpt.get();
         InputComponent.Config config = (InputComponent.Config)input.confObj();
         InputComponent.Config def = (InputComponent.Config)input.defObj();
         return Optional.of(OptionGroup.createBuilder().name(class_2561.method_43471("controlify.gui.group.controller_mapping")).option(LabelOption.create(class_2561.method_43471("controlify.gui.controller_mapping.explanation"))).option(ButtonOption.createBuilder().name(class_2561.method_43471("controlify.gui.create_gamepad_mapping")).description(OptionDescription.of(new class_2561[]{class_2561.method_43471("controlify.gui.create_gamepad_mapping.tooltip")})).action((screen, button) -> {
            class_310.method_1551().method_1507(ControllerMappingMakerScreen.createGamepadMapping(input, screen));
         }).build()).option(ButtonOption.createBuilder().name(class_2561.method_43471("controlify.gui.clear_mapping")).description(OptionDescription.of(new class_2561[]{class_2561.method_43471("controlify.gui.clear_mapping.tooltip")})).action((screen, button) -> {
            config.mapping = def.mapping;
         }).build()).collapsed(true).build());
      }
   }

   private Optional<OptionGroup> makeVibrationGroup(ControllerEntity controller) {
      dev.isxander.yacl3.api.OptionGroup.Builder vibrationGroup = OptionGroup.createBuilder().name(class_2561.method_43471("controlify.gui.group.vibration")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.group.vibration.tooltip")}).build());
      Optional<RumbleComponent> rumbleOpt = controller.rumble();
      if (rumbleOpt.isEmpty()) {
         vibrationGroup.option(LabelOption.create(class_2561.method_43471("controlify.gui.allow_vibrations.not_available").method_27692(class_124.field_1061)));
         return Optional.of(vibrationGroup.build());
      } else {
         RumbleComponent rumble = (RumbleComponent)rumbleOpt.get();
         RumbleComponent.Config config = (RumbleComponent.Config)rumble.confObj();
         RumbleComponent.Config def = (RumbleComponent.Config)rumble.defObj();
         List<Option<Float>> strengthOptions = new ArrayList();
         Option allowVibrationOption;
         vibrationGroup.option(allowVibrationOption = Option.createBuilder().name(class_2561.method_43471("controlify.gui.allow_vibrations")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.allow_vibrations.tooltip")}).build()).binding(def.enabled, () -> {
            return config.enabled;
         }, (v) -> {
            config.enabled = v;
         }).addListener((opt, event) -> {
            strengthOptions.forEach((so) -> {
               so.setAvailable((Boolean)opt.pendingValue());
            });
         }).controller(TickBoxControllerBuilder::create).build());
         controller.hdHaptics().ifPresent((haptics) -> {
            vibrationGroup.option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.hd_haptics")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.hd_haptics.tooltip")}).build()).binding(((HDHapticComponent.Config)haptics.defObj()).enabled, () -> {
               return ((HDHapticComponent.Config)haptics.confObj()).enabled;
            }, (v) -> {
               ((HDHapticComponent.Config)haptics.confObj()).enabled = v;
            }).controller(TickBoxControllerBuilder::create).build());
         });
         Iterator var9 = RumbleSource.values().iterator();

         while(var9.hasNext()) {
            RumbleSource source = (RumbleSource)var9.next();
            dev.isxander.yacl3.api.Option.Builder var10000 = Option.createBuilder();
            String var10001 = source.id().method_12836();
            var10000 = var10000.name(class_2561.method_43471("controlify.vibration_strength." + var10001 + "." + source.id().method_12832()));
            dev.isxander.yacl3.api.OptionDescription.Builder var12 = OptionDescription.createBuilder();
            class_2561[] var10002 = new class_2561[1];
            String var10005 = source.id().method_12836();
            var10002[0] = class_2561.method_43471("controlify.vibration_strength." + var10005 + "." + source.id().method_12832() + ".tooltip");
            Option<Float> option = var10000.description(var12.text(var10002).build()).binding((Float)def.vibrationStrengths.getOrDefault(source.id(), 1.0F), () -> {
               return (Float)config.vibrationStrengths.getOrDefault(source.id(), 1.0F);
            }, (v) -> {
               config.vibrationStrengths.put(source.id(), v);
            }).controller((opt) -> {
               return ((FloatSliderControllerBuilder)((FloatSliderControllerBuilder)FloatSliderControllerBuilder.create(opt).range(0.0F, 2.0F)).step(0.05F)).formatValue(percentOrOffFormatter);
            }).available((Boolean)allowVibrationOption.pendingValue()).build();
            strengthOptions.add(option);
            vibrationGroup.option(option);
         }

         vibrationGroup.option(ButtonOption.createBuilder().name(class_2561.method_43471("controlify.gui.test_vibration")).description(OptionDescription.of(new class_2561[]{class_2561.method_43471("controlify.gui.test_vibration.tooltip")})).action((screen, btn) -> {
            rumble.rumbleManager().play(RumbleSource.MASTER, BasicRumbleEffect.byTime((t) -> {
               return new RumbleState(0.0F, t);
            }, 20).join(BasicRumbleEffect.byTime((t) -> {
               return new RumbleState(0.0F, 1.0F - t);
            }, 20)).repeat(3).join(BasicRumbleEffect.constant(1.0F, 0.0F, 5).join(BasicRumbleEffect.constant(0.0F, 1.0F, 5)).repeat(10)).earlyFinish(BasicRumbleEffect.finishOnScreenChange()));
         }).build());
         return Optional.of(vibrationGroup.build());
      }
   }

   private Optional<OptionGroup> makeGyroGroup(ControllerEntity controller) {
      dev.isxander.yacl3.api.OptionGroup.Builder gyroGroup = OptionGroup.createBuilder().name(class_2561.method_43471("controlify.gui.group.gyro")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.group.gyro.tooltip")}).build());
      Optional<GyroComponent> gyroOpt = controller.gyro();
      if (gyroOpt.isEmpty()) {
         gyroGroup.collapsed(true);
         gyroGroup.option(LabelOption.create(class_2561.method_43471("controlify.gui.group.gyro.no_gyro.tooltip").method_27692(class_124.field_1061)));
         return Optional.of(gyroGroup.build());
      } else {
         GyroComponent.Config config = (GyroComponent.Config)((GyroComponent)gyroOpt.get()).confObj();
         GyroComponent.Config def = (GyroComponent.Config)((GyroComponent)gyroOpt.get()).defObj();
         List<Option<?>> gyroOptions = new ArrayList();
         Option gyroSensitivity;
         gyroGroup.option(gyroSensitivity = Option.createBuilder().name(class_2561.method_43471("controlify.gui.gyro_look_sensitivity")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.gyro_look_sensitivity.tooltip")}).build()).binding(def.lookSensitivity, () -> {
            return config.lookSensitivity;
         }, (v) -> {
            config.lookSensitivity = v;
         }).controller((opt) -> {
            return ((FloatSliderControllerBuilder)((FloatSliderControllerBuilder)FloatSliderControllerBuilder.create(opt).range(0.0F, 3.0F)).step(0.1F)).formatValue(percentOrOffFormatter);
         }).addListener((opt, event) -> {
            gyroOptions.forEach((o) -> {
               o.setAvailable((Float)opt.pendingValue() > 0.0F);
               o.requestSetDefault();
            });
         }).build());
         Option<Boolean> relativeModeOpt = Option.createBuilder().name(class_2561.method_43471("controlify.gui.gyro_behaviour")).description((val) -> {
            return OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.gyro_behaviour.tooltip")}).text(new class_2561[]{val ? class_2561.method_43471("controlify.gui.gyro_behaviour.relative.tooltip") : class_2561.method_43471("controlify.gui.gyro_behaviour.absolute.tooltip")}).build();
         }).binding(def.relativeGyroMode, () -> {
            return config.relativeGyroMode;
         }, (v) -> {
            config.relativeGyroMode = v;
         }).controller((opt) -> {
            return BooleanControllerBuilder.create(opt).formatValue((v) -> {
               return v ? class_2561.method_43471("controlify.gui.gyro_behaviour.relative") : class_2561.method_43471("controlify.gui.gyro_behaviour.absolute");
            });
         }).build();
         gyroGroup.option(relativeModeOpt);
         gyroGroup.option((Option)class_156.method_656(() -> {
            Option<GyroYawMode> option = Option.createBuilder().name(class_2561.method_43471("controlify.gui.gyro_yaw_mode")).description((val) -> {
               return OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.gyro_yaw_mode.tooltip")}).text(new class_2561[]{val == GyroYawMode.YAW ? class_2561.method_43471("controlify.gui.gyro_yaw_mode.tooltip.yaw_only") : class_2561.method_43473()}).text(new class_2561[]{val == GyroYawMode.ROLL ? class_2561.method_43471("controlify.gui.gyro_yaw_mode.tooltip.roll_only") : class_2561.method_43473()}).text(new class_2561[]{val == GyroYawMode.BOTH ? class_2561.method_43471("controlify.gui.gyro_yaw_mode.tooltip.both") : class_2561.method_43473()}).build();
            }).binding(def.yawMode, () -> {
               return config.yawMode;
            }, (v) -> {
               config.yawMode = v;
            }).controller((opt) -> {
               return EnumControllerBuilder.create(opt).enumClass(GyroYawMode.class);
            }).build();
            gyroOptions.add(option);
            return option;
         }));
         gyroGroup.option((Option)class_156.method_656(() -> {
            Option<Boolean> opt = Option.createBuilder().name(class_2561.method_43471("controlify.gui.gyro_invert_x")).description(OptionDescription.of(new class_2561[]{class_2561.method_43471("controlify.gui.gyro_invert_x.tooltip")})).binding(def.invertX, () -> {
               return config.invertX;
            }, (v) -> {
               config.invertX = v;
            }).controller(TickBoxControllerBuilder::create).build();
            gyroOptions.add(opt);
            return opt;
         }));
         gyroGroup.option((Option)class_156.method_656(() -> {
            Option<Boolean> opt = Option.createBuilder().name(class_2561.method_43471("controlify.gui.gyro_invert_y")).description(OptionDescription.of(new class_2561[]{class_2561.method_43471("controlify.gui.gyro_invert_y.tooltip")})).binding(def.invertY, () -> {
               return config.invertY;
            }, (v) -> {
               config.invertY = v;
            }).controller(TickBoxControllerBuilder::create).build();
            gyroOptions.add(opt);
            return opt;
         }));
         gyroGroup.option((Option)class_156.method_656(() -> {
            Option<GyroButtonMode> opt = Option.createBuilder().name(class_2561.method_43471("controlify.gui.gyro_requires_button")).description((val) -> {
               return OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.gyro_requires_button.tooltip")}).text(new class_2561[]{val == GyroButtonMode.ON ? class_2561.method_43471("controlify.gui.gyro_requires_button.tooltip.on") : class_2561.method_43473()}).text(new class_2561[]{val == GyroButtonMode.INVERT ? class_2561.method_43471("controlify.gui.gyro_requires_button.tooltip.invert") : class_2561.method_43473()}).text(new class_2561[]{val == GyroButtonMode.TOGGLE ? class_2561.method_43471("controlify.gui.gyro_requires_button.tooltip.toggle") : class_2561.method_43473()}).text(new class_2561[]{val == GyroButtonMode.OFF ? class_2561.method_43471("controlify.gui.gyro_requires_button.tooltip.off") : class_2561.method_43473()}).build();
            }).binding(def.requiresButton, () -> {
               return config.requiresButton;
            }, (v) -> {
               config.requiresButton = v;
            }).controller((controllerOpt) -> {
               return EnumControllerBuilder.create(controllerOpt).enumClass(GyroButtonMode.class);
            }).available((Float)gyroSensitivity.pendingValue() > 0.0F).build();
            gyroOptions.add(opt);
            return opt;
         }));
         gyroGroup.option((Option)class_156.method_656(() -> {
            Option<Boolean> opt = Option.createBuilder().name(class_2561.method_43471("controlify.gui.flick_stick")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.flick_stick.tooltip")}).build()).binding(def.flickStick, () -> {
               return config.flickStick;
            }, (v) -> {
               config.flickStick = v;
            }).controller(TickBoxControllerBuilder::create).available((Float)gyroSensitivity.pendingValue() > 0.0F).build();
            gyroOptions.add(opt);
            return opt;
         }));
         return Optional.of(gyroGroup.build());
      }
   }

   private Optional<ConfigCategory> makeBindsCategory(ControllerEntity controller) {
      Optional<InputComponent> inputOpt = controller.input();
      if (inputOpt.isEmpty()) {
         return Optional.empty();
      } else {
         InputComponent input = (InputComponent)inputOpt.get();
         dev.isxander.yacl3.api.ConfigCategory.Builder category = ConfigCategory.createBuilder().name(class_2561.method_43471("controlify.gui.group.controls"));
         InputComponent.Config config = (InputComponent.Config)input.confObj();
         InputComponent.Config def = (InputComponent.Config)input.defObj();
         List<ControllerConfigScreenFactory.OptionBindPair> optionBinds = new ArrayList();
         ButtonOption editRadialButton = ButtonOption.createBuilder().name(class_2561.method_43471("controlify.gui.radial_menu").method_27692(class_124.field_1065)).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.radial_menu.tooltip")}).text(new class_2561[]{newOptionLabel}).build()).action((screen, opt) -> {
            class_310.method_1551().method_1507(new RadialMenuScreen(controller, (InputBinding)null, RadialItems.createBindings(controller), class_2561.method_43473(), new RadialItems.BindingEditMode(controller), screen));
         }).text(class_2561.method_43471("controlify.gui.radial_menu.btn_text")).build();
         Option<?> radialBind = createBindingOpt(ControlifyBindings.RADIAL_MENU, controller).addListener((opt, val) -> {
            this.updateConflictingBinds(optionBinds);
         }).build();
         optionBinds.add(new ControllerConfigScreenFactory.OptionBindPair(radialBind, ControlifyBindings.RADIAL_MENU.on(controller)));
         category.option(editRadialButton);
         category.option(radialBind);
         category.option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.radial_menu.btn_focus_timeout")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.radial_menu.btn_focus_timeout.tooltip")}).build()).binding(def.radialButtonFocusTimeoutTicks, () -> {
            return config.radialButtonFocusTimeoutTicks;
         }, (v) -> {
            config.radialButtonFocusTimeoutTicks = v;
         }).controller((opt) -> {
            return ((IntegerSliderControllerBuilder)((IntegerSliderControllerBuilder)IntegerSliderControllerBuilder.create(opt).range(2, 40)).step(1)).formatValue(ticksToMillisFormatter);
         }).build());
         groupBindings(input.getAllBindings()).forEach((categoryName, bindGroup) -> {
            dev.isxander.yacl3.api.OptionGroup.Builder controlsGroup = OptionGroup.createBuilder().name(categoryName);
            controlsGroup.options(bindGroup.stream().flatMap((binding) -> {
               if (binding != ControlifyBindings.RADIAL_MENU.on(controller)) {
                  dev.isxander.yacl3.api.Option.Builder<?> option = createBindingOpt(binding, controller).addListener((opt, val) -> {
                     this.updateConflictingBinds(optionBinds);
                  });
                  Option<?> built = option.build();
                  optionBinds.add(new ControllerConfigScreenFactory.OptionBindPair(built, binding));
                  return Stream.of(built);
               } else {
                  return Stream.empty();
               }
            }).toList());
            category.group(controlsGroup.build());
         });
         this.updateConflictingBinds(optionBinds);
         category.option(ButtonOption.createBuilder().name(class_2561.method_43471("controlify.gui.reset_all_binds")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.reset_all_binds.tooltip")}).build()).action((screen, opt) -> {
            Iterator var3 = optionBinds.iterator();

            while(var3.hasNext()) {
               ControllerConfigScreenFactory.OptionBindPair pair = (ControllerConfigScreenFactory.OptionBindPair)var3.next();
               pair.option().requestSet(pair.binding.defaultInput());
            }

         }).build());
         return Optional.of(category.build());
      }
   }

   private void updateConflictingBinds(List<ControllerConfigScreenFactory.OptionBindPair> all) {
      all.forEach((pair) -> {
         ((BindController)pair.option().controller()).setConflicting(false);
      });
      Iterator var2 = all.iterator();

      while(var2.hasNext()) {
         ControllerConfigScreenFactory.OptionBindPair opt = (ControllerConfigScreenFactory.OptionBindPair)var2.next();
         Set<BindContext> ctxs = opt.binding().contexts();
         List<ControllerConfigScreenFactory.OptionBindPair> conflicting = all.stream().filter((pair) -> {
            return pair.binding() != opt.binding();
         }).filter((pair) -> {
            Stream var10000 = pair.binding().contexts().stream();
            Objects.requireNonNull(ctxs);
            boolean contextsMatch = var10000.anyMatch(ctxs::contains);
            boolean bindMatches = pair.option().pendingValue().equals(opt.option().pendingValue());
            boolean bindIsNotEmpty = !(pair.option().pendingValue() instanceof EmptyInput);
            return contextsMatch && bindMatches && bindIsNotEmpty;
         }).toList();
         conflicting.forEach((conflict) -> {
            ((BindController)conflict.option().controller()).setConflicting(true);
         });
      }

   }

   private static Map<class_2561, List<InputBinding>> groupBindings(Collection<InputBinding> bindings) {
      return (Map)bindings.stream().collect(Collectors.groupingBy(InputBinding::category, LinkedHashMap::new, Collectors.toList()));
   }

   private static dev.isxander.yacl3.api.Option.Builder<Input> createBindingOpt(InputBindingSupplier bindingSupplier, ControllerEntity controller) {
      return createBindingOpt(bindingSupplier.on(controller), controller);
   }

   private static dev.isxander.yacl3.api.Option.Builder<Input> createBindingOpt(InputBinding binding, ControllerEntity controller) {
      dev.isxander.yacl3.api.Option.Builder var10000 = Option.createBuilder().name(binding.name()).description((v) -> {
         return OptionDescription.createBuilder().text(new class_2561[]{binding.description()}).text(new class_2561[]{class_2561.method_43469("controlify.gui.bind.currently_bound_to", new Object[]{class_2561.method_43473().method_10852(Controlify.instance().inputFontMapper().getComponentFromInputs(controller.info().type().namespace(), v.getRelevantInputs())).method_10852(class_5244.field_41874).method_10852(Inputs.getInputComponentAnd(v.getRelevantInputs()))})}).text(new class_2561[]{v.equals(binding.defaultInput()) ? class_2561.method_43473() : class_2561.method_43469("controlify.gui.bind.default_bound_to", new Object[]{class_2561.method_43473().method_10852(Controlify.instance().inputFontMapper().getComponentFromInputs(controller.info().type().namespace(), binding.defaultInput().getRelevantInputs())).method_10852(class_5244.field_41874).method_10852(Inputs.getInputComponentAnd(binding.defaultInput().getRelevantInputs()))})}).build();
      });
      EmptyInput var10001 = EmptyInput.INSTANCE;
      Objects.requireNonNull(binding);
      Supplier var10002 = binding::boundInput;
      Objects.requireNonNull(binding);
      return var10000.binding(var10001, var10002, binding::setBoundInput).customController((opt) -> {
         return new BindController(opt, controller);
      });
   }

   private static class_2960 screenshot(String filename) {
      return CUtil.rl("textures/screenshots/" + filename);
   }

   static {
      newOptionLabel = class_2561.method_43471("controlify.gui.new_options.label").method_27692(class_124.field_1065);
      ticksToMillisFormatter = (v) -> {
         return class_2561.method_43470(String.format("%03dms", v * 50));
      };
   }

   private static enum OnScreenKeyboardMode implements NameableEnum {
      OFF("controlify.gui.show_keyboard.off"),
      CONTROLIFY("controlify.gui.show_keyboard.controlify"),
      SYSTEM("controlify.gui.show_keyboard.system");

      private final class_2561 displayName;

      public static ControllerConfigScreenFactory.OnScreenKeyboardMode[] valuesForController(ControllerEntity controller) {
         return controller.nativeKeyboard().isPresent() ? new ControllerConfigScreenFactory.OnScreenKeyboardMode[]{OFF, CONTROLIFY, SYSTEM} : new ControllerConfigScreenFactory.OnScreenKeyboardMode[]{OFF, CONTROLIFY};
      }

      public static ControllerConfigScreenFactory.OnScreenKeyboardMode getForController(ControllerEntity controller) {
         if (((GenericControllerConfig)controller.genericConfig().config()).showOnScreenKeyboard) {
            return (Boolean)controller.nativeKeyboard().map((nativeKeyboard) -> {
               return ((NativeKeyboardComponent.Config)nativeKeyboard.confObj()).useNativeKeyboard;
            }).orElse(false) ? SYSTEM : CONTROLIFY;
         } else {
            return OFF;
         }
      }

      public void setForController(ControllerEntity controller) {
         GenericControllerConfig genericConfig = (GenericControllerConfig)controller.genericConfig().config();
         Optional<NativeKeyboardComponent.Config> nativeConfig = controller.nativeKeyboard().map(ConfigHolder::confObj);
         switch(this.ordinal()) {
         case 0:
            genericConfig.showOnScreenKeyboard = false;
            nativeConfig.ifPresent((config) -> {
               config.useNativeKeyboard = false;
            });
            break;
         case 1:
            genericConfig.showOnScreenKeyboard = true;
            nativeConfig.ifPresent((config) -> {
               config.useNativeKeyboard = false;
            });
            break;
         case 2:
            genericConfig.showOnScreenKeyboard = true;
            nativeConfig.ifPresent((config) -> {
               config.useNativeKeyboard = true;
            });
         }

      }

      private OnScreenKeyboardMode(String key) {
         this.displayName = class_2561.method_43471(key);
      }

      public class_2561 getDisplayName() {
         return this.displayName;
      }

      // $FF: synthetic method
      private static ControllerConfigScreenFactory.OnScreenKeyboardMode[] $values() {
         return new ControllerConfigScreenFactory.OnScreenKeyboardMode[]{OFF, CONTROLIFY, SYSTEM};
      }
   }

   private static record OptionBindPair(Option<?> option, InputBinding binding) {
      private OptionBindPair(Option<?> option, InputBinding binding) {
         this.option = option;
         this.binding = binding;
      }

      public Option<?> option() {
         return this.option;
      }

      public InputBinding binding() {
         return this.binding;
      }
   }
}
