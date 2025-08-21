package dev.isxander.splitscreen.client.ipc.packets.controllerbound.play;

import dev.isxander.splitscreen.client.host.ipc.ControllerPlayPacketListener;
import java.util.Optional;
import net.minecraft.class_2540;
import net.minecraft.class_5195;
import net.minecraft.class_9135;
import net.minecraft.class_9139;
import net.minecraft.class_9145;
import org.jetbrains.annotations.Nullable;

public record ControllerboundRequestPlayMusicPacket(Optional<class_5195> music, float volume) implements ControllerboundPlayPacket {
   public static final class_9139<class_2540, ControllerboundRequestPlayMusicPacket> CODEC;
   public static final class_9145<ControllerboundRequestPlayMusicPacket> TYPE;

   public ControllerboundRequestPlayMusicPacket(@Nullable class_5195 music, float volume) {
      this(Optional.ofNullable(music), volume);
   }

   public ControllerboundRequestPlayMusicPacket(Optional<class_5195> music, float volume) {
      this.music = music;
      this.volume = volume;
   }

   public void handle(ControllerPlayPacketListener handler) {
   }

   public class_9145<ControllerboundRequestPlayMusicPacket> method_65080() {
      return TYPE;
   }

   public Optional<class_5195> music() {
      return this.music;
   }

   public float volume() {
      return this.volume;
   }

   static {
      CODEC = class_9139.method_56435(class_9135.method_56382(class_9135.method_56368(class_5195.field_24627)), ControllerboundRequestPlayMusicPacket::music, class_9135.field_48552, ControllerboundRequestPlayMusicPacket::volume, ControllerboundRequestPlayMusicPacket::new);
      TYPE = ControllerboundPlayPacket.createType("request_play_music");
   }
}
