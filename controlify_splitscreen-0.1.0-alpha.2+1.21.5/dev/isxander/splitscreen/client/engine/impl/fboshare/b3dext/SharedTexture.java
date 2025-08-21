package dev.isxander.splitscreen.client.engine.impl.fboshare.b3dext;

import com.mojang.blaze3d.textures.TextureFormat;
import net.minecraft.class_2540;
import net.minecraft.class_9135;
import net.minecraft.class_9139;

public record SharedTexture(ShareHandle handle, TextureFormat format) {
   public static final class_9139<class_2540, SharedTexture> STREAM_CODEC;

   public SharedTexture(ShareHandle handle, TextureFormat format) {
      this.handle = handle;
      this.format = format;
   }

   public ShareHandle handle() {
      return this.handle;
   }

   public TextureFormat format() {
      return this.format;
   }

   static {
      STREAM_CODEC = class_9139.method_56435(ShareHandle.CODEC, SharedTexture::handle, class_9135.field_49675.method_56432((ordinal) -> {
         return TextureFormat.values()[ordinal];
      }, Enum::ordinal), SharedTexture::format, SharedTexture::new);
   }
}
