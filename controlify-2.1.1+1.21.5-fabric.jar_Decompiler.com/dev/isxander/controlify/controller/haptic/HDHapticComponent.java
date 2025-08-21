package dev.isxander.controlify.controller.haptic;

import dev.isxander.controlify.controller.ECSComponent;
import dev.isxander.controlify.controller.impl.ConfigImpl;
import dev.isxander.controlify.controller.serialization.ConfigClass;
import dev.isxander.controlify.controller.serialization.ConfigHolder;
import dev.isxander.controlify.controller.serialization.IConfig;
import dev.isxander.controlify.mixins.feature.hdhaptics.SoundBufferAccessor;
import dev.isxander.controlify.mixins.feature.hdhaptics.SoundEngineAccessor;
import dev.isxander.controlify.mixins.feature.hdhaptics.SoundManagerAccessor;
import dev.isxander.controlify.utils.CUtil;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import javax.sound.sampled.AudioFormat;
import net.minecraft.class_1140;
import net.minecraft.class_1144;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3414;
import net.minecraft.class_4231;
import net.minecraft.class_4237;
import net.minecraft.class_5819;

public class HDHapticComponent implements ECSComponent, ConfigHolder<HDHapticComponent.Config> {
   public static final class_2960 ID = CUtil.rl("hd_haptics");
   private final IConfig<HDHapticComponent.Config> config = new ConfigImpl(HDHapticComponent.Config::new, HDHapticComponent.Config.class);
   private Consumer<CompleteSoundData> playHapticConsumer;
   private final class_5819 randomSource = class_5819.method_43047();
   private static final class_4237 hapticBufferLibrary = new class_4237(class_310.method_1551().method_1478());
   private static final Map<class_2960, CompleteSoundData> hapticData = new HashMap();

   public void playHaptic(class_2960 haptic) {
      if (((HDHapticComponent.Config)this.confObj()).enabled && this.playHapticConsumer != null) {
         this.getSoundData(haptic, hapticBufferLibrary.method_19743(haptic)).thenAccept(this.playHapticConsumer).exceptionally((throwable) -> {
            throwable.printStackTrace();
            return null;
         });
      }
   }

   public void playHaptic(class_3414 sound) {
      class_2960 location = class_310.method_1551().method_1483().method_4869(sound.comp_3319()).method_4887(this.randomSource).method_4767();
      class_1144 soundManager = class_310.method_1551().method_1483();
      class_1140 soundEngine = ((SoundManagerAccessor)soundManager).getSoundEngine();
      class_4237 bufferLibrary = ((SoundEngineAccessor)soundEngine).getSoundBuffers();
      class_2960 soundId = location.method_45138("sounds/").method_48331(".ogg");
      this.getSoundData(soundId, bufferLibrary.method_19743(soundId)).thenAccept(this.playHapticConsumer).exceptionally((throwable) -> {
         throwable.printStackTrace();
         return null;
      });
   }

   public void acceptPlayHaptic(Consumer<CompleteSoundData> consumer) {
      this.playHapticConsumer = consumer;
   }

   private CompletableFuture<CompleteSoundData> getSoundData(class_2960 id, CompletableFuture<class_4231> sound) {
      return sound.thenApply((soundBuffer) -> {
         return (CompleteSoundData)hapticData.computeIfAbsent(id, (key) -> {
            SoundBufferAccessor accessor = (SoundBufferAccessor)soundBuffer;
            ByteBuffer bytes = accessor.getData();
            AudioFormat format = accessor.getFormat();
            if (bytes == null) {
               return null;
            } else {
               bytes.rewind();
               byte[] audio = new byte[bytes.remaining()];
               bytes.get(audio);
               return new CompleteSoundData(audio, format);
            }
         });
      });
   }

   public IConfig<HDHapticComponent.Config> config() {
      return this.config;
   }

   public class_2960 id() {
      return ID;
   }

   public static class Config implements ConfigClass {
      public boolean enabled = true;
   }
}
