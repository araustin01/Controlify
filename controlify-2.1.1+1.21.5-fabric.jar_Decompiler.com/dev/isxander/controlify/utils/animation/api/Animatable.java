package dev.isxander.controlify.utils.animation.api;

import org.jetbrains.annotations.ApiStatus.Internal;

public interface Animatable {
   Animatable play();

   @Internal
   void tick(float var1);

   void skipToEnd();

   void abort();

   boolean hasStarted();

   boolean isDone();

   boolean isPlaying();

   Animatable copy();
}
