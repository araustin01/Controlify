package dev.isxander.controlify.driver.steamdeck;

import dev.isxander.controlify.debug.DebugProperties;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.log.ControlifyLogger;
import dev.isxander.deckapi.api.SteamDeck;
import dev.isxander.deckapi.api.SteamDeckException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.class_2960;
import org.jetbrains.annotations.Nullable;

public final class SteamDeckUtil {
   private static final ControlifyLogger logger;
   @Nullable
   private static SteamDeck deckInstance;
   private static boolean triedToLoad;
   public static final boolean IS_STEAM_DECK;
   public static final SteamDeckMode DECK_MODE;
   public static final boolean IS_SANDBOXED;
   public static final class_2960 STEAM_DECK_NAMESPACE;

   public static Optional<SteamDeck> getDeckInstance() {
      if (triedToLoad) {
         return Optional.ofNullable(deckInstance);
      } else {
         triedToLoad = true;
         if (!DECK_MODE.isGamingMode()) {
            logger.warn("Device is not a Steam Deck or not in gaming mode, skipping Steam Deck driver initialization.");
            return Optional.empty();
         } else {
            try {
               String url = DebugProperties.STEAM_DECK_CUSTOM_CEF_URL;
               if (url == null) {
                  url = "http://127.0.0.1:8080";
               }

               deckInstance = SteamDeck.create(url);
            } catch (SteamDeckException var1) {
               logger.error("Failed to create SteamDeck instance", (Throwable)var1);
               deckInstance = null;
            }

            return Optional.ofNullable(deckInstance);
         }
      }
   }

   private static boolean isHardwareSteamDeck() {
      logger.debugLog("Checking if hardware is Steam Deck.");
      String platformName = System.getProperty("os.name");
      logger.debugLog("os.name: {}", platformName);
      boolean isLinux = "Linux".equals(platformName);
      if (!isLinux) {
         return false;
      } else {
         String kernelVersion = System.getProperty("os.version");
         logger.debugLog("os.version: {}", kernelVersion);
         boolean valveKernel = kernelVersion.contains("valve");
         if (valveKernel) {
            logger.debugLog("Detected valve kernel.");
         }

         String boardVendor = readFile("/sys/class/dmi/id/board_vendor");
         if (boardVendor == null) {
            return false;
         } else {
            logger.debugLog("Board vendor: {}", boardVendor);
            String boardName = readFile("/sys/class/dmi/id/board_name");
            if (boardName == null) {
               return false;
            } else {
               logger.debugLog("Board name: {}", boardName);
               Stream<String> validBoardNames = Stream.of("Jupiter", "Galileo");
               boolean var10000;
               if (boardVendor.contains("Valve")) {
                  Objects.requireNonNull(boardName);
                  if (validBoardNames.anyMatch(boardName::contains)) {
                     var10000 = true;
                     return var10000;
                  }
               }

               var10000 = false;
               return var10000;
            }
         }
      }
   }

   private static SteamDeckMode getSteamDeckMode() {
      if (IS_STEAM_DECK) {
         String steamDeck = System.getenv("SteamDeck");
         return steamDeck != null && steamDeck.equals("1") ? SteamDeckMode.GAMING_MODE : SteamDeckMode.DESKTOP_MODE;
      } else {
         return SteamDeckMode.NOT_STEAM_DECK;
      }
   }

   private static String readFile(String path) {
      try {
         return Files.readString(Paths.get(path));
      } catch (IOException var2) {
         return null;
      }
   }

   static {
      logger = CUtil.LOGGER.createSubLogger("SteamDeckUtil");
      triedToLoad = false;
      IS_STEAM_DECK = isHardwareSteamDeck();
      DECK_MODE = getSteamDeckMode();
      IS_SANDBOXED = "1".equals(System.getenv("container"));
      STEAM_DECK_NAMESPACE = CUtil.rl("steam_deck");
   }
}
