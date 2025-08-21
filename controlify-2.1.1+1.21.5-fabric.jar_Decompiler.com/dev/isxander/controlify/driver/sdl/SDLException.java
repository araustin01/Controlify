package dev.isxander.controlify.driver.sdl;

import dev.isxander.sdl3java.api.error.SdlError;

public class SDLException extends RuntimeException {
   public SDLException(String message) {
      super(message);
   }

   public static SDLException useSDLError(String message) {
      return new SDLException(message + ": " + SdlError.SDL_GetError());
   }

   public static SDLException useSDLError() {
      return useSDLError("SDL error");
   }
}
