package dev.isxander.controlify.controller.keyboard;

import dev.isxander.controlify.controller.ECSComponent;
import dev.isxander.controlify.controller.impl.ConfigImpl;
import dev.isxander.controlify.controller.serialization.ConfigClass;
import dev.isxander.controlify.controller.serialization.ConfigHolder;
import dev.isxander.controlify.controller.serialization.IConfig;
import dev.isxander.controlify.utils.CUtil;
import net.minecraft.class_2960;

public class NativeKeyboardComponent implements ECSComponent, ConfigHolder<NativeKeyboardComponent.Config> {
   public static final class_2960 ID = CUtil.rl("native_keyboard");
   private final ConfigImpl<NativeKeyboardComponent.Config> config = new ConfigImpl(NativeKeyboardComponent.Config::new, NativeKeyboardComponent.Config.class);
   private final Runnable onOpen;
   private final float keyboardHeight;

   public NativeKeyboardComponent(Runnable onOpen, float keyboardHeight) {
      this.onOpen = onOpen;
      this.keyboardHeight = keyboardHeight;
   }

   public void open() {
      this.onOpen.run();
   }

   public float getKeyboardHeight() {
      return this.keyboardHeight;
   }

   public IConfig<NativeKeyboardComponent.Config> config() {
      return this.config;
   }

   public class_2960 id() {
      return ID;
   }

   public static class Config implements ConfigClass {
      public boolean useNativeKeyboard = false;
   }
}
