package dev.isxander.controlify.mixins.feature.virtualmouse.snapping;

import dev.isxander.controlify.api.vmousesnapping.ISnapBehaviour;
import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import java.util.function.Consumer;
import net.minecraft.class_3971;
import net.minecraft.class_3979;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_3979.class})
public abstract class StonecutterScreenMixin extends AbstractContainerScreenMixin<class_3971> implements ISnapBehaviour {
   @Unique
   private static final int COLUMNS = 4;
   @Unique
   private static final int ROWS = 3;
   @Unique
   private static final int SLOTS = 12;
   @Unique
   private static final int BTN_WIDTH = 16;
   @Unique
   private static final int BTN_HEIGHT = 18;
   @Unique
   private static final int BTN_Y_PADDING = 2;
   @Shadow
   private int field_17671;

   public void controlify$collectSnapPoints(Consumer<SnapPoint> consumer) {
      super.controlify$collectSnapPoints(consumer);
      int startIndex = this.field_17671;
      int endIndex = this.field_17671 + 12;
      int visibleRecipes = ((class_3971)this.field_2797).method_17864();
      int gridLeft = this.field_2776 + 52;
      int gridTop = this.field_2800 + 14;
      int halfWidth = 8;
      int halfHeight = 9;
      int snapRadius = halfHeight + 2;

      for(int absoluteIndex = startIndex; absoluteIndex < endIndex && absoluteIndex < visibleRecipes + startIndex; ++absoluteIndex) {
         int relativeIndex = absoluteIndex - startIndex;
         int column = relativeIndex % 4;
         int row = relativeIndex / 4;
         int posX = gridLeft + column * 16 + halfWidth;
         int posY = gridTop + row * 18 + 2 + halfHeight;
         consumer.accept(new SnapPoint(posX, posY, snapRadius));
      }

   }
}
