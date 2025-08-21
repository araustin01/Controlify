package dev.isxander.controlify.controller.input.mapping;

import dev.isxander.controlify.controller.input.ControllerState;
import dev.isxander.controlify.controller.input.HatState;
import dev.isxander.controlify.controller.input.ModifiableControllerState;
import dev.isxander.controlify.utils.MthExt;
import net.minecraft.class_2960;

public interface MappingEntry {
   void apply(ControllerState var1, ModifiableControllerState var2);

   MapType inputType();

   MapType outputType();

   public interface FromNothing extends MappingEntry {
      public static record ToHat(class_2960 to, MapType inputType, MapType outputType) implements MappingEntry.FromNothing {
         public ToHat(class_2960 to) {
            this(to, MapType.NOTHING, MapType.HAT);
         }

         public ToHat(class_2960 to, MapType inputType, MapType outputType) {
            this.to = to;
            this.inputType = inputType;
            this.outputType = outputType;
         }

         public void apply(ControllerState oldState, ModifiableControllerState newState) {
            newState.setHat(this.to, HatState.CENTERED);
         }

         public class_2960 to() {
            return this.to;
         }

         public MapType inputType() {
            return this.inputType;
         }

         public MapType outputType() {
            return this.outputType;
         }
      }

      public static record ToAxis(class_2960 to, float state, MapType inputType, MapType outputType) implements MappingEntry.FromNothing {
         public ToAxis(class_2960 to, float state) {
            this(to, state, MapType.NOTHING, MapType.AXIS);
         }

         public ToAxis(class_2960 to, float state, MapType inputType, MapType outputType) {
            this.to = to;
            this.state = state;
            this.inputType = inputType;
            this.outputType = outputType;
         }

         public void apply(ControllerState oldState, ModifiableControllerState newState) {
            newState.setAxis(this.to, this.state);
         }

         public class_2960 to() {
            return this.to;
         }

         public float state() {
            return this.state;
         }

         public MapType inputType() {
            return this.inputType;
         }

         public MapType outputType() {
            return this.outputType;
         }
      }

      public static record ToButton(class_2960 to, boolean state, MapType inputType, MapType outputType) implements MappingEntry.FromNothing {
         public ToButton(class_2960 to, boolean state) {
            this(to, state, MapType.NOTHING, MapType.BUTTON);
         }

         public ToButton(class_2960 to, boolean state, MapType inputType, MapType outputType) {
            this.to = to;
            this.state = state;
            this.inputType = inputType;
            this.outputType = outputType;
         }

         public void apply(ControllerState oldState, ModifiableControllerState newState) {
            newState.setButton(this.to, this.state);
         }

         public class_2960 to() {
            return this.to;
         }

         public boolean state() {
            return this.state;
         }

         public MapType inputType() {
            return this.inputType;
         }

         public MapType outputType() {
            return this.outputType;
         }
      }
   }

   public interface FromHat extends MappingEntry {
      public static record ToHat(class_2960 from, class_2960 to, MapType inputType, MapType outputType) implements MappingEntry.FromHat {
         public ToHat(class_2960 from, class_2960 to) {
            this(from, to, MapType.HAT, MapType.HAT);
         }

         public ToHat(class_2960 from, class_2960 to, MapType inputType, MapType outputType) {
            this.from = from;
            this.to = to;
            this.inputType = inputType;
            this.outputType = outputType;
         }

         public void apply(ControllerState oldState, ModifiableControllerState newState) {
            newState.setHat(this.to, oldState.getHatState(this.from));
         }

         public class_2960 from() {
            return this.from;
         }

         public class_2960 to() {
            return this.to;
         }

         public MapType inputType() {
            return this.inputType;
         }

         public MapType outputType() {
            return this.outputType;
         }
      }

      public static record ToAxis(class_2960 from, class_2960 to, HatState targetState, float onState, float offState, MapType inputType, MapType outputType) implements MappingEntry.FromHat {
         public ToAxis(class_2960 from, class_2960 to, HatState targetState, float onState, float offState) {
            this(from, to, targetState, onState, offState, MapType.HAT, MapType.AXIS);
         }

         public ToAxis(class_2960 from, class_2960 to, HatState targetState, float onState, float offState, MapType inputType, MapType outputType) {
            this.from = from;
            this.to = to;
            this.targetState = targetState;
            this.onState = onState;
            this.offState = offState;
            this.inputType = inputType;
            this.outputType = outputType;
         }

         public void apply(ControllerState oldState, ModifiableControllerState newState) {
            newState.setAxis(this.to, oldState.getHatState(this.from) == this.targetState ? this.onState : this.offState);
         }

         public class_2960 from() {
            return this.from;
         }

         public class_2960 to() {
            return this.to;
         }

         public HatState targetState() {
            return this.targetState;
         }

         public float onState() {
            return this.onState;
         }

         public float offState() {
            return this.offState;
         }

         public MapType inputType() {
            return this.inputType;
         }

         public MapType outputType() {
            return this.outputType;
         }
      }

      public static record ToButton(class_2960 from, class_2960 to, HatState targetState, MapType inputType, MapType outputType) implements MappingEntry.FromHat {
         public ToButton(class_2960 from, class_2960 to, HatState targetState) {
            this(from, to, targetState, MapType.HAT, MapType.BUTTON);
         }

         public ToButton(class_2960 from, class_2960 to, HatState targetState, MapType inputType, MapType outputType) {
            this.from = from;
            this.to = to;
            this.targetState = targetState;
            this.inputType = inputType;
            this.outputType = outputType;
         }

         public void apply(ControllerState oldState, ModifiableControllerState newState) {
            newState.setButton(this.to, oldState.getHatState(this.from) == this.targetState);
         }

         public class_2960 from() {
            return this.from;
         }

         public class_2960 to() {
            return this.to;
         }

         public HatState targetState() {
            return this.targetState;
         }

         public MapType inputType() {
            return this.inputType;
         }

         public MapType outputType() {
            return this.outputType;
         }
      }
   }

   public interface FromAxis extends MappingEntry {
      public static record ToHat(class_2960 from, class_2960 to, float threshold, HatState targetState, MapType inputType, MapType outputType) implements MappingEntry.FromAxis {
         public ToHat(class_2960 from, class_2960 to, float threshold, HatState targetState) {
            this(from, to, threshold, targetState, MapType.AXIS, MapType.HAT);
         }

         public ToHat(class_2960 from, class_2960 to, float threshold, HatState targetState, MapType inputType, MapType outputType) {
            this.from = from;
            this.to = to;
            this.threshold = threshold;
            this.targetState = targetState;
            this.inputType = inputType;
            this.outputType = outputType;
         }

         public void apply(ControllerState oldState, ModifiableControllerState newState) {
            float oldVal = oldState.getAxisState(this.from);
            newState.setHat(this.to, oldVal >= this.threshold ? this.targetState : HatState.CENTERED);
         }

         public class_2960 from() {
            return this.from;
         }

         public class_2960 to() {
            return this.to;
         }

         public float threshold() {
            return this.threshold;
         }

         public HatState targetState() {
            return this.targetState;
         }

         public MapType inputType() {
            return this.inputType;
         }

         public MapType outputType() {
            return this.outputType;
         }
      }

      public static record ToAxis(class_2960 from, class_2960 to, float minIn, float minOut, float maxIn, float maxOut, MapType inputType, MapType outputType) implements MappingEntry.FromAxis {
         public ToAxis(class_2960 from, class_2960 to, float minIn, float minOut, float maxIn, float maxOut) {
            this(from, to, minIn, minOut, maxIn, maxOut, MapType.AXIS, MapType.AXIS);
         }

         public ToAxis(class_2960 from, class_2960 to, float minIn, float minOut, float maxIn, float maxOut, MapType inputType, MapType outputType) {
            this.from = from;
            this.to = to;
            this.minIn = minIn;
            this.minOut = minOut;
            this.maxIn = maxIn;
            this.maxOut = maxOut;
            this.inputType = inputType;
            this.outputType = outputType;
         }

         public void apply(ControllerState oldState, ModifiableControllerState newState) {
            float oldVal = oldState.getAxisState(this.from);
            float newVal = MthExt.remap(oldVal, this.minIn, this.maxIn, this.minOut, this.maxOut);
            newState.setAxis(this.to, newVal);
         }

         public class_2960 from() {
            return this.from;
         }

         public class_2960 to() {
            return this.to;
         }

         public float minIn() {
            return this.minIn;
         }

         public float minOut() {
            return this.minOut;
         }

         public float maxIn() {
            return this.maxIn;
         }

         public float maxOut() {
            return this.maxOut;
         }

         public MapType inputType() {
            return this.inputType;
         }

         public MapType outputType() {
            return this.outputType;
         }
      }

      public static record ToButton(class_2960 from, class_2960 to, float threshold, MapType inputType, MapType outputType) implements MappingEntry.FromAxis {
         public ToButton(class_2960 from, class_2960 to, float threshold) {
            this(from, to, threshold, MapType.AXIS, MapType.BUTTON);
         }

         public ToButton(class_2960 from, class_2960 to, float threshold, MapType inputType, MapType outputType) {
            this.from = from;
            this.to = to;
            this.threshold = threshold;
            this.inputType = inputType;
            this.outputType = outputType;
         }

         public void apply(ControllerState oldState, ModifiableControllerState newState) {
            newState.setButton(this.to, oldState.getAxisState(this.from) >= this.threshold);
         }

         public class_2960 from() {
            return this.from;
         }

         public class_2960 to() {
            return this.to;
         }

         public float threshold() {
            return this.threshold;
         }

         public MapType inputType() {
            return this.inputType;
         }

         public MapType outputType() {
            return this.outputType;
         }
      }
   }

   public interface FromButton extends MappingEntry {
      public static record ToHat(class_2960 from, class_2960 to, HatState offState, HatState onState, MapType inputType, MapType outputType) implements MappingEntry.FromButton {
         public ToHat(class_2960 from, class_2960 to, HatState offState, HatState onState) {
            this(from, to, offState, onState, MapType.BUTTON, MapType.HAT);
         }

         public ToHat(class_2960 from, class_2960 to, HatState offState, HatState onState, MapType inputType, MapType outputType) {
            this.from = from;
            this.to = to;
            this.offState = offState;
            this.onState = onState;
            this.inputType = inputType;
            this.outputType = outputType;
         }

         public void apply(ControllerState oldState, ModifiableControllerState newState) {
            newState.setHat(this.to, oldState.isButtonDown(this.from) ? this.onState : this.offState);
         }

         public class_2960 from() {
            return this.from;
         }

         public class_2960 to() {
            return this.to;
         }

         public HatState offState() {
            return this.offState;
         }

         public HatState onState() {
            return this.onState;
         }

         public MapType inputType() {
            return this.inputType;
         }

         public MapType outputType() {
            return this.outputType;
         }
      }

      public static record ToAxis(class_2960 from, class_2960 to, float offState, float onState, MapType inputType, MapType outputType) implements MappingEntry.FromButton {
         public ToAxis(class_2960 from, class_2960 to, float offState, float onState) {
            this(from, to, offState, onState, MapType.BUTTON, MapType.AXIS);
         }

         public ToAxis(class_2960 from, class_2960 to, float offState, float onState, MapType inputType, MapType outputType) {
            this.from = from;
            this.to = to;
            this.offState = offState;
            this.onState = onState;
            this.inputType = inputType;
            this.outputType = outputType;
         }

         public void apply(ControllerState oldState, ModifiableControllerState newState) {
            newState.setAxis(this.to, oldState.isButtonDown(this.from) ? this.onState : this.offState);
         }

         public class_2960 from() {
            return this.from;
         }

         public class_2960 to() {
            return this.to;
         }

         public float offState() {
            return this.offState;
         }

         public float onState() {
            return this.onState;
         }

         public MapType inputType() {
            return this.inputType;
         }

         public MapType outputType() {
            return this.outputType;
         }
      }

      public static record ToButton(class_2960 from, class_2960 to, boolean invert, MapType inputType, MapType outputType) implements MappingEntry.FromButton {
         public ToButton(class_2960 from, class_2960 to, boolean invert) {
            this(from, to, invert, MapType.BUTTON, MapType.BUTTON);
         }

         public ToButton(class_2960 from, class_2960 to, boolean invert, MapType inputType, MapType outputType) {
            this.from = from;
            this.to = to;
            this.invert = invert;
            this.inputType = inputType;
            this.outputType = outputType;
         }

         public void apply(ControllerState oldState, ModifiableControllerState newState) {
            boolean fromState = oldState.isButtonDown(this.from);
            if (this.invert()) {
               fromState = !fromState;
            }

            newState.setButton(this.to, fromState);
         }

         public class_2960 from() {
            return this.from;
         }

         public class_2960 to() {
            return this.to;
         }

         public boolean invert() {
            return this.invert;
         }

         public MapType inputType() {
            return this.inputType;
         }

         public MapType outputType() {
            return this.outputType;
         }
      }
   }
}
