package dev.isxander.controlify.controllermanager;

import com.google.common.collect.ImmutableList;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.event.ControlifyEvents;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.controlify.driver.steamdeck.SteamDeckUtil;
import dev.isxander.controlify.hid.ControllerHIDService;
import dev.isxander.controlify.hid.HIDDevice;
import dev.isxander.controlify.hid.HIDIdentifier;
import dev.isxander.controlify.utils.ControllerUtils;
import dev.isxander.controlify.utils.log.ControlifyLogger;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.class_128;
import net.minecraft.class_129;
import net.minecraft.class_310;
import net.minecraft.class_5912;

public abstract class AbstractControllerManager implements ControllerManager {
   protected final Controlify controlify = Controlify.instance();
   protected final class_310 minecraft = class_310.method_1551();
   protected final Map<UniqueControllerID, ControllerEntity> controllersByJid = new Object2ObjectOpenHashMap();
   protected final Map<ControllerUID, ControllerEntity> controllersByUid = new Object2ObjectOpenHashMap();
   protected final Map<ControllerUID, ControllerHIDService.ControllerHIDInfo> hidInfoByUid = new Object2ObjectOpenHashMap();
   protected final ControlifyLogger logger;

   public AbstractControllerManager(ControlifyLogger logger) {
      this.logger = logger.createSubLogger("ControllerManager");
      this.loadGamepadMappings(this.minecraft.method_1478());
   }

   public Optional<ControllerEntity> tryCreate(UniqueControllerID ucid, ControllerHIDService.ControllerHIDInfo hidInfo) {
      ControlifyLogger controllerLogger = this.logger.createSubLogger("Controller #" + String.valueOf(ucid));

      try {
         if (this.controllersByJid.containsKey(ucid)) {
            controllerLogger.warn("Tried to create controller that already is initialised: {}.", ucid);
            return Optional.empty();
         } else if (hidInfo.type().dontLoad()) {
            controllerLogger.debugLog("Preventing load of controller #" + String.valueOf(ucid) + " because its type prevents loading.");
            return Optional.empty();
         } else if (hidInfo.type().isSteamDeck() && SteamDeckUtil.DECK_MODE.isDesktopMode()) {
            controllerLogger.log("Preventing load of controller #{} because Steam Deck is in desktop mode.", ucid);
            return Optional.empty();
         } else {
            return this.createController(ucid, hidInfo, controllerLogger);
         }
      } catch (Throwable var7) {
         controllerLogger.error("Failed to create controller #{}!", var7, ucid);
         String var10001 = String.valueOf(ucid);
         class_128 crashReport = class_128.method_560(var7, "Creating controller #" + var10001);
         class_129 category = crashReport.method_562("Controller Info");
         category.method_578("Unique controller ID", ucid);
         category.method_578("Controller identification", hidInfo.type());
         category.method_578("HID path", hidInfo.hidDevice().map(HIDDevice::path).orElse("N/A"));
         category.method_578("HID service status", Controlify.instance().controllerHIDService().isDisabled() ? "Disabled" : "Enabled");
         category.method_578("System name", Optional.ofNullable(this.getControllerSystemName(ucid)).orElse("N/A"));
         controllerLogger.crashReport(crashReport);
         return Optional.empty();
      }
   }

   protected abstract Optional<ControllerEntity> createController(UniqueControllerID var1, ControllerHIDService.ControllerHIDInfo var2, ControlifyLogger var3);

   public void tick(boolean outOfFocus) {
      Iterator var2 = this.controllersByUid.values().iterator();

      while(var2.hasNext()) {
         ControllerEntity controller = (ControllerEntity)var2.next();
         controller.update(outOfFocus);
         ControlifyEvents.CONTROLLER_STATE_UPDATE.invoke(new ControlifyEvents.ControllerStateUpdate(controller));
      }

   }

   protected void onControllerConnected(ControllerEntity controller, boolean hotplug) {
      boolean newController = this.controlify.config().loadControllerConfig(controller);
      this.logger.log("Controller connected: {}", ControllerUtils.createControllerString(controller));
      this.controlify.onControllerAdded(controller, hotplug, newController);
   }

   protected void onControllerRemoved(ControllerEntity controller) {
      this.logger.log("Controller disconnected: {}", ControllerUtils.createControllerString(controller));
      this.closeController(controller.uid());
      ControlifyEvents.CONTROLLER_DISCONNECTED.invoke(new ControlifyEvents.ControllerDisconnected(controller));
   }

   public Optional<ControllerEntity> reinitController(ControllerEntity controller, ControllerHIDService.ControllerHIDInfo hidInfo) {
      this.onControllerRemoved(controller);
      Optional<ControllerEntity> newController = this.tryCreate(controller.info().ucid(), hidInfo);
      newController.ifPresent((c) -> {
         ControllerUtils.wrapControllerError(() -> {
            this.onControllerConnected(c, true);
         }, "Connecting controller", c);
      });
      return newController;
   }

   protected void addController(UniqueControllerID ucid, ControllerEntity controller) {
      this.controllersByUid.put(controller.uid(), controller);
      this.controllersByJid.put(ucid, controller);
   }

   public void closeController(ControllerUID uid) {
      ControllerEntity controller = (ControllerEntity)this.controllersByUid.remove(uid);
      if (controller != null) {
         controller.close();
         this.controllersByJid.remove(controller.info().ucid());
         Optional var10000 = Optional.ofNullable((ControllerHIDService.ControllerHIDInfo)this.hidInfoByUid.remove(uid));
         ControllerHIDService var10001 = this.controlify.controllerHIDService();
         Objects.requireNonNull(var10001);
         var10000.ifPresent(var10001::unconsumeController);
      }
   }

   public List<ControllerEntity> getConnectedControllers() {
      return ImmutableList.copyOf(this.controllersByUid.values());
   }

   public Optional<ControllerEntity> getController(ControllerUID uid) {
      return Optional.ofNullable((ControllerEntity)this.controllersByUid.get(uid));
   }

   public boolean isControllerConnected(ControllerUID uid) {
      return this.controllersByUid.containsKey(uid);
   }

   protected int getControllerCountWithMatchingHID(HIDIdentifier hid) {
      return (int)this.controllersByJid.values().stream().filter((c) -> {
         return c.info().hid().isPresent() && ((HIDDevice)c.info().hid().get()).asIdentifier().equals(hid);
      }).count();
   }

   public void close() {
      this.controllersByUid.values().forEach(ControllerEntity::close);
   }

   protected abstract void loadGamepadMappings(class_5912 var1);

   protected abstract String getControllerSystemName(UniqueControllerID var1);
}
