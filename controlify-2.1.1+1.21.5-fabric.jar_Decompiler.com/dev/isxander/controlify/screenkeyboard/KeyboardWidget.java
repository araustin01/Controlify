package dev.isxander.controlify.screenkeyboard;

import com.mojang.datafixers.util.Pair;
import dev.isxander.controlify.api.ControlifyApi;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.api.bind.InputBindingSupplier;
import dev.isxander.controlify.bindings.ControlifyBindings;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.screenop.ComponentProcessor;
import dev.isxander.controlify.screenop.ScreenControllerEventListener;
import dev.isxander.controlify.screenop.ScreenProcessor;
import dev.isxander.controlify.screenop.ScreenProcessorProvider;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.HoldRepeatHelper;
import dev.isxander.controlify.utils.render.Blit;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_364;
import net.minecraft.class_4069;
import net.minecraft.class_437;
import net.minecraft.class_6382;
import net.minecraft.class_8016;
import net.minecraft.class_8023;
import org.apache.commons.lang3.Validate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class KeyboardWidget<T extends KeyboardWidget.Key> extends class_339 implements class_4069 {
   protected final List<T> keys;
   protected final KeyPressConsumer keyPressConsumer;
   protected boolean shiftMode;
   @Nullable
   private class_364 focused;
   private boolean isDragging;
   private final class_437 containingScreen;

   public KeyboardWidget(class_437 screen, int x, int y, int width, int height, KeyPressConsumer keyPressConsumer) {
      super(x, y, width, height, class_2561.method_43470("On-screen keyboard"));
      this.containingScreen = screen;
      this.keyPressConsumer = keyPressConsumer;
      this.keys = new ArrayList();
      this.arrangeKeys();
   }

   protected abstract void arrangeKeys();

   protected void method_48579(class_332 guiGraphics, int mouseX, int mouseY, float partialTick) {
      Iterator var5 = this.keys.iterator();

      while(var5.hasNext()) {
         T key = (KeyboardWidget.Key)var5.next();
         key.method_25394(guiGraphics, mouseX, mouseY, partialTick);
      }

      Blit.batchDraw(guiGraphics, () -> {
         guiGraphics.method_25294(this.method_46426(), this.method_46427(), this.method_46426() + this.method_25368(), this.method_46427() + this.method_25364(), Integer.MIN_VALUE);
         guiGraphics.method_49601(this.method_46426(), this.method_46427(), this.method_25368(), this.method_25364(), -5592406);
         Iterator var5 = this.keys.iterator();

         KeyboardWidget.Key key;
         while(var5.hasNext()) {
            key = (KeyboardWidget.Key)var5.next();
            key.renderKeyBackground(guiGraphics, mouseX, mouseY, partialTick);
         }

         var5 = this.keys.iterator();

         while(var5.hasNext()) {
            key = (KeyboardWidget.Key)var5.next();
            key.renderKeyForeground(guiGraphics, mouseX, mouseY, partialTick);
         }

      });
   }

   protected void method_47399(class_6382 narrationElementOutput) {
   }

   @NotNull
   public List<T> method_25396() {
      return this.keys;
   }

   public boolean method_25397() {
      return this.isDragging;
   }

   public void method_25398(boolean dragging) {
      this.isDragging = dragging;
   }

   @Nullable
   public class_364 method_25399() {
      return this.focused;
   }

   public void method_25395(@Nullable class_364 focused) {
      if (this.focused != null) {
         this.focused.method_25365(false);
      }

      if (focused != null) {
         focused.method_25365(true);
      }

      this.focused = focused;
   }

   @Nullable
   public class_8016 method_48205(class_8023 event) {
      return super.method_48205(event);
   }

   public boolean method_25402(double mouseX, double mouseY, int button) {
      return super.method_25402(mouseX, mouseY, button);
   }

   public boolean method_25406(double mouseX, double mouseY, int button) {
      return super.method_25406(mouseX, mouseY, button);
   }

   public boolean method_25403(double mouseX, double mouseY, int button, double dragX, double dragY) {
      return super.method_25403(mouseX, mouseY, button, dragX, dragY);
   }

   public boolean method_25370() {
      return super.method_25370();
   }

   public void method_25365(boolean focused) {
      super.method_25365(focused);
   }

   public static class Key extends class_339 implements ComponentProcessor, ScreenControllerEventListener {
      public static final class_2960 SPRITE = CUtil.rl("keyboard/key");
      private final KeyboardWidget<?> keyboard;
      private final KeyboardWidget.KeyFunction normalFunction;
      private final KeyboardWidget.KeyFunction shiftedFunction;
      private boolean highlighted;
      private final HoldRepeatHelper holdRepeatHelper;
      private final InputBindingSupplier shortcutPressBind;
      private boolean shortcutPressed;

      public Key(class_437 screen, int x, int y, int width, int height, KeyboardWidget.KeyFunction normalFunction, @Nullable KeyboardWidget.KeyFunction shiftedFunction, KeyboardWidget<?> keyboard, @Nullable InputBindingSupplier shortcutPressBind) {
         super(x, y, width, height, class_2561.method_43470("Key"));
         this.keyboard = keyboard;
         this.normalFunction = normalFunction;
         if (shiftedFunction != null) {
            this.shiftedFunction = shiftedFunction;
         } else {
            this.shiftedFunction = normalFunction;
         }

         this.holdRepeatHelper = new HoldRepeatHelper(10, 2);
         this.shortcutPressBind = shortcutPressBind;
         ScreenProcessorProvider.provide(screen).addEventListener(this);
      }

      public Key(class_437 screen, int x, int y, int width, int height, Pair<KeyboardWidget.KeyFunction, KeyboardWidget.KeyFunction> functions, KeyboardWidget<?> keyboard, @Nullable InputBindingSupplier shortcutPressBind) {
         this(screen, x, y, width, height, (KeyboardWidget.KeyFunction)functions.getFirst(), (KeyboardWidget.KeyFunction)functions.getSecond(), keyboard, shortcutPressBind);
      }

      protected void renderKeyBackground(class_332 graphics, int mouseX, int mouseY, float partialTick) {
         Blit.sprite(graphics, SPRITE, this.method_46426() + 1, this.method_46427() + 1, this.method_25368() - 2, this.method_25364() - 2);
      }

      protected void renderKeyForeground(class_332 graphics, int mouseX, int mouseY, float partialTick) {
         if (this.keyboard.shiftMode) {
            this.shiftedFunction.renderer.render(graphics, mouseX, mouseY, partialTick, this);
         } else {
            this.normalFunction.renderer.render(graphics, mouseX, mouseY, partialTick, this);
         }

         if (!this.method_25367() && !this.shortcutPressed) {
            this.holdRepeatHelper.reset();
         } else {
            graphics.method_49601(this.method_46426(), this.method_46427(), this.method_25368(), this.method_25364(), -1);
         }

      }

      protected void method_48579(class_332 graphics, int mouseX, int mouseY, float partialTick) {
      }

      public boolean overrideControllerButtons(ScreenProcessor<?> screen, ControllerEntity controller) {
         if (this.holdRepeatHelper.shouldAction(ControlifyBindings.GUI_PRESS.on(controller))) {
            this.onPress();
            this.holdRepeatHelper.onNavigate();
         }

         return true;
      }

      public void onControllerInput(ControllerEntity controller) {
         if (this.shortcutPressBind != null) {
            InputBinding shortcutBind = this.shortcutPressBind.on(controller);
            this.shortcutPressed = shortcutBind.digitalNow();
            if (this.holdRepeatHelper.shouldAction(shortcutBind)) {
               this.onPress();
               this.holdRepeatHelper.onNavigate();
            }

         }
      }

      public boolean method_25402(double mouseX, double mouseY, int button) {
         if (this.method_25405(mouseX, mouseY)) {
            this.onPress();
            return true;
         } else {
            return false;
         }
      }

      protected void onPress() {
         if (this.keyboard.shiftMode) {
            this.shiftedFunction.consumer.accept(this.keyboard.keyPressConsumer, this);
         } else {
            this.normalFunction.consumer.accept(this.keyboard.keyPressConsumer, this);
         }

      }

      public class_2561 modifyKeyName(class_2561 name) {
         Optional<ControllerEntity> controller = ControlifyApi.get().getCurrentController().filter((c) -> {
            return ControlifyApi.get().currentInputMode().isController();
         });
         if (this.shortcutPressBind != null && controller.isPresent()) {
            InputBinding binding = this.shortcutPressBind.on((ControllerEntity)controller.get());
            return class_2561.method_43473().method_10852(binding.inputIcon()).method_10852(name);
         } else {
            return name;
         }
      }

      protected void method_47399(class_6382 narrationElementOutput) {
      }

      public void setHighlighted(boolean highlighted) {
         this.highlighted = highlighted;
      }

      public boolean isHighlighted() {
         return this.highlighted;
      }

      public static KeyboardWidget.KeyBuilder<KeyboardWidget.Key> builder(Pair<KeyboardWidget.KeyFunction, KeyboardWidget.KeyFunction> functions, @Nullable InputBindingSupplier shortcutPressBind) {
         return (screen, x, y, w, h, kb) -> {
            return new KeyboardWidget.Key(screen, x, y, w, h, (KeyboardWidget.KeyFunction)functions.getFirst(), (KeyboardWidget.KeyFunction)functions.getSecond(), kb, shortcutPressBind);
         };
      }

      public static KeyboardWidget.KeyBuilder<KeyboardWidget.Key> builder(KeyboardWidget.KeyFunction normalFunction, KeyboardWidget.KeyFunction shiftedFunction, @Nullable InputBindingSupplier shortcutPressBind) {
         return (screen, x, y, w, h, kb) -> {
            return new KeyboardWidget.Key(screen, x, y, w, h, normalFunction, shiftedFunction, kb, shortcutPressBind);
         };
      }

      public interface ForegroundRenderer {
         void render(class_332 var1, int var2, int var3, float var4, KeyboardWidget.Key var5);

         static KeyboardWidget.Key.ForegroundRenderer text(class_2561 text) {
            return (guiGraphics, mouseX, mouseY, partialTick, key) -> {
               guiGraphics.method_27534(class_310.method_1551().field_1772, key.modifyKeyName(text), key.method_46426() + key.method_25368() / 2, key.method_46427() + key.method_25364() / 2 - 4, -1);
            };
         }
      }
   }

   @FunctionalInterface
   public interface KeyBuilder<T extends KeyboardWidget.Key> {
      T build(class_437 var1, int var2, int var3, int var4, int var5, KeyboardWidget<T> var6);
   }

   public static class KeyLayoutBuilder<T extends KeyboardWidget.Key> {
      private final List<List<KeyboardWidget.KeyLayoutBuilder.KeyLayout<T>>> layout;
      private final float maxUnitWidth;
      private final int rowCount;
      private final KeyboardWidget<T> keyboard;
      private float currentWidth;
      private int currentRow;

      public KeyLayoutBuilder(float maxUnitWidth, int rowCount, KeyboardWidget<T> keyboard) {
         this.maxUnitWidth = maxUnitWidth;
         this.rowCount = rowCount;
         this.keyboard = keyboard;
         this.layout = new ArrayList(rowCount);

         for(int i = 0; i < rowCount; ++i) {
            this.layout.add(new ArrayList());
         }

      }

      public void key(KeyboardWidget.KeyBuilder<T> key, float width) {
         Validate.isTrue(this.currentWidth + width <= this.maxUnitWidth, "Key width exceeds row width", new Object[0]);
         ((List)this.layout.get(this.currentRow)).add(new KeyboardWidget.KeyLayoutBuilder.KeyLayout(key, width));
         this.currentWidth += width;
      }

      public void nextRow() {
         Validate.isTrue(this.currentRow < this.rowCount, "Row index out of bounds", new Object[0]);
         this.currentWidth = 0.0F;
         ++this.currentRow;
      }

      public void build(Consumer<T> keyConsumer) {
         int trueWidth = this.keyboard.method_25368();
         int trueHeight = this.keyboard.method_25364();
         float unitWidth = (float)trueWidth / this.maxUnitWidth;
         float keyHeight = (float)trueHeight / (float)this.rowCount;
         float y = (float)this.keyboard.method_46427();

         for(Iterator var7 = this.layout.iterator(); var7.hasNext(); y += keyHeight) {
            List<KeyboardWidget.KeyLayoutBuilder.KeyLayout<T>> row = (List)var7.next();
            float x = (float)this.keyboard.method_46426();

            float keyWidth;
            for(Iterator var10 = row.iterator(); var10.hasNext(); x += keyWidth) {
               KeyboardWidget.KeyLayoutBuilder.KeyLayout<T> keyLayout = (KeyboardWidget.KeyLayoutBuilder.KeyLayout)var10.next();
               keyWidth = unitWidth * keyLayout.unitWidth;
               T key = keyLayout.keyBuilder.build(this.keyboard.containingScreen, (int)x, (int)y, (int)keyWidth, (int)keyHeight, this.keyboard);
               keyConsumer.accept(key);
            }
         }

      }

      private static record KeyLayout<T extends KeyboardWidget.Key>(KeyboardWidget.KeyBuilder<T> keyBuilder, float unitWidth) {
         private KeyLayout(KeyboardWidget.KeyBuilder<T> keyBuilder, float unitWidth) {
            this.keyBuilder = keyBuilder;
            this.unitWidth = unitWidth;
         }

         public KeyboardWidget.KeyBuilder<T> keyBuilder() {
            return this.keyBuilder;
         }

         public float unitWidth() {
            return this.unitWidth;
         }
      }
   }

   public static record KeyFunction(BiConsumer<KeyPressConsumer, KeyboardWidget.Key> consumer, KeyboardWidget.Key.ForegroundRenderer renderer) {
      public KeyFunction(BiConsumer<KeyPressConsumer, KeyboardWidget.Key> consumer, KeyboardWidget.Key.ForegroundRenderer renderer) {
         this.consumer = consumer;
         this.renderer = renderer;
      }

      public static Pair<KeyboardWidget.KeyFunction, KeyboardWidget.KeyFunction> ofChar(int normalKeyCode, char normalChar, int normalModifier, int shiftedKeyCode, char shiftedChar, int shiftedModifier) {
         return Pair.of(new KeyboardWidget.KeyFunction((screen, key) -> {
            screen.acceptKeyCode(normalKeyCode, 0, normalModifier);
            screen.acceptChar(normalChar, normalModifier);
         }, KeyboardWidget.Key.ForegroundRenderer.text(class_2561.method_43470(String.valueOf(normalChar)))), new KeyboardWidget.KeyFunction((screen, key) -> {
            screen.acceptKeyCode(shiftedKeyCode, 0, shiftedModifier);
            screen.acceptChar(shiftedChar, shiftedModifier);
         }, KeyboardWidget.Key.ForegroundRenderer.text(class_2561.method_43470(String.valueOf(shiftedChar)))));
      }

      public static Pair<KeyboardWidget.KeyFunction, KeyboardWidget.KeyFunction> ofChar(int keyCode, char ch) {
         return ofChar(keyCode, Character.toLowerCase(ch), 0, keyCode, Character.toUpperCase(ch), 1);
      }

      public static Pair<KeyboardWidget.KeyFunction, KeyboardWidget.KeyFunction> ofRegularKey(int keycode, String normal) {
         KeyboardWidget.KeyFunction function = new KeyboardWidget.KeyFunction((screen, key) -> {
            screen.acceptKeyCode(keycode, 0, 0);
         }, KeyboardWidget.Key.ForegroundRenderer.text(class_2561.method_43470(normal)));
         return Pair.of(function, function);
      }

      public static Pair<KeyboardWidget.KeyFunction, KeyboardWidget.KeyFunction> ofShiftableKey(int normalKeyCode, int normalModifier, String normalName, int shiftKeyCode, int shiftModifier, String shiftName) {
         return Pair.of(new KeyboardWidget.KeyFunction((screen, key) -> {
            screen.acceptKeyCode(normalKeyCode, 0, normalModifier);
         }, KeyboardWidget.Key.ForegroundRenderer.text(class_2561.method_43470(normalName))), new KeyboardWidget.KeyFunction((screen, key) -> {
            screen.acceptKeyCode(shiftKeyCode, 0, shiftModifier);
         }, KeyboardWidget.Key.ForegroundRenderer.text(class_2561.method_43470(shiftName))));
      }

      public static Pair<KeyboardWidget.KeyFunction, KeyboardWidget.KeyFunction> ofShiftableKey(int keyCode, String normal, String shift) {
         return ofShiftableKey(keyCode, 0, normal, keyCode, 1, shift);
      }

      public Pair<KeyboardWidget.KeyFunction, KeyboardWidget.KeyFunction> copyShifted() {
         return Pair.of(this, this);
      }

      public BiConsumer<KeyPressConsumer, KeyboardWidget.Key> consumer() {
         return this.consumer;
      }

      public KeyboardWidget.Key.ForegroundRenderer renderer() {
         return this.renderer;
      }
   }
}
