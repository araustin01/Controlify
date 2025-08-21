package dev.isxander.controlify.virtualmouse;

import com.mojang.datafixers.util.Pair;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.InputMode;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.api.event.ControlifyEvents;
import dev.isxander.controlify.api.vmousesnapping.ISnapBehaviour;
import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.input.GamepadInputs;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.controller.touchpad.TouchpadComponent;
import dev.isxander.controlify.debug.DebugProperties;
import dev.isxander.controlify.mixins.feature.virtualmouse.KeyboardHandlerAccessor;
import dev.isxander.controlify.mixins.feature.virtualmouse.MouseHandlerAccessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.ControllerUtils;
import dev.isxander.controlify.utils.HoldRepeatHelper;
import dev.isxander.controlify.utils.ToastUtils;
import dev.isxander.controlify.utils.render.Blit;
import dev.isxander.controlify.utils.render.CGuiPose;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import net.minecraft.class_1041;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_437;
import net.minecraft.class_8027;
import net.minecraft.class_8028;
import org.joml.Vector2d;
import org.joml.Vector2dc;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;

public class VirtualMouseHandler {
   private static final class_2960 CURSOR_TEXTURE = CUtil.rl("textures/gui/virtual_mouse.png");
   private double targetX;
   private double targetY;
   private double currentX;
   private double currentY;
   private double scrollX;
   private double scrollY;
   private float prevXFinger;
   private float prevYFinger;
   private final class_310 minecraft = class_310.method_1551();
   private boolean virtualMouseEnabled;
   private Set<SnapPoint> snapPoints = this.collectSnapPoints();
   private SnapPoint lastSnappedPoint;
   private final HoldRepeatHelper holdRepeatHelper = new HoldRepeatHelper(10, 3);

   public VirtualMouseHandler() {
      ControlifyEvents.INPUT_MODE_CHANGED.register((event) -> {
         this.onInputModeChanged(event.mode());
      });
   }

   public void handleControllerInput(ControllerEntity controller) {
      if (ControlifyBindings.VMOUSE_TOGGLE.on(controller).justPressed()) {
         this.toggleVirtualMouse();
      }

      if (this.virtualMouseEnabled) {
         InputComponent input = (InputComponent)controller.input().orElseThrow();
         Optional<TouchpadComponent> touchpad = controller.touchpad();
         InputBinding moveRight = ControlifyBindings.VMOUSE_MOVE_RIGHT.on(controller);
         InputBinding moveLeft = ControlifyBindings.VMOUSE_MOVE_LEFT.on(controller);
         InputBinding moveDown = ControlifyBindings.VMOUSE_MOVE_DOWN.on(controller);
         InputBinding moveUp = ControlifyBindings.VMOUSE_MOVE_UP.on(controller);
         Vector2d impulse = ControllerUtils.applyEasingToLength((double)(moveRight.analogueNow() - moveLeft.analogueNow()), (double)(moveDown.analogueNow() - moveUp.analogueNow()), (x) -> {
            return Math.pow(x, 3.0D);
         });
         Vector2d prevImpulse = ControllerUtils.applyEasingToLength((double)(moveRight.analoguePrev() - moveLeft.analoguePrev()), (double)(moveDown.analoguePrev() - moveUp.analoguePrev()), (x) -> {
            return Math.pow(x, 3.0D);
         });
         this.snapPoints = this.collectSnapPoints();
         if (impulse.x == 0.0D && impulse.y == 0.0D && (prevImpulse.x != 0.0D || prevImpulse.y != 0.0D)) {
            this.snapToClosestPoint();
         }

         float sensitivity = ((InputComponent.Config)input.config().config()).virtualMouseSensitivity;
         float windowSizeModifier;
         if (!((InputComponent.Config)input.confObj()).isLCE) {
            windowSizeModifier = (float)Math.max(this.minecraft.method_22683().method_4489(), this.minecraft.method_22683().method_4506()) / 800.0F;
            this.targetX += impulse.x * 20.0D * (double)sensitivity * (double)windowSizeModifier;
            this.targetY += impulse.y * 20.0D * (double)sensitivity * (double)windowSizeModifier;
         } else {
            windowSizeModifier = (float)this.minecraft.method_22683().method_4480() / (float)this.minecraft.method_22683().method_4486();
            this.targetX += impulse.x * 10.0D * (double)sensitivity * (double)windowSizeModifier;
            this.targetY += impulse.y * 10.0D * (double)sensitivity * (double)windowSizeModifier;
         }

         this.targetX = class_3532.method_15350(this.targetX, 0.0D, (double)this.minecraft.method_22683().method_4489());
         this.targetY = class_3532.method_15350(this.targetY, 0.0D, (double)this.minecraft.method_22683().method_4506());
         this.scrollY += (double)(ControlifyBindings.VMOUSE_SCROLL_UP.on(controller).analogueNow() - ControlifyBindings.VMOUSE_SCROLL_DOWN.on(controller).analogueNow());
         if (this.holdRepeatHelper.shouldAction(ControlifyBindings.VMOUSE_SNAP_UP.on(controller))) {
            this.snapInDirection(class_8028.field_41826);
            this.holdRepeatHelper.onNavigate();
         } else if (this.holdRepeatHelper.shouldAction(ControlifyBindings.VMOUSE_SNAP_DOWN.on(controller))) {
            this.snapInDirection(class_8028.field_41827);
            this.holdRepeatHelper.onNavigate();
         } else if (this.holdRepeatHelper.shouldAction(ControlifyBindings.VMOUSE_SNAP_LEFT.on(controller))) {
            this.snapInDirection(class_8028.field_41828);
            this.holdRepeatHelper.onNavigate();
         } else if (this.holdRepeatHelper.shouldAction(ControlifyBindings.VMOUSE_SNAP_RIGHT.on(controller))) {
            this.snapInDirection(class_8028.field_41829);
            this.holdRepeatHelper.onNavigate();
         }

         if (ScreenProcessorProvider.provide(this.minecraft.field_1755).virtualMouseBehaviour().isDefaultOr(VirtualMouseBehaviour.ENABLED)) {
            this.handleCompatibilityBinds(controller);
         }

         if (ControlifyBindings.GUI_BACK.on(controller).justPressed() && this.minecraft.field_1755 != null) {
            ScreenProcessor.playClackSound();
            this.minecraft.field_1755.method_25419();
         }

      }
   }

   public void handleCompatibilityBinds(ControllerEntity controller) {
      MouseHandlerAccessor mouseHandler = (MouseHandlerAccessor)this.minecraft.field_1729;
      KeyboardHandlerAccessor keyboardHandler = (KeyboardHandlerAccessor)this.minecraft.field_1774;
      InputComponent input = (InputComponent)controller.input().orElseThrow();
      boolean touchpadPressed = input.stateNow().isButtonDown(GamepadInputs.TOUCHPAD_1_BUTTON);
      boolean prevTouchpadPressed = input.stateThen().isButtonDown(GamepadInputs.TOUCHPAD_1_BUTTON);
      if (ControlifyBindings.VMOUSE_LCLICK.on(controller).justPressed() || touchpadPressed && !prevTouchpadPressed) {
         mouseHandler.invokeOnPress(this.minecraft.method_22683().method_4490(), 0, 1, 0);
      } else if (ControlifyBindings.VMOUSE_LCLICK.on(controller).justReleased() || !touchpadPressed && prevTouchpadPressed) {
         mouseHandler.invokeOnPress(this.minecraft.method_22683().method_4490(), 0, 0, 0);
      }

      if (ControlifyBindings.VMOUSE_RCLICK.on(controller).justPressed() || touchpadPressed && !prevTouchpadPressed) {
         mouseHandler.invokeOnPress(this.minecraft.method_22683().method_4490(), 1, 1, 0);
      } else if (ControlifyBindings.VMOUSE_RCLICK.on(controller).justReleased() || !touchpadPressed && prevTouchpadPressed) {
         mouseHandler.invokeOnPress(this.minecraft.method_22683().method_4490(), 1, 0, 0);
      }

      if (ControlifyBindings.VMOUSE_SHIFT_CLICK.on(controller).justPressed()) {
         mouseHandler.invokeOnPress(this.minecraft.method_22683().method_4490(), 0, 1, 0);
      } else if (ControlifyBindings.VMOUSE_SHIFT_CLICK.on(controller).justReleased()) {
         mouseHandler.invokeOnPress(this.minecraft.method_22683().method_4490(), 0, 0, 0);
      }

   }

   public void updateMouse() {
      if (this.virtualMouseEnabled) {
         float delta = this.minecraft.method_61966().method_60638();
         if ((double)Math.round(this.targetX * 100.0D) / 100.0D == (double)Math.round(this.currentX * 100.0D) / 100.0D && (double)Math.round(this.targetY * 100.0D) / 100.0D == (double)Math.round(this.currentY * 100.0D) / 100.0D) {
            this.currentX = this.targetX;
            this.currentY = this.targetY;
         } else {
            this.currentX = class_3532.method_16436((double)delta, this.currentX, this.targetX);
            this.currentY = class_3532.method_16436((double)delta, this.currentY, this.targetY);
            ((MouseHandlerAccessor)this.minecraft.field_1729).invokeOnMove(this.minecraft.method_22683().method_4490(), this.currentX, this.currentY);
         }

         if (!(Math.abs(this.scrollX) >= 0.01D) && !(Math.abs(this.scrollY) >= 0.01D)) {
            this.scrollX = this.scrollY = 0.0D;
         } else {
            double currentScrollY = this.scrollY * (double)delta;
            this.scrollY -= currentScrollY;
            double currentScrollX = this.scrollX * (double)delta;
            this.scrollX -= currentScrollX;
            ((MouseHandlerAccessor)this.minecraft.field_1729).invokeOnScroll(this.minecraft.method_22683().method_4490(), currentScrollX, currentScrollY);
         }

      }
   }

   public void snapToClosestPoint() {
      class_1041 window = this.minecraft.method_22683();
      Vector2d scaleFactor = new Vector2d((double)window.method_4486() / (double)window.method_4480(), (double)window.method_4502() / (double)window.method_4507());
      Vector2d target = (new Vector2d(this.targetX, this.targetY)).mul(scaleFactor);
      if (this.lastSnappedPoint != null && this.lastSnappedPoint.position().distanceSquared(new Vector2i(target, 2)) > (long)this.lastSnappedPoint.range() * (long)this.lastSnappedPoint.range()) {
         this.lastSnappedPoint = null;
      }

      SnapPoint closestSnapPoint = (SnapPoint)((Pair)this.snapPoints.stream().filter((snapPoint) -> {
         return !snapPoint.equals(this.lastSnappedPoint);
      }).map((snapPoint) -> {
         return new Pair(snapPoint, snapPoint.position().distanceSquared(new Vector2i(target, 2)));
      }).filter((point) -> {
         return (Long)point.getSecond() <= (long)((SnapPoint)point.getFirst()).range() * (long)((SnapPoint)point.getFirst()).range();
      }).min(Comparator.comparingLong(Pair::getSecond)).orElse(new Pair((Object)null, Long.MAX_VALUE))).getFirst();
      if (closestSnapPoint != null) {
         this.snapToPoint(closestSnapPoint, scaleFactor);
      }

   }

   public void snapInDirection(class_8028 direction) {
      class_1041 window = this.minecraft.method_22683();
      Vector2d scaleFactor = new Vector2d((double)window.method_4486() / (double)window.method_4480(), (double)window.method_4502() / (double)window.method_4507());
      Vector2d target = (new Vector2d(this.targetX, this.targetY)).mul(scaleFactor);
      Optional<SnapPoint> closestSnapPoint = findOrthogonalSnapPoint(this.lastSnappedPoint, direction, target, this.snapPoints).or(() -> {
         Vector2d orthogonalTarget = new Vector2d(target);
         switch(direction) {
         case field_41826:
            orthogonalTarget.y = (double)window.method_4502();
            break;
         case field_41827:
            orthogonalTarget.y = 0.0D;
            break;
         case field_41828:
            orthogonalTarget.x = (double)window.method_4486();
            break;
         case field_41829:
            orthogonalTarget.x = 0.0D;
         }

         return findOrthogonalSnapPoint(this.lastSnappedPoint, direction, orthogonalTarget, this.snapPoints);
      });
      closestSnapPoint.ifPresent((snapPoint) -> {
         this.snapToPoint(snapPoint, scaleFactor);
      });
   }

   private static Optional<SnapPoint> findOrthogonalSnapPoint(SnapPoint from, class_8028 direction, Vector2d target, Collection<SnapPoint> snapPoints) {
      return snapPoints.stream().filter((snapPoint) -> {
         return !snapPoint.equals(from);
      }).map((snapPoint) -> {
         return new Pair(snapPoint, new Vector2d((double)snapPoint.position().x() - target.x(), (double)snapPoint.position().y() - target.y()));
      }).filter((pair) -> {
         Vector2d dist = (Vector2d)pair.getSecond();
         double axis = direction.method_48237() == class_8027.field_41822 ? dist.x : dist.y;
         double positive = direction.method_48241() ? 1.0D : -1.0D;
         return axis * positive > 0.0D;
      }).filter((pair) -> {
         SnapPoint snapPoint = (SnapPoint)pair.getFirst();
         Vector2d dist = (Vector2d)pair.getSecond();
         double distance = Math.abs(direction.method_48237() == class_8027.field_41822 ? dist.x : dist.y);
         double deviation = Math.abs(direction.method_48237() == class_8027.field_41822 ? dist.y : dist.x);
         ((Vector2d)pair.getSecond()).set(distance, deviation * 4.0D);
         return distance >= (double)snapPoint.range() && deviation < distance * 2.0D;
      }).min(Comparator.comparingDouble((pair) -> {
         Vector2d distDev = (Vector2d)pair.getSecond();
         return distDev.x + distDev.y;
      })).map(Pair::getFirst);
   }

   public void snapToPoint(SnapPoint snapPoint, Vector2dc scaleFactor) {
      this.lastSnappedPoint = snapPoint;
      this.targetX = this.currentX = (double)snapPoint.position().x() / scaleFactor.x();
      this.targetY = this.currentY = (double)snapPoint.position().y() / scaleFactor.y();
      ((MouseHandlerAccessor)this.minecraft.field_1729).invokeOnMove(this.minecraft.method_22683().method_4490(), this.currentX, this.currentY);
   }

   public void onScreenChanged() {
      if (this.minecraft.field_1755 != null) {
         if (this.requiresVirtualMouse()) {
            this.enableVirtualMouse();
         } else {
            this.disableVirtualMouse();
         }

         if (Controlify.instance().currentInputMode().isController()) {
            GLFW.glfwSetInputMode(this.minecraft.method_22683().method_4490(), 208897, 212994);
         }
      } else if (this.virtualMouseEnabled) {
         this.disableVirtualMouse();
         this.minecraft.field_1729.method_1612();
      }

   }

   public void onInputModeChanged(InputMode mode) {
      if (mode.isController()) {
         if (this.requiresVirtualMouse()) {
            this.enableVirtualMouse();
         }
      } else if (this.virtualMouseEnabled) {
         this.disableVirtualMouse();
      }

   }

   public void renderVirtualMouse(class_332 graphics) {
      if (this.virtualMouseEnabled) {
         if (DebugProperties.DEBUG_SNAPPING) {
            Iterator var2 = this.snapPoints.iterator();

            while(var2.hasNext()) {
               SnapPoint snapPoint = (SnapPoint)var2.next();
               graphics.method_25294(snapPoint.position().x() - snapPoint.range(), snapPoint.position().y() - snapPoint.range(), snapPoint.position().x() + snapPoint.range(), snapPoint.position().y() + snapPoint.range(), 872415231);
               graphics.method_25294(snapPoint.position().x() - 1, snapPoint.position().y() - 1, snapPoint.position().x() + 1, snapPoint.position().y() + 1, snapPoint.equals(this.lastSnappedPoint) ? -256 : -65536);
            }
         }

         double scaledX = this.currentX * (double)this.minecraft.method_22683().method_4486() / (double)this.minecraft.method_22683().method_4480();
         double scaledY = this.currentY * (double)this.minecraft.method_22683().method_4502() / (double)this.minecraft.method_22683().method_4507();
         CGuiPose pose = CGuiPose.ofPush(graphics);
         pose.translate((float)scaledX, (float)scaledY);
         pose.scale(0.5F, 0.5F);
         Blit.tex(graphics, CURSOR_TEXTURE, -16, -16, 0, 0, 32, 32, 32, 32);
         pose.pop();
      }
   }

   public void enableVirtualMouse() {
      if (!this.virtualMouseEnabled) {
         GLFW.glfwSetInputMode(this.minecraft.method_22683().method_4490(), 208897, 212995);
         this.virtualMouseEnabled = true;
         if (this.minecraft.field_1729.method_1603() == -50.0D && this.minecraft.field_1729.method_1604() == -50.0D) {
            this.targetX = this.currentX = (double)((float)this.minecraft.method_22683().method_4480() / 2.0F);
            this.targetY = this.currentY = (double)((float)this.minecraft.method_22683().method_4507() / 2.0F);
         } else {
            this.targetX = this.currentX = this.minecraft.field_1729.method_1603();
            this.targetY = this.currentY = this.minecraft.field_1729.method_1604();
         }

         this.setMousePosition();
         ControlifyEvents.VIRTUAL_MOUSE_TOGGLED.invoke(new ControlifyEvents.VirtualMouseToggled(true));
         if (this.minecraft.field_1755 != null) {
            ScreenProcessorProvider.provide(this.minecraft.field_1755).onVirtualMouseToggled(true);
         }

      }
   }

   public void disableVirtualMouse() {
      if (this.virtualMouseEnabled) {
         ((MouseHandlerAccessor)this.minecraft.field_1729).setMouseGrabbed(false);
         Controlify.instance().hideMouse(true, true);
         GLFW.glfwSetInputMode(this.minecraft.method_22683().method_4490(), 208897, 212993);
         this.setMousePosition();
         this.virtualMouseEnabled = false;
         this.targetX = this.currentX = this.minecraft.field_1729.method_1603();
         this.targetY = this.currentY = this.minecraft.field_1729.method_1604();
         ControlifyEvents.VIRTUAL_MOUSE_TOGGLED.invoke(new ControlifyEvents.VirtualMouseToggled(false));
         if (this.minecraft.field_1755 != null) {
            ScreenProcessorProvider.provide(this.minecraft.field_1755).onVirtualMouseToggled(false);
         }

      }
   }

   private void setMousePosition() {
      GLFW.glfwSetCursorPos(this.minecraft.method_22683().method_4490(), this.targetX, this.targetY);
   }

   public boolean requiresVirtualMouse() {
      boolean isController = Controlify.instance().currentInputMode().isController();
      boolean hasScreen = this.minecraft.field_1755 != null;
      if (isController && hasScreen) {
         boolean var10000;
         switch(ScreenProcessorProvider.provide(this.minecraft.field_1755).virtualMouseBehaviour()) {
         case DEFAULT:
            var10000 = Controlify.instance().config().globalSettings().virtualMouseScreens.stream().anyMatch((s) -> {
               return s.isAssignableFrom(this.minecraft.field_1755.getClass());
            });
            break;
         case ENABLED:
         case CURSOR_ONLY:
            var10000 = true;
            break;
         case DISABLED:
            var10000 = false;
            break;
         default:
            throw new MatchException((String)null, (Throwable)null);
         }

         return var10000;
      } else {
         return false;
      }
   }

   public void toggleVirtualMouse() {
      if (this.minecraft.field_1755 != null) {
         if (ScreenProcessorProvider.provide(this.minecraft.field_1755).virtualMouseBehaviour() != VirtualMouseBehaviour.DEFAULT) {
            ToastUtils.sendToast(class_2561.method_43471("controlify.toast.vmouse_unavailable.title"), class_2561.method_43471("controlify.toast.vmouse_unavailable.description"), false);
         } else {
            List<Class<?>> screens = Controlify.instance().config().globalSettings().virtualMouseScreens;
            Class<? extends class_437> screenClass = this.minecraft.field_1755.getClass();
            if (screens.contains(screenClass)) {
               screens.remove(screenClass);
               this.disableVirtualMouse();
               Controlify.instance().hideMouse(true, false);
               ToastUtils.sendToast(class_2561.method_43471("controlify.toast.vmouse_disabled.title"), class_2561.method_43471("controlify.toast.vmouse_disabled.description"), false);
            } else {
               screens.add(screenClass);
               this.enableVirtualMouse();
               ToastUtils.sendToast(class_2561.method_43471("controlify.toast.vmouse_enabled.title"), class_2561.method_43471("controlify.toast.vmouse_enabled.description"), false);
            }

            Controlify.instance().config().save();
         }
      }
   }

   public boolean isVirtualMouseEnabled() {
      return this.virtualMouseEnabled;
   }

   public int getCurrentX(float deltaTime) {
      return (int)class_3532.method_16436((double)deltaTime, this.currentX, this.targetX);
   }

   public int getCurrentY(float deltaTime) {
      return (int)class_3532.method_16436((double)deltaTime, this.currentY, this.targetY);
   }

   public void preventScrollingThisTick() {
      this.scrollX = 0.0D;
      this.scrollY = 0.0D;
   }

   private Set<SnapPoint> collectSnapPoints() {
      class_437 var2 = this.minecraft.field_1755;
      if (var2 instanceof ISnapBehaviour) {
         ISnapBehaviour snapBehaviour = (ISnapBehaviour)var2;
         Set<SnapPoint> points = new HashSet();
         Objects.requireNonNull(points);
         snapBehaviour.controlify$collectSnapPoints(points::add);
         return points;
      } else {
         return Set.of();
      }
   }
}
