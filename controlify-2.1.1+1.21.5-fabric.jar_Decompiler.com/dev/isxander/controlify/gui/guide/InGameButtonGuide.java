package dev.isxander.controlify.gui.guide;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.api.event.ControlifyEvents;
import dev.isxander.controlify.api.guide.ActionPriority;
import dev.isxander.controlify.api.guide.GuideActionNameSupplier;
import dev.isxander.controlify.api.ingameguide.ActionLocation;
import dev.isxander.controlify.api.ingameguide.IngameGuideContext;
import dev.isxander.controlify.api.ingameguide.IngameGuideRegistry;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.GenericControllerConfig;
import dev.isxander.controlify.gui.layout.AnchorPoint;
import dev.isxander.controlify.gui.layout.ColumnLayoutComponent;
import dev.isxander.controlify.gui.layout.PositionedComponent;
import dev.isxander.controlify.mixins.feature.guide.ingame.PlayerAccessor;
import dev.isxander.controlify.utils.render.Blit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.class_10366;
import net.minecraft.class_1041;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1496;
import net.minecraft.class_1675;
import net.minecraft.class_1799;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_315;
import net.minecraft.class_332;
import net.minecraft.class_3966;
import net.minecraft.class_746;
import net.minecraft.class_239.class_240;
import org.joml.Matrix4f;

public class InGameButtonGuide implements IngameGuideRegistry {
   private final ControllerEntity controller;
   private final class_746 player;
   private final class_310 minecraft = class_310.method_1551();
   private final List<GuideAction<IngameGuideContext>> leftGuides = new ArrayList();
   private final List<GuideAction<IngameGuideContext>> rightGuides = new ArrayList();
   private PositionedComponent<ColumnLayoutComponent<GuideActionRenderer<IngameGuideContext>>> leftLayout;
   private PositionedComponent<ColumnLayoutComponent<GuideActionRenderer<IngameGuideContext>>> rightLayout;

   public InGameButtonGuide(ControllerEntity controller, class_746 localPlayer) {
      this.controller = controller;
      this.player = localPlayer;
      this.registerDefaultActions();
      ControlifyEvents.INGAME_GUIDE_REGISTRY.invoke(new ControlifyEvents.IngameGuideRegistryEvent(controller, this));
      Collections.sort(this.leftGuides);
      Collections.sort(this.rightGuides);
      this.refreshLayout();
   }

   public void refreshLayout() {
      boolean bottom = ((GenericControllerConfig)this.controller.genericConfig().config()).ingameGuideBottom;
      this.leftLayout = new PositionedComponent(ColumnLayoutComponent.builder().spacing(1).colPadding(2, 2).elementPosition(ColumnLayoutComponent.ElementPosition.LEFT).elements((Collection)this.leftGuides.stream().map((guide) -> {
         return new GuideActionRenderer(guide, false, true);
      }).toList()).build(), !bottom ? AnchorPoint.TOP_LEFT : AnchorPoint.BOTTOM_LEFT, 0, 0, !bottom ? AnchorPoint.TOP_LEFT : AnchorPoint.BOTTOM_LEFT);
      this.rightLayout = new PositionedComponent(ColumnLayoutComponent.builder().spacing(1).colPadding(2, 2).elementPosition(ColumnLayoutComponent.ElementPosition.RIGHT).elements((Collection)this.rightGuides.stream().map((guide) -> {
         return new GuideActionRenderer(guide, true, true);
      }).toList()).build(), !bottom ? AnchorPoint.TOP_RIGHT : AnchorPoint.BOTTOM_RIGHT, 0, 0, !bottom ? AnchorPoint.TOP_RIGHT : AnchorPoint.BOTTOM_RIGHT);
   }

   public void renderHud(class_332 graphics, float tickDelta) {
      boolean debugOpen = this.minecraft.method_53526().method_53536();
      boolean hideGui = this.minecraft.field_1690.field_1842;
      if (((GenericControllerConfig)this.controller.genericConfig().config()).showIngameGuide && this.minecraft.field_1755 == null && !debugOpen && !hideGui) {
         float scale = Controlify.instance().config().globalSettings().ingameButtonGuideScale;
         this.renderWithCustomGuiScale(graphics, scale, () -> {
            Blit.batchDraw(graphics, () -> {
               this.leftLayout.renderComponent(graphics, tickDelta);
               this.rightLayout.renderComponent(graphics, tickDelta);
            });
         });
      }
   }

   public void tick() {
      IngameGuideContext context = new IngameGuideContext(class_310.method_1551(), this.player, this.minecraft.field_1687, this.calculateHitResult(), this.controller);
      ((ColumnLayoutComponent)this.leftLayout.getComponent()).getChildComponents().forEach((renderer) -> {
         renderer.updateName(context);
      });
      ((ColumnLayoutComponent)this.rightLayout.getComponent()).getChildComponents().forEach((renderer) -> {
         renderer.updateName(context);
      });
      double guiScale = this.minecraft.method_22683().method_4495() * (double)Controlify.instance().config().globalSettings().ingameButtonGuideScale;
      int width = (int)((double)this.minecraft.method_22683().method_4489() / guiScale);
      int height = (int)((double)this.minecraft.method_22683().method_4506() / guiScale);
      this.leftLayout.updatePosition(width, height);
      this.rightLayout.updatePosition(width, height);
   }

   public void registerGuideAction(InputBinding binding, ActionLocation location, GuideActionNameSupplier<IngameGuideContext> supplier) {
      this.registerGuideAction(binding, location, ActionPriority.NORMAL, supplier);
   }

   public void registerGuideAction(InputBinding binding, ActionLocation location, ActionPriority priority, GuideActionNameSupplier<IngameGuideContext> supplier) {
      if (location == ActionLocation.LEFT) {
         this.leftGuides.add(new GuideAction(binding, supplier, priority));
      } else {
         this.rightGuides.add(new GuideAction(binding, supplier, priority));
      }

   }

   private void registerDefaultActions() {
      class_315 options = this.minecraft.field_1690;
      this.registerGuideAction(ControlifyBindings.JUMP.on(this.controller), ActionLocation.LEFT, (ctx) -> {
         class_746 player = ctx.player();
         if (player.method_5854() != null) {
            class_1297 var10000 = player.method_5854();
            Objects.requireNonNull(var10000);
            class_1297 selector0$temp = var10000;
            int index$1 = 0;
            switch(selector0$temp.typeSwitch<invokedynamic>(selector0$temp, index$1)) {
            case 0:
               class_1496 horse = (class_1496)selector0$temp;
               return horse.method_66672() ? Optional.of(class_2561.method_43471("key.jump")) : Optional.empty();
            }
         }

         if (player.method_31549().field_7479) {
            return Optional.of(class_2561.method_43471("controlify.guide.ingame.fly_up"));
         } else if (player.method_24828()) {
            return Optional.of(class_2561.method_43471("key.jump"));
         } else if (player.method_5799()) {
            return Optional.of(class_2561.method_43471("controlify.guide.ingame.swim_up"));
         } else {
            boolean canGlide = ((PlayerAccessor)player).callCanGlide() && !player.method_6101();
            return !player.method_24828() && !player.method_6128() && !player.method_5799() && canGlide ? Optional.of(class_2561.method_43471("controlify.guide.ingame.start_elytra")) : Optional.empty();
         }
      });
      this.registerGuideAction(ControlifyBindings.SNEAK.on(this.controller), ActionLocation.LEFT, (ctx) -> {
         class_746 player = ctx.player();
         boolean shifting = player.field_3913.field_54155.comp_3164();
         if (player.method_5854() != null) {
            return Optional.of(class_2561.method_43471("controlify.guide.ingame.dismount"));
         } else if (player.method_31549().field_7479) {
            return Optional.of(class_2561.method_43471("controlify.guide.ingame.fly_down"));
         } else if (player.method_5799() && !player.method_24828()) {
            return Optional.of(class_2561.method_43471("controlify.guide.ingame.swim_down"));
         } else if (((GenericControllerConfig)ctx.controller().genericConfig().config()).toggleSneak) {
            return Optional.of(class_2561.method_43471(shifting ? "controlify.guide.ingame.stop_sneaking" : "controlify.guide.ingame.start_sneaking"));
         } else {
            return !shifting ? Optional.of(class_2561.method_43471("controlify.guide.ingame.sneak")) : Optional.empty();
         }
      });
      this.registerGuideAction(ControlifyBindings.SPRINT.on(this.controller), ActionLocation.LEFT, (ctx) -> {
         class_746 player = ctx.player();
         boolean sprinting = player.field_3913.field_54155.comp_3165();
         if (!sprinting) {
            if (!player.field_3913.method_3128().method_1016(class_241.field_1340)) {
               if (player.method_5869()) {
                  return Optional.of(class_2561.method_43471("controlify.guide.ingame.start_swimming"));
               }

               return Optional.of(class_2561.method_43471("controlify.guide.ingame.start_sprinting"));
            }
         } else if (((GenericControllerConfig)ctx.controller().genericConfig().config()).toggleSprint) {
            if (player.method_5869()) {
               return Optional.of(class_2561.method_43471("controlify.guide.ingame.stop_swimming"));
            }

            return Optional.of(class_2561.method_43471("controlify.guide.ingame.stop_sprinting"));
         }

         return Optional.empty();
      });
      this.registerGuideAction(ControlifyBindings.INVENTORY.on(this.controller), ActionLocation.RIGHT, (ctx) -> {
         return ctx.client().field_1755 == null ? Optional.of(class_2561.method_43471("controlify.guide.ingame.inventory")) : Optional.empty();
      });
      this.registerGuideAction(ControlifyBindings.RADIAL_MENU.on(this.controller), ActionLocation.RIGHT, (ctx) -> {
         return ctx.client().field_1755 == null ? Optional.of(class_2561.method_43471("controlify.gui.radial_menu")) : Optional.empty();
      });
      this.registerGuideAction(ControlifyBindings.ATTACK.on(this.controller), ActionLocation.RIGHT, (ctx) -> {
         class_239 hitResult = ctx.hitResult();
         if (hitResult.method_17783() == class_240.field_1331) {
            return this.player.method_7325() ? Optional.of(class_2561.method_43471("controlify.guide.ingame.spectate")) : Optional.of(class_2561.method_43471("controlify.guide.ingame.attack"));
         } else {
            return hitResult.method_17783() == class_240.field_1332 ? Optional.of(class_2561.method_43471("controlify.guide.ingame.break")) : Optional.empty();
         }
      });
      this.registerGuideAction(ControlifyBindings.USE.on(this.controller), ActionLocation.RIGHT, (ctx) -> {
         class_239 hitResult = ctx.hitResult();
         class_746 player = ctx.player();
         if (hitResult.method_17783() == class_240.field_1331) {
            return player.method_7325() ? Optional.of(class_2561.method_43471("controlify.guide.ingame.spectate")) : Optional.of(class_2561.method_43471("controlify.guide.ingame.interact"));
         } else {
            return hitResult.method_17783() != class_240.field_1332 && !player.method_6084(class_1304.field_6173) && !player.method_6084(class_1304.field_6171) ? Optional.empty() : Optional.of(class_2561.method_43471("controlify.guide.ingame.use"));
         }
      });
      this.registerGuideAction(ControlifyBindings.DROP_INGAME.on(this.controller), ActionLocation.RIGHT, (ctx) -> {
         class_1799 holdingItem = ctx.player().method_31548().method_7391();
         return !holdingItem.method_7960() ? Optional.of(class_2561.method_43471("controlify.guide.ingame.drop")) : Optional.empty();
      });
      this.registerGuideAction(ControlifyBindings.DROP_STACK.on(this.controller), ActionLocation.RIGHT, (ctx) -> {
         class_1799 holdingItem = ctx.player().method_31548().method_7391();
         return !holdingItem.method_7960() && holdingItem.method_7947() > 1 ? Optional.of(class_2561.method_43471("controlify.binding.controlify.drop_stack")) : Optional.empty();
      });
      this.registerGuideAction(ControlifyBindings.SWAP_HANDS.on(this.controller), ActionLocation.RIGHT, (ctx) -> {
         class_746 player = ctx.player();
         return !player.method_6084(class_1304.field_6173) && !player.method_6084(class_1304.field_6171) ? Optional.empty() : Optional.of(class_2561.method_43471("controlify.guide.ingame.swap_hands"));
      });
      this.registerGuideAction(ControlifyBindings.PICK_BLOCK.on(this.controller), ActionLocation.RIGHT, (ctx) -> {
         return ctx.hitResult().method_17783() == class_240.field_1332 && ctx.player().method_68878() ? Optional.of(class_2561.method_43471("controlify.guide.ingame.pick_block")) : Optional.empty();
      });
      this.registerGuideAction(ControlifyBindings.PICK_BLOCK_NBT.on(this.controller), ActionLocation.RIGHT, (ctx) -> {
         return ctx.hitResult().method_17783() == class_240.field_1332 && ctx.player().method_68878() ? Optional.of(class_2561.method_43471("controlify.binding.controlify.pick_block_nbt")) : Optional.empty();
      });
   }

   private class_239 calculateHitResult() {
      double pickRange = this.minecraft.field_1724.method_55754();
      class_239 pickResult = this.player.method_5745(pickRange, 1.0F, false);
      class_243 eyePos = this.player.method_5836(1.0F);
      pickRange = this.minecraft.field_1724.method_55755();
      double maxPickRange = pickResult.method_17784().method_1025(eyePos);
      class_243 viewVec = this.player.method_5828(1.0F);
      class_243 reachVec = eyePos.method_1031(viewVec.field_1352 * pickRange, viewVec.field_1351 * pickRange, viewVec.field_1350 * pickRange);
      class_238 box = this.player.method_5829().method_18804(viewVec.method_1021(pickRange)).method_1009(1.0D, 1.0D, 1.0D);
      class_3966 entityHitResult = class_1675.method_18075(this.player, eyePos, reachVec, box, (entity) -> {
         return !entity.method_7325() && entity.method_5863();
      }, maxPickRange);
      return (class_239)(entityHitResult != null && entityHitResult.method_17784().method_1025(eyePos) < pickResult.method_17784().method_1025(eyePos) ? entityHitResult : pickResult);
   }

   private void renderWithCustomGuiScale(class_332 graphics, float guiScale, Runnable renderer) {
      if (guiScale == 1.0F) {
         renderer.run();
      } else {
         class_1041 window = this.minecraft.method_22683();
         float vanillaGuiScale = (float)window.method_4495();
         float scale = vanillaGuiScale * guiScale;
         Matrix4f projMatrix = (new Matrix4f()).setOrtho(0.0F, (float)window.method_4489() / scale, (float)window.method_4506() / scale, 0.0F, 1000.0F, 21000.0F);
         Matrix4f oldProjection = RenderSystem.getProjectionMatrix();
         graphics.method_51452();
         RenderSystem.setProjectionMatrix(projMatrix, class_10366.field_54954);
         renderer.run();
         graphics.method_51452();
         RenderSystem.setProjectionMatrix(oldProjection, class_10366.field_54954);
      }
   }
}
