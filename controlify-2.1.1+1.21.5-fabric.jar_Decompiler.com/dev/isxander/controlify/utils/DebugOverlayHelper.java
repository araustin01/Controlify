package dev.isxander.controlify.utils;

import dev.isxander.controlify.mixins.feature.input.DebugScreenOverlayAccessor;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_5244;

public final class DebugOverlayHelper {
   private static final class_310 mc = class_310.method_1551();

   public static boolean isOverlayEnabled() {
      return ((DebugScreenOverlayAccessor)mc.method_53526()).isRenderDebug();
   }

   public static void toggleOverlay() {
      mc.method_53526().method_53539();
   }

   public static void toggleFpsOverlay() {
      mc.method_53526().method_53541();
   }

   public static void toggleNetworkOverlay() {
      mc.method_53526().method_53540();
   }

   public static void toggleProfilerOverlay() {
      mc.method_53526().method_53542();
   }

   public static void reloadChunks() {
      mc.field_1769.method_3279();
      debugFeedbackTranslated("debug.reload_chunks.message");
   }

   public static void toggleChunkBorders() {
      boolean flag = mc.field_1709.method_3713();
      debugFeedbackTranslated(flag ? "debug.chunk_boundaries.on" : "debug.chunk_boundaries.off");
   }

   public static void toggleAdvancedTooltips() {
      boolean enabled = mc.field_1690.field_1827 = !mc.field_1690.field_1827;
      mc.field_1690.method_1640();
      debugFeedbackTranslated(enabled ? "debug.advanced_tooltips.on" : "debug.advanced_tooltips.off");
   }

   public static void toggleEntityHitboxes() {
      boolean flag = !mc.method_1561().method_3958();
      mc.method_1561().method_3955(flag);
      debugFeedbackTranslated(flag ? "debug.show_hitboxes.on" : "debug.show_hitboxes.off");
   }

   public static void reloadResourcePacks() {
      debugFeedbackTranslated("debug.reload_resourcepacks.message");
      mc.method_1521();
   }

   public static void startStopProfiling() {
      if (mc.method_34745(DebugOverlayHelper::debugFeedbackComponent)) {
         debugFeedbackTranslated("debug.profiling.start", 10);
      }

   }

   public static void clearChat() {
      mc.field_1705.method_1743().method_1808(false);
   }

   private static void debugComponent(class_124 formatting, class_2561 message) {
      mc.field_1705.method_1743().method_1812(class_2561.method_43473().method_10852(class_2561.method_43471("debug.prefix").method_27695(new class_124[]{formatting, class_124.field_1067})).method_10852(class_5244.field_41874).method_10852(message));
   }

   private static void debugFeedbackComponent(class_2561 message) {
      debugComponent(class_124.field_1054, message);
   }

   private static void debugFeedbackTranslated(String message, Object... args) {
      debugFeedbackComponent(class_2561.method_43469(message, args));
   }
}
