package dev.isxander.controlify.bindings.input;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import dev.isxander.controlify.controller.input.ControllerStateView;
import java.util.List;
import net.minecraft.class_2960;

public interface Input {
   MapCodec<Input> MAP_CODEC = InputType.createCodec(InputType.TYPES, InputType::codec, Input::type, "type");
   Codec<Input> CODEC = MAP_CODEC.codec();

   float state(ControllerStateView var1);

   List<class_2960> getRelevantInputs();

   InputType<?> type();
}
