package dev.isxander.splitscreen.client.mixins.music;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import net.minecraft.class_10383;
import net.minecraft.class_1142;
import org.spongepowered.asm.mixin.Mixin;

@Mixin({class_1142.class})
public class MusicManagerMixin {
   @WrapMethod(
      method = {"method_4858(Lnet/minecraft/class_10383;)V"}
   )
   private void preventMusicIfPawn(class_10383 music, Operation<Void> original) {
      SplitscreenBootstrapper.getControllerBridge().ifPresentOrElse((bridge) -> {
         bridge.requestPlayMusic(music.comp_3344(), music.comp_3345());
      }, () -> {
         original.call(new Object[]{music});
      });
   }
}
