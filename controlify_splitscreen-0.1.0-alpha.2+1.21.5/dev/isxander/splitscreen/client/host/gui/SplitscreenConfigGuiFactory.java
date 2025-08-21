package dev.isxander.splitscreen.client.host.gui;

import dev.isxander.splitscreen.client.config.SplitscreenConfig;
import dev.isxander.splitscreen.config.AudioMethod;
import dev.isxander.splitscreen.config.MusicMethod;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.api.OptionGroup;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import dev.isxander.yacl3.api.YetAnotherConfigLib.Builder;
import dev.isxander.yacl3.api.controller.BooleanControllerBuilder;
import dev.isxander.yacl3.api.controller.CyclingListControllerBuilder;
import dev.isxander.yacl3.config.v3.ConfigEntry;
import java.util.Objects;
import java.util.function.BiFunction;
import net.minecraft.class_2561;
import net.minecraft.class_437;
import org.jetbrains.annotations.Nullable;

public class SplitscreenConfigGuiFactory {
   public static class_437 buildScreen(@Nullable class_437 parent) {
      Builder var10000 = YetAnotherConfigLib.createBuilder().title(class_2561.method_43471("controlify.splitscreen.config.title")).category(buildBasicCategory());
      SplitscreenConfig var10001 = SplitscreenConfig.INSTANCE;
      Objects.requireNonNull(var10001);
      return var10000.save(var10001::saveToFile).build().generateScreen(parent);
   }

   private static ConfigCategory buildBasicCategory() {
      SplitscreenConfig config = config();
      return ConfigCategory.createBuilder().name(class_2561.method_43471("controlify.splitscreen.config.basic")).option(((dev.isxander.yacl3.api.Option.Builder)startOption(config.preferVerticalSplitscreen, "basic", (builder, translationKey) -> {
         return builder.controller((opt) -> {
            return BooleanControllerBuilder.create(opt).formatValue((vertical) -> {
               return class_2561.method_43471(translationKey + (vertical ? ".vertical" : ".horizontal"));
            });
         });
      })).build()).group(OptionGroup.createBuilder().name(class_2561.method_43471("controlify.splitscreen.config.audio")).option(((dev.isxander.yacl3.api.Option.Builder)startOption(config.audioMethod, "basic", (builder, translationKey) -> {
         return builder.controller((opt) -> {
            return CyclingListControllerBuilder.create(opt).values(AudioMethod.values()).formatValue((method) -> {
               return class_2561.method_43471(translationKey + "." + method.method_15434());
            });
         });
      })).build()).option(((dev.isxander.yacl3.api.Option.Builder)startOption(config.musicMethod, "basic", (builder, translationKey) -> {
         return builder.controller((opt) -> {
            return CyclingListControllerBuilder.create(opt).values(MusicMethod.values()).formatValue((method) -> {
               return class_2561.method_43471(translationKey + "." + method.method_15434());
            });
         });
      })).build()).build()).build();
   }

   private static SplitscreenConfig config() {
      return SplitscreenConfig.INSTANCE;
   }

   private static <T, U> U startOption(ConfigEntry<T> entry, String category, BiFunction<dev.isxander.yacl3.api.Option.Builder<T>, String, U> builder) {
      String translationKey = "controlify.splitscreen.config." + category + "." + entry.fieldName();
      dev.isxander.yacl3.api.Option.Builder<T> start = Option.createBuilder().name(class_2561.method_43471(translationKey)).description(OptionDescription.of(new class_2561[]{class_2561.method_43471(translationKey + ".desc")})).binding(entry.asBinding());
      return builder.apply(start, translationKey);
   }

   private static <T> dev.isxander.yacl3.api.Option.Builder<T> startOption(ConfigEntry<T> entry, String category) {
      return (dev.isxander.yacl3.api.Option.Builder)startOption(entry, category, (opt, translationKey) -> {
         return opt;
      });
   }
}
