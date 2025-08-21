package dev.isxander.controlify.bindings.defaults;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.MapCodec;
import dev.isxander.controlify.api.bind.ControlifyBindApi;
import dev.isxander.controlify.bindings.input.Input;
import java.util.Map;
import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;

public record MapBackedDefaultBindProvider(Map<class_2960, Input> map) implements DefaultBindProvider {
   public static final MapCodec<MapBackedDefaultBindProvider> MAP_CODEC;

   public MapBackedDefaultBindProvider(Map<class_2960, Input> map) {
      this.map = map;
   }

   @Nullable
   public Input getDefaultBind(class_2960 bindId) {
      return (Input)this.map.get(bindId);
   }

   public Map<class_2960, Input> map() {
      return this.map;
   }

   static {
      MAP_CODEC = Codec.simpleMap(class_2960.field_25139, Input.CODEC, Keyable.forStrings(() -> {
         return ControlifyBindApi.get().getAllBindIds().map(class_2960::toString);
      })).xmap(MapBackedDefaultBindProvider::new, MapBackedDefaultBindProvider::map);
   }
}
