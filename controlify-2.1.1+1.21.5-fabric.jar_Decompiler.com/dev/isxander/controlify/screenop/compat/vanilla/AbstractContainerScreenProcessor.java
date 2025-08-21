package dev.isxander.controlify.screenop.compat.vanilla;

import dev.isxander.controlify.InputMode;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.GenericControllerConfig;
import dev.isxander.controlify.controller.haptic.HapticEffects;
import dev.isxander.controlify.gui.guide.ContainerGuideCtx;
import dev.isxander.controlify.gui.guide.GuideAction;
import dev.isxander.controlify.gui.guide.GuideActionRenderer;
import dev.isxander.controlify.gui.layout.AnchorPoint;
import dev.isxander.controlify.gui.layout.ColumnLayoutComponent;
import dev.isxander.controlify.gui.layout.PositionedComponent;
import dev.isxander.controlify.gui.layout.RowLayoutComponent;
import dev.isxander.controlify.mixins.feature.guide.screen.AbstractContainerScreenAccessor;
import dev.isxander.controlify.mixins.feature.screenop.ScreenAccessor;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.virtualmouse.VirtualMouseBehaviour;
import dev.isxander.controlify.virtualmouse.VirtualMouseHandler;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_2561;
import net.minecraft.class_3489;
import net.minecraft.class_4068;
import net.minecraft.class_465;
import net.minecraft.class_5537;

public class AbstractContainerScreenProcessor<T extends class_465<?>> extends ScreenProcessor<T> {
   private PositionedComponent<ColumnLayoutComponent<RowLayoutComponent<GuideActionRenderer<ContainerGuideCtx>>>> leftLayout;
   private PositionedComponent<ColumnLayoutComponent<RowLayoutComponent<GuideActionRenderer<ContainerGuideCtx>>>> rightLayout;
   private final Supplier<class_1735> hoveredSlot;
   private final AbstractContainerScreenProcessor.ClickSlotFunction clickSlotFunction;
   private final Predicate<ControllerEntity> doItemSlotActions;

   public AbstractContainerScreenProcessor(T screen, Supplier<class_1735> hoveredSlot, AbstractContainerScreenProcessor.ClickSlotFunction clickSlotFunction, Predicate<ControllerEntity> doItemSlotActions) {
      super(screen);
      this.hoveredSlot = hoveredSlot;
      this.clickSlotFunction = clickSlotFunction;
      this.doItemSlotActions = doItemSlotActions;
   }

   protected void handleScreenVMouse(ControllerEntity controller, VirtualMouseHandler vmouse) {
      AbstractContainerScreenAccessor accessor = (AbstractContainerScreenAccessor)this.screen;
      ContainerGuideCtx ctx = new ContainerGuideCtx((class_1735)this.hoveredSlot.get(), ((class_465)this.screen).method_17577().method_34255(), accessor.invokeHasClickedOutside((double)vmouse.getCurrentX(1.0F), (double)vmouse.getCurrentY(1.0F), accessor.getLeftPos(), accessor.getTopPos(), 0));
      class_1735 hoveredSlot = (class_1735)this.hoveredSlot.get();
      if (hoveredSlot != null) {
         if (hoveredSlot.method_7681() && this.doItemSlotActions.test(controller)) {
            return;
         }

         if (ControlifyBindings.INV_SELECT.on(controller).justPressed()) {
            this.clickSlotFunction.clickSlot(hoveredSlot, hoveredSlot.field_7874, 0, class_1713.field_7790);
            this.hapticNavigate();
         }

         if (ControlifyBindings.INV_QUICK_MOVE.on(controller).justPressed()) {
            this.clickSlotFunction.clickSlot(hoveredSlot, hoveredSlot.field_7874, 0, class_1713.field_7794);
            this.hapticNavigate();
         }

         if (ControlifyBindings.INV_TAKE_HALF.on(controller).justPressed()) {
            this.clickSlotFunction.clickSlot(hoveredSlot, hoveredSlot.field_7874, 1, class_1713.field_7790);
            this.hapticNavigate();
         }
      } else {
         vmouse.handleCompatibilityBinds(controller);
      }

      if (!((class_465)this.screen).method_17577().method_34255().method_7960() && ControlifyBindings.DROP_INVENTORY.on(controller).justPressed()) {
         this.clickSlotFunction.clickSlot((class_1735)null, -999, 0, class_1713.field_7790);
         this.hapticNavigate();
      }

      if (this.leftLayout != null && this.rightLayout != null) {
         Iterator var6 = ((ColumnLayoutComponent)this.leftLayout.getComponent()).getChildComponents().iterator();

         RowLayoutComponent row;
         Iterator var8;
         GuideActionRenderer element;
         while(var6.hasNext()) {
            row = (RowLayoutComponent)var6.next();
            var8 = row.getChildComponents().iterator();

            while(var8.hasNext()) {
               element = (GuideActionRenderer)var8.next();
               element.updateName(ctx);
            }
         }

         var6 = ((ColumnLayoutComponent)this.rightLayout.getComponent()).getChildComponents().iterator();

         while(var6.hasNext()) {
            row = (RowLayoutComponent)var6.next();
            var8 = row.getChildComponents().iterator();

            while(var8.hasNext()) {
               element = (GuideActionRenderer)var8.next();
               element.updateName(ctx);
            }
         }

         this.leftLayout.updatePosition(((class_465)this.screen).field_22789, ((class_465)this.screen).field_22790);
         this.rightLayout.updatePosition(((class_465)this.screen).field_22789, ((class_465)this.screen).field_22790);
      }

   }

   public void onWidgetRebuild() {
      ControllerEntity controller = (ControllerEntity)ControlifyApi.get().getCurrentController().filter((c) -> {
         return c.input().isPresent();
      }).orElse((Object)null);
      if (controller != null) {
         this.leftLayout = new PositionedComponent(ColumnLayoutComponent.builder().spacing(2).elementPosition(ColumnLayoutComponent.ElementPosition.LEFT).colPadding(2).element(RowLayoutComponent.builder().spacing(5).rowPadding(0).elementPosition(RowLayoutComponent.ElementPosition.MIDDLE).element(new GuideActionRenderer(new GuideAction(ControlifyBindings.INV_SELECT.on(controller), (ctx) -> {
            if (!ctx.holdingItem().method_7960()) {
               if (ctx.hoveredSlot() != null && ctx.hoveredSlot().method_7681()) {
                  if (ctx.hoveredSlot().method_7680(ctx.holdingItem())) {
                     if (ctx.holdingItem().method_7947() > 1) {
                        return Optional.of(class_2561.method_43471("controlify.guide.container.place_all"));
                     }

                     return Optional.of(class_2561.method_43471("controlify.guide.container.place_one"));
                  }

                  return Optional.of(class_2561.method_43471("controlify.guide.container.swap"));
               }

               if (ctx.cursorOutsideContainer()) {
                  return Optional.of(class_2561.method_43471("controlify.guide.container.drop"));
               }
            }

            return ctx.hoveredSlot() != null && ctx.hoveredSlot().method_7681() ? Optional.of(class_2561.method_43471("controlify.guide.container.take")) : Optional.empty();
         }), false, false)).element(new GuideActionRenderer(new GuideAction(ControlifyBindings.GUI_BACK.on(controller), (ctx) -> {
            return Optional.of(class_2561.method_43471("controlify.guide.container.exit"));
         }), false, false)).build()).build(), AnchorPoint.BOTTOM_LEFT, 0, 0, AnchorPoint.BOTTOM_LEFT);
         this.rightLayout = new PositionedComponent(ColumnLayoutComponent.builder().spacing(2).elementPosition(ColumnLayoutComponent.ElementPosition.RIGHT).colPadding(2).element(RowLayoutComponent.builder().spacing(5).rowPadding(0).elementPosition(RowLayoutComponent.ElementPosition.MIDDLE).element(new GuideActionRenderer(new GuideAction(ControlifyBindings.DROP_INVENTORY.on(controller), (ctx) -> {
            return !ctx.holdingItem().method_7960() ? Optional.of(class_2561.method_43471("controlify.guide.container.drop")) : Optional.empty();
         }), true, false)).build()).element(RowLayoutComponent.builder().spacing(5).rowPadding(0).elementPosition(RowLayoutComponent.ElementPosition.MIDDLE).element(new GuideActionRenderer(new GuideAction(ControlifyBindings.INV_QUICK_MOVE.on(controller), (ctx) -> {
            return ctx.hoveredSlot() != null && ctx.hoveredSlot().method_7681() && ctx.holdingItem().method_7960() ? Optional.of(class_2561.method_43471("controlify.guide.container.quick_move")) : Optional.empty();
         }), true, false)).element(new GuideActionRenderer(new GuideAction(ControlifyBindings.INV_TAKE_HALF.on(controller), (ctx) -> {
            if (ctx.hoveredSlot() != null && ctx.hoveredSlot().method_7677().method_31573(class_3489.field_54294) && ctx.holdingItem().method_7960() && class_5537.method_61643(ctx.hoveredSlot().method_7677()) != -1) {
               return Optional.of(class_2561.method_43471("controlify.guide.container.take_from_bundle"));
            } else if (ctx.hoveredSlot() != null && ctx.hoveredSlot().method_7677().method_7947() > 1 && ctx.holdingItem().method_7960()) {
               return Optional.of(class_2561.method_43471("controlify.guide.container.take_half"));
            } else {
               return ctx.hoveredSlot() != null && !ctx.holdingItem().method_7960() && ctx.hoveredSlot().method_7680(ctx.holdingItem()) ? Optional.of(class_2561.method_43471("controlify.guide.container.take_one")) : Optional.empty();
            }
         }), true, false)).build()).build(), AnchorPoint.BOTTOM_RIGHT, 0, 0, AnchorPoint.BOTTOM_RIGHT);
         if (ControlifyApi.get().currentInputMode().isController()) {
            this.setRenderGuide(true);
         }

      }
   }

   public void onInputModeChanged(InputMode mode) {
      this.setRenderGuide(mode.isController());
   }

   private void setRenderGuide(boolean render) {
      render &= (Boolean)ControlifyApi.get().getCurrentController().map((c) -> {
         return ((GenericControllerConfig)c.genericConfig().config()).showScreenGuides;
      }).orElse(false);
      List<class_4068> renderables = ((ScreenAccessor)this.screen).getRenderables();
      if (this.leftLayout != null && this.rightLayout != null) {
         if (render) {
            if (!renderables.contains(this.leftLayout)) {
               renderables.add(this.leftLayout);
            }

            if (!renderables.contains(this.rightLayout)) {
               renderables.add(this.rightLayout);
            }
         } else {
            renderables.remove(this.leftLayout);
            renderables.remove(this.rightLayout);
         }

      }
   }

   public void onHoveredSlotChanged(class_1735 newSlot, class_1735 oldSlot) {
      if (ControlifyApi.get().currentInputMode().isController()) {
         this.hapticNavigate();
      }

   }

   private void hapticNavigate() {
      ControlifyApi.get().getCurrentController().flatMap(ControllerEntity::hdHaptics).ifPresent((hh) -> {
         hh.playHaptic(HapticEffects.NAVIGATE);
      });
   }

   public VirtualMouseBehaviour virtualMouseBehaviour() {
      return VirtualMouseBehaviour.CURSOR_ONLY;
   }

   @FunctionalInterface
   public interface ClickSlotFunction {
      void clickSlot(class_1735 var1, int var2, int var3, class_1713 var4);
   }
}
