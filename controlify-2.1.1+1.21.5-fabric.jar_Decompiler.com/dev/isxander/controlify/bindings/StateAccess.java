package dev.isxander.controlify.bindings;

public interface StateAccess {
   float analogue(int var1);

   boolean digital(int var1);

   boolean isSuppressed();

   boolean isValid();

   int maxHistory();
}
