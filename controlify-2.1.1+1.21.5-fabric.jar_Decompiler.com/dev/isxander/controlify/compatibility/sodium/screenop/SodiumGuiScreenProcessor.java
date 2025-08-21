package dev.isxander.controlify.compatibility.sodium.screenop;

import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.api.buttonguide.ButtonGuideApi;
import dev.isxander.controlify.api.buttonguide.ButtonGuidePredicate;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.compatibility.sodium.mixins.FlatButtonWidgetAccessor;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.screenop.ScreenProcessor;
import net.minecraft.class_437;

public class SodiumGuiScreenProcessor extends ScreenProcessor<class_437> {
   private final SodiumScreenOperations operations;

   public SodiumGuiScreenProcessor(class_437 screen, SodiumScreenOperations operations) {
      super(screen);
      this.operations = operations;
   }

   protected void handleComponentNavigation(ControllerEntity controller) {
      super.handleComponentNavigation(controller);
   }

   protected void handleButtons(ControllerEntity controller) {
      if (ControlifyBindings.GUI_ABSTRACT_ACTION_1.on(controller).justPressed()) {
         ((FlatButtonWidgetAccessor)this.operations.controlify$getApplyButton()).invokeDoAction();
      }

      if (ControlifyBindings.GUI_ABSTRACT_ACTION_2.on(controller).justPressed()) {
         ((FlatButtonWidgetAccessor)this.operations.controlify$getUndoButton()).invokeDoAction();
      }

      if (ControlifyBindings.GUI_NEXT_TAB.on(controller).justPressed()) {
         this.operations.controlify$nextPage();
      }

      if (ControlifyBindings.GUI_PREV_TAB.on(controller).justPressed()) {
         this.operations.controlify$prevPage();
      }

      super.handleButtons(controller);
   }

   protected void setInitialFocus() {
   }

   public void onRebuildGUI() {
      ButtonGuideApi.addGuideToButton(this.operations.controlify$getApplyButton(), (InputBindingSupplier)ControlifyBindings.GUI_ABSTRACT_ACTION_1, ButtonGuidePredicate.always());
      ButtonGuideApi.addGuideToButton(this.operations.controlify$getUndoButton(), (InputBindingSupplier)ControlifyBindings.GUI_ABSTRACT_ACTION_2, ButtonGuidePredicate.always());
      ButtonGuideApi.addGuideToButton(this.operations.controlify$getCloseButton(), (InputBindingSupplier)ControlifyBindings.GUI_BACK, ButtonGuidePredicate.always());
      super.onWidgetRebuild();
   }
}
