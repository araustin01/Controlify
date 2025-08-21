package dev.isxander.controlify.utils;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controllermanager.ControllerManager;
import dev.isxander.controlify.driver.sdl.SDL3NativesManager;
import dev.isxander.controlify.platform.Environment;
import dev.isxander.controlify.platform.main.PlatformMainUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Optional;
import net.minecraft.class_155;
import net.minecraft.class_2960;

public class DebugDump {
   public static String dumpDebug() {
      DebugDump.IndentedStringBuilder dump = new DebugDump.IndentedStringBuilder();
      LocalDateTime dateTime = LocalDateTime.now();
      String formattedDate = dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
      dump.line("CONTROLIFY DEBUG DUMP - ", formattedDate, '\n');
      dump.line("Controlify version: ", PlatformMainUtil.getControlifyVersion());
      dump.line("Minecraft version: ", class_155.method_16673().method_48019());
      if (PlatformMainUtil.getEnv() == Environment.CLIENT) {
         dump.line("Client").pushIndent();
         dumpClientDebug(dump);
         dump.popIndent();
      }

      return dump.build();
   }

   private static void dumpClientDebug(DebugDump.IndentedStringBuilder dump) {
      dump.line("SDL3 loaded: ", SDL3NativesManager.isLoaded());
      dump.line("Platform: ", SDL3NativesManager.Target.CURRENT.formatted());
      dump.line();
      Optional<ControllerManager> controllerManagerOpt = Controlify.instance().getControllerManager();
      if (controllerManagerOpt.isPresent()) {
         ControllerManager controllerManager = (ControllerManager)controllerManagerOpt.get();
         dump.line("Controller-manager: ", controllerManager.getClass().getName());
         dump.line("Connected Controllers:").pushIndent();
         Iterator var3 = controllerManager.getConnectedControllers().iterator();

         while(var3.hasNext()) {
            ControllerEntity controller = (ControllerEntity)var3.next();
            dump.line("Name: ", controller.name());
            dump.line("Identified type: ", controller.info().type());
            dump.line("GUID: ", controller.guid());
            dump.line("UID: ", controller.uid());
            dump.line("UCID: ", controller.info().ucid());
            controller.info().hid().ifPresent((hid) -> {
               dump.line("HID: ", hid.asIdentifier());
            });
            controller.input().ifPresentOrElse((input) -> {
               dump.line("Input Component:").pushIndent();
               dump.line("Definitely gamepad: ", input.isDefinitelyGamepad());
               dump.line("Available inputs:").pushIndent();
               Iterator var2 = input.stateNow().getButtons().iterator();

               class_2960 hat;
               while(var2.hasNext()) {
                  hat = (class_2960)var2.next();
                  dump.line("BTN ", hat);
               }

               var2 = input.stateNow().getAxes().iterator();

               while(var2.hasNext()) {
                  hat = (class_2960)var2.next();
                  dump.line("AXS ", hat);
               }

               var2 = input.stateNow().getHats().iterator();

               while(var2.hasNext()) {
                  hat = (class_2960)var2.next();
                  dump.line("HAT ", hat);
               }

               dump.popIndent();
               dump.popIndent();
            }, () -> {
               dump.line("Input Component: UNSUPPORTED");
            });
            dump.line("Rumble supported: ", controller.rumble().isPresent());
            dump.line("Trigger rumble supported: ", controller.triggerRumble().isPresent());
            dump.line("Battery level: ", controller.batteryLevel().map((b) -> {
               return b.getBatteryLevel().toString();
            }).orElse("UNSUPPORTED"));
            dump.line("Gyro supported: ", controller.gyro().isPresent());
            dump.line("Touchpads supported: ", controller.touchpad().map((touchpad) -> {
               return touchpad.touchpads().length;
            }).orElse(0));
            dump.line("HD haptics supported: ", controller.hdHaptics().isPresent());
            dump.line("Log:").pushIndent().line(controller.getLogger().export()).popIndent();
            dump.line();
         }

         dump.popIndent();
      }

   }

   private static class IndentedStringBuilder {
      private final StringBuilder sb = new StringBuilder();
      private int indent;

      public DebugDump.IndentedStringBuilder line(Object... parts) {
         this.sb.append("  ".repeat(this.indent));
         Object[] var2 = parts;
         int var3 = parts.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            Object part = var2[var4];
            String stringified = part.toString().replace("\n", "\n" + "  ".repeat(this.indent));
            this.sb.append(stringified);
         }

         this.sb.append('\n');
         return this;
      }

      public DebugDump.IndentedStringBuilder pushIndent() {
         ++this.indent;
         return this;
      }

      public DebugDump.IndentedStringBuilder popIndent() {
         --this.indent;
         return this;
      }

      public String build() {
         return this.sb.toString();
      }
   }
}
