package dev.isxander.controlify.ingame;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.api.event.ControlifyEvents;
import dev.isxander.controlify.api.ingameinput.LookInputModifier;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.GenericControllerConfig;
import dev.isxander.controlify.controller.gyro.GyroButtonMode;
import dev.isxander.controlify.controller.gyro.GyroComponent;
import dev.isxander.controlify.controller.gyro.GyroState;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.driver.steamdeck.SteamDeckDriver;
import dev.isxander.controlify.gui.screen.RadialItems;
import dev.isxander.controlify.gui.screen.RadialMenuScreen;
import dev.isxander.controlify.mixins.feature.steamdeck.ScreenshotAccessor;
import dev.isxander.controlify.server.ServerPolicies;
import dev.isxander.controlify.utils.ControllerUtils;
import dev.isxander.controlify.utils.DebugOverlayHelper;
import dev.isxander.controlify.utils.HoldRepeatHelper;
import dev.isxander.controlify.utils.animation.api.Animation;
import dev.isxander.controlify.utils.animation.api.EasingFunction;
import java.io.File;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2846;
import net.minecraft.class_310;
import net.minecraft.class_318;
import net.minecraft.class_3532;
import net.minecraft.class_437;
import net.minecraft.class_490;
import net.minecraft.class_5498;
import net.minecraft.class_744;
import net.minecraft.class_746;
import net.minecraft.class_2846.class_2847;
import org.joml.Vector2d;
import org.joml.Vector2f;

public class InGameInputHandler {
   private final ControllerEntity controller;
   private final Controlify controlify;
   private final class_310 minecraft;
   private double lookInputX;
   private double lookInputY;
   private final GyroState gyroInput = new GyroState();
   private boolean gyroToggledOn;
   private boolean wasAiming;
   private Animation flickAnimation;
   private boolean shouldShowPlayerList;
   private final HoldRepeatHelper dropRepeatHelper;
   private boolean dropRepeating;
   private final HoldRepeatHelper hotbarNextRepeatHelper;
   private final HoldRepeatHelper hotbarPrevRepeatHelper;

   public InGameInputHandler(ControllerEntity controller) {
      this.controller = controller;
      this.minecraft = class_310.method_1551();
      this.controlify = Controlify.instance();
      this.dropRepeatHelper = new HoldRepeatHelper(20, 1);
      this.hotbarNextRepeatHelper = new HoldRepeatHelper(10, 4);
      this.hotbarPrevRepeatHelper = new HoldRepeatHelper(10, 4);
      this.gyroToggledOn = false;
   }

   public void inputTick() {
      boolean isController = ControllerPlayerMovement.shouldBeControllerInput();
      this.handlePlayerLookInput(isController);
      ControllerPlayerMovement.ensureCorrectInput(this.minecraft.field_1724);
      if (isController) {
         this.handleKeybinds();
         this.preventFlyDrifting();
      }

   }

   protected void handleKeybinds() {
      if (this.minecraft.field_1755 == null) {
         if (ControlifyBindings.PAUSE.on(this.controller).justPressed()) {
            this.minecraft.method_20539(false);
         }

         if (this.minecraft.field_1724 != null) {
            class_1661 inventory = this.minecraft.field_1724.method_31548();
            if (this.hotbarNextRepeatHelper.shouldAction(ControlifyBindings.NEXT_SLOT.on(this.controller))) {
               this.hotbarNextRepeatHelper.onNavigate();
               inventory.method_61496((inventory.method_67532() + 1) % class_1661.method_7368());
            }

            if (this.hotbarPrevRepeatHelper.shouldAction(ControlifyBindings.PREV_SLOT.on(this.controller))) {
               this.hotbarPrevRepeatHelper.onNavigate();
               inventory.method_61496((inventory.method_67532() - 1 + class_1661.method_7368()) % class_1661.method_7368());
            }

            if (!this.minecraft.field_1724.method_7325()) {
               if (ControlifyBindings.DROP_STACK.on(this.controller).justPressed()) {
                  if (this.minecraft.field_1724.method_7290(true)) {
                     this.minecraft.field_1724.method_6104(class_1268.field_5808);
                  }
               } else {
                  if (ControlifyBindings.DROP_INGAME.on(this.controller).justPressed()) {
                     this.dropRepeating = true;
                  } else if (ControlifyBindings.DROP_INGAME.on(this.controller).justReleased()) {
                     this.dropRepeating = false;
                  }

                  if (this.dropRepeating && this.dropRepeatHelper.shouldAction(ControlifyBindings.DROP_INGAME.on(this.controller)) && this.minecraft.field_1724.method_7290(false)) {
                     this.dropRepeatHelper.onNavigate();
                     this.minecraft.field_1724.method_6104(class_1268.field_5808);
                  }
               }

               if (ControlifyBindings.SWAP_HANDS.on(this.controller).justPressed()) {
                  this.minecraft.field_1724.field_3944.method_52787(new class_2846(class_2847.field_12969, class_2338.field_10980, class_2350.field_11033));
               }
            }

            if (ControlifyBindings.INVENTORY.on(this.controller).justPressed()) {
               if (this.minecraft.field_1761.method_2895()) {
                  this.minecraft.field_1724.method_3132();
               } else {
                  this.minecraft.method_1577().method_4912();
                  this.minecraft.method_1507(new class_490(this.minecraft.field_1724));
               }
            }

            if (ControlifyBindings.CHANGE_PERSPECTIVE.on(this.controller).justPressed()) {
               class_5498 cameraType = this.minecraft.field_1690.method_31044();
               this.minecraft.field_1690.method_31043(this.minecraft.field_1690.method_31044().method_31036());
               if (cameraType.method_31034() != this.minecraft.field_1690.method_31044().method_31034()) {
                  this.minecraft.field_1773.method_3167(this.minecraft.field_1690.method_31044().method_31034() ? this.minecraft.method_1560() : null);
               }

               this.minecraft.field_1769.method_3292();
            }
         }

         if (ControlifyBindings.TOGGLE_HUD_VISIBILITY.on(this.controller).justPressed()) {
            this.minecraft.field_1690.field_1842 = !this.minecraft.field_1690.field_1842;
         }

         if (ControlifyBindings.SHOW_PLAYER_LIST.on(this.controller).justPressed()) {
            this.shouldShowPlayerList = !this.shouldShowPlayerList;
         }

         if (ControlifyBindings.TOGGLE_DEBUG_MENU.on(this.controller).justPressed()) {
            DebugOverlayHelper.toggleOverlay();
         }

         if (ControlifyBindings.TOGGLE_DEBUG_MENU_FPS.on(this.controller).justPressed()) {
            DebugOverlayHelper.toggleFpsOverlay();
         }

         if (ControlifyBindings.TOGGLE_DEBUG_MENU_NET.on(this.controller).justPressed()) {
            DebugOverlayHelper.toggleNetworkOverlay();
         }

         if (ControlifyBindings.TOGGLE_DEBUG_MENU_PROF.on(this.controller).justPressed()) {
            DebugOverlayHelper.toggleProfilerOverlay();
         }

         if (ControlifyBindings.DEBUG_RADIAL.on(this.controller).justPressed()) {
            this.minecraft.method_1507(new RadialMenuScreen(this.controller, ControlifyBindings.DEBUG_RADIAL.on(this.controller), RadialItems.createDebug(), class_2561.method_43473(), (RadialMenuScreen.EditMode)null, (class_437)null));
         }

         if (ControlifyBindings.TAKE_SCREENSHOT.on(this.controller).justPressed()) {
            File screenshotFile = ScreenshotAccessor.invokeGetFile(new File(this.minecraft.field_1697, "screenshots"));
            class_318.method_1659(this.minecraft.field_1697, this.minecraft.method_1522(), (component) -> {
               this.minecraft.execute(() -> {
                  this.minecraft.field_1705.method_1743().method_1812(component);
                  SteamDeckDriver.getDeck().ifPresent((deck) -> {
                     deck.doSteamScreenshot(screenshotFile.getAbsoluteFile().toPath(), "");
                  });
               });
            });
         }

         if (ControlifyBindings.PICK_BLOCK.on(this.controller).justPressed()) {
            ((PickBlockAccessor)this.minecraft).controlify$pickBlock();
         }

         if (ControlifyBindings.PICK_BLOCK_NBT.on(this.controller).justPressed()) {
            ((PickBlockAccessor)this.minecraft).controlify$pickBlockWithNbt();
         }

         if (ControlifyBindings.RADIAL_MENU.on(this.controller).justPressed()) {
            this.minecraft.method_1507(new RadialMenuScreen(this.controller, ControlifyBindings.RADIAL_MENU.on(this.controller), RadialItems.createBindings(this.controller), class_2561.method_43471("controlify.radial_menu.configure_hint"), (RadialMenuScreen.EditMode)null, (class_437)null));
         }

         if (ControlifyBindings.GAME_MODE_SWITCHER.on(this.controller).justPressed()) {
            this.minecraft.method_1507(new RadialMenuScreen(this.controller, ControlifyBindings.GAME_MODE_SWITCHER.on(this.controller), RadialItems.createGameModes(), class_2561.method_43473(), (RadialMenuScreen.EditMode)null, (class_437)null));
         }

         if (ControlifyBindings.HOTBAR_SLOT_SELECT.on(this.controller).justPressed()) {
            this.minecraft.method_1507(new RadialMenuScreen(this.controller, ControlifyBindings.HOTBAR_SLOT_SELECT.on(this.controller), RadialItems.createHotbarItemSelect(), class_2561.method_43473(), (RadialMenuScreen.EditMode)null, (class_437)null));
         }

         if (this.minecraft.field_1724.method_56992()) {
            if (ControlifyBindings.HOTBAR_LOAD_RADIAL.on(this.controller).justPressed()) {
               this.minecraft.method_1507(new RadialMenuScreen(this.controller, ControlifyBindings.HOTBAR_LOAD_RADIAL.on(this.controller), RadialItems.createHotbarLoad(), class_2561.method_43471("controlify.radial.hotbar_load_hint"), (RadialMenuScreen.EditMode)null, (class_437)null));
            }

            if (ControlifyBindings.HOTBAR_SAVE_RADIAL.on(this.controller).justPressed()) {
               this.minecraft.method_1507(new RadialMenuScreen(this.controller, ControlifyBindings.HOTBAR_SAVE_RADIAL.on(this.controller), RadialItems.createHotbarSave(), class_2561.method_43471("controlify.radial.hotbar_save_hint"), (RadialMenuScreen.EditMode)null, (class_437)null));
            }
         }

      }
   }

   protected void handlePlayerLookInput(boolean isController) {
      class_746 player = this.minecraft.field_1724;
      if (isController && this.canProcessLookInput()) {
         boolean aiming = this.isAiming(player);
         Vector2d lookImpulse = new Vector2d();
         this.controller.gyro().ifPresent((gyro) -> {
            this.handleGyroLook(gyro, lookImpulse, aiming);
         });
         if ((Boolean)this.controller.gyro().map((gyro) -> {
            return ((GyroComponent.Config)gyro.confObj()).lookSensitivity > 0.0F && ((GyroComponent.Config)gyro.confObj()).flickStick;
         }).orElse(false)) {
            this.handleFlickStick(player);
         } else {
            this.controller.input().ifPresent((input) -> {
               this.handleRegularLook(input, lookImpulse, aiming, player);
            });
         }

         LookInputModifier modifier = new LookInputModifier(new Vector2f((float)lookImpulse.x, (float)lookImpulse.y), this.controller);
         ControlifyEvents.LOOK_INPUT_MODIFIER.invoke(modifier);
         lookImpulse.set(modifier.lookInput());
         this.lookInputX = lookImpulse.x;
         this.lookInputY = lookImpulse.y;
         this.wasAiming = aiming;
      } else {
         this.lookInputX = 0.0D;
         this.lookInputY = 0.0D;
      }
   }

   protected void handleRegularLook(InputComponent input, Vector2d impulse, boolean aiming, class_746 player) {
      InputComponent.Config config = (InputComponent.Config)input.confObj();
      Vector2d regularImpulse = new Vector2d((double)(ControlifyBindings.LOOK_RIGHT.on(this.controller).analogueNow() - ControlifyBindings.LOOK_LEFT.on(this.controller).analogueNow()), (double)(ControlifyBindings.LOOK_DOWN.on(this.controller).analogueNow() - ControlifyBindings.LOOK_UP.on(this.controller).analogueNow()));
      if (config.vLookInvert) {
         regularImpulse.y *= -1.0D;
      }

      if (!config.isLCE) {
         ControllerUtils.applyEasingToLength(regularImpulse, (d) -> {
            return d * Math.abs(d);
         });
      } else {
         regularImpulse.x *= Math.abs(regularImpulse.x);
         regularImpulse.y *= Math.abs(regularImpulse.y);
      }

      if (config.reduceAimingSensitivity && player.method_6115()) {
         float var10000;
         if (config.isLCE) {
            switch(player.method_6030().method_7976()) {
            case field_8953:
            case field_8947:
            case field_8951:
            case field_27079:
               var10000 = 0.15F;
               break;
            default:
               var10000 = 1.0F;
            }
         } else {
            switch(player.method_6030().method_7976()) {
            case field_8953:
            case field_8947:
            case field_8951:
               var10000 = 0.6F;
               break;
            case field_27079:
               var10000 = 0.2F;
               break;
            default:
               var10000 = 1.0F;
            }
         }

         float aimMultiplier = var10000;
         regularImpulse.mul((double)aimMultiplier);
      }

      regularImpulse.x *= (double)(config.hLookSensitivity * 10.0F);
      regularImpulse.y *= (double)(config.vLookSensitivity * 10.0F);
      impulse.add(regularImpulse);
   }

   protected void handleGyroLook(GyroComponent gyro, Vector2d impulse, boolean aiming) {
      GyroComponent.Config config = (GyroComponent.Config)gyro.confObj();
      InputBinding gyroButton = ControlifyBindings.GYRO_BUTTON.on(this.controller);
      if (config.requiresButton.equals(GyroButtonMode.ON) && !gyroButton.digitalNow() && !aiming) {
         this.gyroInput.set(0.0F);
      } else if (config.requiresButton.equals(GyroButtonMode.INVERT) && gyroButton.digitalNow() && !aiming) {
         this.gyroInput.set(0.0F);
      } else if (config.requiresButton.equals(GyroButtonMode.TOGGLE) && !this.gyroToggledOn && !aiming) {
         this.gyroInput.set(0.0F);
      } else if (config.relativeGyroMode) {
         this.gyroInput.add((new GyroState(gyro.getState())).mul(0.1F));
      } else {
         this.gyroInput.set(gyro.getState());
      }

      if (config.requiresButton.equals(GyroButtonMode.TOGGLE) && gyroButton.justPressed()) {
         this.gyroToggledOn = !this.gyroToggledOn;
      }

      GyroState thisInput = (new GyroState(this.gyroInput)).mul(57.295776F).div(20.0F).mul(config.lookSensitivity);
      impulse.y += (double)(-thisInput.pitch() * (float)(config.invertY ? -1 : 1));
      double var10001 = impulse.x;
      float var10002;
      switch(config.yawMode) {
      case YAW:
         var10002 = -thisInput.yaw();
         break;
      case ROLL:
         var10002 = -thisInput.roll();
         break;
      case BOTH:
         var10002 = -thisInput.yaw() - thisInput.roll();
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      impulse.x = var10001 + (double)(var10002 * (float)(config.invertX ? -1 : 1));
   }

   protected void handleFlickStick(class_746 player) {
      float y = ControlifyBindings.LOOK_DOWN.on(this.controller).analogueNow() - ControlifyBindings.LOOK_UP.on(this.controller).analogueNow();
      float x = ControlifyBindings.LOOK_RIGHT.on(this.controller).analogueNow() - ControlifyBindings.LOOK_LEFT.on(this.controller).analogueNow();
      float flickAngle = class_3532.method_15393((float)class_3532.method_15349((double)y, (double)x) * 57.295776F + 90.0F);
      if (ControlifyBindings.LOOK_DOWN.on(this.controller).justPressed() || ControlifyBindings.LOOK_UP.on(this.controller).justPressed() || ControlifyBindings.LOOK_LEFT.on(this.controller).justPressed() || ControlifyBindings.LOOK_RIGHT.on(this.controller).justPressed()) {
         if (this.flickAnimation != null && this.flickAnimation.isPlaying()) {
            this.flickAnimation.skipToEnd();
         }

         this.flickAnimation = Animation.of(8).easing(EasingFunction.EASE_OUT_EXPO).deltaConsumerD((angle) -> {
            player.method_5872(angle, 0.0D);
         }, 0.0D, (double)flickAngle / 0.15D).play();
      }
   }

   public void processPlayerLook(float deltaTime) {
      if (this.minecraft.field_1724 != null) {
         this.minecraft.field_1724.method_5872(this.lookInputX / 0.15D * (double)deltaTime, this.lookInputY / 0.15D * (double)deltaTime);
      }

   }

   public boolean shouldShowPlayerList() {
      return this.shouldShowPlayerList;
   }

   public void preventFlyDrifting() {
      if (((GenericControllerConfig)this.controller.genericConfig().config()).disableFlyDrifting && ServerPolicies.DISABLE_FLY_DRIFTING.get()) {
         class_746 player = this.minecraft.field_1724;
         if (player != null && player.method_31549().field_7479 && !player.method_24828()) {
            class_243 motion = player.method_18798();
            double x = motion.field_1352;
            double y = motion.field_1351;
            double z = motion.field_1350;
            boolean jumping = player.field_3913.field_54155.comp_3163();
            boolean shiftKeyDown = player.field_3913.field_54155.comp_3164();
            if (!jumping) {
               y = Math.min(y, 0.0D);
            }

            if (!shiftKeyDown) {
               y = Math.max(y, 0.0D);
            }

            class_241 moveVec = getMoveVec(player.field_3913);
            if (moveVec.field_1343 == 0.0F && moveVec.field_1342 == 0.0F) {
               x = 0.0D;
               z = 0.0D;
            }

            player.method_18800(x, y, z);
         }

      }
   }

   private boolean isAiming(class_1657 player) {
      boolean var10000;
      if (player.method_6115()) {
         switch(player.method_6030().method_7976()) {
         case field_8953:
         case field_8947:
         case field_8951:
         case field_27079:
            var10000 = true;
            return var10000;
         }
      }

      var10000 = false;
      return var10000;
   }

   private boolean canProcessLookInput() {
      boolean mouseNotGrabbed = !this.minecraft.field_1729.method_1613() && !this.controlify.config().globalSettings().outOfFocusInput;
      boolean outOfFocus = !this.minecraft.method_1569() && !this.controlify.config().globalSettings().outOfFocusInput;
      boolean screenVisible = this.minecraft.field_1755 != null;
      boolean playerExists = this.minecraft.field_1724 != null;
      return !mouseNotGrabbed && !outOfFocus && !screenVisible && playerExists;
   }

   public static class_241 getMoveVec(class_744 input) {
      return input.method_3128();
   }
}
