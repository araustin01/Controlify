package dev.isxander.controlify.mixins.feature.virtualmouse.snapping;

import dev.isxander.controlify.api.vmousesnapping.ISnapBehaviour;
import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import java.util.function.Consumer;
import net.minecraft.class_1703;
import net.minecraft.class_465;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({class_465.class})
public abstract class AbstractContainerScreenMixin<T extends class_1703> extends ScreenMixin implements ISnapBehaviour {
   @Shadow
   protected int field_2776;
   @Shadow
   protected int field_2800;
   @Shadow
   protected int field_2779;
   @Shadow
   @Final
   protected T field_2797;
   @Shadow
   protected int field_2792;

   @Shadow
   public abstract T method_17577();

   public void controlify$collectSnapPoints(Consumer<SnapPoint> consumer) {
      super.controlify$collectSnapPoints(consumer);
      this.method_17577().field_7761.stream().map((slot) -> {
         return new SnapPoint(new Vector2i(this.field_2776 + slot.field_7873 + 8, this.field_2800 + slot.field_7872 + 8), 17);
      }).forEach(consumer);
   }
}
