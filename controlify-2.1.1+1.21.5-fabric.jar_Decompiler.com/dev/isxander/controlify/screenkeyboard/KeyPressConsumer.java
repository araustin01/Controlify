package dev.isxander.controlify.screenkeyboard;

public interface KeyPressConsumer {
   void acceptKeyCode(int var1, int var2, int var3);

   void acceptChar(char var1, int var2);

   static KeyPressConsumer of(KeyPressConsumer.KeyCodeConsumer keyCodeConsumer, KeyPressConsumer.CharConsumer charConsumer) {
      return new KeyPressConsumer() {
         public void acceptKeyCode(int keycode, int scancode, int modifiers) {
            keyCodeConsumer.accept(keycode, scancode, modifiers);
         }

         public void acceptChar(char codePoint, int modifiers) {
            charConsumer.accept(codePoint, modifiers);
         }
      };
   }

   public interface KeyCodeConsumer {
      void accept(int var1, int var2, int var3);
   }

   public interface CharConsumer {
      void accept(char var1, int var2);
   }
}
