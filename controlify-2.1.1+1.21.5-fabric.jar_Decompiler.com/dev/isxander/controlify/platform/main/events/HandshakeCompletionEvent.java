package dev.isxander.controlify.platform.main.events;

import net.minecraft.class_3248;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface HandshakeCompletionEvent<I> {
   void onCompletion(@Nullable I var1, class_3248 var2);
}
