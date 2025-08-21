package dev.isxander.controlify.gui;

public record DrawSize(int width, int height) {
   public DrawSize(int width, int height) {
      this.width = width;
      this.height = height;
   }

   public int width() {
      return this.width;
   }

   public int height() {
      return this.height;
   }
}
