package dev.isxander.splitscreen.client.engine.impl.fboshare.b3dext;

import net.minecraft.class_2540;
import net.minecraft.class_9135;
import net.minecraft.class_9139;

public record SharedRenderTarget(SharedTexture colour, SharedTexture depth, int width, int height) {
   public static final class_9139<class_2540, SharedRenderTarget> STREAM_CODEC;

   public SharedRenderTarget(SharedTexture colour, SharedTexture depth, int width, int height) {
      this.colour = colour;
      this.depth = depth;
      this.width = width;
      this.height = height;
   }

   public int getAllocationSize() {
      int ppf = this.width() * this.height();
      return this.colour().format().pixelSize() * ppf + this.depth().format().pixelSize() * ppf;
   }

   public SharedTexture colour() {
      return this.colour;
   }

   public SharedTexture depth() {
      return this.depth;
   }

   public int width() {
      return this.width;
   }

   public int height() {
      return this.height;
   }

   static {
      STREAM_CODEC = class_9139.method_56905(SharedTexture.STREAM_CODEC, SharedRenderTarget::colour, SharedTexture.STREAM_CODEC, SharedRenderTarget::depth, class_9135.field_49675, SharedRenderTarget::width, class_9135.field_49675, SharedRenderTarget::height, SharedRenderTarget::new);
   }
}
