package dev.isxander.controlify.font;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.bindings.input.Input;
import dev.isxander.controlify.controller.id.ControllerType;
import dev.isxander.controlify.platform.client.resource.SimpleControlifyReloadListener;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.log.ControlifyLogger;
import java.io.BufferedReader;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_3298;
import net.minecraft.class_3300;
import net.minecraft.class_7654;
import org.jetbrains.annotations.Nullable;

public class InputFontMapper implements SimpleControlifyReloadListener<InputFontMapper.Preparations> {
   private ImmutableMap<class_2960, InputFontMapper.FontMap> mappings = ImmutableMap.of();
   private InputFontMapper.FontMap defaultFontMap;
   private static final Codec<Character> CHAR_CODEC;
   private static final Codec<Pair<Character, Map<class_2960, Character>>> FONT_MAP_CODEC;
   private static final class_7654 fileToIdConverter;

   public CompletableFuture<InputFontMapper.Preparations> load(class_3300 manager, Executor executor) {
      return CompletableFuture.supplyAsync(() -> {
         Map<class_2960, class_3298> mappingResources = fileToIdConverter.method_45113(manager);
         Map<class_2960, InputFontMapper.FontMap> mappings = (Map)mappingResources.entrySet().stream().flatMap((entry) -> {
            class_2960 rl = (class_2960)entry.getKey();
            class_3298 resource = (class_3298)entry.getValue();
            class_2960 namespace = fileToIdConverter.method_45115(rl);

            try {
               BufferedReader reader = resource.method_43039();

               Stream var7;
               label54: {
                  try {
                     JsonElement element = JsonParser.parseReader(reader);
                     DataResult var10000 = FONT_MAP_CODEC.parse(JsonOps.INSTANCE, element);
                     ControlifyLogger var10001 = CUtil.LOGGER;
                     Objects.requireNonNull(var10001);
                     InputFontMapper.FontMap map = (InputFontMapper.FontMap)var10000.resultOrPartial(var10001::error).map((pair) -> {
                        return new InputFontMapper.FontMap(namespace, (Character)pair.getFirst(), (Map)pair.getSecond());
                     }).orElse((Object)null);
                     if (map != null) {
                        var7 = Stream.of(Pair.of(namespace, map));
                        break label54;
                     }
                  } catch (Throwable var9) {
                     if (reader != null) {
                        try {
                           reader.close();
                        } catch (Throwable var8) {
                           var9.addSuppressed(var8);
                        }
                     }

                     throw var9;
                  }

                  if (reader != null) {
                     reader.close();
                  }

                  return Stream.empty();
               }

               if (reader != null) {
                  reader.close();
               }

               return var7;
            } catch (Exception var10) {
               CUtil.LOGGER.error("Failed to load font mappings for namespace {}", namespace, var10);
               return Stream.empty();
            }
         }).collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));
         return new InputFontMapper.Preparations(mappings);
      });
   }

   public CompletableFuture<Void> apply(InputFontMapper.Preparations data, class_3300 manager, Executor executor) {
      return CompletableFuture.runAsync(() -> {
         Builder<class_2960, InputFontMapper.FontMap> builder = ImmutableMap.builder();
         Map var10000 = data.mappings();
         Objects.requireNonNull(builder);
         var10000.forEach(builder::put);
         this.mappings = builder.build();
         this.defaultFontMap = (InputFontMapper.FontMap)this.mappings.get(ControllerType.DEFAULT.namespace());
      }, executor);
   }

   public InputFontMapper.FontMap getMappings(class_2960 namespace) {
      return (InputFontMapper.FontMap)this.mappings.getOrDefault(namespace, this.defaultFontMap);
   }

   public class_2561 getComponentFromBinding(class_2960 namespace, @Nullable InputBinding binding) {
      if (binding == null) {
         return class_2561.method_43470("?");
      } else {
         List<class_2960> relevantInputs = binding.boundInput().getRelevantInputs();
         return this.getComponentFromInputs(namespace, relevantInputs);
      }
   }

   public class_2561 getComponentFromBind(class_2960 namespace, Input input) {
      List<class_2960> relevantInputs = input.getRelevantInputs();
      return this.getComponentFromInputs(namespace, relevantInputs);
   }

   public class_2561 getComponentFromInputs(class_2960 namespace, List<class_2960> inputs) {
      if (inputs.isEmpty()) {
         return class_2561.method_43470("<unbound>");
      } else {
         InputFontMapper.FontMap fontMap = this.getMappings(namespace);
         String literal = (String)inputs.stream().map((input) -> {
            return String.valueOf(this.getChar(fontMap, input));
         }).collect(Collectors.joining("+"));
         return class_2561.method_43470(literal).method_27694((style) -> {
            return style.method_27704(fontMap.namespace().method_45138("controller/"));
         });
      }
   }

   private char getChar(InputFontMapper.FontMap fontMap, class_2960 input) {
      Character ch = (Character)fontMap.inputToChar().get(input);
      if (ch == null) {
         ch = (Character)this.defaultFontMap.inputToChar().getOrDefault(input, fontMap.unknown());
      }

      return ch;
   }

   public class_2960 getReloadId() {
      return CUtil.rl("font_mappings");
   }

   static {
      CHAR_CODEC = Codec.STRING.comapFlatMap((str) -> {
         return str.length() != 1 ? DataResult.error(() -> {
            return "Expected a single character string, got " + str;
         }) : DataResult.success(str.charAt(0));
      }, String::valueOf);
      FONT_MAP_CODEC = Codec.pair(CHAR_CODEC.fieldOf("unknown").codec(), Codec.unboundedMap(class_2960.field_25139, CHAR_CODEC));
      fileToIdConverter = class_7654.method_45114("controllers/font_mappings");
   }

   public static record Preparations(Map<class_2960, InputFontMapper.FontMap> mappings) {
      public Preparations(Map<class_2960, InputFontMapper.FontMap> mappings) {
         this.mappings = mappings;
      }

      public Map<class_2960, InputFontMapper.FontMap> mappings() {
         return this.mappings;
      }
   }

   public static record FontMap(class_2960 namespace, char unknown, Map<class_2960, Character> inputToChar) {
      public FontMap(class_2960 namespace, char unknown, Map<class_2960, Character> inputToChar) {
         this.namespace = namespace;
         this.unknown = unknown;
         this.inputToChar = inputToChar;
      }

      public class_2960 namespace() {
         return this.namespace;
      }

      public char unknown() {
         return this.unknown;
      }

      public Map<class_2960, Character> inputToChar() {
         return this.inputToChar;
      }
   }
}
