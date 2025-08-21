package dev.isxander.controlify.compatibility.rso.mixins;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import dev.isxander.controlify.compatibility.sodium.screenop.SodiumGuiScreenProcessor;
import dev.isxander.controlify.compatibility.sodium.screenop.SodiumScreenOperations;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import java.util.List;
import java.util.Optional;
import me.flashyreese.mods.reeses_sodium_options.client.gui.SodiumVideoOptionsScreen;
import me.flashyreese.mods.reeses_sodium_options.client.gui.frame.AbstractFrame;
import me.flashyreese.mods.reeses_sodium_options.client.gui.frame.tab.Tab;
import me.flashyreese.mods.reeses_sodium_options.client.gui.frame.tab.TabFrame;
import net.caffeinemc.mods.sodium.client.gui.options.control.ControlElement;
import net.caffeinemc.mods.sodium.client.gui.widgets.FlatButtonWidget;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_364;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({SodiumVideoOptionsScreen.class})
public abstract class SodiumVideoOptionsScreenMixin extends class_437 implements ScreenProcessorProvider, SodiumScreenOperations {
   @Shadow
   private FlatButtonWidget applyButton;
   @Shadow
   private FlatButtonWidget closeButton;
   @Shadow
   private FlatButtonWidget undoButton;
   @Unique
   private final SodiumGuiScreenProcessor controlify$screenProcessor = new SodiumGuiScreenProcessor((SodiumVideoOptionsScreen)this, this);
   @Unique
   private TabFrame tabFrame;

   protected SodiumVideoOptionsScreenMixin(class_2561 title) {
      super(title);
   }

   @Inject(
      method = {"method_25426()V"},
      at = {@At("RETURN")}
   )
   private void notifyProcessorRebuild(CallbackInfo ci) {
      this.controlify$screenProcessor.onRebuildGUI();
      this.focusOnFirstControl();
   }

   @ModifyExpressionValue(
      method = {"lambda$parentBasicFrameBuilder$9(Lnet/caffeinemc/mods/sodium/client/util/Dim2i;Lnet/caffeinemc/mods/sodium/client/util/Dim2i;)Lnet/caffeinemc/mods/sodium/client/gui/widgets/AbstractWidget;"},
      at = {@At(
   value = "INVOKE",
   target = "Lme/flashyreese/mods/reeses_sodium_options/client/gui/frame/tab/TabFrame$Builder;build()Lme/flashyreese/mods/reeses_sodium_options/client/gui/frame/tab/TabFrame;"
)}
   )
   private TabFrame storeBuiltTabFrame(TabFrame original) {
      return this.tabFrame = original;
   }

   @ModifyArg(
      method = {"lambda$parentBasicFrameBuilder$9(Lnet/caffeinemc/mods/sodium/client/util/Dim2i;Lnet/caffeinemc/mods/sodium/client/util/Dim2i;)Lnet/caffeinemc/mods/sodium/client/gui/widgets/AbstractWidget;"},
      at = @At(
   value = "INVOKE",
   target = "Lme/flashyreese/mods/reeses_sodium_options/client/gui/frame/tab/TabFrame$Builder;onSetTab(Ljava/lang/Runnable;)Lme/flashyreese/mods/reeses_sodium_options/client/gui/frame/tab/TabFrame$Builder;"
)
   )
   private Runnable setInitialFocusOnTabChange(Runnable onSetTab) {
      return () -> {
         onSetTab.run();
         class_310.method_1551().method_63588(this::focusOnFirstControl);
      };
   }

   @Unique
   private void focusOnFirstControl() {
      AbstractFrame tabContentsFrame = ((TabFrameAccessor)this.tabFrame).getSelectedFrame();
      List<ControlElement<?>> controlElements = ((AbstractFrameAccessor)tabContentsFrame).getControlElements();
      if (!controlElements.isEmpty()) {
         System.out.println(((ControlElement)controlElements.get(0)).getOption().getName().getString());
         this.method_48265((class_364)controlElements.get(0));
      }

   }

   public ScreenProcessor<?> screenProcessor() {
      return this.controlify$screenProcessor;
   }

   public void controlify$nextPage() {
      TabFrameAccessor accessor = (TabFrameAccessor)this.tabFrame;
      List<Tab<?>> tabs = accessor.getTabs();
      Optional<Tab<?>> selectedTab = this.getSelectedTab(accessor);
      if (!selectedTab.isEmpty()) {
         int currentIndex = tabs.indexOf(selectedTab.get());
         int nextIndex = (currentIndex + 1) % tabs.size();
         this.tabFrame.setTab(Optional.of((Tab)tabs.get(nextIndex)));
      }
   }

   public void controlify$prevPage() {
      TabFrameAccessor accessor = (TabFrameAccessor)this.tabFrame;
      List<Tab<?>> tabs = accessor.getTabs();
      Optional<Tab<?>> selectedTab = this.getSelectedTab(accessor);
      if (!selectedTab.isEmpty()) {
         int currentIndex = tabs.indexOf(selectedTab.get());
         int nextIndex = (currentIndex - 1 + tabs.size()) % tabs.size();
         this.tabFrame.setTab(Optional.of((Tab)tabs.get(nextIndex)));
      }
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

   @Unique
   private Optional<Tab<?>> getSelectedTab(TabFrameAccessor accessor) {
      return accessor.getSelectedTab();
   }
}
