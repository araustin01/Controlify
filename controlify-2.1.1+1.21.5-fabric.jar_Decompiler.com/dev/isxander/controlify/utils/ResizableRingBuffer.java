package dev.isxander.controlify.utils;

import java.util.function.Supplier;

public class ResizableRingBuffer<T> {
   private T[] elements;
   private int head;
   private int tail;
   private int size;
   private final Supplier<T> def;

   public ResizableRingBuffer(int initialSize, Supplier<T> def) {
      this.size = initialSize;
      this.elements = new Object[initialSize];
      this.head = 0;
      this.tail = this.head + this.size - 1;
      this.def = def;
   }

   public void push(T element) {
      this.head = this.wrapIndex(this.head + 1);
      this.tail = this.wrapIndex(this.tail + 1);
      this.elements[this.tail] = element;
   }

   public T head() {
      return this.get(this.head);
   }

   public T head(int offset) {
      return this.get(this.wrapIndex(this.head + offset));
   }

   public T tail() {
      return this.get(this.tail);
   }

   public T tail(int offset) {
      return this.get(this.wrapIndex(this.tail - offset));
   }

   private T get(int index) {
      T obj = this.elements[index];
      return obj == null ? this.def.get() : obj;
   }

   private int wrapIndex(int index) {
      if (index < 0) {
         index += this.size;
      }

      if (index >= this.size) {
         index -= this.size;
      }

      return index;
   }

   public void setSize(int newSize) {
      if (this.size != newSize) {
         T[] newElements = new Object[newSize];

         for(int i = 0; i < Math.min(this.size, newSize); ++i) {
            newElements[this.size - 1 - i] = this.elements[this.wrapIndex(this.tail - i + this.size)];
         }

         this.tail = 0;
         this.head = this.size - 1;
         this.elements = newElements;
         this.size = newSize;
      }
   }

   public int size() {
      return this.size;
   }
}
