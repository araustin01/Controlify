package dev.isxander.controlify.compatibility.sodium.mixins;

import dev.isxander.controlify.compatibility.sodium.screenop.SodiumGuiScreenProcessor;
import dev.isxander.controlify.compatibility.sodium.screenop.SodiumScreenOperations;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import java.util.List;
import net.caffeinemc.mods.sodium.client.gui.SodiumOptionsGUI;
import net.caffeinemc.mods.sodium.client.gui.options.OptionPage;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlElement;
import net.caffeinemc.mods.sodium.client.gui.widgets.FlatButtonWidget;
import net.minecraft.class_2561;
import net.minecraft.class_364;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({SodiumOptionsGUI.class})
public abstract class SodiumOptionsGUIMixin extends class_437 implements ScreenProcessorProvider, SodiumScreenOperations {
   @Shadow
   @Final
   private List<ControlElement<?>> controls;
   @Shadow
   @Final
   private List<OptionPage> pages;
   @Shadow
   private OptionPage currentPage;
   @Shadow
   private FlatButtonWidget applyButton;
   @Shadow
   private FlatButtonWidget closeButton;
   @Shadow
   private FlatButtonWidget undoButton;
   @Unique
   private final SodiumGuiScreenProcessor controlify$screenProcessor = new SodiumGuiScreenProcessor((SodiumOptionsGUI)this, this);

   @Shadow
   public abstract void setPage(OptionPage var1);

   protected SodiumOptionsGUIMixin(class_2561 title) {
      super(title);
   }

   @Inject(
      method = {"rebuildGUIOptions()V"},
      at = {@At("RETURN")}
   )
   private void focusFirstButton(CallbackInfo ci) {
      this.method_48265((class_364)this.controls.get(0));
   }

   @Inject(
      method = {"rebuildGUI()V"},
      at = {@At("RETURN")}
   )
   private void notifyScreenProcessorOfRebuild(CallbackInfo ci) {
      this.controlify$screenProcessor.onRebuildGUI();
   }

   public ScreenProcessor<?> screenProcessor() {
      return this.controlify$screenProcessor;
   }

   public void controlify$nextPage() {
      int currentIndex = this.pages.indexOf(this.currentPage);
      int nextIndex = (currentIndex + 1) % this.pages.size();
      this.setPage((OptionPage)this.pages.get(nextIndex));
   }

   public void controlify$prevPage() {
      int currentIndex = this.pages.indexOf(this.currentPage);
      int nextIndex = (currentIndex - 1 + this.pages.size()) % this.pages.size();
      this.setPage((OptionPage)this.pages.get(nextIndex));
   }

   public FlatButtonWidget controlify$getApplyButton() {
      return this.applyButton;
   }

   public FlatButtonWidget controlify$getCloseButton() {
      return this.closeButton;
   }

   public FlatButtonWidget controlify$getUndoButton() {
      return this.undoButton;
   }
}
