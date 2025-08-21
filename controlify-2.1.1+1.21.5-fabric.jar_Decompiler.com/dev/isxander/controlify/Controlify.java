package dev.isxander.controlify;

import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.event.ControlifyEvents;
import dev.isxander.controlify.bindings.BindContext;
import dev.isxander.controlify.bindings.ControlifyBindApiImpl;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.bindings.defaults.DefaultBindManager;
import dev.isxander.controlify.compatibility.ControlifyCompat;
import dev.isxander.controlify.config.ControlifyConfig;
import dev.isxander.controlify.config.GlobalSettings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.gyro.GyroComponent;
import dev.isxander.controlify.controller.id.ControllerTypeManager;
import dev.isxander.controlify.controller.input.ControllerState;
import dev.isxander.controlify.controller.input.ControllerStateView;
import dev.isxander.controlify.controller.input.HatState;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.controller.misc.BluetoothDeviceComponent;
import dev.isxander.controlify.controller.rumble.RumbleComponent;
import dev.isxander.controlify.controllermanager.ControllerManager;
import dev.isxander.controlify.controllermanager.GLFWControllerManager;
import dev.isxander.controlify.controllermanager.SDLControllerManager;
import dev.isxander.controlify.debug.DebugProperties;
import dev.isxander.controlify.driver.sdl.SDL3NativesManager;
import dev.isxander.controlify.driver.steamdeck.SteamDeckMode;
import dev.isxander.controlify.driver.steamdeck.SteamDeckUtil;
import dev.isxander.controlify.font.InputFontMapper;
import dev.isxander.controlify.gui.guide.InGameButtonGuide;
import dev.isxander.controlify.gui.screen.AskToMapControllerScreen;
import dev.isxander.controlify.gui.screen.BluetoothWarningScreen;
import dev.isxander.controlify.gui.screen.ControllerCalibrationScreen;
import dev.isxander.controlify.gui.screen.ControllerCarouselScreen;
import dev.isxander.controlify.gui.screen.DontInteruptScreen;
import dev.isxander.controlify.gui.screen.NoSDLScreen;
import dev.isxander.controlify.gui.screen.SDLOnboardingScreen;
import dev.isxander.controlify.gui.screen.SteamDeckAlerts;
import dev.isxander.controlify.hid.ControllerHIDService;
import dev.isxander.controlify.ingame.ControllerPlayerMovement;
import dev.isxander.controlify.ingame.InGameInputHandler;
import dev.isxander.controlify.mixins.feature.virtualmouse.MouseHandlerAccessor;
import dev.isxander.controlify.platform.client.PlatformClientUtil;
import dev.isxander.controlify.platform.main.PlatformMainUtil;
import dev.isxander.controlify.platform.network.SidedNetworkApi;
import dev.isxander.controlify.rumble.RumbleManager;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import dev.isxander.controlify.server.ControlifyHandshake;
import dev.isxander.controlify.server.ServerPolicies;
import dev.isxander.controlify.server.ServerPolicy;
import dev.isxander.controlify.server.packets.EntityVibrationPacket;
import dev.isxander.controlify.server.packets.OriginVibrationPacket;
import dev.isxander.controlify.server.packets.ServerPolicyPacket;
import dev.isxander.controlify.server.packets.VibrationPacket;
import dev.isxander.controlify.sound.ControlifyClientSounds;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.ControllerSetupWizard;
import dev.isxander.controlify.utils.ControllerUtils;
import dev.isxander.controlify.utils.DebugDump;
import dev.isxander.controlify.utils.DebugLog;
import dev.isxander.controlify.utils.InitialScreenRegistryDuck;
import dev.isxander.controlify.utils.ToastUtils;
import dev.isxander.controlify.utils.UnhandledCompletableFutures;
import dev.isxander.controlify.virtualmouse.VirtualMouseHandler;
import dev.isxander.controlify.wireless.LowBatteryNotifier;
import dev.isxander.yacl3.gui.YACLScreen;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.class_2561;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3673;
import net.minecraft.class_433;
import net.minecraft.class_437;
import net.minecraft.class_642;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.MixinEnvironment;

public class Controlify implements ControlifyApi {
   private static Controlify instance = null;
   private class_310 minecraft = null;
   private ControllerManager controllerManager;
   private boolean finishedInit = false;
   private boolean probeMode = false;
   private ControllerEntity currentController = null;
   private InputMode currentInputMode;
   private InGameInputHandler inGameInputHandler;
   public InGameButtonGuide inGameButtonGuide;
   private VirtualMouseHandler virtualMouseHandler;
   private InputFontMapper inputFontMapper;
   private DefaultBindManager defaultBindManager;
   private ControllerTypeManager controllerTypeManager;
   private Set<BindContext> thisTickContexts;
   private ControllerHIDService controllerHIDService;
   private CompletableFuture<Boolean> nativeOnboardingFuture;
   private final ControlifyConfig config;
   private final Queue<ControllerSetupWizard> setupWizards;
   private ControllerSetupWizard currentSetupWizard;
   private boolean hasDiscoveredControllers;
   private int consecutiveInputSwitches;
   private double lastInputSwitchTime;
   private int showMouseTicks;

   public Controlify() {
      this.currentInputMode = InputMode.KEYBOARD_MOUSE;
      this.nativeOnboardingFuture = null;
      this.config = new ControlifyConfig(this);
      this.setupWizards = new ArrayDeque();
      this.currentSetupWizard = null;
      this.hasDiscoveredControllers = false;
      this.consecutiveInputSwitches = 0;
      this.lastInputSwitchTime = 0.0D;
      this.showMouseTicks = 0;
   }

   public void preInitialiseControlify() {
      DebugProperties.printProperties();
      CUtil.LOGGER.log("Pre-initializing Controlify...");
      if (DebugProperties.MIXIN_AUDIT) {
         MixinEnvironment.getCurrentEnvironment().audit();
      }

      this.inputFontMapper = new InputFontMapper();
      this.defaultBindManager = new DefaultBindManager();
      this.controllerTypeManager = new ControllerTypeManager();
      PlatformClientUtil.registerAssetReloadListener(this.inputFontMapper);
      PlatformClientUtil.registerAssetReloadListener(this.defaultBindManager);
      PlatformClientUtil.registerAssetReloadListener(this.controllerTypeManager);
      this.controllerHIDService = new ControllerHIDService();
      this.controllerHIDService.start();
      this.registerBuiltinPack("legacy_console");
      ControlifyClientSounds.init();
      ControlifyHandshake.setupOnClient();
      SidedNetworkApi.S2C().listenForPacket(VibrationPacket.CHANNEL, (packet) -> {
         if (this.config().globalSettings().allowServerRumble) {
            this.getCurrentController().flatMap(ControllerEntity::rumble).ifPresent((rumble) -> {
               rumble.rumbleManager().play(packet.source(), packet.createEffect());
            });
         }

      });
      SidedNetworkApi.S2C().listenForPacket(OriginVibrationPacket.CHANNEL, (packet) -> {
         if (this.config().globalSettings().allowServerRumble) {
            this.getCurrentController().flatMap(ControllerEntity::rumble).ifPresent((rumble) -> {
               rumble.rumbleManager().play(packet.source(), packet.createEffect());
            });
         }

      });
      SidedNetworkApi.S2C().listenForPacket(EntityVibrationPacket.CHANNEL, (packet) -> {
         if (this.config().globalSettings().allowServerRumble) {
            this.getCurrentController().flatMap(ControllerEntity::rumble).ifPresent((rumble) -> {
               rumble.rumbleManager().play(packet.source(), packet.createEffect());
            });
         }

      });
      SidedNetworkApi.S2C().listenForPacket(ServerPolicyPacket.CHANNEL, (packet) -> {
         CUtil.LOGGER.log("Connected server specified '{}' policy is {}.", packet.id(), packet.allowed() ? "ALLOWED" : "DISALLOWED");
         ServerPolicies.getById(packet.id()).set(ServerPolicy.fromBoolean(packet.allowed()));
      });
      PlatformClientUtil.registerClientDisconnected((client) -> {
         DebugLog.log("Disconnected from server, resetting server policies");
         ServerPolicies.unsetAll();
      });
      PlatformClientUtil.addHudLayer(CUtil.rl("button_guide"), (graphics, deltaTracker) -> {
         this.inGameButtonGuide().ifPresent((guide) -> {
            guide.renderHud(graphics, deltaTracker.method_60637(false));
         });
      });
   }

   private void registerBuiltinPack(String id) {
      PlatformClientUtil.registerBuiltinResourcePack(CUtil.rl(id), class_2561.method_43471("controlify.extra_pack." + id + ".name"));
   }

   public void initializeControlify() {
      CUtil.LOGGER.log("Initializing Controlify...");
      this.minecraft = class_310.method_1551();
      this.inGameInputHandler = null;
      this.virtualMouseHandler = new VirtualMouseHandler();
      this.config().load();
      ControlifyEvents.CONTROLLER_DISCONNECTED.register((event) -> {
         this.onControllerRemoved(event.controller());
      });
      ControlifyBindings.registerModdedBindings();
      PlatformClientUtil.registerPostScreenRender((screen, graphics, mouseX, mouseY, tickDelta) -> {
         ControlifyApi.get().getCurrentController().ifPresent((controller) -> {
            this.virtualMouseHandler().renderVirtualMouse(graphics);
            ScreenProcessorProvider.provide(screen).render(controller, graphics, tickDelta);
         });
      });
      PlatformMainUtil.applyToControlifyEntrypoint((entrypoint) -> {
         try {
            entrypoint.onControlifyInit(this);
         } catch (Throwable var3) {
            CUtil.LOGGER.error("Failed to run `onControlifyInit` on Controlify entrypoint: {}", entrypoint.getClass().getName(), var3);
         }

      });
      if (this.config().globalSettings().isQuietMode()) {
         boolean controllersConnected = GLFWControllerManager.areControllersConnected();
         if (controllersConnected) {
            ToastUtils.sendToast(class_2561.method_43471("controlify.toast.setup_in_config.title"), class_2561.method_43469("controlify.toast.setup_in_config.description", new Object[]{class_2561.method_43471("options.title"), class_2561.method_43471("controls.title"), class_2561.method_43470("Controlify")}), false);
         } else {
            this.probeMode = true;
            PlatformClientUtil.registerClientTickEnded((client) -> {
               this.probeTick();
            });
         }
      } else {
         this.finishControlifyInit();
      }

      PlatformClientUtil.registerClientStopping((client) -> {
         this.controllerHIDService().stop();
      });
      if (this.config().globalSettings().useEnhancedSteamDeckDriver) {
         this.doSteamDeckChecks();
      }

   }

   private void doSteamDeckChecks() {
      CUtil.LOGGER.log("Steam Deck state: {}", SteamDeckUtil.DECK_MODE);
      if (SteamDeckUtil.IS_STEAM_DECK) {
         boolean connectedToCef = SteamDeckUtil.getDeckInstance().isPresent();
         if (!connectedToCef) {
            CUtil.LOGGER.error("Controlify could not connect to CEF debugger instance. Decky is probably not installed.");
            InitialScreenRegistryDuck.registerInitialScreen(SteamDeckAlerts::createDeckyRequiredWarning);
         }

         if (SteamDeckUtil.DECK_MODE == SteamDeckMode.DESKTOP_MODE) {
            CUtil.LOGGER.warn("Controlify is running in SteamOS desktop mode.");
            InitialScreenRegistryDuck.registerInitialScreen(SteamDeckAlerts::createDesktopModeWarning);
         }

         if (connectedToCef && SteamDeckUtil.DECK_MODE == SteamDeckMode.GAMING_MODE) {
            CUtil.LOGGER.log("Steam Deck is in gaming mode and Controlify has successfully connected to CEF.");
         }

      }
   }

   public void discoverControllers() {
      if (this.hasDiscoveredControllers) {
         CUtil.LOGGER.warn("Attempted to discover controllers twice!");
      } else {
         this.hasDiscoveredControllers = true;
         DebugLog.log("Discovering and initializing controllers...");
         this.controllerManager.discoverControllers();
         if (this.controllerManager.getConnectedControllers().isEmpty()) {
            CUtil.LOGGER.log("No controllers found.");
         }

         if (this.getCurrentController().isEmpty()) {
            Optional preferredController;
            if (this.config().currentControllerUid() == null) {
               preferredController = this.controllerManager.getConnectedControllers().stream().findAny();
            } else {
               preferredController = this.controllerManager.getConnectedControllers().stream().filter((c) -> {
                  return c.uid().equals(this.config().currentControllerUid());
               }).findAny();
            }

            this.setCurrentController((ControllerEntity)preferredController.orElse((Object)null), false);
         }

         this.config().saveIfDirty();
         PlatformMainUtil.applyToControlifyEntrypoint((entrypoint) -> {
            try {
               entrypoint.onControllersDiscovered(this);
            } catch (Throwable var3) {
               CUtil.LOGGER.error("Failed to run `onControllersDiscovered` on Controlify entrypoint: {}", entrypoint.getClass().getName(), var3);
            }

         });
      }
   }

   public CompletableFuture<Void> finishControlifyInit() {
      if (this.finishedInit) {
         return CompletableFuture.completedFuture((Object)null);
      } else {
         this.probeMode = false;
         this.finishedInit = true;
         return this.askNatives().whenComplete((loaded, th) -> {
            UnhandledCompletableFutures.run(() -> {
               CUtil.LOGGER.log("Finishing Controlify init...");
               if (!loaded) {
                  CUtil.LOGGER.warn("CONTROLIFY DID NOT LOAD SDL3 NATIVES. MANY FEATURES DISABLED!");
               }

               try {
                  this.controllerManager = (ControllerManager)(loaded ? new SDLControllerManager(CUtil.LOGGER) : new GLFWControllerManager(CUtil.LOGGER));
               } catch (Throwable var3) {
                  CUtil.LOGGER.error("Failed to initialize controller manager", var3);
                  return;
               }

               PlatformClientUtil.registerClientTickStarted(this::tick);
               ControlifyCompat.init();
               ControlifyBindApiImpl.INSTANCE.lock();
               if (this.config().globalSettings().isQuietMode()) {
                  this.config().globalSettings().quietMode = false;
                  this.config().setDirty();
               }

               this.discoverControllers();
               if (DebugProperties.INIT_DUMP) {
                  CUtil.LOGGER.log("\n{}", DebugDump.dumpDebug());
               }

               ControlifyEvents.FINISHED_INIT.invoke(new ControlifyEvents.FinishedInit(this));
            }, this.minecraft);
         }).thenApply((t) -> {
            return null;
         });
      }
   }

   public void onControllerAdded(ControllerEntity controller, boolean hotplugged, boolean newController) {
      ControllerSetupWizard wizard = new ControllerSetupWizard();
      boolean silentSetup = true;
      boolean calibrated = (Boolean)controller.input().map((input) -> {
         return ((InputComponent.Config)input.config().config()).deadzonesCalibrated;
      }).orElse(false) || (Boolean)controller.gyro().map((gyro) -> {
         return ((GyroComponent.Config)gyro.config().config()).calibrated;
      }).orElse(false);
      if (hotplugged && this.getCurrentController().isEmpty() && (this.config().currentControllerUid() == null || controller.uid().equals(this.config().currentControllerUid()))) {
         this.setCurrentController(controller, true);
      }

      wizard.addStage(() -> {
         Optional<InputComponent> inputOpt = controller.input();
         if (!inputOpt.isPresent()) {
            return false;
         } else {
            InputComponent input = (InputComponent)inputOpt.get();
            return !input.isDefinitelyGamepad() && ((InputComponent.Config)input.confObj()).mapping == null;
         }
      }, (nextScreen) -> {
         return new AskToMapControllerScreen(controller, nextScreen);
      });
      if (!silentSetup) {
         wizard.addStage(() -> {
            return !calibrated;
         }, (nextScreen) -> {
            return new ControllerCalibrationScreen(controller, nextScreen);
         });
      }

      if (!silentSetup) {
         wizard.addStage(() -> {
            return controller.dualSense().isPresent() && (Boolean)controller.bluetooth().map((bt) -> {
               return !((BluetoothDeviceComponent.Config)bt.confObj()).dontShowWarningAgain;
            }).orElse(false);
         }, (nextScreen) -> {
            return new BluetoothWarningScreen((BluetoothDeviceComponent)controller.bluetooth().orElseThrow(), nextScreen);
         });
      }

      if (hotplugged) {
         ToastUtils.sendToast(class_2561.method_43471("controlify.toast.controller_connected.title"), class_2561.method_43469("controlify.toast.controller_connected.description", new Object[]{controller.name()}), false);
      }

      class_437 var8 = this.minecraft.field_1755;
      if (var8 instanceof ControllerCarouselScreen) {
         ControllerCarouselScreen controllerListScreen = (ControllerCarouselScreen)var8;
         controllerListScreen.refreshControllers();
      }

      if (hotplugged) {
         this.config().saveIfDirty();
      }

      this.setupWizards.add(wizard);
      ControlifyEvents.CONTROLLER_CONNECTED.invoke(new ControlifyEvents.ControllerConnected(controller, hotplugged, newController));
   }

   private void onControllerRemoved(ControllerEntity controller) {
      if (this.getCurrentController().isPresent() && ((ControllerEntity)this.getCurrentController().get()).equals(controller)) {
         this.setCurrentController((ControllerEntity)null, true);
         this.setInputMode(InputMode.KEYBOARD_MOUSE);
      }

      ToastUtils.sendToast(class_2561.method_43471("controlify.toast.controller_disconnected.title"), class_2561.method_43469("controlify.toast.controller_disconnected.description", new Object[]{controller.name()}), false);
   }

   public CompletableFuture<Boolean> askNatives() {
      if (this.nativeOnboardingFuture != null) {
         return this.nativeOnboardingFuture;
      } else {
         GlobalSettings settings = this.config().globalSettings();
         if ((!settings.vibrationOnboarded || settings.loadVibrationNatives) && SDL3NativesManager.tryOfflineLoadAndStart()) {
            settings.vibrationOnboarded = true;
            settings.loadVibrationNatives = true;
            this.config().setDirty();
            return this.nativeOnboardingFuture = CompletableFuture.completedFuture(true);
         } else if (!SDL3NativesManager.isSupportedOnThisPlatform()) {
            CUtil.LOGGER.warn("SDL is not supported on this platform. Platform: {}", SDL3NativesManager.Target.CURRENT);
            this.nativeOnboardingFuture = new CompletableFuture();
            this.minecraft.method_1507(new NoSDLScreen(() -> {
               this.nativeOnboardingFuture.complete(false);
            }, this.minecraft.field_1755));
            return this.nativeOnboardingFuture;
         } else if (this.config().globalSettings().vibrationOnboarded) {
            return this.config().globalSettings().loadVibrationNatives ? (this.nativeOnboardingFuture = SDL3NativesManager.maybeLoad()) : (this.nativeOnboardingFuture = CompletableFuture.completedFuture(false));
         } else {
            this.nativeOnboardingFuture = new CompletableFuture();
            InitialScreenRegistryDuck.registerInitialScreen((runnable) -> {
               return new SDLOnboardingScreen(runnable, (answer) -> {
                  if (answer) {
                     SDL3NativesManager.maybeLoad().whenComplete((loaded, th) -> {
                        if (th != null) {
                           this.nativeOnboardingFuture.completeExceptionally(th);
                        } else {
                           this.nativeOnboardingFuture.complete(loaded);
                        }

                     });
                  } else {
                     this.nativeOnboardingFuture.complete(false);
                  }

               });
            });
            return this.nativeOnboardingFuture;
         }
      }
   }

   public void tick(class_310 client) {
      if (this.minecraft.method_18506() == null) {
         if (this.currentSetupWizard != null && this.currentSetupWizard.isDone()) {
            this.currentSetupWizard = null;
         }

         if (!this.setupWizards.isEmpty() && !(this.minecraft.field_1755 instanceof DontInteruptScreen)) {
            this.currentSetupWizard = (ControllerSetupWizard)this.setupWizards.poll();
            this.minecraft.method_1507(this.currentSetupWizard.start(this.minecraft.field_1755));
         }
      }

      boolean outOfFocus = !this.config().globalSettings().outOfFocusInput && !client.method_1569();
      this.thisTickContexts = (Set)BindContext.REGISTRY.method_10220().filter((ctx) -> {
         return (Boolean)ctx.isApplicable().apply(this.minecraft);
      }).collect(Collectors.toUnmodifiableSet());
      this.controllerManager.tick(outOfFocus);
      if (this.minecraft.field_1729.method_1613()) {
         this.showMouseTicks = 0;
      }

      if (this.currentInputMode() == InputMode.MIXED && this.showMouseTicks > 0) {
         --this.showMouseTicks;
         if (this.showMouseTicks == 0) {
            this.hideMouse(true, false);
            if (this.virtualMouseHandler().requiresVirtualMouse()) {
               this.virtualMouseHandler().enableVirtualMouse();
            }
         }
      }

      LowBatteryNotifier.tick();
      this.getCurrentController().ifPresent((currentController) -> {
         ControllerUtils.wrapControllerError(() -> {
            this.tickController(currentController, outOfFocus);
         }, "Ticking current controller", currentController);
      });
   }

   private void tickController(ControllerEntity controller, boolean outOfFocus) {
      InputComponent input = (InputComponent)controller.input().orElseThrow();
      ControllerStateView state = input.stateNow();
      Optional<RumbleManager> rumbleManager = controller.rumble().map(RumbleComponent::rumbleManager);
      boolean isPaused = this.minecraft.method_1493() || this.minecraft.field_1755 instanceof class_433;
      boolean isConfigScreen = this.minecraft.field_1755 instanceof YACLScreen;
      rumbleManager.ifPresent((rumble) -> {
         rumble.setSilent(outOfFocus || isPaused && !isConfigScreen);
      });
      if (outOfFocus) {
         state = ControllerState.EMPTY;
      } else {
         rumbleManager.ifPresent(RumbleManager::tick);
      }

      boolean var9;
      label49: {
         Stream var10000 = ((ControllerStateView)state).getButtons().stream();
         Objects.requireNonNull(state);
         if (!var10000.anyMatch(state::isButtonDown)) {
            var10000 = ((ControllerStateView)state).getAxes().stream();
            Objects.requireNonNull(state);
            if (!var10000.map(state::getAxisState).anyMatch((axis) -> {
               return Math.abs(axis) > 0.1F;
            })) {
               var10000 = ((ControllerStateView)state).getHats().stream();
               Objects.requireNonNull(state);
               if (!var10000.map(state::getHatState).anyMatch((hat) -> {
                  return hat != HatState.CENTERED;
               })) {
                  var9 = false;
                  break label49;
               }
            }
         }

         var9 = true;
      }

      boolean givingInput = var9;
      if (givingInput) {
         this.minecraft.method_61964().method_61939();
         if (!this.currentInputMode().isController()) {
            this.setInputMode(((InputComponent.Config)input.confObj()).mixedInput ? InputMode.MIXED : InputMode.CONTROLLER);
            return;
         }
      }

      if (this.consecutiveInputSwitches > 100) {
         CUtil.LOGGER.warn("Controlify detected current controller to be constantly giving input and has been disabled.");
         ToastUtils.sendToast(class_2561.method_43471("controlify.toast.faulty_input.title"), class_2561.method_43471("controlify.toast.faulty_input.description"), true);
         this.setCurrentController((ControllerEntity)null, true);
         this.consecutiveInputSwitches = 0;
      } else {
         if (this.minecraft.field_1687 != null) {
            this.inGameInputHandler().ifPresent(InGameInputHandler::inputTick);
         }

         if (this.currentInputMode().isController()) {
            if (this.minecraft.field_1755 != null) {
               ScreenProcessorProvider.provide(this.minecraft.field_1755).onControllerUpdate(controller);
            }

            ControlifyEvents.ACTIVE_CONTROLLER_TICKED.invoke(new ControlifyEvents.ControllerStateUpdate(controller));
         }

      }
   }

   private void probeTick() {
      if (this.probeMode && GLFWControllerManager.areControllersConnected()) {
         this.probeMode = false;
         this.minecraft.execute(this::finishControlifyInit);
      }

   }

   public ControlifyConfig config() {
      return this.config;
   }

   @NotNull
   public Optional<ControllerEntity> getCurrentController() {
      return Optional.ofNullable(this.currentController);
   }

   public void setCurrentController(@Nullable ControllerEntity controller, boolean changeInputMode) {
      if (this.currentController != controller) {
         this.currentController = controller;
         if (controller == null) {
            this.setInputMode(InputMode.KEYBOARD_MOUSE);
            this.inGameInputHandler = null;
            this.inGameButtonGuide = null;
            DebugLog.log("Updated current controller to null");
            this.config().save();
         } else {
            DebugLog.log("Updated current controller to {}({})", controller.name(), controller.uid());
            if (!controller.uid().equals(this.config().currentControllerUid())) {
               this.config().setCurrentControllerUid(controller.uid());
               this.config().setDirty();
            }

            this.inGameInputHandler = new InGameInputHandler(controller);
            ControllerPlayerMovement.ensureCorrectInput(this.minecraft.field_1724);
            if ((Boolean)controller.input().map((input) -> {
               return ((InputComponent.Config)input.config().config()).mixedInput;
            }).orElse(false)) {
               this.setInputMode(InputMode.MIXED);
            } else if (changeInputMode) {
               this.setInputMode(InputMode.CONTROLLER);
            }

            this.config().saveIfDirty();
         }
      }
   }

   public Optional<ControllerManager> getControllerManager() {
      return Optional.ofNullable(this.controllerManager);
   }

   public Optional<InGameInputHandler> inGameInputHandler() {
      return Optional.ofNullable(this.inGameInputHandler);
   }

   public Optional<InGameButtonGuide> inGameButtonGuide() {
      return Optional.ofNullable(this.inGameButtonGuide);
   }

   public VirtualMouseHandler virtualMouseHandler() {
      return this.virtualMouseHandler;
   }

   public ControllerHIDService controllerHIDService() {
      return this.controllerHIDService;
   }

   @NotNull
   public InputMode currentInputMode() {
      return this.currentInputMode;
   }

   public boolean setInputMode(@NotNull InputMode currentInputMode) {
      if (this.currentInputMode == currentInputMode) {
         return false;
      } else {
         this.currentInputMode = currentInputMode;
         if (!this.minecraft.field_1729.method_1613()) {
            this.hideMouse(currentInputMode.isController(), true);
         }

         if (this.minecraft.field_1755 != null) {
            ScreenProcessorProvider.provide(this.minecraft.field_1755).onInputModeChanged(currentInputMode);
         }

         if (class_310.method_1551().field_1724 != null) {
            if (currentInputMode == InputMode.KEYBOARD_MOUSE) {
               this.inGameButtonGuide = null;
            } else {
               this.inGameButtonGuide = (InGameButtonGuide)this.getCurrentController().map((c) -> {
                  return new InGameButtonGuide(c, class_310.method_1551().field_1724);
               }).orElse((Object)null);
            }
         }

         if (class_3673.method_15974() - this.lastInputSwitchTime < 20.0D) {
            ++this.consecutiveInputSwitches;
         } else {
            this.consecutiveInputSwitches = 0;
         }

         this.lastInputSwitchTime = class_3673.method_15974();
         if (this.currentInputMode.isController()) {
            this.getCurrentController().flatMap(ControllerEntity::input).ifPresent((state) -> {
               state.rawStateNow().clearState();
               state.rawStateThen().clearState();
            });
            if (this.minecraft.method_1558() != null) {
               this.notifyNewServer(this.minecraft.method_1558());
            }
         }

         class_304.method_52231();
         ControllerPlayerMovement.updatePlayerInput(this.minecraft.field_1724);
         ControlifyEvents.INPUT_MODE_CHANGED.invoke(new ControlifyEvents.InputModeChanged(currentInputMode));
         return true;
      }
   }

   public void hideMouse(boolean hide, boolean moveMouse) {
      GLFW.glfwSetInputMode(this.minecraft.method_22683().method_4490(), 208897, hide ? 212994 : 212993);
      if (this.minecraft.field_1755 != null) {
         MouseHandlerAccessor mouseHandlerAccessor = (MouseHandlerAccessor)this.minecraft.field_1729;
         if (hide && !this.virtualMouseHandler().isVirtualMouseEnabled() && moveMouse) {
            mouseHandlerAccessor.invokeOnMove(this.minecraft.method_22683().method_4490(), -50.0D, -50.0D);
         }
      }

   }

   public void showCursorTemporarily() {
      if (this.currentInputMode() == InputMode.MIXED && !this.minecraft.field_1729.method_1613()) {
         this.hideMouse(false, false);
         this.showMouseTicks = 40;
         if (this.virtualMouseHandler().isVirtualMouseEnabled()) {
            this.virtualMouseHandler().disableVirtualMouse();
         }
      }

   }

   public InputFontMapper inputFontMapper() {
      return this.inputFontMapper;
   }

   public DefaultBindManager defaultBindManager() {
      return this.defaultBindManager;
   }

   public ControllerTypeManager controllerTypeManager() {
      return this.controllerTypeManager;
   }

   public Set<BindContext> thisTickBindContexts() {
      return this.thisTickContexts;
   }

   public void notifyNewServer(class_642 data) {
      if (this.currentInputMode().isController()) {
         if (this.config().globalSettings().seenServers.add(data.field_3761)) {
            ToastUtils.sendToast(class_2561.method_43471("controlify.toast.new_server.title"), class_2561.method_43469("controlify.toast.new_server.description", new Object[]{data.field_3752}), true);
            this.config().save();
         }

      }
   }

   public static Controlify instance() {
      if (instance == null) {
         instance = new Controlify();
      }

      return instance;
   }
}
