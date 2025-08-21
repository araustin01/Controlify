package dev.isxander.controlify.rumble;

public interface RumbleCapable {
   boolean setRumble(float var1, float var2);

   boolean supportsRumble();

   RumbleState applyRumbleSourceStrength(RumbleState var1, RumbleSource var2);
}
