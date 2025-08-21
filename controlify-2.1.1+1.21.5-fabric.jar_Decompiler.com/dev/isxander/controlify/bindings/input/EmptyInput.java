package dev.isxander.controlify.bindings.input;

import com.mojang.serialization.MapCodec;
import dev.isxander.controlify.controller.input.ControllerStateView;
import java.util.List;
import net.minecraft.class_2960;

public record EmptyInput() implements Input {
   public static final EmptyInput INSTANCE = new EmptyInput();
   public static final String INPUT_ID = "empty";
   public static final MapCodec<EmptyInput> CODEC = MapCodec.unit(() -> {
      return INSTANCE;
   });

   public float state(ControllerStateView state) {
      return 0.0F;
   }

   public List<class_2960> getRelevantInputs() {
      return List.of();
   }

   public InputType<?> type() {
      return InputType.EMPTY;
   }

   public static boolean equals(Input input) {
      return input instanceof EmptyInput;
   }
}
