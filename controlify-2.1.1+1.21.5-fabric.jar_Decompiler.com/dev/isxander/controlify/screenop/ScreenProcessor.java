package dev.isxander.controlify.screenop;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.InputMode;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.haptic.HapticEffects;
import dev.isxander.controlify.controller.input.ControllerStateView;
import dev.isxander.controlify.controller.input.GamepadInputs;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.mixins.feature.screenop.ScreenAccessor;
import dev.isxander.controlify.mixins.feature.screenop.vanilla.TabNavigationBarAccessor;
import dev.isxander.controlify.sound.ControlifyClientSounds;
import dev.isxander.controlify.utils.HoldRepeatHelper;
import dev.isxander.controlify.virtualmouse.VirtualMouseBehaviour;
import dev.isxander.controlify.virtualmouse.VirtualMouseHandler;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Stream;
import net.minecraft.class_1109;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.minecraft.class_364;
import net.minecraft.class_437;
import net.minecraft.class_8016;
import net.minecraft.class_8028;
import net.minecraft.class_8087;
import net.minecraft.class_8089;
import net.minecraft.class_8023.class_8024;

public class ScreenProcessor<T extends class_437> {
   public final T screen;
   protected final HoldRepeatHelper holdRepeatHelper = new HoldRepeatHelper(10, 3);
   protected static final class_310 minecraft = class_310.method_1551();
   private final List<ScreenControllerEventListener> eventListeners = new ArrayList();

   public ScreenProcessor(T screen) {
      this.screen = screen;
      if (screen instanceof ScreenControllerEventListener) {
         ScreenControllerEventListener eventListener = (ScreenControllerEventListener)screen;
         this.eventListeners.add(eventListener);
      }

   }

   public void onControllerUpdate(ControllerEntity controller) {
      Controlify.instance().virtualMouseHandler().handleControllerInput(controller);
      if (!Controlify.instance().virtualMouseHandler().isVirtualMouseEnabled()) {
         if (!this.handleComponentNavOverride(controller)) {
            this.handleComponentNavigation(controller);
         }

         if (!this.handleComponentButtonOverride(controller)) {
            this.handleButtons(controller);
         }
      } else {
         this.handleScreenVMouse(controller, Controlify.instance().virtualMouseHandler());
      }

      this.handleTabNavigation(controller);
      this.eventListeners.forEach((listener) -> {
         listener.onControllerInput(controller);
      });
   }

   public void render(ControllerEntity controller, class_332 graphics, float tickDelta) {
      VirtualMouseHandler vmouse = Controlify.instance().virtualMouseHandler();
      this.render(controller, graphics, tickDelta, vmouse.isVirtualMouseEnabled() ? Optional.of(vmouse) : Optional.empty());
   }

   public void onInputModeChanged(InputMode mode) {
      switch(mode) {
      case KEYBOARD_MOUSE:
         boolean shouldKeepFocus = this.getFocusTree().stream().anyMatch((component) -> {
            return ComponentProcessorProvider.provide(component).shouldKeepFocusOnKeyboardMode(this);
         });
         if (!shouldKeepFocus) {
            ((ScreenAccessor)this.screen).invokeClearFocus();
         }
         break;
      case CONTROLLER:
      case MIXED:
         if (!Controlify.instance().virtualMouseHandler().isVirtualMouseEnabled()) {
            this.setInitialFocus();
         }
      }

   }

   protected void handleComponentNavigation(ControllerEntity controller) {
      if (this.screen.method_25399() == null) {
         this.setInitialFocus();
      }

      List<class_364> focuses = List.copyOf(this.getFocusTree());
      ScreenAccessor accessor = (ScreenAccessor)this.screen;
      boolean repeatEventAvailable = this.holdRepeatHelper.canNavigate();
      InputComponent input = (InputComponent)controller.input().orElseThrow();
      ControllerStateView state = input.stateNow();
      ControllerStateView prevState = input.stateThen();
      InputBinding guiNaviRight = ControlifyBindings.GUI_NAVI_RIGHT.on(controller);
      InputBinding guiNaviLeft = ControlifyBindings.GUI_NAVI_LEFT.on(controller);
      InputBinding guiNaviUp = ControlifyBindings.GUI_NAVI_UP.on(controller);
      InputBinding guiNaviDown = ControlifyBindings.GUI_NAVI_DOWN.on(controller);
      class_8024 event = null;
      if (!guiNaviRight.digitalNow() || !repeatEventAvailable && guiNaviRight.digitalPrev()) {
         if (guiNaviLeft.digitalNow() && (repeatEventAvailable || !guiNaviLeft.digitalPrev())) {
            event = accessor.invokeCreateArrowEvent(class_8028.field_41828);
            if (!guiNaviLeft.digitalPrev()) {
               this.holdRepeatHelper.reset();
            }
         } else if (guiNaviUp.digitalNow() && (repeatEventAvailable || !guiNaviUp.digitalPrev())) {
            event = accessor.invokeCreateArrowEvent(class_8028.field_41826);
            if (!guiNaviUp.digitalPrev()) {
               this.holdRepeatHelper.reset();
            }
         } else if (guiNaviDown.digitalNow() && (repeatEventAvailable || !guiNaviDown.digitalPrev())) {
            event = accessor.invokeCreateArrowEvent(class_8028.field_41827);
            if (!guiNaviDown.digitalPrev()) {
               this.holdRepeatHelper.reset();
            }
         } else if (state.isButtonDown(GamepadInputs.DPAD_RIGHT_BUTTON) && (repeatEventAvailable || !prevState.isButtonDown(GamepadInputs.DPAD_RIGHT_BUTTON))) {
            event = accessor.invokeCreateArrowEvent(class_8028.field_41829);
            if (!prevState.isButtonDown(GamepadInputs.DPAD_RIGHT_BUTTON)) {
               this.holdRepeatHelper.reset();
            }
         } else if (state.isButtonDown(GamepadInputs.DPAD_LEFT_BUTTON) && (repeatEventAvailable || !prevState.isButtonDown(GamepadInputs.DPAD_LEFT_BUTTON))) {
            event = accessor.invokeCreateArrowEvent(class_8028.field_41828);
            if (!prevState.isButtonDown(GamepadInputs.DPAD_LEFT_BUTTON)) {
               this.holdRepeatHelper.reset();
            }
         } else if (state.isButtonDown(GamepadInputs.DPAD_UP_BUTTON) && (repeatEventAvailable || !prevState.isButtonDown(GamepadInputs.DPAD_UP_BUTTON))) {
            event = accessor.invokeCreateArrowEvent(class_8028.field_41826);
            if (!prevState.isButtonDown(GamepadInputs.DPAD_UP_BUTTON)) {
               this.holdRepeatHelper.reset();
            }
         } else if (state.isButtonDown(GamepadInputs.DPAD_DOWN_BUTTON) && (repeatEventAvailable || !prevState.isButtonDown(GamepadInputs.DPAD_DOWN_BUTTON))) {
            event = accessor.invokeCreateArrowEvent(class_8028.field_41827);
            if (!prevState.isButtonDown(GamepadInputs.DPAD_DOWN_BUTTON)) {
               this.holdRepeatHelper.reset();
            }
         }
      } else {
         event = accessor.invokeCreateArrowEvent(class_8028.field_41829);
         if (!guiNaviRight.digitalPrev()) {
            this.holdRepeatHelper.reset();
         }
      }

      if (event != null) {
         class_8016 path = this.screen.method_48205(event);
         if (path != null) {
            accessor.invokeChangeFocus(path);
            this.holdRepeatHelper.onNavigate();
            controller.input().ifPresent(InputComponent::notifyGuiPressOutputsOfNavigate);
            if (Controlify.instance().config().globalSettings().uiSounds) {
               minecraft.method_1483().method_4873(class_1109.method_4758((class_3414)ControlifyClientSounds.SCREEN_FOCUS_CHANGE.get(), 1.0F));
            }

            controller.hdHaptics().ifPresent((haptics) -> {
               haptics.playHaptic(HapticEffects.NAVIGATE);
            });
            Queue newFocusTree = this.getFocusTree();

            while(!newFocusTree.isEmpty() && !focuses.contains(newFocusTree.peek())) {
               ComponentProcessorProvider.provide((class_364)newFocusTree.poll()).onFocusGained(this, controller);
            }
         }
      }

   }

   protected void handleButtons(ControllerEntity controller) {
      boolean vmouseEnabled = Controlify.instance().virtualMouseHandler().isVirtualMouseEnabled();
      InputComponent input = (InputComponent)controller.input().orElseThrow();
      boolean touchpadPressed = input.stateNow().isButtonDown(GamepadInputs.TOUCHPAD_1_BUTTON);
      boolean prevTouchpadPressed = input.stateThen().isButtonDown(GamepadInputs.TOUCHPAD_1_BUTTON);
      if (ControlifyBindings.GUI_PRESS.on(controller).guiPressed().get() || vmouseEnabled && touchpadPressed && !prevTouchpadPressed) {
         this.screen.method_25404(257, 0, 0);
      }

      if (this.screen.method_25422() && ControlifyBindings.GUI_BACK.on(controller).guiPressed().get()) {
         playClackSound();
         this.screen.method_25419();
      }

   }

   protected void handleScreenVMouse(ControllerEntity controller, VirtualMouseHandler vmouse) {
   }

   protected boolean handleComponentButtonOverride(ControllerEntity controller) {
      Queue focusTree = this.getFocusTree();

      ComponentProcessor processor;
      do {
         if (focusTree.isEmpty()) {
            return false;
         }

         class_364 focused = (class_364)focusTree.poll();
         processor = ComponentProcessorProvider.provide(focused);
      } while(!processor.overrideControllerButtons(this, controller));

      return true;
   }

   protected boolean handleComponentNavOverride(ControllerEntity controller) {
      Queue focusTree = this.getFocusTree();

      ComponentProcessor processor;
      do {
         if (focusTree.isEmpty()) {
            return false;
         }

         class_364 focused = (class_364)focusTree.poll();
         processor = ComponentProcessorProvider.provide(focused);
      } while(!processor.overrideControllerNavigation(this, controller));

      return true;
   }

   protected void handleTabNavigation(ControllerEntity controller) {
      boolean nextTab = ControlifyBindings.GUI_NEXT_TAB.on(controller).justPressed();
      boolean prevTab = ControlifyBindings.GUI_PREV_TAB.on(controller).justPressed();
      if (nextTab || prevTab) {
         Stream var10000 = this.screen.method_25396().stream().filter((child) -> {
            return child instanceof class_8089;
         });
         Objects.requireNonNull(class_8089.class);
         var10000.map(class_8089.class::cast).findAny().ifPresent((navBar) -> {
            TabNavigationBarAccessor accessor = (TabNavigationBarAccessor)navBar;
            List<class_8087> tabs = accessor.getTabs();
            int currentIndex = tabs.indexOf(accessor.getTabManager().method_48614());
            int newIndex = currentIndex + (prevTab ? -1 : 1);
            if (newIndex < 0) {
               newIndex = tabs.size() - 1;
            }

            if (newIndex >= tabs.size()) {
               newIndex = 0;
            }

            navBar.method_48987(newIndex, true);
            this.onTabChanged(controller);
         });
      }

   }

   protected void onTabChanged(ControllerEntity controller) {
   }

   public void onWidgetRebuild() {
      this.setInitialFocus();
   }

   public void onVirtualMouseToggled(boolean enabled) {
      if (enabled) {
         ((ScreenAccessor)this.screen).invokeClearFocus();
      } else {
         this.setInitialFocus();
      }

   }

   protected void render(ControllerEntity controller, class_332 graphics, float tickDelta, Optional<VirtualMouseHandler> vmouse) {
   }

   protected void setInitialFocus() {
      if (this.screen.method_25399() == null && Controlify.instance().currentInputMode().isController() && !Controlify.instance().virtualMouseHandler().isVirtualMouseEnabled()) {
         ScreenAccessor accessor = (ScreenAccessor)this.screen;
         class_8016 path = this.screen.method_48205(accessor.invokeCreateArrowEvent(class_8028.field_41827));
         if (path != null) {
            accessor.invokeChangeFocus(path);
            this.holdRepeatHelper.clearDelay();
         }
      }

   }

   public VirtualMouseBehaviour virtualMouseBehaviour() {
      return VirtualMouseBehaviour.DEFAULT;
   }

   public void addEventListener(ScreenControllerEventListener listener) {
      this.eventListeners.add(listener);
   }

   protected Queue<class_364> getFocusTree() {
      if (this.screen.method_25399() == null) {
         return new ArrayDeque();
      } else {
         ArrayDeque<class_364> tree = new ArrayDeque();
         class_364 focused = this.screen.method_25399();
         tree.add(focused);

         while(focused instanceof CustomFocus) {
            CustomFocus customFocus = (CustomFocus)focused;
            focused = customFocus.getCustomFocus();
            if (focused != null) {
               tree.addFirst(focused);
            }
         }

         return tree;
      }
   }

   protected final Optional<class_339> getWidget(class_2561 message) {
      Stream var10000 = this.screen.method_25396().stream().filter((child) -> {
         return child instanceof class_339;
      });
      Objects.requireNonNull(class_339.class);
      return var10000.map(class_339.class::cast).filter((widget) -> {
         return widget.method_25369().equals(message);
      }).findAny();
   }

   protected final Optional<class_339> getWidget(String translationKey) {
      String translatedName = class_2561.method_43471(translationKey).getString();
      Stream var10000 = this.screen.method_25396().stream().filter((child) -> {
         return child instanceof class_339;
      });
      Objects.requireNonNull(class_339.class);
      return var10000.map(class_339.class::cast).filter((widget) -> {
         return widget.method_25369().getString().equals(translatedName);
      }).findAny();
   }

   public static void playClackSound() {
      minecraft.method_1483().method_4873(class_1109.method_47978(class_3417.field_15015, 1.0F));
   }
}
