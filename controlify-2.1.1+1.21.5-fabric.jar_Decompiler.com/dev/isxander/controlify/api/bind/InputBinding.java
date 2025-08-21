package dev.isxander.controlify.api.bind;

import dev.isxander.controlify.bindings.BindContext;
import dev.isxander.controlify.bindings.StateAccess;
import dev.isxander.controlify.bindings.input.EmptyInput;
import dev.isxander.controlify.bindings.input.Input;
import dev.isxander.controlify.bindings.output.AnalogueOutput;
import dev.isxander.controlify.bindings.output.DigitalOutput;
import dev.isxander.controlify.bindings.output.GuiPressOutput;
import dev.isxander.controlify.controller.input.ControllerStateView;
import dev.isxander.controlify.utils.CUtil;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface InputBinding {
   class_2960 ANALOGUE_NOW = CUtil.rl("analogue_now");
   class_2960 ANALOGUE_PREV = CUtil.rl("analogue_prev");
   class_2960 DIGITAL_NOW = CUtil.rl("digital_now");
   class_2960 DIGITAL_PREV = CUtil.rl("digital_prev");
   class_2960 JUST_PRESSED = CUtil.rl("just_pressed");
   class_2960 JUST_RELEASED = CUtil.rl("just_released");
   class_2960 JUST_TAPPED = CUtil.rl("just_tapped");
   class_2960 GUI_PRESSED = CUtil.rl("gui_pressed");
   class_2960 KEY_EMULATION = CUtil.rl("key_emulation");

   class_2960 id();

   class_2561 name();

   class_2561 description();

   class_2561 category();

   class_2561 inputIcon();

   StateAccess createStateAccess(int var1);

   StateAccess createStateAccess(int var1, Consumer<StateAccess> var2);

   void returnStateAccess(StateAccess var1);

   @Internal
   void pushState(ControllerStateView var1);

   void fakePress();

   void setBoundInput(Input var1);

   Input boundInput();

   Input defaultInput();

   default boolean isUnbound() {
      return EmptyInput.equals(this.boundInput());
   }

   Set<BindContext> contexts();

   Optional<class_2960> radialIcon();

   float analogueNow();

   float analoguePrev();

   boolean digitalNow();

   boolean digitalPrev();

   boolean justPressed();

   boolean justReleased();

   boolean justTapped();

   GuiPressOutput guiPressed();

   <T extends DigitalOutput> T getDigitalOutput(class_2960 var1);

   <T extends DigitalOutput> T addDigitalOutput(class_2960 var1, T var2);

   <T extends AnalogueOutput> T getAnalogueOutput(class_2960 var1);

   <T extends AnalogueOutput> T addAnalogueOutput(class_2960 var1, T var2);
}
