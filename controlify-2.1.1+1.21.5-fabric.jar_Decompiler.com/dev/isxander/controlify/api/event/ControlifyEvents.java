package dev.isxander.controlify.api.event;

import dev.isxander.controlify.InputMode;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.ingameguide.IngameGuideRegistry;
import dev.isxander.controlify.api.ingameinput.LookInputModifier;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.platform.EventHandler;

public final class ControlifyEvents {
   public static final EventHandler<ControlifyEvents.ControllerConnected> CONTROLLER_CONNECTED = EventHandler.createPlatformBackedEvent();
   public static final EventHandler<ControlifyEvents.ControllerDisconnected> CONTROLLER_DISCONNECTED = EventHandler.createPlatformBackedEvent();
   public static final EventHandler<ControlifyEvents.FinishedInit> FINISHED_INIT = EventHandler.createPlatformBackedEvent();
   public static final EventHandler<ControlifyEvents.InputModeChanged> INPUT_MODE_CHANGED = EventHandler.createPlatformBackedEvent();
   public static final EventHandler<ControlifyEvents.ControllerStateUpdate> ACTIVE_CONTROLLER_TICKED = EventHandler.createPlatformBackedEvent();
   /** @deprecated */
   @Deprecated
   public static final EventHandler<ControlifyEvents.ControllerStateUpdate> CONTROLLER_STATE_UPDATED;
   public static final EventHandler<ControlifyEvents.ControllerStateUpdate> CONTROLLER_STATE_UPDATE;
   public static final EventHandler<ControlifyEvents.IngameGuideRegistryEvent> INGAME_GUIDE_REGISTRY;
   public static final EventHandler<ControlifyEvents.VirtualMouseToggled> VIRTUAL_MOUSE_TOGGLED;
   public static final EventHandler<LookInputModifier> LOOK_INPUT_MODIFIER;

   static {
      CONTROLLER_STATE_UPDATED = ACTIVE_CONTROLLER_TICKED;
      CONTROLLER_STATE_UPDATE = EventHandler.createPlatformBackedEvent();
      INGAME_GUIDE_REGISTRY = EventHandler.createPlatformBackedEvent();
      VIRTUAL_MOUSE_TOGGLED = EventHandler.createPlatformBackedEvent();
      LOOK_INPUT_MODIFIER = EventHandler.createPlatformBackedEvent();
   }

   public static record VirtualMouseToggled(boolean enabled) {
      public VirtualMouseToggled(boolean enabled) {
         this.enabled = enabled;
      }

      public boolean enabled() {
         return this.enabled;
      }
   }

   public static record IngameGuideRegistryEvent(ControllerEntity bindings, IngameGuideRegistry registry) {
      public IngameGuideRegistryEvent(ControllerEntity bindings, IngameGuideRegistry registry) {
         this.bindings = bindings;
         this.registry = registry;
      }

      public ControllerEntity bindings() {
         return this.bindings;
      }

      public IngameGuideRegistry registry() {
         return this.registry;
      }
   }

   public static record ControllerStateUpdate(ControllerEntity controller) {
      public ControllerStateUpdate(ControllerEntity controller) {
         this.controller = controller;
      }

      public ControllerEntity controller() {
         return this.controller;
      }
   }

   public static record InputModeChanged(InputMode mode) {
      public InputModeChanged(InputMode mode) {
         this.mode = mode;
      }

      public InputMode mode() {
         return this.mode;
      }
   }

   public static record FinishedInit(ControlifyApi controlify) {
      public FinishedInit(ControlifyApi controlify) {
         this.controlify = controlify;
      }

      public ControlifyApi controlify() {
         return this.controlify;
      }
   }

   public static record ControllerDisconnected(ControllerEntity controller) {
      public ControllerDisconnected(ControllerEntity controller) {
         this.controller = controller;
      }

      public ControllerEntity controller() {
         return this.controller;
      }
   }

   public static record ControllerConnected(ControllerEntity controller, boolean hotplugged, boolean newController) {
      public ControllerConnected(ControllerEntity controller, boolean hotplugged, boolean newController) {
         this.controller = controller;
         this.hotplugged = hotplugged;
         this.newController = newController;
      }

      public ControllerEntity controller() {
         return this.controller;
      }

      public boolean hotplugged() {
         return this.hotplugged;
      }

      public boolean newController() {
         return this.newController;
      }
   }
}
