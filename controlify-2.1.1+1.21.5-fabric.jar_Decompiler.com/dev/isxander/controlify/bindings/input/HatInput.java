package dev.isxander.controlify.bindings.input;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.isxander.controlify.controller.input.ControllerStateView;
import dev.isxander.controlify.controller.input.HatState;
import java.util.List;
import net.minecraft.class_2960;

public record HatInput(class_2960 hat, HatState targetState) implements Input {
   public static final String INPUT_ID = "hat";
   public static final MapCodec<HatInput> CODEC = RecordCodecBuilder.mapCodec((instance) -> {
      return instance.group(class_2960.field_25139.fieldOf("hat").forGetter(HatInput::hat), HatState.CODEC.fieldOf("target_state").forGetter(HatInput::targetState)).apply(instance, HatInput::new);
   });

   public HatInput(class_2960 hat, HatState targetState) {
      this.hat = hat;
      this.targetState = targetState;
   }

   public float state(ControllerStateView state) {
      return state.getHatState(this.hat) == this.targetState ? 1.0F : 0.0F;
   }

   public List<class_2960> getRelevantInputs() {
      return List.of(this.hat);
   }

   public InputType<?> type() {
      return InputType.HAT;
   }

   public class_2960 hat() {
      return this.hat;
   }

   public HatState targetState() {
      return this.targetState;
   }
}
