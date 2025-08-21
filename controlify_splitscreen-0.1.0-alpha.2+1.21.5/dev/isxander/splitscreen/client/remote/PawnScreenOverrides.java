package dev.isxander.splitscreen.client.remote;

import dev.isxander.splitscreen.client.LocalSplitscreenPawn;
import dev.isxander.splitscreen.client.SplitscreenBootstrapper;
import dev.isxander.splitscreen.client.remote.gui.PawnPauseScreen;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.class_310;
import net.minecraft.class_433;
import net.minecraft.class_437;
import org.jetbrains.annotations.Nullable;

public final class PawnScreenOverrides {
   private static boolean initialized = false;

   public static void init() {
      if (!initialized) {
         initialized = true;
         register(class_433.class, (original, minecraft, pawn) -> {
            return new PawnPauseScreen(pawn);
         });
      }
   }

   private static <T extends class_437> void register(Class<T> screenClass, PawnScreenOverrides.ScreenOverrideFactory<T> override) {
      ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
         SplitscreenBootstrapper.getPawn().ifPresent((pawn) -> {
            if (screenClass.isInstance(screen)) {
               class_437 newScreen = override.create((class_437)screenClass.cast(screen), client, pawn.getPawn());
               client.method_1507(newScreen);
            }

         });
      });
   }

   private PawnScreenOverrides() {
   }

   public interface ScreenOverrideFactory<T extends class_437> {
      @Nullable
      class_437 create(T var1, class_310 var2, LocalSplitscreenPawn var3);
   }
}
