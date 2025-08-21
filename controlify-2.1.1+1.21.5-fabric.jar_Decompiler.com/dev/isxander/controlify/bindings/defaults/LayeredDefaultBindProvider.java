package dev.isxander.controlify.bindings.defaults;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.isxander.controlify.bindings.input.Input;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;

public record LayeredDefaultBindProvider(List<LayeredDefaultBindProvider.Layer> layers) implements DefaultBindProvider {
   public static final Codec<LayeredDefaultBindProvider> CODEC;

   public LayeredDefaultBindProvider(List<LayeredDefaultBindProvider.Layer> layers) {
      this.layers = layers;
   }

   public static DefaultBindProvider of(LayeredDefaultBindProvider.Layer... layers) {
      return new LayeredDefaultBindProvider(Arrays.asList(layers));
   }

   @Nullable
   public Input getDefaultBind(class_2960 bindId) {
      Iterator var2 = this.layers().iterator();

      LayeredDefaultBindProvider.Layer layer;
      do {
         if (!var2.hasNext()) {
            return null;
         }

         layer = (LayeredDefaultBindProvider.Layer)var2.next();
         Input input = layer.provider().getDefaultBind(bindId);
         if (input != null) {
            return input;
         }
      } while(!layer.clearBelow());

      return null;
   }

   public List<LayeredDefaultBindProvider.Layer> layers() {
      return this.layers;
   }

   static {
      CODEC = Codec.list(LayeredDefaultBindProvider.Layer.CODEC).xmap(LayeredDefaultBindProvider::new, LayeredDefaultBindProvider::layers);
   }

   public static record Layer(DefaultBindProvider provider, boolean clearBelow) {
      public static final Codec<LayeredDefaultBindProvider.Layer> CODEC = RecordCodecBuilder.create((instance) -> {
         return instance.group(MapBackedDefaultBindProvider.MAP_CODEC.forGetter((layer) -> {
            return (MapBackedDefaultBindProvider)layer.provider();
         }), Codec.BOOL.fieldOf("clear_below").forGetter(LayeredDefaultBindProvider.Layer::clearBelow)).apply(instance, LayeredDefaultBindProvider.Layer::new);
      });

      public Layer(DefaultBindProvider provider, boolean clearBelow) {
         this.provider = provider;
         this.clearBelow = clearBelow;
      }

      public DefaultBindProvider provider() {
         return this.provider;
      }

      public boolean clearBelow() {
         return this.clearBelow;
      }
   }
}
