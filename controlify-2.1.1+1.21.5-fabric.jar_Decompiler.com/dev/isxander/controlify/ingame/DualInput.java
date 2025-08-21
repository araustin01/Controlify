package dev.isxander.controlify.ingame;

import net.minecraft.class_10185;
import net.minecraft.class_241;
import net.minecraft.class_3532;
import net.minecraft.class_744;
import org.apache.commons.lang3.Validate;

public class DualInput extends class_744 {
   private final class_744 input1;
   private final class_744 input2;

   public DualInput(class_744 input1, class_744 input2) {
      Validate.isTrue(!(input1 instanceof DualInput), "Cannot nest DualInputs", new Object[0]);
      Validate.isTrue(!(input2 instanceof DualInput), "Cannot nest DualInputs", new Object[0]);
      this.input1 = input1;
      this.input2 = input2;
   }

   public void method_3129() {
      this.input1.method_3129();
      this.input2.method_3129();
      class_241 input1MoveVec = InGameInputHandler.getMoveVec(this.input1);
      class_241 input2MoveVec = InGameInputHandler.getMoveVec(this.input2);
      this.setMoveVec(class_3532.method_15363(input1MoveVec.field_1342 + input2MoveVec.field_1342, -1.0F, 1.0F), class_3532.method_15363(input1MoveVec.field_1343 + input2MoveVec.field_1343, -1.0F, 1.0F));
      class_10185 input1 = this.input1.field_54155;
      class_10185 input2 = this.input2.field_54155;
      this.field_54155 = new class_10185(input1.comp_3159() || input2.comp_3159(), input1.comp_3160() || input2.comp_3160(), input1.comp_3161() || input2.comp_3161(), input1.comp_3162() || input2.comp_3162(), input1.comp_3163() || input2.comp_3163(), input1.comp_3164() || input2.comp_3164(), input1.comp_3165() || input2.comp_3165());
   }

   private void setMoveVec(float forward, float left) {
      this.field_55868 = new class_241(left, forward);
   }
}
