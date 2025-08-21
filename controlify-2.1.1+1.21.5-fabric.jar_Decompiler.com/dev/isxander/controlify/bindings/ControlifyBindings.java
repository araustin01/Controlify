package dev.isxander.controlify.bindings;

import dev.isxander.controlify.api.bind.ControlifyBindApi;
import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.controller.GenericControllerConfig;
import dev.isxander.controlify.platform.client.PlatformClientUtil;
import dev.isxander.controlify.utils.CUtil;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.class_124;
import net.minecraft.class_1294;
import net.minecraft.class_1802;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_315;

public final class ControlifyBindings {
   private static final class_315 options;
   private static final class_2561 MOVEMENT_CATEGORY;
   private static final class_2561 GAMEPLAY_CATEGORY;
   private static final class_2561 INVENTORY_CATEGORY;
   private static final class_2561 CREATIVE_CATEGORY;
   private static final class_2561 MISC_CATEGORY;
   private static final class_2561 DEBUG_CATEGORY;
   private static final class_2561 GUI_CATEGORY;
   private static final class_2561 RADIAL_CATEGORY;
   private static final class_2561 VMOUSE_CATEGORY;
   public static final InputBindingSupplier WALK_FORWARD;
   public static final InputBindingSupplier WALK_BACKWARD;
   public static final InputBindingSupplier WALK_LEFT;
   public static final InputBindingSupplier WALK_RIGHT;
   public static final InputBindingSupplier LOOK_UP;
   public static final InputBindingSupplier LOOK_DOWN;
   public static final InputBindingSupplier LOOK_LEFT;
   public static final InputBindingSupplier LOOK_RIGHT;
   public static final InputBindingSupplier GYRO_BUTTON;
   public static final InputBindingSupplier JUMP;
   public static final InputBindingSupplier SPRINT;
   public static final InputBindingSupplier SNEAK;
   public static final InputBindingSupplier ATTACK;
   public static final InputBindingSupplier USE;
   public static final InputBindingSupplier DROP_INGAME;
   public static final InputBindingSupplier DROP_STACK;
   public static final InputBindingSupplier PAUSE;
   public static final InputBindingSupplier CHANGE_PERSPECTIVE;
   public static final InputBindingSupplier SWAP_HANDS;
   public static final InputBindingSupplier NEXT_SLOT;
   public static final InputBindingSupplier PREV_SLOT;
   public static final InputBindingSupplier HOTBAR_SLOT_SELECT;
   public static final InputBindingSupplier GAME_MODE_SWITCHER;
   public static final InputBindingSupplier INVENTORY;
   public static final InputBindingSupplier INV_SELECT;
   public static final InputBindingSupplier INV_QUICK_MOVE;
   public static final InputBindingSupplier INV_TAKE_HALF;
   public static final InputBindingSupplier DROP_INVENTORY;
   public static final InputBindingSupplier BUNDLE_NAVI_UP;
   public static final InputBindingSupplier BUNDLE_NAVI_DOWN;
   public static final InputBindingSupplier BUNDLE_NAVI_LEFT;
   public static final InputBindingSupplier BUNDLE_NAVI_RIGHT;
   public static final InputBindingSupplier PICK_BLOCK;
   public static final InputBindingSupplier PICK_BLOCK_NBT;
   public static final InputBindingSupplier HOTBAR_LOAD_RADIAL;
   public static final InputBindingSupplier HOTBAR_SAVE_RADIAL;
   public static final InputBindingSupplier OPEN_CHAT;
   public static final InputBindingSupplier TOGGLE_HUD_VISIBILITY;
   public static final InputBindingSupplier SHOW_PLAYER_LIST;
   public static final InputBindingSupplier TAKE_SCREENSHOT;
   public static final InputBindingSupplier DEBUG_RADIAL;
   public static final InputBindingSupplier TOGGLE_DEBUG_MENU;
   public static final InputBindingSupplier TOGGLE_DEBUG_MENU_FPS;
   public static final InputBindingSupplier TOGGLE_DEBUG_MENU_NET;
   public static final InputBindingSupplier TOGGLE_DEBUG_MENU_PROF;
   public static final InputBindingSupplier GUI_PRESS;
   public static final InputBindingSupplier GUI_BACK;
   public static final InputBindingSupplier GUI_NEXT_TAB;
   public static final InputBindingSupplier GUI_PREV_TAB;
   public static final InputBindingSupplier GUI_ABSTRACT_ACTION_1;
   public static final InputBindingSupplier GUI_ABSTRACT_ACTION_2;
   public static final InputBindingSupplier GUI_ABSTRACT_ACTION_3;
   public static final InputBindingSupplier GUI_NAVI_UP;
   public static final InputBindingSupplier GUI_NAVI_DOWN;
   public static final InputBindingSupplier GUI_NAVI_LEFT;
   public static final InputBindingSupplier GUI_NAVI_RIGHT;
   public static final InputBindingSupplier CYCLE_OPT_FORWARD;
   public static final InputBindingSupplier CYCLE_OPT_BACKWARD;
   public static final InputBindingSupplier RADIAL_MENU;
   public static final InputBindingSupplier RADIAL_AXIS_UP;
   public static final InputBindingSupplier RADIAL_AXIS_DOWN;
   public static final InputBindingSupplier RADIAL_AXIS_LEFT;
   public static final InputBindingSupplier RADIAL_AXIS_RIGHT;
   public static final InputBindingSupplier VMOUSE_MOVE_UP;
   public static final InputBindingSupplier VMOUSE_MOVE_DOWN;
   public static final InputBindingSupplier VMOUSE_MOVE_LEFT;
   public static final InputBindingSupplier VMOUSE_MOVE_RIGHT;
   public static final InputBindingSupplier VMOUSE_SNAP_UP;
   public static final InputBindingSupplier VMOUSE_SNAP_DOWN;
   public static final InputBindingSupplier VMOUSE_SNAP_LEFT;
   public static final InputBindingSupplier VMOUSE_SNAP_RIGHT;
   public static final InputBindingSupplier VMOUSE_LCLICK;
   public static final InputBindingSupplier VMOUSE_RCLICK;
   public static final InputBindingSupplier VMOUSE_SHIFT_CLICK;
   public static final InputBindingSupplier VMOUSE_SCROLL_DOWN;
   public static final InputBindingSupplier VMOUSE_SCROLL_UP;
   public static final InputBindingSupplier VMOUSE_SHIFT;
   public static final InputBindingSupplier VMOUSE_PAGE_NEXT;
   public static final InputBindingSupplier VMOUSE_PAGE_PREV;
   public static final InputBindingSupplier VMOUSE_PAGE_DOWN;
   public static final InputBindingSupplier VMOUSE_PAGE_UP;
   public static final InputBindingSupplier VMOUSE_TOGGLE;
   private static final Map<class_304, InputBindingSupplier> MODDED_BINDS;

   public static void registerModdedBindings() {
      Iterator var0 = PlatformClientUtil.getModdedKeyMappings().iterator();

      while(var0.hasNext()) {
         class_304 keyMapping = (class_304)var0.next();
         if (ControlifyBindApi.get().getKeyCorrelation(keyMapping).isEmpty()) {
            try {
               String idPath = keyMapping.method_1431().toLowerCase().replaceAll("[^a-z0-9/._-]", "_").trim();
               class_2960 identifier = class_2960.method_60655("fabric-key-binding-api-v1", idPath);
               InputBindingSupplier binding = ControlifyBindApi.get().registerBinding((builder) -> {
                  return builder.id(identifier).name(class_2561.method_43471(keyMapping.method_1431())).description(class_2561.method_43471("controlify.custom_binding.vanilla_description").method_27692(class_124.field_1080)).category(class_2561.method_43471(keyMapping.method_1423())).radialCandidate(RadialIcons.FABRIC_ICON).allowedContexts(BindContext.IN_GAME).keyEmulation(keyMapping);
               });
               MODDED_BINDS.put(keyMapping, binding);
            } catch (Exception var5) {
               CUtil.LOGGER.error("Failed to automatically register modded keybind: {}", keyMapping.method_1431(), var5);
            }
         }
      }

   }

   private ControlifyBindings() {
   }

   static {
      options = class_310.method_1551().field_1690;
      MOVEMENT_CATEGORY = class_2561.method_43471("key.categories.movement");
      GAMEPLAY_CATEGORY = class_2561.method_43471("key.categories.gameplay");
      INVENTORY_CATEGORY = class_2561.method_43471("key.categories.inventory");
      CREATIVE_CATEGORY = class_2561.method_43471("key.categories.creative");
      MISC_CATEGORY = class_2561.method_43471("key.categories.misc");
      DEBUG_CATEGORY = class_2561.method_43471("controlify.binding_category.debug");
      GUI_CATEGORY = class_2561.method_43471("controlify.binding_category.gui");
      RADIAL_CATEGORY = class_2561.method_43471("controlify.gui.radial_menu");
      VMOUSE_CATEGORY = class_2561.method_43471("controlify.binding_category.vmouse");
      WALK_FORWARD = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "walk_forward").category(MOVEMENT_CATEGORY).allowedContexts(BindContext.IN_GAME);
      });
      WALK_BACKWARD = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "walk_backward").category(MOVEMENT_CATEGORY).allowedContexts(BindContext.IN_GAME);
      });
      WALK_LEFT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "strafe_left").category(MOVEMENT_CATEGORY).allowedContexts(BindContext.IN_GAME);
      });
      WALK_RIGHT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "strafe_right").category(MOVEMENT_CATEGORY).allowedContexts(BindContext.IN_GAME);
      });
      LOOK_UP = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "look_up").category(MOVEMENT_CATEGORY).allowedContexts(BindContext.IN_GAME);
      });
      LOOK_DOWN = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "look_down").category(MOVEMENT_CATEGORY).allowedContexts(BindContext.IN_GAME);
      });
      LOOK_LEFT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "look_left").category(MOVEMENT_CATEGORY).allowedContexts(BindContext.IN_GAME);
      });
      LOOK_RIGHT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "look_right").category(MOVEMENT_CATEGORY).allowedContexts(BindContext.IN_GAME);
      });
      GYRO_BUTTON = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "gyro_button").category(MOVEMENT_CATEGORY).allowedContexts(BindContext.IN_GAME);
      }, (c) -> {
         return c.gyro().isPresent();
      });
      JUMP = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "jump").category(MOVEMENT_CATEGORY).allowedContexts(BindContext.IN_GAME).radialCandidate(RadialIcons.getEffect(class_1294.field_5913));
      });
      SPRINT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "sprint").category(MOVEMENT_CATEGORY).allowedContexts(BindContext.IN_GAME).keyEmulation(options.field_1867, (c) -> {
            return ((GenericControllerConfig)c.genericConfig().config()).toggleSprint;
         });
      });
      SNEAK = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "sneak").category(MOVEMENT_CATEGORY).allowedContexts(BindContext.IN_GAME).addKeyCorrelation(options.field_1832);
      });
      ATTACK = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "attack").category(GAMEPLAY_CATEGORY).allowedContexts(BindContext.IN_GAME).keyEmulation(options.field_1886);
      });
      USE = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "use").category(GAMEPLAY_CATEGORY).allowedContexts(BindContext.IN_GAME).keyEmulation(options.field_1904);
      });
      DROP_INGAME = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "drop").category(GAMEPLAY_CATEGORY).allowedContexts(BindContext.IN_GAME).radialCandidate(RadialIcons.getItem(class_1802.field_8077));
      });
      DROP_STACK = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "drop_stack").category(GAMEPLAY_CATEGORY).allowedContexts(BindContext.IN_GAME).radialCandidate(RadialIcons.getItem(class_1802.field_8626));
      });
      PAUSE = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "pause").category(GAMEPLAY_CATEGORY).allowedContexts(BindContext.IN_GAME).radialCandidate(RadialIcons.getItem(class_1802.field_8615));
      });
      CHANGE_PERSPECTIVE = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "change_perspective").category(GAMEPLAY_CATEGORY).allowedContexts(BindContext.IN_GAME).radialCandidate(RadialIcons.getItem(class_1802.field_8892));
      });
      SWAP_HANDS = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "swap_hands").category(GAMEPLAY_CATEGORY).allowedContexts(BindContext.IN_GAME).radialCandidate(RadialIcons.getItem(class_1802.field_8606));
      });
      NEXT_SLOT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "next_slot").category(GAMEPLAY_CATEGORY).allowedContexts(BindContext.IN_GAME);
      });
      PREV_SLOT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "prev_slot").category(GAMEPLAY_CATEGORY).allowedContexts(BindContext.IN_GAME);
      });
      HOTBAR_SLOT_SELECT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "hotbar_item_select_radial").category(GAMEPLAY_CATEGORY).allowedContexts(BindContext.IN_GAME, BindContext.RADIAL_MENU);
      });
      GAME_MODE_SWITCHER = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "game_mode_switcher").category(GAMEPLAY_CATEGORY).allowedContexts(BindContext.IN_GAME, BindContext.RADIAL_MENU);
      });
      INVENTORY = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "inventory").category(INVENTORY_CATEGORY).allowedContexts(BindContext.IN_GAME).radialCandidate(RadialIcons.getItem(class_1802.field_8106));
      });
      INV_SELECT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "inv_select").category(INVENTORY_CATEGORY).allowedContexts(BindContext.CONTAINER);
      });
      INV_QUICK_MOVE = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "inv_quick_move").category(INVENTORY_CATEGORY).allowedContexts(BindContext.CONTAINER);
      });
      INV_TAKE_HALF = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "inv_take_half").category(INVENTORY_CATEGORY).allowedContexts(BindContext.CONTAINER);
      });
      DROP_INVENTORY = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "drop_inventory").category(INVENTORY_CATEGORY).allowedContexts(BindContext.CONTAINER);
      });
      BUNDLE_NAVI_UP = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "bundle_navi_up").category(INVENTORY_CATEGORY).allowedContexts(BindContext.CONTAINER);
      });
      BUNDLE_NAVI_DOWN = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "bundle_navi_down").category(INVENTORY_CATEGORY).allowedContexts(BindContext.CONTAINER);
      });
      BUNDLE_NAVI_LEFT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "bundle_navi_left").category(INVENTORY_CATEGORY).allowedContexts(BindContext.CONTAINER);
      });
      BUNDLE_NAVI_RIGHT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "bundle_navi_right").category(INVENTORY_CATEGORY).allowedContexts(BindContext.CONTAINER);
      });
      PICK_BLOCK = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "pick_block").category(CREATIVE_CATEGORY).allowedContexts(BindContext.IN_GAME).radialCandidate(RadialIcons.getItem(class_1802.field_8600));
      });
      PICK_BLOCK_NBT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "pick_block_nbt").category(CREATIVE_CATEGORY).allowedContexts(BindContext.IN_GAME).radialCandidate(RadialIcons.getItem(class_1802.field_8688));
      });
      HOTBAR_LOAD_RADIAL = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "hotbar_load_radial").category(CREATIVE_CATEGORY).allowedContexts(BindContext.IN_GAME, BindContext.RADIAL_MENU);
      });
      HOTBAR_SAVE_RADIAL = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "hotbar_save_radial").category(CREATIVE_CATEGORY).allowedContexts(BindContext.IN_GAME, BindContext.RADIAL_MENU);
      });
      OPEN_CHAT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "open_chat").category(MISC_CATEGORY).allowedContexts(BindContext.IN_GAME).radialCandidate(RadialIcons.getItem(class_1802.field_8674)).keyEmulation(options.field_1890);
      });
      TOGGLE_HUD_VISIBILITY = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "toggle_hud_visibility").category(MISC_CATEGORY).allowedContexts(BindContext.IN_GAME).radialCandidate(RadialIcons.getEffect(class_1294.field_5905));
      });
      SHOW_PLAYER_LIST = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "show_player_list").category(MISC_CATEGORY).allowedContexts(BindContext.IN_GAME).radialCandidate(RadialIcons.getItem(class_1802.field_8575));
      });
      TAKE_SCREENSHOT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "take_screenshot").category(MISC_CATEGORY).allowedContexts(BindContext.IN_GAME).radialCandidate(RadialIcons.getItem(class_1802.field_27070));
      });
      DEBUG_RADIAL = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "debug_radial").category(DEBUG_CATEGORY).allowedContexts(BindContext.IN_GAME, BindContext.RADIAL_MENU);
      });
      TOGGLE_DEBUG_MENU = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "toggle_debug_menu").category(DEBUG_CATEGORY).allowedContexts(BindContext.IN_GAME).radialCandidate(RadialIcons.getItem(class_1802.field_8688));
      });
      TOGGLE_DEBUG_MENU_FPS = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "toggle_debug_menu_fps").category(DEBUG_CATEGORY).allowedContexts(BindContext.IN_GAME).radialCandidate(RadialIcons.getItem(class_1802.field_8688));
      });
      TOGGLE_DEBUG_MENU_NET = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "toggle_debug_menu_net").category(DEBUG_CATEGORY).allowedContexts(BindContext.IN_GAME).radialCandidate(RadialIcons.getItem(class_1802.field_8688));
      });
      TOGGLE_DEBUG_MENU_PROF = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "toggle_debug_menu_prof").category(DEBUG_CATEGORY).allowedContexts(BindContext.IN_GAME).radialCandidate(RadialIcons.getItem(class_1802.field_8688));
      });
      GUI_PRESS = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "gui_press").category(GUI_CATEGORY).allowedContexts(BindContext.REGULAR_SCREEN);
      });
      GUI_BACK = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "gui_back").category(GUI_CATEGORY).allowedContexts(BindContext.ANY_SCREEN);
      });
      GUI_NEXT_TAB = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "gui_next_tab").category(GUI_CATEGORY).allowedContexts(BindContext.ANY_SCREEN);
      });
      GUI_PREV_TAB = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "gui_prev_tab").category(GUI_CATEGORY).allowedContexts(BindContext.ANY_SCREEN);
      });
      GUI_ABSTRACT_ACTION_1 = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "gui_abstract_action_1").category(GUI_CATEGORY).allowedContexts(BindContext.REGULAR_SCREEN);
      });
      GUI_ABSTRACT_ACTION_2 = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "gui_abstract_action_2").category(GUI_CATEGORY).allowedContexts(BindContext.REGULAR_SCREEN);
      });
      GUI_ABSTRACT_ACTION_3 = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "gui_abstract_action_3").category(GUI_CATEGORY).allowedContexts(BindContext.REGULAR_SCREEN);
      });
      GUI_NAVI_UP = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "gui_navi_up").category(GUI_CATEGORY).allowedContexts(BindContext.REGULAR_SCREEN);
      });
      GUI_NAVI_DOWN = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "gui_navi_down").category(GUI_CATEGORY).allowedContexts(BindContext.REGULAR_SCREEN);
      });
      GUI_NAVI_LEFT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "gui_navi_left").category(GUI_CATEGORY).allowedContexts(BindContext.REGULAR_SCREEN);
      });
      GUI_NAVI_RIGHT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "gui_navi_right").category(GUI_CATEGORY).allowedContexts(BindContext.REGULAR_SCREEN);
      });
      CYCLE_OPT_FORWARD = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "cycle_opt_forward").category(GUI_CATEGORY).allowedContexts(BindContext.REGULAR_SCREEN);
      });
      CYCLE_OPT_BACKWARD = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "cycle_opt_backward").category(GUI_CATEGORY).allowedContexts(BindContext.REGULAR_SCREEN);
      });
      RADIAL_MENU = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "radial_menu").category(RADIAL_CATEGORY).allowedContexts(BindContext.IN_GAME, BindContext.RADIAL_MENU);
      });
      RADIAL_AXIS_UP = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "radial_axis_up").category(RADIAL_CATEGORY).allowedContexts(BindContext.RADIAL_MENU);
      });
      RADIAL_AXIS_DOWN = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "radial_axis_down").category(RADIAL_CATEGORY).allowedContexts(BindContext.RADIAL_MENU);
      });
      RADIAL_AXIS_LEFT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "radial_axis_left").category(RADIAL_CATEGORY).allowedContexts(BindContext.RADIAL_MENU);
      });
      RADIAL_AXIS_RIGHT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "radial_axis_right").category(RADIAL_CATEGORY).allowedContexts(BindContext.RADIAL_MENU);
      });
      VMOUSE_MOVE_UP = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "vmouse_move_up").category(VMOUSE_CATEGORY).allowedContexts(BindContext.V_MOUSE_CURSOR);
      });
      VMOUSE_MOVE_DOWN = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "vmouse_move_down").category(VMOUSE_CATEGORY).allowedContexts(BindContext.V_MOUSE_CURSOR);
      });
      VMOUSE_MOVE_LEFT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "vmouse_move_left").category(VMOUSE_CATEGORY).allowedContexts(BindContext.V_MOUSE_CURSOR);
      });
      VMOUSE_MOVE_RIGHT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "vmouse_move_right").category(VMOUSE_CATEGORY).allowedContexts(BindContext.V_MOUSE_CURSOR);
      });
      VMOUSE_SNAP_UP = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "vmouse_snap_up").category(VMOUSE_CATEGORY).allowedContexts(BindContext.V_MOUSE_CURSOR);
      });
      VMOUSE_SNAP_DOWN = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "vmouse_snap_down").category(VMOUSE_CATEGORY).allowedContexts(BindContext.V_MOUSE_CURSOR);
      });
      VMOUSE_SNAP_LEFT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "vmouse_snap_left").category(VMOUSE_CATEGORY).allowedContexts(BindContext.V_MOUSE_CURSOR);
      });
      VMOUSE_SNAP_RIGHT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "vmouse_snap_right").category(VMOUSE_CATEGORY).allowedContexts(BindContext.V_MOUSE_CURSOR);
      });
      VMOUSE_LCLICK = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "vmouse_lclick").category(VMOUSE_CATEGORY).allowedContexts(BindContext.V_MOUSE_COMPAT, BindContext.V_MOUSE_CURSOR);
      });
      VMOUSE_RCLICK = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "vmouse_rclick").category(VMOUSE_CATEGORY).allowedContexts(BindContext.V_MOUSE_COMPAT, BindContext.V_MOUSE_CURSOR);
      });
      VMOUSE_SHIFT_CLICK = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "vmouse_shift_click").category(VMOUSE_CATEGORY).allowedContexts(BindContext.V_MOUSE_COMPAT, BindContext.V_MOUSE_CURSOR);
      });
      VMOUSE_SCROLL_DOWN = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "vmouse_scroll_down").category(VMOUSE_CATEGORY).allowedContexts(BindContext.V_MOUSE_COMPAT, BindContext.V_MOUSE_CURSOR);
      });
      VMOUSE_SCROLL_UP = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "vmouse_scroll_up").category(VMOUSE_CATEGORY).allowedContexts(BindContext.V_MOUSE_COMPAT, BindContext.V_MOUSE_CURSOR);
      });
      VMOUSE_SHIFT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "vmouse_shift").category(VMOUSE_CATEGORY).allowedContexts(BindContext.V_MOUSE_COMPAT, BindContext.V_MOUSE_CURSOR);
      });
      VMOUSE_PAGE_NEXT = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "vmouse_page_next").category(VMOUSE_CATEGORY).allowedContexts(BindContext.V_MOUSE_COMPAT, BindContext.V_MOUSE_CURSOR);
      });
      VMOUSE_PAGE_PREV = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "vmouse_page_prev").category(VMOUSE_CATEGORY).allowedContexts(BindContext.V_MOUSE_COMPAT, BindContext.V_MOUSE_CURSOR);
      });
      VMOUSE_PAGE_DOWN = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "vmouse_page_down").category(VMOUSE_CATEGORY).allowedContexts(BindContext.V_MOUSE_COMPAT, BindContext.V_MOUSE_CURSOR);
      });
      VMOUSE_PAGE_UP = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "vmouse_page_up").category(VMOUSE_CATEGORY).allowedContexts(BindContext.V_MOUSE_COMPAT, BindContext.V_MOUSE_CURSOR);
      });
      VMOUSE_TOGGLE = ControlifyBindApi.get().registerBinding((builder) -> {
         return builder.id("controlify", "vmouse_toggle").category(VMOUSE_CATEGORY).allowedContexts(BindContext.V_MOUSE_CURSOR, BindContext.V_MOUSE_COMPAT, BindContext.ANY_SCREEN);
      });
      MODDED_BINDS = new LinkedHashMap();
   }
}
