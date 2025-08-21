package dev.isxander.splitscreen.client.remote.gui;

import dev.isxander.controlify.api.buttonguide.ButtonGuideApi;
import dev.isxander.controlify.api.buttonguide.ButtonGuidePredicate;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.screenop.ScreenControllerEventListener;
import dev.isxander.splitscreen.client.LocalSplitscreenPawn;
import dev.isxander.splitscreen.client.features.screenop.ScreenSplitscreenBehaviour;
import dev.isxander.splitscreen.client.features.screenop.ScreenSplitscreenMode;
import java.util.function.Supplier;
import net.minecraft.class_2561;
import net.minecraft.class_339;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_447;
import net.minecraft.class_457;
import net.minecraft.class_7843;
import net.minecraft.class_7845;
import net.minecraft.class_7845.class_7939;

public class PawnPauseScreen extends class_437 implements ScreenSplitscreenBehaviour, ScreenControllerEventListener {
   private static final int BTN_WIDTH = 204;
   private static final int BTN_PADDING = 4;
   private static final int BTN_HALF_WIDTH = 98;
   private final LocalSplitscreenPawn pawn;
   private class_4185 returnToGame;
   private class_4185 advancements;
   private class_4185 stats;
   private class_4185 disconnectController;

   public PawnPauseScreen(LocalSplitscreenPawn pawn) {
      super(class_2561.method_43471("menu.game"));
      this.pawn = pawn;
   }

   protected void method_25426() {
      class_7845 grid = new class_7845();
      grid.method_46458().method_46466(4, 4, 4, 0);
      class_7939 rows = grid.method_47610(2);
      rows.method_47614(this.returnToGame = class_4185.method_46430(class_2561.method_43471("menu.returnToGame"), (button) -> {
         this.field_22787.method_1507((class_437)null);
         this.field_22787.field_1729.method_1612();
      }).method_46432(204).method_46431(), 2, grid.method_46457().method_46471(50));
      ButtonGuideApi.addGuideToButton(this.returnToGame, ControlifyBindings.GUI_BACK, ButtonGuidePredicate.always());
      rows.method_47612(this.advancements = this.openScreenButton(class_2561.method_43471("gui.advancements"), () -> {
         return new class_457(this.field_22787.field_1724.field_3944.method_2869(), this);
      }));
      rows.method_47612(this.stats = this.openScreenButton(class_2561.method_43471("gui.stats"), () -> {
         return new class_447(this, this.field_22787.field_1724.method_3143());
      }));
      rows.method_47613(this.disconnectController = class_4185.method_46430(class_2561.method_43470("Disconnect Controller"), (button) -> {
         button.field_22763 = false;
         this.disconnectController();
      }).method_46432(204).method_46431(), 2);
      ButtonGuideApi.addGuideToButton(this.disconnectController, () -> {
         return this.disconnectController.method_25370() ? ControlifyBindings.GUI_PRESS : ControlifyBindings.GUI_ABSTRACT_ACTION_2;
      }, ButtonGuidePredicate.always());
      grid.method_48222();
      class_7843.method_46443(grid, 0, 0, this.field_22789, this.field_22790, 0.5F, 0.25F);
      grid.method_48206((x$0) -> {
         class_339 var10000 = (class_339)this.method_37063(x$0);
      });
   }

   public void onControllerInput(ControllerEntity controller) {
      if (ControlifyBindings.GUI_ABSTRACT_ACTION_2.on(controller).guiPressed().get()) {
         this.method_25395(this.disconnectController);
      }

   }

   private void disconnectController() {
      this.pawn.closeGame();
   }

   private class_4185 openScreenButton(class_2561 message, Supplier<class_437> screenSupplier) {
      return class_4185.method_46430(message, (button) -> {
         this.field_22787.method_1507((class_437)screenSupplier.get());
      }).method_46432(98).method_46431();
   }

   public ScreenSplitscreenMode getSplitscreenMode() {
      return ScreenSplitscreenMode.SPLITSCREEN;
   }
}
