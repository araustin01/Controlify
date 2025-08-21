package dev.isxander.controlify.bindings.input;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.isxander.controlify.controller.input.ControllerStateView;
import java.util.List;
import net.minecraft.class_2960;

public record AxisInput(class_2960 axis) implements Input {
   public static final String INPUT_ID = "axis";
   public static final MapCodec<AxisInput> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
      return instance.group(class_2960.field_25139.fieldOf("axis").forGetter(AxisInput::axis)).apply(instance, AxisInput::new);
   });

   public AxisInput(class_2960 axis) {
      this.axis = axis;
   }

   public float state(ControllerStateView state) {
      return state.getAxisState(this.axis);
   }

   public List<class_2960> getRelevantInputs() {
      return List.of(this.axis);
   }

   public InputType<?> type() {
      return InputType.AXIS;
   }

   public class_2960 axis() {
      return this.axis;
   }
}
