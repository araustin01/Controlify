package dev.isxander.controlify.gui.screen;

import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.api.bind.RadialIcon;
import dev.isxander.controlify.bindings.RadialIcons;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.DebugOverlayHelper;
import dev.isxander.controlify.utils.render.CGuiPose;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import net.minecraft.class_1294;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1934;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_481;
import net.minecraft.class_748;

public final class RadialItems {
   public static final RadialMenuScreen.RadialItem EMPTY_ACTION;

   public static RadialMenuScreen.RadialItem[] createBindings(ControllerEntity controller) {
      RadialMenuScreen.RadialItem[] items = new RadialMenuScreen.RadialItem[8];

      for(int i = 0; i < 8; ++i) {
         class_2960 bindingId = ((InputComponent.Config)((InputComponent)controller.input().orElseThrow()).confObj()).radialActions[i];
         items[i] = getItemForBinding(bindingId, controller);
      }

      return items;
   }

   public static RadialMenuScreen.RadialItem[] createGameModes() {
      RadialMenuScreen.RadialItem[] items = new RadialMenuScreen.RadialItem[]{new RadialItems.GameModeItem(class_1934.field_9220), new RadialItems.GameModeItem(class_1934.field_9215), new RadialItems.GameModeItem(class_1934.field_9216), new RadialItems.GameModeItem(class_1934.field_9219)};
      return items;
   }

   public static RadialMenuScreen.RadialItem[] createHotbarSave() {
      class_310 mc = class_310.method_1551();
      RadialMenuScreen.RadialItem[] items = new RadialMenuScreen.RadialItem[9];

      for(int i = 0; i < 9; ++i) {
         items[i] = new RadialItems.RadialItemRecord(class_2561.method_43469("controlify.radial.hotbar", new Object[]{class_2561.method_43470(Integer.toString(i + 1))}), getIconForHotbar(i, true), () -> {
            class_481.method_2462(mc, i, false, true);
            return true;
         }, CUtil.rl("hotbar_save/" + i));
      }

      return items;
   }

   public static RadialMenuScreen.RadialItem[] createHotbarLoad() {
      class_310 mc = class_310.method_1551();
      RadialMenuScreen.RadialItem[] items = new RadialMenuScreen.RadialItem[9];

      for(int i = 0; i < items.length; ++i) {
         items[i] = new RadialItems.RadialItemRecord(class_2561.method_43469("controlify.radial.hotbar", new Object[]{class_2561.method_43470(Integer.toString(i + 1))}), getIconForHotbar(i, true), () -> {
            class_481.method_2462(mc, i, true, false);
            return true;
         }, CUtil.rl("hotbar_load/" + i));
      }

      return items;
   }

   public static RadialMenuScreen.RadialItem[] createHotbarItemSelect() {
      class_310 mc = class_310.method_1551();
      RadialMenuScreen.RadialItem[] items = new RadialMenuScreen.RadialItem[9];

      for(int i = 0; i < items.length; ++i) {
         items[i] = new RadialItems.RadialItemRecord(class_2561.method_43469("controlify.radial.hotbar", new Object[]{class_2561.method_43470(Integer.toString(i + 1))}), (graphics, x, y, tickDelta) -> {
            graphics.method_51427(mc.field_1724.method_31548().method_5438(i), x, y);
         }, () -> {
            mc.field_1724.method_31548().method_61496(i);
            return true;
         }, CUtil.rl("hotbar_item_select/" + i));
      }

      return items;
   }

   public static RadialMenuScreen.RadialItem[] createDebug() {
      RadialMenuScreen.RadialItem[] items = new RadialMenuScreen.RadialItem[]{new RadialItems.RadialItemRecord(class_2561.method_43471("controlify.radial.debug.reload_chunks"), (RadialIcon)RadialIcons.getIcons().get(RadialIcons.getItem(class_1802.field_8251)), () -> {
         DebugOverlayHelper.reloadChunks();
         return true;
      }, CUtil.rl("debug/reload_chunks")), new RadialItems.RadialItemRecord(class_2561.method_43471("controlify.radial.debug.chunk_borders"), (RadialIcon)RadialIcons.getIcons().get(RadialIcons.getItem(class_1802.field_8204)), () -> {
         DebugOverlayHelper.toggleChunkBorders();
         return true;
      }, CUtil.rl("debug/chunk_borders")), new RadialItems.RadialItemRecord(class_2561.method_43471("controlify.radial.debug.advanced_tooltips"), (RadialIcon)RadialIcons.getIcons().get(RadialIcons.getItem(class_1802.field_8674)), () -> {
         DebugOverlayHelper.toggleAdvancedTooltips();
         return true;
      }, CUtil.rl("debug/advanced_tooltips")), new RadialItems.RadialItemRecord(class_2561.method_43471("controlify.radial.debug.entity_hitboxes"), (RadialIcon)RadialIcons.getIcons().get(RadialIcons.getItem(class_1802.field_8470)), () -> {
         DebugOverlayHelper.toggleEntityHitboxes();
         return true;
      }, CUtil.rl("debug/entity_hitboxes")), new RadialItems.RadialItemRecord(class_2561.method_43471("controlify.radial.debug.reload_packs"), (RadialIcon)RadialIcons.getIcons().get(RadialIcons.getItem(class_1802.field_8330)), () -> {
         DebugOverlayHelper.reloadResourcePacks();
         return true;
      }, CUtil.rl("debug/reload_packs")), new RadialItems.RadialItemRecord(class_2561.method_43471("controlify.radial.debug.clear_chat"), (RadialIcon)RadialIcons.getIcons().get(RadialIcons.getItem(class_1802.field_8884)), () -> {
         DebugOverlayHelper.clearChat();
         return true;
      }, CUtil.rl("debug/clear_chat")), new RadialItems.RadialItemRecord(class_2561.method_43471("controlify.radial.debug.profile"), (RadialIcon)RadialIcons.getIcons().get(RadialIcons.getItem(class_1802.field_8238)), () -> {
         DebugOverlayHelper.startStopProfiling();
         return true;
      }, CUtil.rl("debug/profile"))};
      boolean overlayEnabled = DebugOverlayHelper.isOverlayEnabled();
      RadialMenuScreen.RadialItem[] overlayItems = !overlayEnabled ? new RadialMenuScreen.RadialItem[]{new RadialItems.RadialItemRecord(class_2561.method_43471("controlify.radial.debug.overlay"), (RadialIcon)RadialIcons.getIcons().get(RadialIcons.getItem(class_1802.field_8688)), () -> {
         DebugOverlayHelper.toggleOverlay();
         return true;
      }, CUtil.rl("debug/overlay")), new RadialItems.RadialItemRecord(class_2561.method_43471("controlify.radial.debug.overlay_fps"), (RadialIcon)RadialIcons.getIcons().get(RadialIcons.getItem(class_1802.field_8557)), () -> {
         DebugOverlayHelper.toggleFpsOverlay();
         return true;
      }, CUtil.rl("debug/fps")), new RadialItems.RadialItemRecord(class_2561.method_43471("controlify.radial.debug.overlay_net"), (RadialIcon)RadialIcons.getIcons().get(RadialIcons.getItem(class_1802.field_28101)), () -> {
         DebugOverlayHelper.toggleNetworkOverlay();
         return true;
      }, CUtil.rl("debug/fps")), new RadialItems.RadialItemRecord(class_2561.method_43471("controlify.radial.debug.overlay_prof"), (RadialIcon)RadialIcons.getIcons().get(RadialIcons.getItem(class_1802.field_22420)), () -> {
         DebugOverlayHelper.toggleProfilerOverlay();
         return true;
      }, CUtil.rl("debug/fps"))} : new RadialMenuScreen.RadialItem[]{new RadialItems.RadialItemRecord(class_2561.method_43471("controlify.radial.debug.hide_overlay"), (RadialIcon)RadialIcons.getIcons().get(RadialIcons.getEffect(class_1294.field_5905)), () -> {
         DebugOverlayHelper.toggleOverlay();
         return true;
      }, CUtil.rl("debug/reload_chunks"))};
      RadialMenuScreen.RadialItem[] allItems = new RadialMenuScreen.RadialItem[items.length + overlayItems.length];
      System.arraycopy(overlayItems, 0, allItems, 0, overlayItems.length);
      System.arraycopy(items, 0, allItems, overlayItems.length, items.length);
      return allItems;
   }

   private static RadialIcon getIconForHotbar(int hotbarIndex, boolean showNumbers) {
      class_310 mc = class_310.method_1551();
      class_748 hotbar = mc.method_1571().method_1410(hotbarIndex);
      List<class_1799> hotbarItems = hotbar.method_56839(mc.field_1724.method_56673());

      for(int i = 0; i < 9; ++i) {
         class_1799 stack = (class_1799)hotbarItems.get(i);
         if (!stack.method_31574(class_1802.field_8162)) {
            return (graphics, x, y, tickDelta) -> {
               graphics.method_51427(stack, x, y);
               if (showNumbers) {
                  CGuiPose pose = CGuiPose.ofPush(graphics);
                  graphics.method_25303(mc.field_1772, Integer.toString(hotbarIndex + 1), x, y, -1);
                  pose.pop();
               }

            };
         }
      }

      return (graphics, x, y, tickDelta) -> {
         if (showNumbers) {
            graphics.method_25303(mc.field_1772, Integer.toString(hotbarIndex + 1), x, y, -1);
         }

      };
   }

   private static RadialMenuScreen.RadialItem getItemForBinding(class_2960 id, ControllerEntity controller) {
      InputBinding binding = ((InputComponent)controller.input().orElseThrow()).getBinding(id);
      if (binding != null && !binding.radialIcon().isEmpty()) {
         RadialIcon icon = (RadialIcon)RadialIcons.getIcons().get(binding.radialIcon().get());
         return new RadialItems.RadialItemRecord(binding.name(), icon, () -> {
            binding.fakePress();
            return true;
         }, id);
      } else {
         CUtil.LOGGER.warn("Binding {} does not exist or is not a radial candidate", binding);
         return EMPTY_ACTION;
      }
   }

   static {
      EMPTY_ACTION = new RadialItems.RadialItemRecord(class_2561.method_43473(), RadialIcon.EMPTY, () -> {
         return false;
      }, RadialIcons.EMPTY);
   }

   private static class GameModeItem implements RadialMenuScreen.RadialItem {
      private final class_1934 gameType;
      private final class_2561 name;
      private final RadialIcon icon;
      private final String command;

      public GameModeItem(class_1934 gameType) {
         this.gameType = gameType;
         this.name = gameType.method_32763();
         class_2960 var10000;
         switch(gameType) {
         case field_9220:
            var10000 = RadialIcons.getItem(class_1802.field_8270);
            break;
         case field_9215:
            var10000 = RadialIcons.getItem(class_1802.field_8371);
            break;
         case field_9216:
            var10000 = RadialIcons.getItem(class_1802.field_8895);
            break;
         case field_9219:
            var10000 = RadialIcons.getItem(class_1802.field_8449);
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
         }

         class_2960 iconId = var10000;
         this.icon = (RadialIcon)RadialIcons.getIcons().get(iconId);
         String var10001;
         switch(gameType) {
         case field_9220:
            var10001 = "gamemode creative";
            break;
         case field_9215:
            var10001 = "gamemode survival";
            break;
         case field_9216:
            var10001 = "gamemode adventure";
            break;
         case field_9219:
            var10001 = "gamemode spectator";
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
         }

         this.command = var10001;
      }

      public class_2561 name() {
         return this.name;
      }

      public RadialIcon icon() {
         return this.icon;
      }

      public boolean playAction() {
         class_310 client = class_310.method_1551();
         if (client.field_1761 != null && client.field_1724 != null && client.field_1724.method_64475(2) && client.field_1761.method_2920() != this.gameType) {
            client.field_1724.field_3944.method_45730(this.command);
            return true;
         } else {
            return false;
         }
      }
   }

   private static record RadialItemRecord(class_2561 name, RadialIcon icon, Supplier<Boolean> action, class_2960 id) implements RadialMenuScreen.RadialItem {
      private RadialItemRecord(class_2561 name, RadialIcon icon, Supplier<Boolean> action, class_2960 id) {
         this.name = name;
         this.icon = icon;
         this.action = action;
         this.id = id;
      }

      public boolean playAction() {
         return (Boolean)this.action.get();
      }

      public boolean equals(Object obj) {
         if (this == obj) {
            return true;
         } else if (obj instanceof RadialItems.RadialItemRecord) {
            RadialItems.RadialItemRecord record = (RadialItems.RadialItemRecord)obj;
            return this.id.equals(record.id);
         } else {
            return false;
         }
      }

      public class_2561 name() {
         return this.name;
      }

      public RadialIcon icon() {
         return this.icon;
      }

      public Supplier<Boolean> action() {
         return this.action;
      }

      public class_2960 id() {
         return this.id;
      }
   }

   public static class BindingEditMode implements RadialMenuScreen.EditMode {
      private final ControllerEntity controller;

      public BindingEditMode(ControllerEntity controller) {
         this.controller = controller;
      }

      public void setRadialItem(int index, RadialMenuScreen.RadialItem item) {
         ((InputComponent.Config)((InputComponent)this.controller.input().orElseThrow()).confObj()).radialActions[index] = ((RadialItems.RadialItemRecord)item).id();
      }

      public List<RadialMenuScreen.RadialItem> getEditCandidates() {
         List<RadialMenuScreen.RadialItem> items = new ArrayList();
         ((InputComponent)this.controller.input().orElseThrow()).getAllBindings().forEach((binding) -> {
            binding.radialIcon().ifPresent((icon) -> {
               items.add(new RadialItems.RadialItemRecord(binding.name(), (RadialIcon)RadialIcons.getIcons().get(icon), () -> {
                  return false;
               }, binding.id()));
            });
         });
         return items;
      }
   }
}
