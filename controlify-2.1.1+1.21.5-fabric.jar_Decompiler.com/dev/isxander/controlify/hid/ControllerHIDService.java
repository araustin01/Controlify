package dev.isxander.controlify.hid;

import com.google.common.primitives.Ints;
import com.mojang.datafixers.util.Pair;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.controller.id.ControllerType;
import dev.isxander.controlify.debug.DebugProperties;
import dev.isxander.controlify.driver.sdl.SDL3NativesManager;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.ToastUtils;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HexFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import net.minecraft.class_2561;
import org.hid4java.HidDevice;
import org.hid4java.HidException;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.hid4java.HidServicesSpecification;
import org.hid4java.ScanMode;

public class ControllerHIDService {
   private final HidServicesSpecification specification = new HidServicesSpecification();
   private HidServices services;
   private final Queue<Pair<HidDevice, HIDIdentifier>> unconsumedControllerHIDs;
   private final Map<String, HidDevice> attachedDevices = new HashMap();
   private boolean disabled = false;
   private boolean firstFetch = true;
   private static final Set<Integer> CONTROLLER_USAGE_IDS = Set.of(4, 5, 8);

   public ControllerHIDService() {
      this.specification.setAutoStart(false);
      this.specification.setScanMode(ScanMode.NO_SCAN);
      this.unconsumedControllerHIDs = new ArrayBlockingQueue(50);
   }

   public void start() {
      try {
         this.services = HidManager.getHidServices(this.specification);
         this.services.start();
      } catch (HidException var2) {
         CUtil.LOGGER.error("Failed to start controller HID service! If you are on Linux using flatpak or snap, this is likely because your launcher has not added libusb to their package.", (Throwable)var2);
         this.disabled = true;
      }

   }

   public void stop() {
      if (!this.disabled && this.services != null) {
         this.services.shutdown();
         this.disabled = true;
      }

   }

   public ControllerHIDService.ControllerHIDInfo fetchType(int jid) {
      ControllerHIDService.ControllerHIDInfo info;
      try {
         info = this.fetchType0(jid);
      } catch (Throwable var4) {
         CUtil.LOGGER.error("Failed to fetch controller type!", var4);
         info = new ControllerHIDService.ControllerHIDInfo(ControllerType.DEFAULT, Optional.empty());
      }

      if (DebugProperties.PRINT_VID_PID) {
         info.hidDevice.ifPresent((hid) -> {
            HexFormat hex = HexFormat.of().withPrefix("0x");
            CUtil.LOGGER.log("VID: {}, PID: {}", hex.toHexDigits(hid.vendorId()), hex.toHexDigits(hid.productId()));
         });
      }

      return info;
   }

   private ControllerHIDService.ControllerHIDInfo fetchType0(int jid) {
      if (this.firstFetch) {
         this.firstFetch = false;
         if (this.isDisabled() && !SDL3NativesManager.isLoaded() && Controlify.instance().controllerHIDService().isDisabled() && !SDL3NativesManager.isLoaded()) {
            ToastUtils.sendToast(class_2561.method_43471("controlify.error.hid"), class_2561.method_43471("controlify.error.hid.desc"), true);
         }
      }

      if (this.disabled) {
         return new ControllerHIDService.ControllerHIDInfo(ControllerType.DEFAULT, Optional.empty());
      } else {
         this.doScanOnThisThread();
         Pair<HidDevice, HIDIdentifier> hid = (Pair)this.unconsumedControllerHIDs.poll();
         if (hid == null) {
            CUtil.LOGGER.warn("No controller found via USB hardware scan! Using SDL if available.");
            return new ControllerHIDService.ControllerHIDInfo(ControllerType.DEFAULT, Optional.empty());
         } else {
            ControllerType type = Controlify.instance().controllerTypeManager().getControllerType((HIDIdentifier)hid.getSecond());
            this.unconsumedControllerHIDs.removeIf((h) -> {
               return ((HidDevice)hid.getFirst()).getPath().equals(((HidDevice)h.getFirst()).getPath());
            });
            return new ControllerHIDService.ControllerHIDInfo(type, Optional.of(new HIDDevice.Hid4Java((HidDevice)hid.getFirst())));
         }
      }
   }

   public boolean isDisabled() {
      return this.disabled;
   }

   private void doScanOnThisThread() {
      List<String> removeList = new ArrayList();
      List<HidDevice> attachedHidDeviceList = this.services.getAttachedHidDevices();
      Iterator var3 = attachedHidDeviceList.iterator();

      while(var3.hasNext()) {
         HidDevice attachedDevice = (HidDevice)var3.next();
         if (!this.attachedDevices.containsKey(attachedDevice.getId())) {
            this.attachedDevices.put(attachedDevice.getId(), attachedDevice);
            HIDIdentifier identifier = new HIDIdentifier(attachedDevice.getVendorId(), attachedDevice.getProductId());
            if (this.isController(attachedDevice)) {
               this.unconsumedControllerHIDs.add(new Pair(attachedDevice, identifier));
            }
         }
      }

      var3 = this.attachedDevices.entrySet().iterator();

      while(var3.hasNext()) {
         Entry<String, HidDevice> entry = (Entry)var3.next();
         String deviceId = (String)entry.getKey();
         HidDevice hidDevice = (HidDevice)entry.getValue();
         if (!attachedHidDeviceList.contains(hidDevice)) {
            removeList.add(deviceId);
            this.unconsumedControllerHIDs.removeIf((device) -> {
               return ((HidDevice)this.attachedDevices.get(deviceId)).getPath().equals(((HidDevice)device.getFirst()).getPath());
            });
         }
      }

      if (!removeList.isEmpty()) {
         Set var10001 = this.attachedDevices.keySet();
         Objects.requireNonNull(var10001);
         removeList.forEach(var10001::remove);
      }

   }

   public void unconsumeController(ControllerHIDService.ControllerHIDInfo hid) {
      hid.hidDevice.ifPresent((device) -> {
         this.attachedDevices.remove(device.path());
      });
   }

   private boolean isController(HidDevice device) {
      boolean isControllerType = Controlify.instance().controllerTypeManager().getTypeMap().containsKey(new HIDIdentifier(device.getVendorId(), device.getProductId()));
      boolean isGenericDesktopControlOrGameControl = device.getUsagePage() == 1 || device.getUsagePage() == 5;
      boolean isSelfIdentifiedController = CONTROLLER_USAGE_IDS.contains(device.getUsage());
      return isControllerType || isGenericDesktopControlOrGameControl && isSelfIdentifiedController;
   }

   public static record ControllerHIDInfo(ControllerType type, Optional<HIDDevice> hidDevice) {
      public ControllerHIDInfo(ControllerType type, Optional<HIDDevice> hidDevice) {
         this.type = type;
         this.hidDevice = hidDevice;
      }

      public Optional<String> createControllerUID(int controllerIndex) {
         MessageDigest md;
         try {
            md = MessageDigest.getInstance("SHA-1");
         } catch (NoSuchAlgorithmException var4) {
            throw new RuntimeException(var4);
         }

         md.update(Ints.toByteArray(controllerIndex));
         this.hidDevice.ifPresent((hid) -> {
            md.update(Ints.toByteArray(hid.vendorId()));
            md.update(Ints.toByteArray(hid.productId()));
         });
         String namespace = this.type().namespace().toString();
         if ("controlify".equals(this.type().namespace().method_12836())) {
            namespace = this.type().namespace().method_12832();
         }

         md.update(namespace.getBytes());
         return Optional.of(UUID.nameUUIDFromBytes(md.digest()).toString());
      }

      public ControllerType type() {
         return this.type;
      }

      public Optional<HIDDevice> hidDevice() {
         return this.hidDevice;
      }
   }
}
