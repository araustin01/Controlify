package dev.isxander.controlify.bindings;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.bindings.input.Input;
import dev.isxander.controlify.bindings.output.AnalogueOutput;
import dev.isxander.controlify.bindings.output.DigitalOutput;
import dev.isxander.controlify.bindings.output.GuiPressOutput;
import dev.isxander.controlify.bindings.output.JustPressedOutput;
import dev.isxander.controlify.bindings.output.JustReleasedOutput;
import dev.isxander.controlify.bindings.output.JustTappedOutput;
import dev.isxander.controlify.bindings.output.SimpleAnalogueOutput;
import dev.isxander.controlify.bindings.output.SimpleDigitalOutput;
import dev.isxander.controlify.controller.ControllerEntity;
import dev.isxander.controlify.controller.input.ControllerStateView;
import dev.isxander.controlify.controller.input.InputComponent;
import dev.isxander.controlify.utils.ResizableRingBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;

public class InputBindingImpl implements InputBinding {
   private final ControllerEntity controller;
   private final class_2960 id;
   private final class_2561 name;
   private final class_2561 description;
   private final class_2561 category;
   private Input boundInput;
   private final Supplier<Input> defaultBindSupplier;
   private final Set<BindContext> contexts;
   @Nullable
   private final class_2960 radialIcon;
   private final ResizableRingBuffer<Float> stateHistory;
   private final Set<InputBindingImpl.StateAccessImpl> borrowedAccesses;
   private boolean suppressed;
   private final AnalogueOutput analogueNow;
   private final AnalogueOutput analoguePrev;
   private final DigitalOutput digitalNow;
   private final DigitalOutput digitalThen;
   private final DigitalOutput justPressed;
   private final DigitalOutput justReleased;
   private final DigitalOutput justTapped;
   private final GuiPressOutput guiPressOutput;
   private final Map<class_2960, DigitalOutput> digitalOutputs;
   private final Map<class_2960, AnalogueOutput> analogueOutputs;
   private int fakePressState = -1;

   public InputBindingImpl(ControllerEntity controller, class_2960 id, class_2561 name, class_2561 description, class_2561 category, Supplier<Input> defaultBindSupplier, Set<BindContext> contexts, @Nullable class_2960 radialIcon) {
      this.controller = controller;
      this.id = id;
      this.name = name;
      this.description = description;
      this.category = category;
      this.stateHistory = new ResizableRingBuffer(2, () -> {
         return 0.0F;
      });
      this.boundInput = (Input)defaultBindSupplier.get();
      this.defaultBindSupplier = defaultBindSupplier;
      this.contexts = contexts;
      this.radialIcon = radialIcon;
      this.borrowedAccesses = new HashSet();
      this.digitalOutputs = new HashMap();
      this.analogueOutputs = new HashMap();
      this.analogueNow = this.addAnalogueOutput(ANALOGUE_NOW, new SimpleAnalogueOutput(this, 0));
      this.analoguePrev = this.addAnalogueOutput(ANALOGUE_PREV, new SimpleAnalogueOutput(this, 1));
      this.digitalNow = this.addDigitalOutput(DIGITAL_NOW, new SimpleDigitalOutput(this, 0));
      this.digitalThen = this.addDigitalOutput(DIGITAL_PREV, new SimpleDigitalOutput(this, 1));
      this.justPressed = this.addDigitalOutput(JUST_PRESSED, new JustPressedOutput(this));
      this.justReleased = this.addDigitalOutput(JUST_RELEASED, new JustReleasedOutput(this));
      this.justTapped = this.addDigitalOutput(JUST_TAPPED, new JustTappedOutput(this));
      this.guiPressOutput = (GuiPressOutput)this.addDigitalOutput(GUI_PRESSED, new GuiPressOutput(this));
   }

   public class_2960 id() {
      return this.id;
   }

   public class_2561 name() {
      return this.name;
   }

   public class_2561 description() {
      return this.description;
   }

   public class_2561 category() {
      return this.category;
   }

   public class_2561 inputIcon() {
      return Controlify.instance().inputFontMapper().getComponentFromInputs(this.controller.info().type().namespace(), this.boundInput.getRelevantInputs());
   }

   public StateAccess createStateAccess(int historyRequired) {
      return this.createStateAccess(historyRequired, (Consumer)null);
   }

   public StateAccess createStateAccess(int historyRequired, Consumer<StateAccess> pushEvent) {
      if (historyRequired > this.stateHistory.size()) {
         this.stateHistory.setSize(historyRequired);
      }

      InputBindingImpl.StateAccessImpl access = new InputBindingImpl.StateAccessImpl(historyRequired, pushEvent);
      this.borrowedAccesses.add(access);
      return access;
   }

   public void returnStateAccess(StateAccess stateAccess) {
      if (stateAccess instanceof InputBindingImpl.StateAccessImpl) {
         InputBindingImpl.StateAccessImpl accessImpl = (InputBindingImpl.StateAccessImpl)stateAccess;
         boolean removed = this.borrowedAccesses.remove(accessImpl);
         accessImpl.retire();
         if (removed) {
            OptionalInt newMaxSize = this.borrowedAccesses.stream().mapToInt(InputBindingImpl.StateAccessImpl::maxHistory).max();
            ResizableRingBuffer var10001 = this.stateHistory;
            Objects.requireNonNull(var10001);
            newMaxSize.ifPresent(var10001::setSize);
         }

      } else {
         throw new IllegalStateException("Unknown implementation of state access");
      }
   }

   public void pushState(ControllerStateView state) {
      if (!this.contexts.isEmpty()) {
         Set<BindContext> thisTickContexts = Controlify.instance().thisTickBindContexts();
         Stream var10001 = this.contexts.stream();
         Objects.requireNonNull(thisTickContexts);
         this.suppressed = var10001.noneMatch(thisTickContexts::contains);
      } else {
         this.suppressed = false;
      }

      float analogue = this.boundInput.state(state);
      switch(this.fakePressState) {
      case 0:
         analogue = 0.0F;
         break;
      case 1:
      case 2:
         analogue = 1.0F;
         break;
      case 3:
         analogue = 0.0F;
      }

      if (this.fakePressState >= 0) {
         if (this.fakePressState == 3) {
            this.fakePressState = -1;
         } else {
            ++this.fakePressState;
         }
      }

      this.stateHistory.push(analogue);
      this.borrowedAccesses.forEach(InputBindingImpl.StateAccessImpl::onPush);
   }

   public void fakePress() {
      this.fakePressState = 0;
   }

   public void setBoundInput(Input input) {
      this.boundInput = input;
      Controlify.instance().config().setDirty();
   }

   public Input boundInput() {
      return this.boundInput;
   }

   public Input defaultInput() {
      return (Input)this.defaultBindSupplier.get();
   }

   public Set<BindContext> contexts() {
      return this.contexts;
   }

   public Optional<class_2960> radialIcon() {
      return Optional.ofNullable(this.radialIcon);
   }

   public float analogueNow() {
      return this.analogueNow.get();
   }

   public float analoguePrev() {
      return this.analoguePrev.get();
   }

   public boolean digitalNow() {
      return this.digitalNow.get();
   }

   public boolean digitalPrev() {
      return this.digitalThen.get();
   }

   public boolean justPressed() {
      return this.justPressed.get();
   }

   public boolean justReleased() {
      return this.justReleased.get();
   }

   public boolean justTapped() {
      return this.justTapped.get();
   }

   public GuiPressOutput guiPressed() {
      return this.guiPressOutput;
   }

   public <T extends DigitalOutput> T addDigitalOutput(class_2960 id, T output) {
      this.digitalOutputs.put(id, output);
      return output;
   }

   public <T extends DigitalOutput> T getDigitalOutput(class_2960 id) {
      return (DigitalOutput)this.digitalOutputs.get(id);
   }

   public <T extends AnalogueOutput> T addAnalogueOutput(class_2960 id, T output) {
      this.analogueOutputs.put(id, output);
      return output;
   }

   public <T extends AnalogueOutput> T getAnalogueOutput(class_2960 id) {
      return (AnalogueOutput)this.analogueOutputs.get(id);
   }

   private class StateAccessImpl implements StateAccess {
      private final int maxHistory;
      private boolean valid;
      @Nullable
      private final Consumer<StateAccess> pushListener;

      public StateAccessImpl(int maxHistory, @Nullable Consumer<StateAccess> pushListener) {
         this.maxHistory = maxHistory;
         this.valid = true;
         this.pushListener = pushListener;
      }

      public float analogue(int history) {
         if (!this.valid) {
            throw new IllegalStateException("Tried to access state from returned access!");
         } else if (history > this.maxHistory) {
            throw new IllegalStateException("Overflowing history!");
         } else {
            return (Float)InputBindingImpl.this.stateHistory.tail(history);
         }
      }

      public boolean digital(int history) {
         return this.analogue(history) > ((InputComponent.Config)((InputComponent)InputBindingImpl.this.controller.input().orElseThrow()).confObj()).buttonActivationThreshold;
      }

      public boolean isSuppressed() {
         return InputBindingImpl.this.suppressed;
      }

      public boolean isValid() {
         return this.valid;
      }

      public int maxHistory() {
         return this.maxHistory;
      }

      public void onPush() {
         if (this.pushListener != null) {
            this.pushListener.accept(this);
         }

      }

      public void retire() {
         this.valid = false;
      }
   }
}
