package dev.isxander.controlify.bindings;

import dev.isxander.controlify.api.bind.RadialIcon;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.render.Blit;
import dev.isxander.controlify.utils.render.CGuiPose;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Queue;
import net.minecraft.class_1058;
import net.minecraft.class_1291;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_4074;
import net.minecraft.class_5321;
import net.minecraft.class_6880;
import net.minecraft.class_7923;

public final class RadialIcons {
   private static final class_310 minecraft = class_310.method_1551();
   public static final class_2960 EMPTY = CUtil.rl("empty");
   public static final class_2960 FABRIC_ICON = class_2960.method_60655("fabric-resource-loader-v0", "icon.png");
   private static Map<class_2960, RadialIcon> icons = null;
   private static Queue<Runnable> deferredRegistrations = new ArrayDeque();

   public static Map<class_2960, RadialIcon> getIcons() {
      if (icons == null) {
         icons = registerIcons();
         deferredRegistrations.forEach(Runnable::run);
         deferredRegistrations = null;
      }

      return icons;
   }

   public static void registerIcon(class_2960 location, RadialIcon icon) {
      if (icons == null) {
         deferredRegistrations.add(() -> {
            registerIcon(location, icon);
         });
      } else {
         icons.put(location, icon);
      }
   }

   public static class_2960 getItem(class_1792 item) {
      return class_7923.field_41178.method_10221(item).method_45138("item/");
   }

   public static class_2960 getEffect(class_6880<class_1291> effect) {
      return class_7923.field_41174.method_10221((class_1291)effect.comp_349()).method_45138("effect/");
   }

   private static void addItems(Map<class_2960, RadialIcon> map) {
      class_7923.field_41178.method_29722().forEach((entry) -> {
         class_5321<class_1792> key = (class_5321)entry.getKey();
         class_1799 stack = ((class_1792)entry.getValue()).method_7854();
         map.put(key.method_29177().method_45138("item/"), (graphics, x, y, tickDelta) -> {
            graphics.method_51427(stack, x, y);
         });
      });
   }

   private static void addPotionEffects(Map<class_2960, RadialIcon> map) {
      class_4074 mobEffectTextureManager = minecraft.method_18505();
      class_7923.field_41174.method_29722().forEach((entry) -> {
         class_5321<class_1291> key = (class_5321)entry.getKey();
         class_6880<class_1291> effect = class_7923.field_41174.method_47983((class_1291)entry.getValue());
         boolean render = true;
         class_1058 sprite = mobEffectTextureManager.method_18663(effect);
         if (sprite == null || sprite.method_45852() == null) {
            render = false;
         }

         if (render) {
            map.put(key.method_29177().method_45138("effect/"), (graphics, x, y, tickDelta) -> {
               CGuiPose pose = CGuiPose.ofPush(graphics);
               pose.translate((float)x, (float)y);
               pose.scale(0.88F, 0.88F);
               Blit.sprite(graphics, sprite, 0, 0, 18, 18, -1);
               pose.pop();
            });
         }

      });
   }

   private static Map<class_2960, RadialIcon> registerIcons() {
      Map<class_2960, RadialIcon> map = new Object2ObjectOpenHashMap();
      map.put(EMPTY, (graphics, x, y, tickDelta) -> {
      });
      map.put(FABRIC_ICON, (graphics, x, y, tickDelta) -> {
         CGuiPose pose = CGuiPose.ofPush(graphics);
         pose.translate((float)x, (float)y);
         pose.scale(0.5F, 0.5F);
         Blit.tex(graphics, FABRIC_ICON, 0, 0, 0, 0, 32, 32, 32, 32);
         pose.pop();
      });
      addItems(map);
      addPotionEffects(map);
      return map;
   }
}
