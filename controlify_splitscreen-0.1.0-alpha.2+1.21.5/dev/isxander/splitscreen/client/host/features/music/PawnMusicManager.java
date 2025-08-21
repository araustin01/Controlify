package dev.isxander.splitscreen.client.host.features.music;

import dev.isxander.splitscreen.client.SplitscreenPawn;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import java.util.Map;
import net.minecraft.class_10383;
import net.minecraft.class_5195;
import org.jetbrains.annotations.Nullable;

public class PawnMusicManager {
   private final Map<Integer, class_10383> requestedMusics = new Int2ObjectArrayMap();

   public void onRequest(@Nullable class_5195 music, float volume, SplitscreenPawn pawn) {
      this.requestedMusics.put(pawn.pawnIndex(), new class_10383(music, volume));
   }
}
