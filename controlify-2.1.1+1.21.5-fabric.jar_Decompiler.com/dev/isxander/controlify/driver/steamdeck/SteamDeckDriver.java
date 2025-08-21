package dev.isxander.controlify.driver.steamdeck;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.battery.BatteryLevelComponent;
import dev.isxander.controlify.controller.battery.PowerState;
import dev.isxander.controlify.controller.gyro.GyroComponent;
import dev.isxander.controlify.controller.gyro.GyroState;
import dev.isxander.controlify.controller.impl.ControllerStateImpl;
import dev.isxander.controlify.controller.info.DriverNameComponent;
import dev.isxander.controlify.controller.info.GUIDComponent;
import dev.isxander.controlify.controller.info.UIDComponent;
import dev.isxander.controlify.controller.input.GamepadInputs;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.controller.keyboard.NativeKeyboardComponent;
import dev.isxander.controlify.controller.steamdeck.SteamDeckComponent;
import dev.isxander.controlify.controller.touchpad.TouchpadComponent;
import dev.isxander.controlify.controller.touchpad.Touchpads;
import dev.isxander.controlify.driver.Driver;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.log.ControlifyLogger;
import dev.isxander.deckapi.api.ControllerButton;
import dev.isxander.deckapi.api.ControllerState;
import dev.isxander.deckapi.api.SteamDeck;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.class_310;
import org.joml.Vector2f;

public class SteamDeckDriver implements Driver {
   private static SteamDeck deck;
   private static boolean triedToLoad = false;
   private InputComponent inputComponent;
   private GyroComponent gyroComponent;
   private BatteryLevelComponent batteryLevelComponent;
   private TouchpadComponent touchpadComponent;
   private NativeKeyboardComponent keyboardComponent;
   private ControlifyLogger logger;
   private final AtomicBoolean runningPoller = new AtomicBoolean(false);

   public SteamDeckDriver(ControlifyLogger logger) {
      this.logger = logger.createSubLogger("SteamDeckDriver");
   }

   public void addComponents(ControllerEntity controller) {
      controller.setComponent(new DriverNameComponent("Steam Deck"));
      controller.setComponent(new GUIDComponent("steamdeck"));
      controller.setComponent(new UIDComponent(CUtil.createUIDFromBytes("steamdeck".getBytes())));
      controller.setComponent(this.inputComponent = new InputComponent(controller, 20, 10, 0, true, GamepadInputs.DEADZONE_GROUPS, controller.info().type().mappingId()));
      controller.setComponent(this.gyroComponent = new GyroComponent());
      this.batteryLevelComponent = new BatteryLevelComponent();
      controller.setComponent(this.touchpadComponent = new TouchpadComponent(new Touchpads(new Touchpads.Touchpad[]{new Touchpads.Touchpad(1), new Touchpads.Touchpad(1)})));
      controller.setComponent(this.keyboardComponent = new NativeKeyboardComponent(this::openKeyboard, 0.5F));
      controller.setComponent(new SteamDeckComponent());
   }

   private void ensurePolling() {
      if (!this.runningPoller.get()) {
         this.logger.debugLog("Spinning up poller thread...");
         this.runningPoller.set(true);
         (new Thread(() -> {
            int failureScore = 0;

            while(this.runningPoller.get() && failureScore < 50) {
               try {
                  deck.poll().orTimeout(1000L, TimeUnit.MILLISECONDS).join();
               } catch (Throwable var3) {
                  if (var3 instanceof CompletionException && var3.getCause() instanceof TimeoutException) {
                     this.logger.debugWarn("Encountered timeout exception while polling deck, assuming deck has suspended and waiting...");
                     CUtil.sleepChecked(500L);
                     ++failureScore;
                  } else {
                     this.logger.error("Encountered exception while polling deck", var3);
                     failureScore += 10;
                  }
               }
            }

         }, "Steam Deck Poller")).start();
      }
   }

   public void update(ControllerEntity controller, boolean outOfFocus) {
      this.ensurePolling();
      ControllerStateImpl state = new ControllerStateImpl();
      ControllerState deckState = deck.getControllerState();
      boolean focused = deck.isGameInFocus();
      class_310.method_1551().method_15995(focused);
      state.setButton(GamepadInputs.NORTH_BUTTON, deckState.getButtonState(ControllerButton.Y) && focused);
      state.setButton(GamepadInputs.EAST_BUTTON, deckState.getButtonState(ControllerButton.B) && focused);
      state.setButton(GamepadInputs.SOUTH_BUTTON, deckState.getButtonState(ControllerButton.A) && focused);
      state.setButton(GamepadInputs.WEST_BUTTON, deckState.getButtonState(ControllerButton.X) && focused);
      state.setButton(GamepadInputs.LEFT_SHOULDER_BUTTON, deckState.getButtonState(ControllerButton.L1) && focused);
      state.setButton(GamepadInputs.RIGHT_SHOULDER_BUTTON, deckState.getButtonState(ControllerButton.R1) && focused);
      state.setButton(GamepadInputs.START_BUTTON, deckState.getButtonState(ControllerButton.START) && focused);
      state.setButton(GamepadInputs.BACK_BUTTON, deckState.getButtonState(ControllerButton.SELECT) && focused);
      state.setButton(GamepadInputs.LEFT_STICK_BUTTON, deckState.getButtonState(ControllerButton.L3) && focused);
      state.setButton(GamepadInputs.RIGHT_STICK_BUTTON, deckState.getButtonState(ControllerButton.R3) && focused);
      state.setButton(GamepadInputs.DPAD_UP_BUTTON, deckState.getButtonState(ControllerButton.DPAD_UP) && focused);
      state.setButton(GamepadInputs.DPAD_DOWN_BUTTON, deckState.getButtonState(ControllerButton.DPAD_DOWN) && focused);
      state.setButton(GamepadInputs.DPAD_LEFT_BUTTON, deckState.getButtonState(ControllerButton.DPAD_LEFT) && focused);
      state.setButton(GamepadInputs.DPAD_RIGHT_BUTTON, deckState.getButtonState(ControllerButton.DPAD_RIGHT) && focused);
      state.setButton(GamepadInputs.LEFT_PADDLE_1_BUTTON, deckState.getButtonState(ControllerButton.L4) && focused);
      state.setButton(GamepadInputs.LEFT_PADDLE_2_BUTTON, deckState.getButtonState(ControllerButton.L5) && focused);
      state.setButton(GamepadInputs.RIGHT_PADDLE_1_BUTTON, deckState.getButtonState(ControllerButton.R4) && focused);
      state.setButton(GamepadInputs.RIGHT_PADDLE_2_BUTTON, deckState.getButtonState(ControllerButton.R5) && focused);
      state.setButton(GamepadInputs.TOUCHPAD_1_BUTTON, deckState.getButtonState(ControllerButton.LEFT_TOUCHPAD_CLICK) && focused);
      state.setButton(GamepadInputs.TOUCHPAD_2_BUTTON, deckState.getButtonState(ControllerButton.RIGHT_TOUCHPAD_CLICK) && focused);
      state.setAxis(GamepadInputs.LEFT_STICK_AXIS_UP, zeroUnless(CUtil.positiveAxis(CUtil.mapShortToFloat(deckState.sLeftStickY())), focused));
      state.setAxis(GamepadInputs.LEFT_STICK_AXIS_DOWN, zeroUnless(CUtil.negativeAxis(CUtil.mapShortToFloat(deckState.sLeftStickY())), focused));
      state.setAxis(GamepadInputs.LEFT_STICK_AXIS_LEFT, zeroUnless(CUtil.negativeAxis(CUtil.mapShortToFloat(deckState.sLeftStickX())), focused));
      state.setAxis(GamepadInputs.LEFT_STICK_AXIS_RIGHT, zeroUnless(CUtil.positiveAxis(CUtil.mapShortToFloat(deckState.sLeftStickX())), focused));
      state.setAxis(GamepadInputs.RIGHT_STICK_AXIS_UP, zeroUnless(CUtil.positiveAxis(CUtil.mapShortToFloat(deckState.sRightStickY())), focused));
      state.setAxis(GamepadInputs.RIGHT_STICK_AXIS_DOWN, zeroUnless(CUtil.negativeAxis(CUtil.mapShortToFloat(deckState.sRightStickY())), focused));
      state.setAxis(GamepadInputs.RIGHT_STICK_AXIS_LEFT, zeroUnless(CUtil.negativeAxis(CUtil.mapShortToFloat(deckState.sRightStickX())), focused));
      state.setAxis(GamepadInputs.RIGHT_STICK_AXIS_RIGHT, zeroUnless(CUtil.positiveAxis(CUtil.mapShortToFloat(deckState.sRightStickX())), focused));
      state.setAxis(GamepadInputs.LEFT_TRIGGER_AXIS, zeroUnless(CUtil.mapShortToFloat(deckState.sTriggerL()), focused));
      state.setAxis(GamepadInputs.RIGHT_TRIGGER_AXIS, zeroUnless(CUtil.mapShortToFloat(deckState.sTriggerR()), focused));
      this.inputComponent.pushState(state);
      this.gyroComponent.setState((new GyroState(deckState.flSoftwareGyroDegreesPerSecondPitch(), -deckState.flSoftwareGyroDegreesPerSecondYaw(), -deckState.flSoftwareGyroDegreesPerSecondRoll())).mul(0.017453292F));
      this.batteryLevelComponent.setBatteryLevel(new PowerState.Depleting((int)(CUtil.mapShortToFloat(deckState.sBatteryLevel()) * 100.0F)));
      this.updateTouchpad(this.touchpadComponent.touchpads()[0], deckState.sLeftPadX(), deckState.sLeftPadY(), deckState.sPressurePadLeft(), deckState.getButtonState(ControllerButton.LEFT_TOUCHPAD_TOUCH));
      this.updateTouchpad(this.touchpadComponent.touchpads()[1], deckState.sRightPadX(), deckState.sRightPadY(), deckState.sPressurePadRight(), deckState.getButtonState(ControllerButton.RIGHT_TOUCHPAD_TOUCH));
   }

   private void updateTouchpad(Touchpads.Touchpad touchpad, short x, short y, short pressure, boolean touching) {
      if (touching) {
         float mappedX = (CUtil.mapShortToFloat(x) + 1.0F) / 2.0F;
         float mappedY = 1.0F - (CUtil.mapShortToFloat(y) + 1.0F) / 2.0F;
         float mappedPressure = CUtil.mapShortToFloat(pressure);
         touchpad.pushFingers(List.of(new Touchpads.Finger(0, new Vector2f(mappedX, mappedY), mappedPressure)));
      } else {
         touchpad.pushFingers(List.of());
      }

   }

   private void openKeyboard() {
      deck.openModalKeyboard(true);
   }

   public void close() {
      this.logger.debugLog("Closing driver...");

      try {
         this.runningPoller.set(false);
         deck.close();
      } catch (Exception var2) {
         this.logger.error("Failed to close driver", (Throwable)var2);
      }

   }

   public static Optional<SteamDeckDriver> create(ControlifyLogger logger) {
      if (!triedToLoad && Controlify.instance().config().globalSettings().useEnhancedSteamDeckDriver) {
         triedToLoad = true;

         try {
            deck = (SteamDeck)SteamDeckUtil.getDeckInstance().orElseThrow();
            return Optional.of(new SteamDeckDriver(logger));
         } catch (Exception var2) {
            logger.error("Failed to create Steam Deck driver", (Throwable)var2);
            return Optional.empty();
         }
      } else {
         return Optional.empty();
      }
   }

   public static Optional<SteamDeck> getDeck() {
      return Optional.ofNullable(deck);
   }

   private static float zeroUnless(float f, boolean condition) {
      return condition ? f : 0.0F;
   }
}
