package dev.isxander.controlify.controller.impl;

import com.google.common.collect.ImmutableMap;
import dev.isxander.controlify.controller.ECSComponent;
import dev.isxander.controlify.controller.ECSEntity;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import net.minecraft.class_2960;

public class ECSEntityImpl implements ECSEntity {
   private final Map<class_2960, ECSComponent> components = new HashMap();

   public <T extends ECSComponent> Optional<T> getComponent(class_2960 id) {
      return Optional.ofNullable((ECSComponent)this.components.get(id));
   }

   public <T extends ECSComponent> boolean setComponent(T component) {
      return this.components.put(component.id(), component) != null;
   }

   public boolean removeComponent(class_2960 id) {
      return this.components.remove(id) != null;
   }

   public Map<class_2960, ECSComponent> getAllComponents() {
      return ImmutableMap.copyOf(this.components);
   }
}
