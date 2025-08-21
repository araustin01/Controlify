package dev.isxander.controlify.controller.impl;

import com.google.common.base.Joiner;
import com.google.common.base.Joiner.MapJoiner;
import dev.isxander.controlify.controller.input.HatState;
import dev.isxander.controlify.controller.input.ModifiableControllerState;
import it.unimi.dsi.fastutil.objects.Object2BooleanArrayMap;
import it.unimi.dsi.fastutil.objects.Object2FloatArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.class_2960;

public class ControllerStateImpl implements ModifiableControllerState {
   private final Map<class_2960, Boolean> buttons = new Object2BooleanArrayMap();
   private final Map<class_2960, Float> axes = new Object2FloatArrayMap();
   private final Map<class_2960, Float> restingAxes = new Object2FloatArrayMap();
   private final Map<class_2960, HatState> hats = new Object2ObjectArrayMap();

   public boolean isButtonDown(class_2960 button) {
      return (Boolean)this.buttons.getOrDefault(button, false);
   }

   public Set<class_2960> getButtons() {
      return this.buttons.keySet();
   }

   public float getAxisState(class_2960 axis) {
      return (Float)this.axes.getOrDefault(axis, 0.0F);
   }

   public Set<class_2960> getAxes() {
      return this.axes.keySet();
   }

   public float getAxisResting(class_2960 axis) {
      return (Float)this.restingAxes.getOrDefault(axis, 0.0F);
   }

   public HatState getHatState(class_2960 hat) {
      return (HatState)this.hats.getOrDefault(hat, HatState.CENTERED);
   }

   public Set<class_2960> getHats() {
      return this.hats.keySet();
   }

   public void setButton(class_2960 button, boolean value) {
      this.buttons.put(button, value);
   }

   public void setAxis(class_2960 axis, float value) {
      this.axes.put(axis, value);
   }

   public void setRestingAxis(class_2960 axis, float value) {
      if (!this.axes.containsKey(axis)) {
         throw new IllegalArgumentException("Cannot set resting axis for axis that doesn't exist");
      } else {
         this.restingAxes.put(axis, value);
      }
   }

   public void setHat(class_2960 hat, HatState value) {
      this.hats.put(hat, value);
   }

   public void clearState() {
      this.buttons.clear();
      this.axes.clear();
      this.restingAxes.clear();
      this.hats.clear();
   }

   public String toDebugString() {
      MapJoiner joiner = Joiner.on(",").withKeyValueSeparator("=");
      return "ControllerState{axes:%s,buttons:%s,hats:%s}".formatted(new Object[]{joiner.join(this.axes), joiner.join(this.buttons), joiner.join(this.hats)});
   }
}
