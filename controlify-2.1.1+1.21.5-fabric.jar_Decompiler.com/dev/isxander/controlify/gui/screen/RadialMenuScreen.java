package dev.isxander.controlify.gui.screen;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.api.bind.RadialIcon;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.haptic.HapticEffects;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.gui.guide.GuideAction;
import dev.isxander.controlify.gui.guide.GuideActionRenderer;
import dev.isxander.controlify.gui.layout.AnchorPoint;
import dev.isxander.controlify.gui.layout.PositionedComponent;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenControllerEventListener;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import dev.isxander.controlify.sound.ControlifyClientSounds;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.animation.api.Animation;
import dev.isxander.controlify.utils.animation.api.EasingFunction;
import dev.isxander.controlify.utils.render.Blit;
import dev.isxander.controlify.utils.render.CGuiPose;
import dev.isxander.controlify.virtualmouse.VirtualMouseBehaviour;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.class_1109;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import net.minecraft.class_3532;
import net.minecraft.class_364;
import net.minecraft.class_4068;
import net.minecraft.class_4069;
import net.minecraft.class_437;
import net.minecraft.class_5244;
import net.minecraft.class_5489;
import net.minecraft.class_6379;
import net.minecraft.class_6381;
import net.minecraft.class_6382;
import net.minecraft.class_8016;
import net.minecraft.class_8023;
import net.minecraft.class_8030;
import net.minecraft.class_6379.class_6380;
import org.jetbrains.annotations.Nullable;

public class RadialMenuScreen extends class_437 implements ScreenControllerEventListener, ScreenProcessorProvider {
   public static final class_2960 EMPTY_ACTION = CUtil.rl("empty_action");
   private final ControllerEntity controller;
   @Nullable
   private final RadialMenuScreen.EditMode editMode;
   private final class_437 parent;
   private final class_2561 text;
   private final RadialMenuScreen.RadialItem[] items;
   private final RadialMenuScreen.RadialButton[] buttons;
   private float radialRadius;
   private final InputBinding openBind;
   private int selectedButton = -1;
   private int idleTicks;
   private final int idleTicksTimeout;
   private boolean isEditing;
   private RadialMenuScreen.ActionSelectList actionSelectList;
   private final RadialMenuScreen.Processor processor = new RadialMenuScreen.Processor(this);

   public RadialMenuScreen(ControllerEntity controller, InputBinding openBind, RadialMenuScreen.RadialItem[] items, class_2561 text, @Nullable RadialMenuScreen.EditMode editMode, class_437 parent) {
      super(text);
      this.text = text;
      this.controller = controller;
      this.items = items;
      this.buttons = new RadialMenuScreen.RadialButton[items.length];
      this.editMode = editMode;
      this.parent = parent;
      this.idleTicksTimeout = ((InputComponent.Config)((InputComponent)controller.input().orElseThrow()).confObj()).radialButtonFocusTimeoutTicks;
      this.openBind = openBind;
   }

   protected void method_25426() {
      int centerX = this.field_22789 / 2;
      int centerY = this.field_22790 / 2;
      float buttonRadius = (float)Math.sqrt(2048.0D) + 8.0F;
      float circumference = buttonRadius * (float)this.items.length;
      this.radialRadius = Math.max(circumference / 6.2831855F, 43.0F);
      Animation animation = Animation.of(5).easing(EasingFunction.EASE_OUT_QUAD);

      for(int i = 0; i < this.items.length; ++i) {
         float angle = 6.2831855F * (float)i / (float)this.items.length - 1.5707964F;
         float x = (float)centerX + class_3532.method_15362(angle) * this.radialRadius;
         float y = (float)centerY + class_3532.method_15374(angle) * this.radialRadius;
         RadialMenuScreen.RadialButton button = this.buttons[i] = new RadialMenuScreen.RadialButton(this.items[i], (float)(centerX - 16), (float)(centerY - 16));
         Objects.requireNonNull(button);
         Animation var10000 = animation.consumerF(button::setX, (double)(centerX - 16), (double)(x - 16.0F));
         Objects.requireNonNull(button);
         var10000.consumerF(button::setY, (double)(centerY - 16), (double)(y - 16.0F));
         this.method_37063(button);
      }

      animation.play();
      if (this.editMode != null) {
         PositionedComponent<GuideActionRenderer<Object>> exitGuide = (PositionedComponent)this.method_37063(new PositionedComponent(new GuideActionRenderer(new GuideAction(ControlifyBindings.GUI_BACK.on(this.controller), (obj) -> {
            return Optional.of(class_5244.field_24334);
         }), false, true), AnchorPoint.BOTTOM_CENTER, 0, -10, AnchorPoint.BOTTOM_CENTER));
         ((GuideActionRenderer)exitGuide.getComponent()).updateName((Object)null);
         exitGuide.updatePosition(this.field_22789, this.field_22790);
      }

   }

   public void onControllerInput(ControllerEntity controller) {
      if (this.controller == controller) {
         if (this.editMode == null && !this.openBind.digitalNow()) {
            if (this.selectedButton != -1 && this.buttons[this.selectedButton].invoke()) {
               this.playClickSound();
            }

            this.method_25419();
         }

         if (this.editMode != null && ControlifyBindings.GUI_BACK.on(controller).justPressed()) {
            this.playClickSound();
            this.method_25419();
         }

         if (!this.isEditing) {
            float x = ControlifyBindings.RADIAL_AXIS_RIGHT.on(controller).analogueNow() - ControlifyBindings.RADIAL_AXIS_LEFT.on(controller).analogueNow();
            float y = ControlifyBindings.RADIAL_AXIS_DOWN.on(controller).analogueNow() - ControlifyBindings.RADIAL_AXIS_UP.on(controller).analogueNow();
            float threshold = ((InputComponent.Config)((InputComponent)controller.input().orElseThrow()).config().config()).buttonActivationThreshold;
            int newSelected;
            if (!(Math.abs(x) >= threshold) && !(Math.abs(y) >= threshold)) {
               if (this.editMode == null) {
                  ++this.idleTicks;
                  if (this.idleTicks >= this.idleTicksTimeout && this.selectedButton != -1) {
                     this.selectedButton = -1;
                     RadialMenuScreen.RadialButton[] var10 = this.buttons;
                     int var11 = var10.length;

                     for(newSelected = 0; newSelected < var11; ++newSelected) {
                        RadialMenuScreen.RadialButton button = var10[newSelected];
                        button.method_25365(false);
                     }

                     controller.hdHaptics().ifPresent((haptics) -> {
                        haptics.playHaptic(HapticEffects.NAVIGATE);
                     });
                  }
               }
            } else {
               float angle = class_3532.method_15393(57.295776F * (float)class_3532.method_15349((double)y, (double)x) - 90.0F) + 180.0F;
               float each = 360.0F / (float)this.buttons.length;
               newSelected = class_3532.method_15375((angle + each / 2.0F) / each) % this.buttons.length;
               if (newSelected != this.selectedButton) {
                  this.selectedButton = newSelected;
                  this.field_22787.method_1483().method_4873(class_1109.method_4758((class_3414)ControlifyClientSounds.SCREEN_FOCUS_CHANGE.get(), 1.0F));
                  controller.hdHaptics().ifPresent((haptics) -> {
                     haptics.playHaptic(HapticEffects.NAVIGATE);
                  });
               }

               for(int i = 0; i < this.buttons.length; ++i) {
                  boolean selected = i == this.selectedButton;
                  this.buttons[i].method_25365(selected);
                  if (selected) {
                     this.method_25395(this.buttons[i]);
                  }
               }

               this.idleTicks = 0;
            }
         }

      }
   }

   public void method_25394(class_332 graphics, int mouseX, int mouseY, float delta) {
      if (this.editMode != null) {
         this.method_25420(graphics, mouseX, mouseY, delta);
      }

      super.method_25394(graphics, mouseX, mouseY, delta);
      if (this.editMode == null) {
         graphics.method_27534(this.field_22793, this.text, this.field_22789 / 2, this.field_22790 - 39, -1);
      }

   }

   private void playClickSound() {
      this.field_22787.method_1483().method_4873(class_1109.method_47978(class_3417.field_15015, 1.0F));
   }

   private void finishEditing() {
      this.isEditing = false;
      this.method_37066(this.actionSelectList);
      this.method_25395((class_364)null);
      this.actionSelectList = null;
   }

   public void method_25419() {
      Controlify.instance().config().saveIfDirty();
      this.field_22787.method_1507(this.parent);
   }

   public boolean method_25421() {
      return this.editMode != null;
   }

   public ScreenProcessor<?> screenProcessor() {
      return this.processor;
   }

   public static class Processor extends ScreenProcessor<RadialMenuScreen> {
      public Processor(RadialMenuScreen screen) {
         super(screen);
      }

      public VirtualMouseBehaviour virtualMouseBehaviour() {
         return VirtualMouseBehaviour.DISABLED;
      }
   }

   public interface RadialItem {
      class_2561 name();

      RadialIcon icon();

      boolean playAction();
   }

   public class RadialButton implements class_4068, class_364, class_6379, ComponentProcessor {
      public static final class_2960 TEXTURE = CUtil.rl("textures/gui/radial-buttons.png");
      private int x;
      private int y;
      private float translateX;
      private float translateY;
      private boolean focused;
      private RadialMenuScreen.RadialItem item;
      private class_5489 name;

      private RadialButton(RadialMenuScreen.RadialItem item, float x, float y) {
         this.setX(x);
         this.setY(y);
         this.setAction(item);
      }

      public void method_25394(class_332 graphics, int mouseX, int mouseY, float delta) {
         CGuiPose pose = CGuiPose.ofPush(graphics);
         pose.translate((float)this.x + this.translateX, (float)this.y + this.translateY);
         pose.push();
         pose.scale(2.0F, 2.0F);
         Blit.tex(graphics, TEXTURE, 0, 0, this.focused ? 16 : 0, 0, 16, 16, 32, 16);
         pose.pop();
         int var10003;
         if (RadialMenuScreen.this.editMode != null && this.focused) {
            class_2561 bind = ControlifyBindings.GUI_PRESS.on(RadialMenuScreen.this.controller).inputIcon();
            class_327 var10001 = RadialMenuScreen.this.field_22793;
            var10003 = 16 - RadialMenuScreen.this.field_22793.method_27525(bind) / 2;
            Objects.requireNonNull(RadialMenuScreen.this.field_22793);
            graphics.method_27535(var10001, bind, var10003, 16 - 9 / 2, -1);
         } else {
            pose.push();
            pose.translate(4.0F, 4.0F);
            pose.scale(1.5F, 1.5F);
            this.item.icon().draw(graphics, 0, 0, delta);
            pose.pop();
         }

         pose.pop();
         if (this.focused) {
            class_5489 var10000 = this.name;
            int var10002 = RadialMenuScreen.this.field_22789 / 2;
            var10003 = RadialMenuScreen.this.field_22790 / 2;
            Objects.requireNonNull(RadialMenuScreen.this.field_22793);
            var10003 -= 9 / 2;
            int var10004 = this.name.method_30887() - 1;
            Objects.requireNonNull(RadialMenuScreen.this.field_22793);
            var10000.method_30888(graphics, var10002, var10003 - var10004 * 9 / 2);
         }

      }

      public boolean invoke() {
         return this.item.playAction();
      }

      public void setAction(RadialMenuScreen.RadialItem item) {
         this.item = item;
         this.name = class_5489.method_30890(RadialMenuScreen.this.field_22793, item.name(), (int)(RadialMenuScreen.this.radialRadius * 2.0F - 32.0F));
      }

      public int getX() {
         return this.x;
      }

      public int getY() {
         return this.y;
      }

      public void setX(float x) {
         this.x = (int)x;
         this.translateX = x - (float)this.x;
      }

      public void setY(float y) {
         this.y = (int)y;
         this.translateY = y - (float)this.y;
      }

      public boolean method_25370() {
         return this.focused;
      }

      public void method_25365(boolean focused) {
         this.focused = focused;
      }

      public boolean overrideControllerButtons(ScreenProcessor<?> screen, ControllerEntity controller) {
         if (RadialMenuScreen.this.editMode != null && controller == RadialMenuScreen.this.controller && ControlifyBindings.GUI_PRESS.on(controller).justPressed()) {
            RadialMenuScreen.RadialButton button = RadialMenuScreen.this.buttons[RadialMenuScreen.this.selectedButton];
            int x = button.x < RadialMenuScreen.this.field_22789 / 2 ? button.x - 110 : button.x + 42;
            RadialMenuScreen.this.actionSelectList = RadialMenuScreen.this.new ActionSelectList(RadialMenuScreen.this.selectedButton, x, button.y, 100, 80);
            RadialMenuScreen.this.method_37063(RadialMenuScreen.this.actionSelectList);
            RadialMenuScreen.this.method_25395(RadialMenuScreen.this.actionSelectList);
            RadialMenuScreen.this.isEditing = true;
            return true;
         } else {
            return false;
         }
      }

      public class_6380 method_37018() {
         return this.method_25370() ? class_6380.field_33786 : class_6380.field_33784;
      }

      public void method_37020(class_6382 builder) {
         builder.method_37034(class_6381.field_33788, this.item.name());
      }

      public class_8030 method_48202() {
         return new class_8030(this.x, this.y, 32, 32);
      }
   }

   public interface EditMode {
      void setRadialItem(int var1, RadialMenuScreen.RadialItem var2);

      List<RadialMenuScreen.RadialItem> getEditCandidates();
   }

   public class ActionSelectList implements class_4068, class_4069, class_6379, ComponentProcessor {
      private final int radialIndex;
      private int x;
      private int y;
      private int width;
      private int height;
      private final int itemHeight = 10;
      private int scrollOffset;
      private boolean focused;
      private RadialMenuScreen.ActionSelectList.ActionEntry focusedEntry;
      private final List<RadialMenuScreen.ActionSelectList.ActionEntry> children = new ArrayList();

      public ActionSelectList(int index, int x, int y, int width, int height) {
         this.radialIndex = index;
         this.x = x;
         this.y = y;
         this.width = width;
         this.height = height;
         Iterator var7 = RadialMenuScreen.this.editMode.getEditCandidates().iterator();

         while(var7.hasNext()) {
            RadialMenuScreen.RadialItem item = (RadialMenuScreen.RadialItem)var7.next();
            this.children.add(new RadialMenuScreen.ActionSelectList.ActionEntry(item));
         }

         RadialMenuScreen.RadialItem itemx = RadialMenuScreen.this.items[this.radialIndex];
         this.children.stream().filter((action) -> {
            return action.item.equals(itemx);
         }).findAny().ifPresent(this::method_25395);
      }

      public void method_25394(class_332 graphics, int mouseX, int mouseY, float delta) {
         graphics.method_25294(this.x, this.y, this.x + this.width, this.y + this.height, Integer.MIN_VALUE);
         graphics.method_44379(this.x, this.y, this.x + this.width, this.y + this.height);
         int y = this.y - this.scrollOffset;

         for(Iterator var6 = this.children.iterator(); var6.hasNext(); y += 10) {
            RadialMenuScreen.ActionSelectList.ActionEntry child = (RadialMenuScreen.ActionSelectList.ActionEntry)var6.next();
            child.render(graphics, this.x, y, this.width, 10, mouseX, mouseY, delta);
         }

         graphics.method_44380();
         graphics.method_49601(this.x - 1, this.y - 1, this.width + 2, this.height + 2, -2130706433);
      }

      public boolean overrideControllerButtons(ScreenProcessor<?> screen, ControllerEntity controller) {
         if (controller == RadialMenuScreen.this.controller && ControlifyBindings.GUI_BACK.on(controller).justPressed()) {
            RadialMenuScreen.this.finishEditing();
            return true;
         } else {
            return false;
         }
      }

      public List<RadialMenuScreen.ActionSelectList.ActionEntry> method_25396() {
         return this.children;
      }

      public boolean method_25397() {
         return false;
      }

      public void method_25398(boolean dragging) {
      }

      @Nullable
      public RadialMenuScreen.ActionSelectList.ActionEntry getFocused() {
         return this.focusedEntry;
      }

      public void method_25395(@Nullable class_364 child) {
         RadialMenuScreen.ActionSelectList.ActionEntry focus = (RadialMenuScreen.ActionSelectList.ActionEntry)child;
         this.focusedEntry = focus;
         if (focus != null) {
            int index = this.method_25396().indexOf(child);
            if (index != -1) {
               int focusY = index * 10 - this.scrollOffset;
               if (focusY < 0) {
                  this.scrollOffset = class_3532.method_15340(index * 10, 0, this.method_25396().size() * 10 - this.height);
               } else if (focusY + 10 > this.height) {
                  this.scrollOffset = class_3532.method_15340(index * 10 + 10 - this.height, 0, this.method_25396().size() * 10 - this.height);
               }
            }
         }

      }

      public void method_25365(boolean focused) {
         this.focused = focused;
      }

      public boolean method_25370() {
         return this.focused;
      }

      public class_6380 method_37018() {
         return this.focused ? class_6380.field_33786 : class_6380.field_33784;
      }

      public void method_37020(class_6382 builder) {
         if (this.getFocused() != null) {
            builder.method_37034(class_6381.field_33788, this.getFocused().item.name());
         }

      }

      public class ActionEntry implements class_364, ComponentProcessor {
         private int x;
         private int y;
         private boolean focused;
         private final RadialMenuScreen.RadialItem item;

         public ActionEntry(RadialMenuScreen.RadialItem item) {
            this.item = item;
         }

         public void render(class_332 graphics, int x, int y, int width, int itemHeight, int mouseX, int mouseY, float delta) {
            this.x = x;
            this.y = y;
            if (this.focused) {
               graphics.method_25294(x, y, x + width, y + itemHeight, -16777216);
            }

            graphics.method_27535(RadialMenuScreen.this.field_22793, this.item.name(), x + 2, y + 1, this.focused ? -1 : -5855578);
         }

         public void method_25365(boolean focused) {
            this.focused = focused;
         }

         public boolean method_25370() {
            return this.focused;
         }

         @Nullable
         public class_8016 method_48205(class_8023 event) {
            return !this.focused ? class_8016.method_48193(this) : null;
         }

         public class_8030 method_48202() {
            return new class_8030(this.x, this.y, ActionSelectList.this.width, 10);
         }

         public boolean overrideControllerButtons(ScreenProcessor<?> screen, ControllerEntity controller) {
            if (controller == RadialMenuScreen.this.controller && ControlifyBindings.GUI_PRESS.on(controller).justPressed()) {
               RadialMenuScreen.this.editMode.setRadialItem(ActionSelectList.this.radialIndex, this.item);
               Controlify.instance().config().setDirty();
               RadialMenuScreen.this.buttons[ActionSelectList.this.radialIndex].setAction(this.item);
               RadialMenuScreen.this.playClickSound();
               RadialMenuScreen.this.finishEditing();
               return true;
            } else {
               return false;
            }
         }
      }
   }
}
