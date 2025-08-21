package dev.isxander.controlify.driver.sdl.dualsense;

import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.Structure.FieldOrder;
import dev.isxander.sdl3java.jna.JnaEnum;

@FieldOrder({"ucEnableBits1", "ucEnableBits2", "ucRumbleRight", "ucRumbleLeft", "ucHeadphoneVolume", "ucSpeakerVolume", "ucMicrophoneVolume", "ucAudioEnableBits", "ucMicLightMode", "ucAudioMuteBits", "rgucRightTriggerEffect", "rgucLeftTriggerEffect", "unknown1", "ucEnableBits3", "unknown2", "ucLedAnim", "ucLedBrightness", "ucPadLights", "ucLedRed", "ucLedGreen", "ucLedBlue"})
public class DS5EffectsState extends Structure {
   public byte ucEnableBits1;
   public byte ucEnableBits2;
   public byte ucRumbleRight;
   public byte ucRumbleLeft;
   public byte ucHeadphoneVolume;
   public byte ucSpeakerVolume;
   public byte ucMicrophoneVolume;
   public byte ucAudioEnableBits;
   public byte ucMicLightMode;
   public byte ucAudioMuteBits;
   public DS5EffectsState.TriggerEffect rgucRightTriggerEffect;
   public DS5EffectsState.TriggerEffect rgucLeftTriggerEffect;
   public byte[] unknown1 = new byte[6];
   public byte ucEnableBits3;
   public byte[] unknown2 = new byte[2];
   public byte ucLedAnim;
   public byte ucLedBrightness;
   public byte ucPadLights;
   public byte ucLedRed;
   public byte ucLedGreen;
   public byte ucLedBlue;

   public DS5EffectsState() {
   }

   public DS5EffectsState(Pointer p) {
      super(p);
   }

   public static final class MuteLightState implements JnaEnum {
      public static final byte OFF = 0;
      public static final byte ON = 1;
      public static final byte BREATHING = 2;

      public static byte fromBoolean(boolean state) {
         return (byte)(state ? 1 : 0);
      }
   }

   public static final class EnableBitFlags2 {
      public static final byte ALLOW_MUTE_LIGHT = 1;
      public static final byte ALLOW_AUDIO_MUTE = 2;
      public static final byte ALLOW_LED_COLOUR = 4;
      public static final byte RESET_LIGHTS = 8;
      public static final byte ALLOW_PLAYER_INDICATORS = 16;
      public static final byte ALLOW_HAPTIC_LOW_PASS = 32;
      public static final byte ALLOW_MOTOR_POWER_LEVEL = 64;
      public static final byte ALLOW_AUDIO_CONTROL_2 = -128;
   }

   public static final class EnableBitFlags1 {
      public static final byte ENABLE_RUMBLE_EMULATION = 1;
      public static final byte USE_RUMBLE_NOT_HAPTICS = 2;
      public static final byte ALLOW_RIGHT_TRIGGER_FFB = 4;
      public static final byte ALLOW_LEFT_TRIGGER_FFB = 8;
      public static final byte ALLOW_HEADPHONE_VOLUME = 16;
      public static final byte ALLOW_SPEAKER_VOLUME = 32;
      public static final byte ALLOW_MIC_VOLUME = 64;
      public static final byte ALLOW_AUDIO_CONTROL = -128;
   }

   @FieldOrder({"effectType", "p0", "p1", "p2", "p3", "p4", "p5", "p6", "p7", "p8", "p9"})
   public static class TriggerEffect extends Structure {
      public byte effectType;
      public byte p0;
      public byte p1;
      public byte p2;
      public byte p3;
      public byte p4;
      public byte p5;
      public byte p6;
      public byte p7;
      public byte p8;
      public byte p9;

      public TriggerEffect() {
      }

      public TriggerEffect(Pointer p) {
         super(p);
      }

      public TriggerEffect(byte effectType, byte p0, byte p1, byte p2, byte p3, byte p4, byte p5, byte p6, byte p7, byte p8, byte p9) {
         this.effectType = effectType;
         this.p0 = p0;
         this.p1 = p1;
         this.p2 = p2;
         this.p3 = p3;
         this.p4 = p4;
         this.p5 = p5;
         this.p6 = p6;
         this.p7 = p7;
         this.p8 = p8;
         this.p9 = p9;
      }

      public TriggerEffect(DualsenseTriggerEffects.EffectType effectType, byte[] params) {
         this.effectType = effectType.value;
         byte[] dest = new byte[10];
         System.arraycopy(params, 0, dest, 0, Math.min(params.length, 10));
         this.p0 = dest[0];
         this.p1 = dest[1];
         this.p2 = dest[2];
         this.p3 = dest[3];
         this.p4 = dest[4];
         this.p5 = dest[5];
         this.p6 = dest[6];
         this.p7 = dest[7];
         this.p8 = dest[8];
         this.p9 = dest[9];
      }
   }

   public static class ByValue extends DS5EffectsState implements com.sun.jna.Structure.ByValue {
   }
}
