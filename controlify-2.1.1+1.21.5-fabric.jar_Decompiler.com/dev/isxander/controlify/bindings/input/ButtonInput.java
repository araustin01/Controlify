package dev.isxander.controlify.bindings.input;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.isxander.controlify.controller.input.ControllerStateView;
import java.util.List;
import net.minecraft.class_2960;

public record ButtonInput(class_2960 button) implements Input {
   public static final String INPUT_ID = "button";
   public static final MapCodec<ButtonInput> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
      return instance.group(class_2960.field_25139.fieldOf("button").forGetter(ButtonInput::button)).apply(instance, ButtonInput::new);
   });

   public ButtonInput(class_2960 button) {
      this.button = button;
   }

   public float state(ControllerStateView state) {
      return state.isButtonDown(this.button) ? 1.0F : 0.0F;
   }

   public List<class_2960> getRelevantInputs() {
      return List.of(this.button);
   }

   public InputType<?> type() {
      return InputType.BUTTON;
   }

   public class_2960 button() {
      return this.button;
   }
}
