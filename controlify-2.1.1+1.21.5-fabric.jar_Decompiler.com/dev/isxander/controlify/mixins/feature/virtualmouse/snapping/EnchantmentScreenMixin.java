package dev.isxander.controlify.mixins.feature.virtualmouse.snapping;

import dev.isxander.controlify.api.vmousesnapping.ISnapBehaviour;
import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import java.util.function.Consumer;
import net.minecraft.class_1718;
import net.minecraft.class_486;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({class_486.class})
public abstract class EnchantmentScreenMixin extends AbstractContainerScreenMixin<class_1718> implements ISnapBehaviour {
   @Unique
   private static final int SLOT_AREA_OFFSET_X = 60;
   @Unique
   private static final int SLOT_AREA_OFFSET_Y = 14;
   @Unique
   private static final int SLOT_WIDTH = 108;
   @Unique
   private static final int SLOT_HEIGHT = 19;
   @Unique
   private static final int SLOT_VERTICAL_SPACING = 19;

   public void controlify$collectSnapPoints(Consumer<SnapPoint> consumer) {
      super.controlify$collectSnapPoints(consumer);
      int leftPos = (this.field_22789 - this.field_2792) / 2;
      int topPos = (this.field_22790 - this.field_2779) / 2;
      int snapRadius = 14;

      for(int slotIndex = 0; slotIndex < 3; ++slotIndex) {
         if (((class_1718)this.field_2797).field_7808[slotIndex] != 0) {
            int slotX = leftPos + 60;
            int slotY = topPos + 14 + slotIndex * 19;
            int centreSlotX = slotX + 54;
            int centreSlotY = slotY + 9;
            consumer.accept(new SnapPoint(centreSlotX, centreSlotY, snapRadius));
         }
      }

   }
}
