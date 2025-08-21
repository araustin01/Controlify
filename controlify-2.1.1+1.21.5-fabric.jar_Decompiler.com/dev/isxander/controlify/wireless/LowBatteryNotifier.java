package dev.isxander.controlify.wireless;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.controlify.controller.battery.BatteryLevelComponent;
import dev.isxander.controlify.controller.battery.PowerState;
import dev.isxander.controlify.controllermanager.ControllerManager;
import dev.isxander.controlify.utils.ToastUtils;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.minecraft.class_2561;

public class LowBatteryNotifier {
   private static final Set<ControllerUID> notifiedControllers = new HashSet();
   private static int interval;

   public static void tick() {
      if (interval > 0) {
         --interval;
      } else {
         interval = 1200;
         if (Controlify.instance().config().globalSettings().notifyLowBattery) {
            ControllerManager controllerManager = (ControllerManager)Controlify.instance().getControllerManager().orElse((Object)null);
            if (controllerManager != null) {
               Iterator var1 = controllerManager.getConnectedControllers().iterator();

               while(true) {
                  while(var1.hasNext()) {
                     ControllerEntity controller = (ControllerEntity)var1.next();
                     PowerState batteryLevel = (PowerState)controller.batteryLevel().map(BatteryLevelComponent::getBatteryLevel).orElse(new PowerState.Unknown());
                     ControllerUID uid = controller.uid();
                     if (batteryLevel instanceof PowerState.Depleting) {
                        PowerState.Depleting var5 = (PowerState.Depleting)batteryLevel;
                        PowerState.Depleting var10000 = var5;

                        int var9;
                        try {
                           var9 = var10000.percent();
                        } catch (Throwable var8) {
                           throw new MatchException(var8.toString(), var8);
                        }

                        int var7 = var9;
                        if (var7 <= 10) {
                           if (!notifiedControllers.contains(uid)) {
                              ToastUtils.sendToast(class_2561.method_43471("controlify.toast.low_battery.title"), class_2561.method_43469("controlify.toast.low_battery.message", new Object[]{controller.name(), var7 + "%"}), true);
                              notifiedControllers.add(uid);
                           }
                           continue;
                        }
                     }

                     notifiedControllers.remove(uid);
                  }

                  return;
               }
            }
         }
      }
   }
}
