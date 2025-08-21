package dev.isxander.splitscreen.server.mixins.status;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.serialization.Codec;
import dev.isxander.splitscreen.server.status.ServerStatusSplitscreenExt;
import net.minecraft.class_2926;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_2926.class})
public class ServerStatusMixin implements ServerStatusSplitscreenExt.Duck {
   @Unique
   private final ServerStatusSplitscreenExt ext;

   public ServerStatusMixin() {
      this.ext = (ServerStatusSplitscreenExt)ServerStatusSplitscreenExt.inProgressParam.get();
   }

   public ServerStatusSplitscreenExt splitscreen$getExt() {
      return this.ext;
   }

   public void splitscreen$setExt(ServerStatusSplitscreenExt ext) {
      throw new UnsupportedOperationException("Cannot set ext on records");
   }

   @ModifyExpressionValue(
      method = {"<clinit>()V"},
      at = {@At(
   value = "INVOKE",
   target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;"
)}
   )
   private static Codec<class_2926> wrapCodec(Codec<class_2926> codec) {
      return ServerStatusSplitscreenExt.wrapCodec(codec);
   }
}
