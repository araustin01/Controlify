package dev.isxander.controlify.gui.screen;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.input.ControllerStateView;
import dev.isxander.controlify.controller.input.DeadzoneGroup;
import dev.isxander.controlify.controller.input.GamepadInputs;
import dev.isxander.controlify.controller.input.HatState;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.controller.input.mapping.ControllerMapping;
import dev.isxander.controlify.controller.input.mapping.MapType;
import dev.isxander.controlify.controller.input.mapping.MappingEntry;
import dev.isxander.controlify.screenop.ScreenControllerEventListener;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.ClientUtils;
import dev.isxander.controlify.utils.ColorUtils;
import dev.isxander.controlify.utils.render.Blit;
import dev.isxander.controlify.utils.render.CGuiPose;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_7919;

public class ControllerMappingMakerScreen extends class_437 implements ScreenControllerEventListener, ScreenProcessorProvider, DontInteruptScreen {
   private final InputComponent inputComponent;
   private final ControllerMapping.Builder mappingBuilder = new ControllerMapping.Builder();
   private final ScreenProcessor<ControllerMappingMakerScreen> screenProcessor = new ControllerMappingMakerScreen.ScreenProcessorImpl(this);
   private int delayTillNextStage = 20;
   public static final List<ControllerMappingMakerScreen.MappingStage> GAMEPAD_STAGES;
   private ControllerMappingMakerScreen.MappingStage currentStage = null;
   private final List<ControllerMappingMakerScreen.MappingStage> stages;
   private class_4185 goBackButton;
   private final class_437 lastScreen;

   public ControllerMappingMakerScreen(InputComponent inputComponent, class_437 lastScreen, List<ControllerMappingMakerScreen.MappingStage> stages, Iterable<DeadzoneGroup> deadzoneGroups) {
      super(class_2561.method_43470("Gamepad Emulation Mapping Creator"));
      this.inputComponent = inputComponent;
      this.lastScreen = lastScreen;
      this.stages = stages;
      this.mappingBuilder.putDeadzoneGroups(deadzoneGroups);
      ((InputComponent.Config)inputComponent.confObj()).mapping = null;
   }

   public static ControllerMappingMakerScreen createGamepadMapping(InputComponent inputComponent, class_437 lastScreen) {
      return new ControllerMappingMakerScreen(inputComponent, lastScreen, GAMEPAD_STAGES, GamepadInputs.DEADZONE_GROUPS);
   }

   protected void method_25426() {
      this.method_37063(this.goBackButton = class_4185.method_46430(class_2561.method_43471("controlify.gui.mapping_maker.go_back"), (button) -> {
         this.goBackStage();
      }).method_46434(this.field_22789 / 2 - 152, this.field_22790 - 60, 150, 20).method_46436(class_7919.method_47407(class_2561.method_43471("controlify.gui.mapping_maker.go_back.tooltip"))).method_46431());
      this.method_37063(class_4185.method_46430(class_2561.method_43471("controlify.gui.mapping_maker.no_map"), (button) -> {
         this.mapAsNone();
      }).method_46434(this.field_22789 / 2 + 2, this.field_22790 - 60, 150, 20).method_46436(class_7919.method_47407(class_2561.method_43471("controlify.gui.mapping_maker.no_map.tooltip"))).method_46431());
      this.goBackButton.field_22763 = false;
   }

   public void method_25393() {
      if (this.delayTillNextStage >= 0) {
         --this.delayTillNextStage;
         if (this.delayTillNextStage == -1) {
            if (this.currentStage == null) {
               this.setStage((ControllerMappingMakerScreen.MappingStage)this.stages.get(0));
            } else {
               int index = this.stages.indexOf(this.currentStage);
               int nextIndex = index + 1;
               if (nextIndex >= this.stages.size()) {
                  this.method_25419();
               } else {
                  this.setStage((ControllerMappingMakerScreen.MappingStage)this.stages.get(index + 1));
                  this.goBackButton.field_22763 = true;
               }
            }
         }
      } else {
         if (this.currentStage == null) {
            this.setStage((ControllerMappingMakerScreen.MappingStage)this.stages.get(0));
         } else if (this.currentStage.isSatisfied() && this.delayTillNextStage == -1) {
            this.delayTillNextStage = 20;
         }

         if (this.currentStage != null && !this.currentStage.isSatisfied()) {
            ControllerStateView stateNow = this.inputComponent.stateNow();
            ControllerStateView stateThen = this.inputComponent.stateThen();
            this.processStage(this.currentStage, stateNow, stateThen);
         }
      }

   }

   private void processStage(ControllerMappingMakerScreen.MappingStage stage, ControllerStateView stateNow, ControllerStateView stateThen) {
      Iterator var4 = stateNow.getButtons().iterator();

      class_2960 hat;
      boolean now;
      boolean prev;
      Object mapping;
      Object var10000;
      do {
         if (!var4.hasNext()) {
            var4 = stateNow.getAxes().iterator();

            float diff;
            do {
               if (!var4.hasNext()) {
                  var4 = stateNow.getHats().iterator();

                  HatState now;
                  HatState prev;
                  do {
                     if (!var4.hasNext()) {
                        return;
                     }

                     hat = (class_2960)var4.next();
                     now = stateNow.getHatState(hat);
                     prev = stateThen.getHatState(hat);
                  } while(now == prev);

                  switch(stage.outputType()) {
                  case BUTTON:
                     var10000 = new MappingEntry.FromHat.ToButton(hat, stage.originInput(), now);
                     break;
                  case AXIS:
                     var10000 = new MappingEntry.FromHat.ToAxis(hat, stage.originInput(), now, 0.0F, 1.0F);
                     break;
                  case HAT:
                     var10000 = new MappingEntry.FromHat.ToHat(hat, stage.originInput());
                     break;
                  case NOTHING:
                     var10000 = null;
                     break;
                  default:
                     throw new MatchException((String)null, (Throwable)null);
                  }

                  mapping = var10000;
                  this.mappingBuilder.putMapping((MappingEntry)mapping);
                  stage.setSatisfied(true);
                  return;
               }

               hat = (class_2960)var4.next();
               float now = stateNow.getAxisState(hat);
               float prev = stateThen.getAxisState(hat);
               diff = prev - now;
            } while(!(Math.abs(diff) > 0.3F));

            switch(stage.outputType()) {
            case BUTTON:
               var10000 = new MappingEntry.FromAxis.ToButton(hat, stage.originInput(), 0.5F);
               break;
            case AXIS:
               var10000 = new MappingEntry.FromAxis.ToAxis(hat, stage.originInput(), 0.0F, 0.0F, 1.0F, 1.0F);
               break;
            case HAT:
               var10000 = new MappingEntry.FromAxis.ToHat(hat, stage.originInput(), 0.5F, diff > 0.0F ? HatState.UP : HatState.DOWN);
               break;
            case NOTHING:
               var10000 = null;
               break;
            default:
               throw new MatchException((String)null, (Throwable)null);
            }

            MappingEntry mapping = var10000;
            this.mappingBuilder.putMapping((MappingEntry)mapping);
            stage.setSatisfied(true);
            return;
         }

         hat = (class_2960)var4.next();
         now = stateNow.isButtonDown(hat);
         prev = stateThen.isButtonDown(hat);
      } while(now == prev);

      switch(stage.outputType()) {
      case BUTTON:
         var10000 = new MappingEntry.FromButton.ToButton(hat, stage.originInput(), !now);
         break;
      case AXIS:
         var10000 = new MappingEntry.FromButton.ToAxis(hat, stage.originInput(), prev ? 1.0F : 0.0F, now ? 1.0F : 0.0F);
         break;
      case HAT:
         HatState state = now ? HatState.DOWN : HatState.UP;
         var10000 = new MappingEntry.FromButton.ToHat(hat, stage.originInput(), HatState.CENTERED, state);
         break;
      case NOTHING:
         var10000 = null;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      mapping = var10000;
      this.mappingBuilder.putMapping((MappingEntry)mapping);
      stage.setSatisfied(true);
   }

   private void setStage(ControllerMappingMakerScreen.MappingStage stage) {
      this.currentStage = stage;
   }

   private void mapAsNone() {
      Object var10000;
      switch(this.currentStage.outputType()) {
      case BUTTON:
         var10000 = new MappingEntry.FromNothing.ToButton(this.currentStage.originInput(), false);
         break;
      case AXIS:
         var10000 = new MappingEntry.FromNothing.ToAxis(this.currentStage.originInput(), 0.0F);
         break;
      case HAT:
         var10000 = new MappingEntry.FromNothing.ToHat(this.currentStage.originInput());
         break;
      case NOTHING:
         var10000 = null;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      MappingEntry mapping = var10000;
      this.mappingBuilder.putMapping((MappingEntry)mapping);
      this.delayTillNextStage = 0;
   }

   public void method_25419() {
      this.field_22787.method_1507(this.lastScreen);
      ((InputComponent.Config)this.inputComponent.confObj()).mapping = this.mappingBuilder.build();
      Controlify.instance().config().save();
   }

   private void goBackStage() {
      int index = this.stages.indexOf(this.currentStage);
      int nextIndex = index - 1;
      if (nextIndex >= 0) {
         this.setStage((ControllerMappingMakerScreen.MappingStage)this.stages.get(index - 1));
         this.currentStage.setSatisfied(false);
      }

      this.goBackButton.field_22763 = nextIndex > 0;
   }

   public void method_25394(class_332 guiGraphics, int i, int j, float f) {
      super.method_25394(guiGraphics, i, j, f);
      guiGraphics.method_27534(this.field_22793, class_2561.method_43471("controlify.gui.mapping_maker.title"), this.field_22789 / 2, 15, 16777215);
      guiGraphics.method_27534(this.field_22793, (class_2561)(this.currentStage == null ? class_2561.method_43471("controlify.gui.mapping_maker.please_wait") : this.currentStage.name()), this.field_22789 / 2, this.field_22790 - 20, 16777215);
      int safeZone = Math.min(this.field_22789, this.field_22790) - 30;
      float scale = (float)safeZone / 32.0F;
      CGuiPose pose = CGuiPose.ofPush(guiGraphics);
      pose.translate((float)this.field_22789 / 2.0F, -5.0F);
      pose.translate(-32.0F * scale / 2.0F, 0.0F);
      pose.scale(scale, scale);
      float colour = this.currentStage != null && this.currentStage.isSatisfied() ? 0.46F : 1.0F;
      if (this.currentStage != null && this.currentStage.background() != null) {
         Blit.tex(guiGraphics, this.currentStage.background(), 0, 0, 0.0F, 0.0F, 32, 32, 32, 32, ColorUtils.grey(colour, 1.0F));
      }

      if (this.currentStage == null || !this.currentStage.isSatisfied()) {
         class_2960 texture = this.currentStage != null ? this.currentStage.foreground() : CUtil.rl("textures/gui/controllerdiagram/faceview.png");
         Blit.tex(guiGraphics, texture, 0, 0, 0.0F, 0.0F, 32, 32, 32, 32, ColorUtils.grey(colour, 1.0F));
      }

      pose.pop();
      float progress = this.currentStage != null ? (float)(this.stages.indexOf(this.currentStage) + 1) / (float)this.stages.size() : 0.0F;
      ClientUtils.drawBar(guiGraphics, this.field_22789 / 2, this.field_22790 - 30, progress);
   }

   private static class_2561 button(String buttonName) {
      return class_2561.method_43469("controlify.gui.mapping_maker.instruction.button", new Object[]{class_2561.method_43471("controlify.gui.mapping_maker.instruction." + buttonName)});
   }

   private static class_2561 axis(String axisName, boolean horizontal) {
      class_2561 axis = class_2561.method_43471("controlify.gui.mapping_maker.instruction." + axisName);
      return horizontal ? class_2561.method_43469("controlify.gui.mapping_maker.instruction.axis_x", new Object[]{axis}) : class_2561.method_43469("controlify.gui.mapping_maker.instruction.axis_y", new Object[]{axis});
   }

   public ScreenProcessor<?> screenProcessor() {
      return this.screenProcessor;
   }

   static {
      GAMEPAD_STAGES = List.of(new ControllerMappingMakerScreen.MappingStage[]{new ControllerMappingMakerScreen.MappingStage(GamepadInputs.SOUTH_BUTTON, MapType.BUTTON, button("face_down"), "face_down", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.WEST_BUTTON, MapType.BUTTON, button("face_left"), "face_left", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.EAST_BUTTON, MapType.BUTTON, button("face_right"), "face_right", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.NORTH_BUTTON, MapType.BUTTON, button("face_up"), "face_up", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.LEFT_SHOULDER_BUTTON, MapType.BUTTON, button("left_bumper"), "left_bumper", "triggerview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.RIGHT_SHOULDER_BUTTON, MapType.BUTTON, button("right_bumper"), "right_bumper", "triggerview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.START_BUTTON, MapType.BUTTON, button("left_special"), "left_special", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.GUIDE_BUTTON, MapType.BUTTON, button("right_special"), "right_special", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.LEFT_STICK_BUTTON, MapType.BUTTON, button("left_stick_down"), "left_stick_press", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.RIGHT_STICK_BUTTON, MapType.BUTTON, button("right_stick_down"), "right_stick_press", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.DPAD_UP_BUTTON, MapType.BUTTON, button("dpad_up"), "dpad_up", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.DPAD_LEFT_BUTTON, MapType.BUTTON, button("dpad_left"), "dpad_left", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.DPAD_DOWN_BUTTON, MapType.BUTTON, button("dpad_down"), "dpad_down", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.DPAD_RIGHT_BUTTON, MapType.BUTTON, button("dpad_right"), "dpad_right", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.LEFT_STICK_AXIS_LEFT, MapType.AXIS, axis("left_stick", true), "left_stick_left", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.LEFT_STICK_AXIS_DOWN, MapType.AXIS, axis("left_stick", false), "left_stick_down", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.LEFT_STICK_AXIS_RIGHT, MapType.AXIS, axis("left_stick", true), "left_stick_right", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.LEFT_STICK_AXIS_UP, MapType.AXIS, axis("left_stick", false), "left_stick_up", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.RIGHT_STICK_AXIS_LEFT, MapType.AXIS, axis("right_stick", true), "right_stick_left", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.RIGHT_STICK_AXIS_DOWN, MapType.AXIS, axis("right_stick", false), "right_stick_down", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.RIGHT_STICK_AXIS_RIGHT, MapType.AXIS, axis("right_stick", true), "right_stick_right", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.RIGHT_STICK_AXIS_UP, MapType.AXIS, axis("right_stick", false), "right_stick_up", "faceview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.LEFT_TRIGGER_AXIS, MapType.AXIS, class_2561.method_43471("controlify.gui.mapping_maker.instruction.left_trigger"), "left_trigger", "triggerview"), new ControllerMappingMakerScreen.MappingStage(GamepadInputs.RIGHT_TRIGGER_AXIS, MapType.AXIS, class_2561.method_43471("controlify.gui.mapping_maker.instruction.right_trigger"), "right_trigger", "triggerview")});
   }

   private static class ScreenProcessorImpl extends ScreenProcessor<ControllerMappingMakerScreen> {
      public ScreenProcessorImpl(ControllerMappingMakerScreen screen) {
         super(screen);
      }

      public void onControllerUpdate(ControllerEntity controller) {
      }
   }

   public static class MappingStage {
      private final class_2960 originInput;
      private final MapType outputType;
      private final class_2561 name;
      private final class_2960 foreground;
      private final class_2960 background;
      private boolean satisfied;

      public MappingStage(class_2960 originInput, MapType outputType, class_2561 name, String foreground, String background) {
         this.originInput = originInput;
         this.outputType = outputType;
         this.name = name;
         this.foreground = CUtil.rl("textures/gui/controllerdiagram/" + foreground + ".png");
         this.background = CUtil.rl("textures/gui/controllerdiagram/" + foreground + ".png");
      }

      public class_2960 originInput() {
         return this.originInput;
      }

      public MapType outputType() {
         return this.outputType;
      }

      public class_2561 name() {
         return this.name;
      }

      public class_2960 foreground() {
         return this.foreground;
      }

      public class_2960 background() {
         return this.background;
      }

      public boolean isSatisfied() {
         return this.satisfied;
      }

      public void setSatisfied(boolean satisfied) {
         this.satisfied = satisfied;
      }
   }
}
