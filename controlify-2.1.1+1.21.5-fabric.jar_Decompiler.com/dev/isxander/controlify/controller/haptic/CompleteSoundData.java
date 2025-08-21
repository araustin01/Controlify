package dev.isxander.controlify.controller.haptic;

import javax.sound.sampled.AudioFormat;

public record CompleteSoundData(byte[] audio, AudioFormat format) {
   public CompleteSoundData(byte[] audio, AudioFormat format) {
      this.audio = audio;
      this.format = format;
   }

   public byte[] audio() {
      return this.audio;
   }

   public AudioFormat format() {
      return this.format;
   }
}
