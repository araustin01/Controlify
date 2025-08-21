package dev.isxander.controlify.gui.screen;

import com.google.common.collect.ImmutableList;
import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.api.buttonguide.ButtonGuideApi;
import dev.isxander.controlify.api.buttonguide.ButtonGuidePredicate;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.ControllerUID;
import dev.isxander.controlify.controller.GenericControllerConfig;
import dev.isxander.controlify.controller.steamdeck.SteamDeckComponent;
import dev.isxander.controlify.controllermanager.ControllerManager;
import dev.isxander.controlify.gui.components.FakePositionPlainTextButton;
import dev.isxander.controlify.mixins.feature.ui.AbstractSelectionListAccessor;
import dev.isxander.controlify.screenop.ScreenControllerEventListener;
import dev.isxander.controlify.utils.ClientUtils;
import dev.isxander.controlify.utils.ColorUtils;
import dev.isxander.controlify.utils.animation.api.Animatable;
import dev.isxander.controlify.utils.animation.api.Animation;
import dev.isxander.controlify.utils.animation.api.EasingFunction;
import dev.isxander.controlify.utils.render.Blit;
import dev.isxander.controlify.utils.render.CGuiPose;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.class_124;
import net.minecraft.class_156;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_362;
import net.minecraft.class_364;
import net.minecraft.class_4068;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_5244;
import net.minecraft.class_6379;
import net.minecraft.class_6381;
import net.minecraft.class_6382;
import net.minecraft.class_7843;
import net.minecraft.class_7845;
import net.minecraft.class_7919;
import net.minecraft.class_8016;
import net.minecraft.class_8023;
import net.minecraft.class_8030;
import net.minecraft.class_6379.class_6380;
import net.minecraft.class_7845.class_7939;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ControllerCarouselScreen extends class_437 implements ScreenControllerEventListener {
   public static final class_2960 CHECKMARK = class_2960.method_60656("icon/checkmark");
   public static final class_2960 DANGER = class_2960.method_60656("icon/unseen_notification");
   private final class_437 parent;
   private int footerY;
   private List<ControllerCarouselScreen.CarouselEntry> carouselEntries = null;
   private int carouselIndex;
   private Animatable carouselAnimation = null;
   private final Controlify controlify;
   private final ControllerManager controllerManager;
   private class_4185 globalSettingsButton;
   private class_4185 unbindControllerButton;
   private class_4185 doneButton;
   private class_4185 controllerNotDetectedButton;

   private ControllerCarouselScreen(class_437 parent) {
      super(class_2561.method_43471("controlify.gui.carousel.title"));
      this.parent = parent;
      this.controlify = Controlify.instance();
      this.controllerManager = (ControllerManager)this.controlify.getControllerManager().orElseThrow();
      this.carouselIndex = (Integer)this.controlify.getCurrentController().map((c) -> {
         return this.controllerManager.getConnectedControllers().indexOf(c);
      }).orElse(0);
   }

   public static void openConfigScreen(class_437 parent) {
      Controlify controlify = Controlify.instance();
      controlify.finishControlifyInit().whenComplete((v, th) -> {
         class_310.method_1551().method_1507(new ControllerCarouselScreen(parent));
      });
   }

   protected void method_25426() {
      this.refreshControllers();
      class_2561 donateText = class_2561.method_43471("controlify.gui.carousel.donate").method_27695(new class_124[]{class_124.field_1065, class_124.field_1067});
      FakePositionPlainTextButton donateBtn = (FakePositionPlainTextButton)this.method_37063(new FakePositionPlainTextButton(donateText, this.field_22793, 3, 3, (btn) -> {
         class_156.method_668().method_670("https://patreon.com/isxander");
      }));
      donateBtn.setFakePosition(new class_8030(0, this.field_22790, this.field_22789, 1));
      class_2561 artCreditText = class_2561.method_43469("controlify.gui.carousel.art_credit", new Object[]{class_2561.method_43470("Andrew Grant")}).method_27692(class_124.field_1063);
      FakePositionPlainTextButton artCreditBtn = (FakePositionPlainTextButton)this.method_37063(new FakePositionPlainTextButton(artCreditText, this.field_22793, this.field_22789 - this.field_22793.method_27525(artCreditText) - 3, 3, (btn) -> {
         class_156.method_668().method_670("https://github.com/Andrew6rant");
      }));
      artCreditBtn.setFakePosition(new class_8030(0, this.field_22790 + 1, this.field_22789, 1));
      class_7845 grid = (new class_7845()).method_48635(10);
      class_7939 rowHelper = grid.method_47610(2);
      this.globalSettingsButton = (class_4185)rowHelper.method_47612(class_4185.method_46430(class_2561.method_43471("controlify.gui.global_settings.title"), (btn) -> {
         this.field_22787.method_1507(GlobalSettingsScreenFactory.createGlobalSettingsScreen(this));
      }).method_46431());
      this.doneButton = (class_4185)rowHelper.method_47612(class_4185.method_46430(class_5244.field_24334, (btn) -> {
         this.method_25419();
      }).method_46431());
      grid.method_48206((widget) -> {
         widget.method_48591(1);
         this.method_37063(widget);
      });
      grid.method_48222();
      class_7843.method_46442(grid, 0, this.field_22790 - 36, this.field_22789, 36);
      this.controllerNotDetectedButton = (class_4185)this.method_37063(class_4185.method_46430(class_2561.method_43471("controlify.gui.carousel.controller_not_detected_btn"), (btn) -> {
         class_156.method_668().method_670("https://docs.isxander.dev/controlify/users/controller-issues#my-controller-is-not-detected");
      }).method_46433(this.field_22789 / 2 - 75, (this.field_22790 - 36) / 2 + 10).method_46436(class_7919.method_47407(class_2561.method_43471("controlify.gui.carousel.controller_not_detected_btn.tooltip"))).method_46431());
      this.controllerNotDetectedButton.field_22764 = this.carouselEntries.isEmpty();
      ButtonGuideApi.addGuideToButton(this.globalSettingsButton, (InputBindingSupplier)ControlifyBindings.GUI_ABSTRACT_ACTION_1, ButtonGuidePredicate.always());
      ButtonGuideApi.addGuideToButton(this.doneButton, (InputBindingSupplier)ControlifyBindings.GUI_BACK, ButtonGuidePredicate.always());
      this.footerY = class_3532.method_28139(this.field_22790 - 36 - 2, 2);
   }

   public void refreshControllers() {
      ControllerEntity prevSelectedController;
      if (this.carouselEntries != null && !this.carouselEntries.isEmpty()) {
         this.carouselEntries.forEach((x$0) -> {
            this.method_37066(x$0);
         });
         prevSelectedController = ((ControllerCarouselScreen.CarouselEntry)this.carouselEntries.get(this.carouselIndex)).controller;
      } else {
         prevSelectedController = null;
      }

      this.carouselEntries = this.controllerManager.getConnectedControllers().stream().map((c) -> {
         return new ControllerCarouselScreen.CarouselEntry(c, this.field_22789 / 3, this.field_22790 - 66);
      }).peek((x$0) -> {
         ControllerCarouselScreen.CarouselEntry var10000 = (ControllerCarouselScreen.CarouselEntry)this.method_37063(x$0);
      }).toList();
      Optional var10001 = this.carouselEntries.stream().filter((e) -> {
         return e.controller == prevSelectedController;
      }).findFirst();
      List var10002 = this.carouselEntries;
      Objects.requireNonNull(var10002);
      this.carouselIndex = (Integer)var10001.map(var10002::indexOf).orElse((Integer)Controlify.instance().getCurrentController().map((c) -> {
         return this.controllerManager.getConnectedControllers().indexOf(c);
      }).orElse(0));
      if (!this.carouselEntries.isEmpty()) {
         ((ControllerCarouselScreen.CarouselEntry)this.carouselEntries.get(this.carouselIndex)).overlayColor = 0;
      }

      float offsetX = (float)this.field_22789 / 2.0F * (float)(-(this.carouselIndex - 1)) - (float)this.field_22789 / 6.0F;

      for(int i = 0; i < this.carouselEntries.size(); ++i) {
         ControllerCarouselScreen.CarouselEntry entry = (ControllerCarouselScreen.CarouselEntry)this.carouselEntries.get(i);
         entry.setX(offsetX + (float)this.field_22789 / 2.0F * (float)i);
         entry.setY(i == this.carouselIndex ? 20.0F : 10.0F);
      }

      if (this.controllerNotDetectedButton != null) {
         this.controllerNotDetectedButton.field_22764 = this.carouselEntries.isEmpty();
      }

   }

   public void method_25394(@NotNull class_332 graphics, int mouseX, int mouseY, float delta) {
      super.method_25394(graphics, mouseX, mouseY, delta);
      Blit.tex(graphics, this.field_22787.field_1687 == null ? class_437.field_49896 : class_437.field_49898, 0, this.footerY, 0.0F, 0.0F, this.field_22789, 2, 32, 2);
      if (this.carouselEntries.isEmpty()) {
         graphics.method_27534(this.field_22793, class_2561.method_43471("controlify.gui.carousel.no_controllers"), this.field_22789 / 2, (this.field_22790 - 36) / 2 - 10, -5592406);
      }

   }

   public void method_25420(class_332 graphics, int i, int j, float f) {
      super.method_25420(graphics, i, j, f);
      Blit.tex(graphics, this.field_22787.field_1687 == null ? AbstractSelectionListAccessor.getMenuListBackground() : AbstractSelectionListAccessor.getInWorldMenuListBackground(), 0, 0, 0.0F, 0.0F, this.field_22789, this.footerY, 32, 32);
   }

   public void focusOnEntry(int index) {
      if (this.carouselAnimation == null || this.carouselAnimation.isDone()) {
         int diff = index - this.carouselIndex;
         if (diff != 0) {
            this.carouselIndex = index;
            Animation animation = Animation.of(10).easing(EasingFunction.EASE_IN_OUT_CUBIC);
            Iterator var4 = this.carouselEntries.iterator();

            while(var4.hasNext()) {
               ControllerCarouselScreen.CarouselEntry entry = (ControllerCarouselScreen.CarouselEntry)var4.next();
               boolean selected = this.carouselEntries.indexOf(entry) == index;
               Objects.requireNonNull(entry);
               animation.consumerF(entry::setX, (double)entry.getX(), (double)((float)entry.getX() + (float)(-diff) * ((float)this.field_22789 / 2.0F)));
               Objects.requireNonNull(entry);
               animation.consumerF(entry::setY, (double)entry.getY(), selected ? 20.0D : 10.0D);
               animation.consumerF((t) -> {
                  entry.overlayColor = ColorUtils.lerpARGB(t, entry.overlayColor, selected ? 0 : -1879048192);
               }, 0.0D, 1.0D);
            }

            this.carouselAnimation = animation.play();
         }
      }
   }

   public void onControllerInput(ControllerEntity controller) {
      if (ControlifyBindings.GUI_ABSTRACT_ACTION_1.on(controller).justPressed()) {
         this.globalSettingsButton.method_25306();
      }

   }

   public void method_25419() {
      this.field_22787.method_1507(this.parent);
   }

   private class CarouselEntry extends class_362 implements class_4068, class_6379 {
      private int x;
      private int y;
      private final int width;
      private final int height;
      private float translationX;
      private float translationY;
      private final ControllerEntity controller;
      private final boolean hasNickname;
      private final class_4185 useControllerButton;
      private final class_4185 settingsButton;
      private final ImmutableList<? extends class_364> children;
      private boolean prevUse;
      private float currentlyUsedPos;
      private Animation currentlyUsedAnimation;
      private int overlayColor = -1879048192;
      private boolean hovered = false;
      private final boolean badSteamDeck;

      private CarouselEntry(ControllerEntity controller, int width, int height) {
         this.width = width;
         this.height = height;
         this.controller = controller;
         this.hasNickname = ((GenericControllerConfig)this.controller.genericConfig().config()).nickname != null;
         this.settingsButton = class_4185.method_46430(class_2561.method_43471("controlify.gui.carousel.entry.settings"), (btn) -> {
            ControllerCarouselScreen.this.field_22787.method_1507(ControllerConfigScreenFactory.generateConfigScreen(ControllerCarouselScreen.this, controller));
         }).method_46432((this.getWidth() - 2) / 2 - 2).method_46431();
         this.useControllerButton = class_4185.method_46430(class_2561.method_43473(), (btn) -> {
            this.onUseStopUsingButton();
         }).method_46432(this.settingsButton.method_25368()).method_46431();
         this.updateUseStopUsingButton();
         this.children = ImmutableList.of(this.settingsButton, this.useControllerButton);
         this.prevUse = this.isCurrentlyUsed();
         this.currentlyUsedPos = this.prevUse ? 1.0F : 0.0F;
         this.badSteamDeck = controller.info().type().isSteamDeck() && controller.getComponent(SteamDeckComponent.ID).isEmpty();
      }

      public void method_25394(class_332 graphics, int mouseX, int mouseY, float delta) {
         this.hovered = this.method_25405((double)mouseX, (double)mouseY);
         graphics.method_44379(this.x, this.y, this.x + this.width + (this.translationX > 0.0F ? 1 : 0), this.y + this.height + (this.translationY > 0.0F ? 1 : 0));
         CGuiPose pose = CGuiPose.ofPush(graphics);
         pose.translate(this.translationX, this.translationY);
         graphics.method_49601(this.x, this.y, this.width, this.height, 1526726655);
         this.useControllerButton.method_25394(graphics, mouseX, mouseY, delta);
         this.settingsButton.method_25394(graphics, mouseX, mouseY, delta);
         class_327 var10001 = ControllerCarouselScreen.this.field_22793;
         String var10002 = this.controller.name();
         int var10003 = this.x + this.width / 2;
         int var10004 = this.y + this.height - 26;
         Objects.requireNonNull(ControllerCarouselScreen.this.field_22793);
         var10004 -= 9;
         int var10005;
         if (this.hasNickname) {
            Objects.requireNonNull(ControllerCarouselScreen.this.field_22793);
            var10005 = 9 + 1;
         } else {
            var10005 = 0;
         }

         graphics.method_25300(var10001, var10002, var10003, var10004 - var10005, 16777215);
         if (this.hasNickname) {
            GenericControllerConfig config = (GenericControllerConfig)this.controller.genericConfig().config();
            String nickname = config.nickname;
            config.nickname = null;
            var10001 = ControllerCarouselScreen.this.field_22793;
            var10002 = this.controller.name();
            var10003 = this.x + this.width / 2;
            var10004 = this.y + this.height - 26;
            Objects.requireNonNull(ControllerCarouselScreen.this.field_22793);
            graphics.method_25300(var10001, var10002, var10003, var10004 - 9, 11184810);
            config.nickname = nickname;
         }

         class_2561 currentlyInUseText = class_2561.method_43471("controlify.gui.carousel.entry.in_use").method_27692(class_124.field_1060);
         pose.push();
         pose.translate((float)(17 + ControllerCarouselScreen.this.field_22793.method_27525(currentlyInUseText)) * (this.currentlyUsedPos - 1.0F), 0.0F);
         if (this.currentlyUsedPos > 0.0F) {
            ClientUtils.drawSprite(graphics, ControllerCarouselScreen.CHECKMARK, this.x + 4, this.y + 4, 9, 8);
            graphics.method_27535(ControllerCarouselScreen.this.field_22793, currentlyInUseText, this.x + 17, this.y + 4, -1);
         }

         if (this.badSteamDeck) {
            ClientUtils.drawSprite(graphics, ControllerCarouselScreen.DANGER, this.x + 4, this.y + 4 + 10, 9, 8);
            graphics.method_27535(ControllerCarouselScreen.this.field_22793, class_2561.method_43471("controlify.steam_deck_no_driver"), this.x + 17, this.y + 4 + 10, -1);
         }

         pose.pop();
         int iconWidth = this.width - 6;
         int var10000 = this.height - 22 - 4;
         Objects.requireNonNull(ControllerCarouselScreen.this.field_22793);
         var10000 = var10000 - 9 - 8;
         Objects.requireNonNull(ControllerCarouselScreen.this.field_22793);
         int iconHeight = var10000 - (9 * (this.hasNickname ? 2 : 1) + 1) - 6;
         int iconSize = class_3532.method_28139(Math.min(iconHeight, iconWidth), 2);
         pose.push();
         float var12 = (float)this.x + (float)this.width / 2.0F - (float)iconSize / 2.0F;
         int var13 = this.y;
         Objects.requireNonNull(ControllerCarouselScreen.this.field_22793);
         pose.translate(var12, (float)(var13 + 9 + 12) + (float)iconHeight / 2.0F - (float)iconSize / 2.0F);
         pose.scale((float)iconSize / 64.0F, (float)iconSize / 64.0F);
         ClientUtils.drawSprite(graphics, this.controller.info().type().getIconSprite(), 0, 0, 64, 64);
         pose.pop();
         pose.translate(0.0F, 0.0F);
         graphics.method_25294(this.x, this.y, this.x + this.width, this.y + this.height, this.overlayColor);
         pose.pop();
         graphics.method_44380();
         if (this.prevUse != this.isCurrentlyUsed()) {
            if (this.currentlyUsedAnimation != null) {
               this.currentlyUsedAnimation.skipToEnd();
            }

            this.currentlyUsedAnimation = Animation.of(20).easing(EasingFunction.EASE_OUT_QUINT).consumerF((t) -> {
               this.currentlyUsedPos = t;
            }, (double)this.currentlyUsedPos, this.isCurrentlyUsed() ? 1.0D : 0.0D).play();
         }

         this.prevUse = this.isCurrentlyUsed();
      }

      public boolean method_25402(double mouseX, double mouseY, int button) {
         if (this.method_25405(mouseX, mouseY)) {
            int index = ControllerCarouselScreen.this.carouselEntries.indexOf(this);
            if (index != ControllerCarouselScreen.this.carouselIndex) {
               if (ControllerCarouselScreen.this.carouselAnimation == null || ControllerCarouselScreen.this.carouselAnimation.isDone()) {
                  ControllerCarouselScreen.this.focusOnEntry(index);
               }

               return true;
            }
         }

         return super.method_25402(mouseX, mouseY, button);
      }

      private void onUseStopUsingButton() {
         if (!this.isCurrentlyUsed()) {
            Controlify.instance().setCurrentController(this.controller, true);
         } else {
            Controlify.instance().setCurrentController((ControllerEntity)null, true);
            Controlify.instance().config().setCurrentControllerUid((ControllerUID)null);
         }

         this.updateUseStopUsingButton();
      }

      private void updateUseStopUsingButton() {
         this.useControllerButton.method_25355(class_2561.method_43471(this.isCurrentlyUsed() ? "controlify.gui.carousel.entry.stop_using" : "controlify.gui.carousel.entry.use"));
      }

      public boolean method_25405(double mouseX, double mouseY) {
         return mouseX >= (double)this.x && mouseX <= (double)(this.x + this.width) && mouseY >= (double)this.y && mouseY <= (double)(this.y + this.height);
      }

      public void method_25365(boolean focused) {
         super.method_25365(focused);
         if (focused) {
            ControllerCarouselScreen.this.focusOnEntry(ControllerCarouselScreen.this.carouselEntries.indexOf(this));
         }

      }

      public List<? extends class_364> method_25396() {
         return this.children;
      }

      public void setX(float x) {
         this.x = (int)x;
         this.settingsButton.method_46421((int)x + 2);
         this.useControllerButton.method_46421(this.settingsButton.method_46426() + this.settingsButton.method_25368() + 2);
         this.translationX = x - (float)((int)x);
      }

      public void setY(float y) {
         this.y = (int)y;
         this.useControllerButton.method_46419((int)y + this.getHeight() - 20 - 2);
         this.settingsButton.method_46419(this.useControllerButton.method_46427());
         this.translationY = y - (float)((int)y);
      }

      public int getX() {
         return this.x;
      }

      public int getY() {
         return this.y;
      }

      public int getWidth() {
         return this.width;
      }

      public int getHeight() {
         return this.height;
      }

      @Nullable
      public class_8016 method_48205(class_8023 event) {
         return ControllerCarouselScreen.this.carouselAnimation != null && !ControllerCarouselScreen.this.carouselAnimation.isDone() ? null : super.method_48205(event);
      }

      public class_8030 method_48202() {
         return new class_8030(this.x, this.y, this.width, this.height);
      }

      public boolean isCurrentlyUsed() {
         return Controlify.instance().getCurrentController().orElse((Object)null) == this.controller;
      }

      public class_6380 method_37018() {
         return this.method_25370() ? class_6380.field_33786 : (this.hovered ? class_6380.field_33785 : class_6380.field_33784);
      }

      public void method_37020(class_6382 builder) {
         builder.method_37033(class_6381.field_33788, this.controller.name());
         builder.method_37034(class_6381.field_33791, class_2561.method_43470("Left arrow to go to previous controller, right arrow to go to next controller."));
      }
   }
}
