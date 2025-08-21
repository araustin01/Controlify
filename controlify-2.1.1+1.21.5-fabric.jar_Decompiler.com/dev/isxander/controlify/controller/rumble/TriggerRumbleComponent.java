package dev.isxander.controlify.controller.rumble;

import dev.isxander.controlify.controller.ECSComponent;
import dev.isxander.controlify.rumble.TriggerRumbleState;
import dev.isxander.controlify.utils.CUtil;
import java.util.Optional;
import net.minecraft.class_2960;
import org.jetbrains.annotations.NotNull;

public class TriggerRumbleComponent implements ECSComponent {
   public static final class_2960 ID = CUtil.rl("trigger_rumble");
   private TriggerRumbleState state = null;

   public void queueTriggerRumble(@NotNull TriggerRumbleState state) {
      this.state = state;
   }

   public Optional<TriggerRumbleState> consumeTriggerRumble() {
      TriggerRumbleState state = this.state;
      this.state = null;
      return Optional.ofNullable(state);
   }

   public class_2960 id() {
      return ID;
   }
}
