package dev.isxander.controlify.mixins.feature.virtualmouse.snapping;

import dev.isxander.controlify.api.vmousesnapping.ISnapBehaviour;
import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Stream;
import net.minecraft.class_310;
import net.minecraft.class_339;
import net.minecraft.class_364;
import net.minecraft.class_437;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({class_437.class})
public abstract class ScreenMixin implements ISnapBehaviour {
   @Shadow
   public int field_22789;
   @Shadow
   public int field_22790;
   @Shadow
   @Nullable
   protected class_310 field_22787;

   @Shadow
   public abstract List<? extends class_364> method_25396();

   public void controlify$collectSnapPoints(Consumer<SnapPoint> consumer) {
      Stream var10000 = this.method_25396().stream().filter((child) -> {
         return child instanceof class_339;
      });
      Objects.requireNonNull(class_339.class);
      var10000.map(class_339.class::cast).map((widget) -> {
         return new SnapPoint(new Vector2i(widget.method_46426() + widget.method_25368() / 2, widget.method_46427() + widget.method_25364() / 2), Math.min(widget.method_25368(), widget.method_25364()) / 2 + 10);
      }).forEach(consumer);
   }
}
