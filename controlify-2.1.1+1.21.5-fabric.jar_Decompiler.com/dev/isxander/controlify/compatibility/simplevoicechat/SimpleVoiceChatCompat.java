package dev.isxander.controlify.compatibility.simplevoicechat;

import de.maxhenkel.voicechat.voice.client.ClientManager;
import de.maxhenkel.voicechat.voice.client.KeyEvents;
import dev.isxander.controlify.api.bind.ControlifyBindApi;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.api.event.ControlifyEvents;
import dev.isxander.controlify.compatibility.simplevoicechat.mixins.KeyEventsAccessor;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.utils.render.Blit;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_5244;

public class SimpleVoiceChatCompat {
   private static InputBindingSupplier pttHoldSupplier;
   private static InputBindingSupplier pttToggleSupplier;
   private static InputBindingSupplier whisperHoldSupplier;
   private static InputBindingSupplier whisperToggleSupplier;
   private static boolean pttDown;
   private static boolean whisperDown;

   public static void init() {
      class_2960 muteIcon = registerIcon16x(class_2960.method_60655("voicechat", "textures/icons/microphone_off.png"));
      class_2960 pttIcon = registerIcon16x(class_2960.method_60655("voicechat", "textures/icons/microphone.png"));
      class_2960 whisperIcon = registerIcon16x(class_2960.method_60655("voicechat", "textures/icons/microphone_whisper.png"));
      class_2561 category = class_2561.method_43471("key.categories.voicechat");
      pttHoldSupplier = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("voicechat", "ptt_hold").category(category).addKeyCorrelation(KeyEvents.KEY_PTT);
      });
      pttToggleSupplier = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("voicechat", "ptt_toggle").category(category).radialCandidate(pttIcon).addKeyCorrelation(KeyEvents.KEY_PTT);
      });
      whisperHoldSupplier = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.name(class_2561.method_43471("key.whisper").method_10852(class_5244.field_41874).method_10852(class_2561.method_43471("controlify.compat.svc.hold"))).id("voicechat", "whisper_hold").category(category).addKeyCorrelation(KeyEvents.KEY_WHISPER);
      });
      whisperToggleSupplier = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.name(class_2561.method_43471("key.whisper").method_10852(class_5244.field_41874).method_10852(class_2561.method_43471("controlify.compat.svc.toggle"))).id("voicechat", "whisper_toggle").category(category).radialCandidate(whisperIcon).addKeyCorrelation(KeyEvents.KEY_WHISPER);
      });
      ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("voicechat", "mute_microphone").category(category).radialCandidate(muteIcon).keyEmulation(KeyEvents.KEY_MUTE);
      });
      ControlifyEvents.ACTIVE_CONTROLLER_TICKED.register((event) -> {
         ControllerEntity controller = event.controller();
         InputBinding pttHold = pttHoldSupplier.on(controller);
         InputBinding pttToggle = pttToggleSupplier.on(controller);
         InputBinding whisperHold = whisperHoldSupplier.on(controller);
         InputBinding whisperToggle = whisperToggleSupplier.on(controller);
         if (pttToggle.justPressed()) {
            pttDown = !pttDown;
            checkConnected();
         }

         if (whisperToggle.justPressed()) {
            whisperDown = !whisperDown;
            checkConnected();
         }

         if (pttHold.justPressed() || whisperHold.justPressed()) {
            checkConnected();
         }

         if (pttHold.digitalNow()) {
            pttDown = true;
         } else if (pttHold.justReleased()) {
            pttDown = false;
         }

         if (whisperHold.digitalNow()) {
            whisperDown = true;
         } else if (whisperHold.justReleased()) {
            whisperDown = false;
         }

         controller.dualSense().ifPresent((ds) -> {
            ds.setMuteLight(ClientManager.getPlayerStateManager().isMuted());
         });
      });
   }

   public static boolean isPTTDown() {
      return pttDown;
   }

   public static boolean isWhisperDown() {
      return whisperDown;
   }

   private static void checkConnected() {
      if (class_310.method_1551().method_18506() == null && class_310.method_1551().field_1755 == null) {
         ((KeyEventsAccessor)ClientManager.instance().getKeyEvents()).invokeCheckConnected();
      }

   }

   private static class_2960 registerIcon16x(class_2960 location) {
      ControlifyBindApi.get().registerRadialIcon(location, (graphics, x, y, tickDelta) -> {
         Blit.tex(graphics, location, x, y, 0.0F, 0.0F, 16, 16, 16, 16);
      });
      return location;
   }
}
