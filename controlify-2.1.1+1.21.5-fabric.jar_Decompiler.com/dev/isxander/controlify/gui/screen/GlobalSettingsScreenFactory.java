package dev.isxander.controlify.gui.screen;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.config.GlobalSettings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.driver.sdl.SDL3NativesManager;
import dev.isxander.controlify.driver.steamdeck.SteamDeckUtil;
import dev.isxander.controlify.gui.controllers.FormattableStringController;
import dev.isxander.controlify.reacharound.ReachAroundMode;
import dev.isxander.controlify.server.ServerPolicies;
import dev.isxander.controlify.server.ServerPolicy;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.DebugDump;
import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.ListOption;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionFlag;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.EnumControllerBuilder;
import dev.isxander.yacl3.api.controller.FloatSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import java.util.concurrent.atomic.AtomicReference;
import net.minecraft.class_124;
import net.minecraft.class_156;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_437;
import net.minecraft.class_5244;
import net.minecraft.class_642;

public class GlobalSettingsScreenFactory {
   public static class_437 createGlobalSettingsScreen(class_437 parent) {
      GlobalSettings globalSettings = Controlify.instance().config().globalSettings();
      AtomicReference<ListOption<String>> whitelist = new AtomicReference();
      boolean is12106OrLater = false;
      return YetAnotherConfigLib.createBuilder().title(class_2561.method_43471("controlify.gui.global_settings.title")).save(() -> {
         Controlify.instance().config().save();
      }).category(ConfigCategory.createBuilder().name(class_2561.method_43471("controlify.gui.global_settings.title")).option(ButtonOption.createBuilder().name(class_2561.method_43471("controlify.gui.open_issue_tracker")).action((screen, button) -> {
         class_156.method_668().method_670("https://github.com/isxander/controlify/issues");
      }).build()).group(OptionGroup.createBuilder().name(class_2561.method_43471("controlify.gui.natives")).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.load_vibration_natives")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.load_vibration_natives.tooltip")}).text(new class_2561[]{class_2561.method_43471("controlify.gui.load_vibration_natives.tooltip.warning").method_27692(class_124.field_1061)}).build()).binding(true, () -> {
         return globalSettings.loadVibrationNatives;
      }, (v) -> {
         globalSettings.loadVibrationNatives = v;
      }).controller((opt) -> {
         return BooleanControllerBuilder.create(opt).yesNoFormatter();
      }).flag(new OptionFlag[]{OptionFlag.GAME_RESTART}).available(SDL3NativesManager.isSupportedOnThisPlatform()).build()).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.custom_natives_path")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.custom_natives_path.tooltip")}).text(new class_2561[]{class_2561.method_43471("controlify.gui.custom_natives_path.tooltip.warning").method_27692(class_124.field_1061)}).build()).binding("", () -> {
         return globalSettings.customVibrationNativesPath;
      }, (v) -> {
         globalSettings.customVibrationNativesPath = v;
      }).customController((opt) -> {
         return new FormattableStringController(opt, (s) -> {
            return s.isEmpty() ? class_2561.method_43471("controlify.gui.custom_natives_path.none") : class_2561.method_43470(s);
         });
      }).build()).build()).group(OptionGroup.createBuilder().name(class_2561.method_43471("controlify.gui.server_options")).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.reach_around")).description((state) -> {
         return OptionDescription.createBuilder().webpImage(screenshot("reach-around-placement.webp")).text(new class_2561[]{class_2561.method_43471("controlify.gui.reach_around.tooltip")}).text(new class_2561[]{class_2561.method_43471("controlify.gui.reach_around.tooltip.parity").method_27692(class_124.field_1080)}).text(new class_2561[]{state == ReachAroundMode.EVERYWHERE ? class_2561.method_43471("controlify.gui.reach_around.tooltip.warning").method_27692(class_124.field_1061) : class_2561.method_43473()}).text(new class_2561[]{ServerPolicies.REACH_AROUND.getPolicy() == ServerPolicy.DISALLOWED ? class_2561.method_43471("controlify.gui.server_controlled").method_27692(class_124.field_1065) : class_2561.method_43473()}).build();
      }).binding(GlobalSettings.DEFAULT.reachAround, () -> {
         return globalSettings.reachAround;
      }, (v) -> {
         globalSettings.reachAround = v;
      }).controller((opt) -> {
         return EnumControllerBuilder.create(opt).enumClass(ReachAroundMode.class).formatValue((mode) -> {
            class_2561 var10000;
            switch(ServerPolicies.REACH_AROUND.getPolicy()) {
            case UNSET:
            case ALLOWED:
               var10000 = mode.getDisplayName();
               break;
            case DISALLOWED:
               var10000 = class_5244.field_24333;
               break;
            default:
               throw new MatchException((String)null, (Throwable)null);
            }

            return var10000;
         });
      }).available(ServerPolicies.REACH_AROUND.get()).build()).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.allow_server_rumble")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.allow_server_rumble.tooltip")}).build()).binding(GlobalSettings.DEFAULT.allowServerRumble, () -> {
         return globalSettings.allowServerRumble;
      }, (v) -> {
         globalSettings.allowServerRumble = v;
      }).controller(TickBoxControllerBuilder::create).addListener((opt, val) -> {
         ControlifyApi.get().getCurrentController().flatMap(ControllerEntity::rumble).ifPresent((rumble) -> {
            rumble.rumbleManager().clearEffects();
         });
      }).build()).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.keyboard_movement")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.keyboard_movement.tooltip")}).build()).binding(GlobalSettings.DEFAULT.alwaysKeyboardMovement, () -> {
         return globalSettings.alwaysKeyboardMovement;
      }, (v) -> {
         globalSettings.alwaysKeyboardMovement = v;
      }).controller(TickBoxControllerBuilder::create).build()).option(ButtonOption.createBuilder().name(class_2561.method_43471("controlify.gui.add_server_to_keyboard_move_whitelist")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.add_server_to_keyboard_move_whitelist.tooltip")}).build()).action((screen, button) -> {
         class_642 server = class_310.method_1551().method_1558();
         if (server != null) {
            ((ListOption)whitelist.get()).insertNewEntry().requestSet(server.field_3761);
         }

      }).available(class_310.method_1551().method_1558() != null).build()).build()).group((OptionGroup)class_156.method_656(() -> {
         ListOption<String> list = ListOption.createBuilder().name(class_2561.method_43471("controlify.gui.keyboard_movement_whitelist")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.keyboard_movement_whitelist.tooltip")}).build()).binding(GlobalSettings.DEFAULT.keyboardMovementWhitelist, () -> {
            return globalSettings.keyboardMovementWhitelist;
         }, (v) -> {
            globalSettings.keyboardMovementWhitelist = v;
         }).controller(StringControllerBuilder::create).initial("Server IP here").build();
         whitelist.set(list);
         return list;
      })).group(OptionGroup.createBuilder().name(class_2561.method_43471("controlify.gui.miscellaneous")).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.ingame_button_guide_scale")).description((val) -> {
         return OptionDescription.createBuilder().text(new class_2561[]{is12106OrLater ? class_2561.method_43470("This setting is currently broken on 1.21.6+").method_27692(class_124.field_1061) : class_2561.method_43473()}).text(new class_2561[]{class_2561.method_43471("controlify.gui.ingame_button_guide_scale.tooltip")}).text(new class_2561[]{val != 1.0F ? class_2561.method_43471("controlify.gui.ingame_button_guide_scale.tooltip.warning").method_27692(class_124.field_1061) : class_2561.method_43473()}).build();
      }).binding(GlobalSettings.DEFAULT.ingameButtonGuideScale, () -> {
         return is12106OrLater ? 1.0F : globalSettings.ingameButtonGuideScale;
      }, (v) -> {
         globalSettings.ingameButtonGuideScale = v;
      }).controller((opt) -> {
         return ((FloatSliderControllerBuilder)((FloatSliderControllerBuilder)FloatSliderControllerBuilder.create(opt).range(0.5F, 1.5F)).step(0.05F)).formatValue((v) -> {
            return class_2561.method_43470(String.format("%.0f%%", v * 100.0F));
         });
      }).available(!is12106OrLater).build()).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.ui_sounds")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.ui_sounds.tooltip")}).build()).binding(GlobalSettings.DEFAULT.uiSounds, () -> {
         return globalSettings.uiSounds;
      }, (v) -> {
         globalSettings.uiSounds = v;
      }).controller(TickBoxControllerBuilder::create).build()).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.out_of_focus_input")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.out_of_focus_input.tooltip")}).build()).binding(GlobalSettings.DEFAULT.outOfFocusInput, () -> {
         return globalSettings.outOfFocusInput;
      }, (v) -> {
         globalSettings.outOfFocusInput = v;
      }).controller(TickBoxControllerBuilder::create).build()).option(Option.createBuilder().name(class_2561.method_43471("controlify.gui.notify_low_battery")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.notify_low_battery.tooltip")}).build()).binding(GlobalSettings.DEFAULT.notifyLowBattery, () -> {
         return globalSettings.notifyLowBattery;
      }, (v) -> {
         globalSettings.notifyLowBattery = v;
      }).controller(TickBoxControllerBuilder::create).build()).optionIf(SteamDeckUtil.IS_STEAM_DECK, Option.createBuilder().name(class_2561.method_43471("controlify.gui.use_enhanced_steam_deck_driver")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.use_enhanced_steam_deck_driver.tooltip")}).build()).binding(GlobalSettings.DEFAULT.useEnhancedSteamDeckDriver, () -> {
         return globalSettings.useEnhancedSteamDeckDriver;
      }, (v) -> {
         globalSettings.useEnhancedSteamDeckDriver = v;
      }).controller(TickBoxControllerBuilder::create).flag(new OptionFlag[]{OptionFlag.GAME_RESTART}).build()).option(ButtonOption.createBuilder().name(class_2561.method_43471("controlify.gui.copy_debug_dump")).description(OptionDescription.createBuilder().text(new class_2561[]{class_2561.method_43471("controlify.gui.copy_debug_dump.tooltip")}).build()).action((screen, btn) -> {
         String dump = DebugDump.dumpDebug();
         String formatted = "Here's my Controlify debug dump\n```\n%s\n```\n".formatted(new Object[]{dump}).stripIndent();
         class_310.method_1551().field_1774.method_1455(formatted);
      }).build()).build()).build()).build().generateScreen(parent);
   }

   private static class_2960 screenshot(String filename) {
      return CUtil.rl("textures/screenshots/" + filename);
   }
}
