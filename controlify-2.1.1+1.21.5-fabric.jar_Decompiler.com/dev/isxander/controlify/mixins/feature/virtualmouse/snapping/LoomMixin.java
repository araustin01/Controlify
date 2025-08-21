package dev.isxander.controlify.mixins.feature.virtualmouse.snapping;

import dev.isxander.controlify.api.vmousesnapping.ISnapBehaviour;
import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.class_1726;
import net.minecraft.class_2582;
import net.minecraft.class_494;
import net.minecraft.class_6880;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_494.class})
public abstract class LoomMixin extends AbstractContainerScreenMixin<class_1726> implements ISnapBehaviour {
   @Unique
   private static final int COLUMNS = 4;
   @Unique
   private static final int ROWS = 4;
   @Unique
   private static final int SLOTS = 16;
   @Unique
   private static final int BTN_SIZE = 14;
   @Shadow
   private boolean field_2965;
   @Shadow
   private int field_39190;

   public void controlify$collectSnapPoints(Consumer<SnapPoint> consumer) {
      super.controlify$collectSnapPoints(consumer);
      if (this.field_2965) {
         List<class_6880<class_2582>> patterns = ((class_1726)this.field_2797).method_43706();
         if (!patterns.isEmpty()) {
            int numPatterns = patterns.size();
            int gridLeft = this.field_2776 + 60;
            int gridTop = this.field_2800 + 14;
            int firstIndex = this.field_39190 * 4;
            int endPatternIndexExclusive = Math.min(numPatterns, firstIndex + 16);

            for(int patternIndex = firstIndex; patternIndex < endPatternIndexExclusive; ++patternIndex) {
               int absoluteRow = patternIndex / 4;
               int gridColumn = patternIndex % 4;
               int gridRow = absoluteRow - this.field_39190;
               int buttonX = gridLeft + gridColumn * 14 + 7;
               int buttonY = gridTop + gridRow * 14 + 7;
               consumer.accept(new SnapPoint(buttonX, buttonY, 7));
            }

         }
      }
   }
}
