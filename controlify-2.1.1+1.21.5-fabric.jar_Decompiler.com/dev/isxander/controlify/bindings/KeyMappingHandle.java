package dev.isxander.controlify.bindings;

import dev.isxander.controlify.controller.ControllerEntity;
import java.util.function.BooleanSupplier;

public interface KeyMappingHandle {
   void controlify$setPressed(boolean var1);

   void controlify$addToggleCondition(ControllerEntity var1, BooleanSupplier var2);
}
