package dev.isxander.controlify.controller;

import net.minecraft.class_2960;

public class SingleValueComponent<T> implements ECSComponent {
   private final T value;
   private final class_2960 id;

   public SingleValueComponent(T value, class_2960 id) {
      this.value = value;
      this.id = id;
   }

   public T value() {
      return this.value;
   }

   public class_2960 id() {
      return this.id;
   }
}
