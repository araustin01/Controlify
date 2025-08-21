package dev.isxander.controlify.controller;

import java.util.Map;
import java.util.Optional;
import net.minecraft.class_2960;

public interface ECSEntity {
   Map<class_2960, ECSComponent> getAllComponents();

   <T extends ECSComponent> boolean setComponent(T var1);

   boolean removeComponent(class_2960 var1);

   <T extends ECSComponent> Optional<T> getComponent(class_2960 var1);
}
