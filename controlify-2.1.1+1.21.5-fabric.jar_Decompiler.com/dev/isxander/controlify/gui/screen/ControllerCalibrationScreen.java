package dev.isxander.controlify.gui.screen;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.gyro.GyroComponent;
import dev.isxander.controlify.controller.gyro.GyroState;
import dev.isxander.controlify.controller.input.ControllerState;
import dev.isxander.controlify.controller.input.DeadzoneGroup;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.controllermanager.ControllerManager;
import dev.isxander.controlify.utils.ClientUtils;
import dev.isxander.controlify.utils.render.CGuiPose;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_5489;
import net.minecraft.class_7919;
import org.jetbrains.annotations.Nullable;

public class ControllerCalibrationScreen extends class_437 implements DontInteruptScreen {
   private static final int CALIBRATION_TIME = 100;
   protected final Controlify controlify;
   protected final ControllerManager controllerManager;
   protected final ControllerEntity controller;
   private final Supplier<class_437> parent;
   private class_5489 waitLabel;
   private class_5489 infoLabel;
   private class_5489 completeLabel;
   protected class_4185 readyButton;
   protected class_4185 laterButton;
   protected boolean calibrating;
   protected boolean calibrated;
   protected int calibrationTicks;
   @Nullable
   private final Map<class_2960, float[]> axisData;
   private GyroState accumulatedGyroVelocity;

   public ControllerCalibrationScreen(ControllerEntity controller, class_437 parent) {
      this(controller, () -> {
         return parent;
      });
   }

   public ControllerCalibrationScreen(ControllerEntity controller, Supplier<class_437> parent) {
      super(class_2561.method_43471("controlify.calibration.title"));
      this.calibrating = false;
      this.calibrated = false;
      this.calibrationTicks = 0;
      this.accumulatedGyroVelocity = new GyroState();
      this.controlify = Controlify.instance();
      this.controllerManager = (ControllerManager)this.controlify.getControllerManager().orElseThrow();
      this.controller = controller;
      this.parent = parent;
      Optional<InputComponent> inputOpt = controller.input();
      this.axisData = (Map)inputOpt.map((inputComponent) -> {
         return new HashMap(inputComponent.axisCount());
      }).orElse((Object)null);
   }

   protected void method_25426() {
      this.method_37063(this.readyButton = class_4185.method_46430(class_2561.method_43471("controlify.calibration.ready"), (btn) -> {
         this.onButtonPress();
      }).method_46432(150).method_46433(this.field_22789 / 2 - 150 - 5, this.field_22790 - 8 - 20).method_46431());
      this.method_37063(this.laterButton = class_4185.method_46430(class_2561.method_43471("controlify.calibration.later"), (btn) -> {
         this.onLaterButtonPress();
      }).method_46432(150).method_46433(this.field_22789 / 2 + 5, this.field_22790 - 8 - 20).method_46436(class_7919.method_47407(class_2561.method_43471("controlify.calibration.later.tooltip"))).method_46431());
      this.infoLabel = class_5489.method_30890(this.field_22793, class_2561.method_43471("controlify.calibration.info"), this.field_22789 - 30);
      this.waitLabel = class_5489.method_30890(this.field_22793, class_2561.method_43471("controlify.calibration.wait"), this.field_22789 - 30);
      this.completeLabel = class_5489.method_30890(this.field_22793, class_2561.method_43471("controlify.calibration.complete"), this.field_22789 - 30);
   }

   protected void startCalibration() {
      this.calibrating = true;
      this.readyButton.field_22763 = false;
      this.readyButton.method_25355(class_2561.method_43471("controlify.calibration.calibrating"));
   }

   public void method_25394(class_332 graphics, int mouseX, int mouseY, float delta) {
      super.method_25394(graphics, mouseX, mouseY, delta);
      graphics.method_27534(this.field_22793, class_2561.method_43469("controlify.calibration.title", new Object[]{this.controller.name()}).method_27692(class_124.field_1067), this.field_22789 / 2, 8, -1);
      CGuiPose pose = CGuiPose.ofPush(graphics);
      pose.scale(2.0F, 2.0F);
      float progress = ((float)(this.calibrationTicks - 1) + delta) / 100.0F;
      progress = 1.0F - (float)Math.pow((double)(1.0F - progress), 3.0D);
      ClientUtils.drawBar(graphics, this.field_22789 / 2 / 2, 15, progress);
      pose.pop();
      class_5489 label;
      if (this.calibrating) {
         label = this.waitLabel;
      } else if (this.calibrated) {
         label = this.completeLabel;
      } else {
         label = this.infoLabel;
      }

      label.method_30888(graphics, this.field_22789 / 2, 55);
      pose.push();
      int var10001 = this.readyButton.method_46427();
      Objects.requireNonNull(this.field_22793);
      float scale = Math.min(3.0F, (float)(var10001 - (55 + 9 * label.method_30887()) - 2) / 64.0F);
      float var9 = (float)this.field_22789 / 2.0F - 32.0F * scale;
      Objects.requireNonNull(this.field_22793);
      pose.translate(var9, (float)(55 + 9 * label.method_30887()));
      pose.scale(scale, scale);
      ClientUtils.drawSprite(graphics, this.controller.info().type().getIconSprite(), 0, 0, 64, 64);
      pose.pop();
   }

   public void method_25393() {
      if (!this.controllerManager.isControllerConnected(this.controller.uid())) {
         this.method_25419();
      } else if (this.calibrating) {
         if (this.stateChanged()) {
            this.calibrationTicks = 0;
            if (this.axisData != null) {
               this.axisData.clear();
            }

            this.accumulatedGyroVelocity = new GyroState();
         }

         if (this.calibrationTicks < 100) {
            this.processAxisData(this.calibrationTicks);
            this.processGyroData();
            ++this.calibrationTicks;
         } else {
            this.calibrateAxis();
            this.generateGyroCalibration();
            this.calibrating = false;
            this.calibrated = true;
            this.readyButton.field_22763 = true;
            this.readyButton.method_25355(class_2561.method_43471("controlify.calibration.done"));
            this.controller.input().map((input) -> {
               return (InputComponent.Config)input.config().config();
            }).ifPresent((config) -> {
               config.deadzonesCalibrated = true;
               config.delayedCalibration = false;
            });
            this.controller.gyro().map((gyro) -> {
               return (GyroComponent.Config)gyro.config().config();
            }).ifPresent((config) -> {
               config.calibrated = true;
            });
            Controlify.instance().config().setDirty();
            Controlify.instance().config().saveIfDirty();
         }

      }
   }

   private void processAxisData(int tick) {
      if (this.axisData != null) {
         InputComponent input = (InputComponent)this.controller.input().orElseThrow();
         ControllerState state = input.rawStateNow();

         float[] axisData;
         float max;
         for(Iterator var4 = input.getDeadzoneGroups().values().iterator(); var4.hasNext(); axisData[tick] = max) {
            DeadzoneGroup group = (DeadzoneGroup)var4.next();
            axisData = (float[])this.axisData.computeIfAbsent(group.name(), (k) -> {
               return new float[100];
            });
            max = 0.0F;

            class_2960 axis;
            for(Iterator var8 = group.axes().iterator(); var8.hasNext(); max = Math.max(max, Math.abs(state.getAxisState(axis)))) {
               axis = (class_2960)var8.next();
            }
         }

      }
   }

   private void processGyroData() {
      this.controller.gyro().ifPresent((gyro) -> {
         this.accumulatedGyroVelocity.add(gyro.getState());
      });
   }

   private void calibrateAxis() {
      if (this.axisData != null) {
         InputComponent input = (InputComponent)this.controller.input().orElseThrow();
         ((InputComponent.Config)input.config().config()).deadzones.clear();
         Iterator var2 = input.getDeadzoneGroups().values().iterator();

         while(true) {
            DeadzoneGroup group;
            float[] axisData;
            do {
               if (!var2.hasNext()) {
                  return;
               }

               group = (DeadzoneGroup)var2.next();
               axisData = (float[])this.axisData.get(group.name());
            } while(axisData == null);

            float maxAbs = 0.0F;

            for(int tick = 0; tick < 100; ++tick) {
               float axisValue = axisData[tick];
               maxAbs = Math.max(maxAbs, Math.abs(axisValue));
            }

            ((InputComponent.Config)input.config().config()).deadzones.put(group.name(), maxAbs + 0.08F);
         }
      }
   }

   private void generateGyroCalibration() {
      this.controller.gyro().ifPresent((gyro) -> {
         ((GyroComponent.Config)gyro.config().config()).calibration = this.accumulatedGyroVelocity.div(100.0F);
      });
   }

   private boolean stateChanged() {
      InputComponent input = (InputComponent)this.controller.input().orElseThrow();
      float amt = 0.4F;
      Iterator var3 = input.rawStateNow().getAxes().iterator();

      while(var3.hasNext()) {
         class_2960 axis = (class_2960)var3.next();
         float[] axisData = (float[])this.axisData.get(axis);
         if (axisData != null) {
            float axisValue = input.rawStateNow().getAxisState(axis);
            float prevAxisValue = input.rawStateThen().getAxisState(axis);
            if (Math.abs(axisValue - prevAxisValue) > amt) {
               return true;
            }
         }
      }

      return false;
   }

   private void onButtonPress() {
      if (!this.calibrated) {
         this.startCalibration();
         this.method_37066(this.laterButton);
         this.readyButton.method_46421(this.field_22789 / 2 - 75);
      } else {
         this.method_25419();
      }

   }

   private void onLaterButtonPress() {
      if (!this.calibrated) {
         boolean dirty = false;
         dirty |= (Boolean)this.controller.input().map((input) -> {
            return ((InputComponent.Config)input.config().config()).delayedCalibration = true;
         }).orElse(false);
         dirty |= (Boolean)this.controller.gyro().map((gyro) -> {
            return ((GyroComponent.Config)gyro.config().config()).delayedCalibration = true;
         }).orElse(false);
         if (dirty) {
            Controlify.instance().config().setDirty();
         }

         this.method_25419();
      }

   }

   public void method_25419() {
      this.field_22787.method_1507((class_437)this.parent.get());
   }

   public boolean method_25422() {
      return false;
   }
}
