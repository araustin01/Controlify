package dev.isxander.controlify.mixins.feature.virtualmouse.snapping;

import dev.isxander.controlify.api.vmousesnapping.SnapPoint;
import java.util.Iterator;
import java.util.function.Consumer;
import net.minecraft.class_1761;
import net.minecraft.class_342;
import net.minecraft.class_481;
import net.minecraft.class_7706;
import net.minecraft.class_1761.class_7915;
import net.minecraft.class_481.class_483;
import org.joml.Vector2i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({class_481.class})
public abstract class CreativeModeInventoryScreenMixin extends AbstractContainerScreenMixin<class_483> {
   @Shadow
   private float field_2890;
   @Shadow
   private class_342 field_2894;

   @Shadow
   protected abstract int method_47422(class_1761 var1);

   @Shadow
   protected abstract boolean method_2465();

   public void controlify$collectSnapPoints(Consumer<SnapPoint> consumer) {
      super.controlify$collectSnapPoints(consumer);
      Iterator var2 = class_7706.method_47335().iterator();

      while(var2.hasNext()) {
         class_1761 tab = (class_1761)var2.next();
         boolean topRow = tab.method_47309() == class_7915.field_41049;
         int x = this.field_2776 + this.method_47422(tab);
         int y = this.field_2800 + (topRow ? -28 : this.field_2779 - 4);
         consumer.accept(new SnapPoint(new Vector2i(x + 13, y + 16), 18));
      }

      if (this.method_2465()) {
         int scrollTop = this.field_2800 + 18;
         int scrollBottom = scrollTop + 112;
         consumer.accept(new SnapPoint(new Vector2i(this.field_2776 + 175 + 6, scrollTop + (int)((float)(scrollBottom - scrollTop - 17) * this.field_2890) + 7), 15));
      }

      if (this.field_2894.method_1885()) {
         consumer.accept(new SnapPoint(new Vector2i(this.field_2894.method_46426() + this.field_2894.method_25368() / 2, this.field_2894.method_46427() + this.field_2894.method_25364() / 2), this.field_2894.method_25364() + 2));
      }

   }
}
