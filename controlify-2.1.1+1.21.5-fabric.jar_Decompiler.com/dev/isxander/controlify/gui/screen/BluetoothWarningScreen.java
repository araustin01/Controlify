package dev.isxander.controlify.gui.screen;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.controller.misc.BluetoothDeviceComponent;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_410;
import net.minecraft.class_437;
import net.minecraft.class_5244;

public class BluetoothWarningScreen extends class_410 {
   public BluetoothWarningScreen(BluetoothDeviceComponent bt, class_437 nextScreen) {
      super((showAgain) -> {
         if (!showAgain) {
            ((BluetoothDeviceComponent.Config)bt.confObj()).dontShowWarningAgain = true;
            Controlify.instance().config().save();
         }

         class_310.method_1551().method_1507(nextScreen);
      }, class_2561.method_43471("controlify.bluetooth_warning.title"), class_2561.method_43471("controlify.bluetooth_warning.desc"), class_5244.field_44914, class_2561.method_43471("controlify.bluetooth_warning.dont_show"));
   }
}
