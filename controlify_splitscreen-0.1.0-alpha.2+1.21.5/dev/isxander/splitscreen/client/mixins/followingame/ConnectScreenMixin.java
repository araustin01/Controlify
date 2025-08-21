package dev.isxander.splitscreen.client.mixins.followingame;

import com.llamalad7.mixinextras.expression.Definition;
import com.llamalad7.mixinextras.expression.Definitions;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import net.minecraft.class_364;
import net.minecraft.class_412;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({class_412.class})
public class ConnectScreenMixin {
   @WrapWithCondition(
      method = {"method_25426()V"},
      at = {@At("MIXINEXTRAS:EXPRESSION")}
   )
   @Definitions({@Definition(
   id = "addRenderableWidget",
   method = {"Lnet/minecraft/class_412;method_37063(Lnet/minecraft/class_364;)Lnet/minecraft/class_364;"}
), @Definition(
   id = "buttonBuilder",
   method = {"Lnet/minecraft/class_4185;method_46430(Lnet/minecraft/class_2561;Lnet/minecraft/class_4185$class_4241;)Lnet/minecraft/class_4185$class_7840;"}
), @Definition(
   id = "GUI_CANCEL",
   field = {"Lnet/minecraft/class_5244;field_24335:Lnet/minecraft/class_2561;"}
)})
   @Expression({"this.addRenderableWidget(buttonBuilder(GUI_CANCEL, ?).?(?, ?, ?, ?).?())"})
   private boolean shouldAddCancelButton(class_412 instance, class_364 guiEventListener) {
      return SplitscreenBootstrapper.getPawn().isEmpty();
   }
}
