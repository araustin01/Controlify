package dev.isxander.controlify.mixins.feature.virtualmouse.snapping;

import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import dev.isxander.controlify.virtualmouse.SnapUtils;
import java.util.function.Consumer;
import net.minecraft.class_10260;
import net.minecraft.class_1703;
import net.minecraft.class_507;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({class_10260.class})
public abstract class AbstractRecipeBookScreenMixin<T extends class_1703> extends AbstractContainerScreenMixin<T> {
   @Shadow
   @Final
   private class_507<?> field_54474;

   public void controlify$collectSnapPoints(Consumer<SnapPoint> consumer) {
      super.controlify$collectSnapPoints(consumer);
      SnapUtils.addRecipeSnapPoints(this.field_54474, consumer);
   }
}
