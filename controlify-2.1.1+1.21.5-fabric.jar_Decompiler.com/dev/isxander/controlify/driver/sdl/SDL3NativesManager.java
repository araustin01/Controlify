package dev.isxander.controlify.driver.sdl;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.config.ControlifyConfig;
import dev.isxander.controlify.debug.DebugProperties;
import dev.isxander.controlify.gui.screen.DownloadingSDLScreen;
import dev.isxander.controlify.platform.main.PlatformMainUtil;
import dev.isxander.controlify.utils.CUtil;
import dev.isxander.controlify.utils.Platform;
import dev.isxander.controlify.utils.TrackingBodySubscriber;
import dev.isxander.controlify.utils.TrackingConsumer;
import dev.isxander.controlify.utils.log.ControlifyLogger;
import dev.isxander.sdl3java.api.SdlInit;
import dev.isxander.sdl3java.api.error.SdlError;
import dev.isxander.sdl3java.api.hints.SdlHints;
import dev.isxander.sdl3java.api.version.SdlVersion;
import dev.isxander.sdl3java.api.version.SdlVersionRecord;
import dev.isxander.sdl3java.jna.SdlNativeLibraryLoader;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse.BodyHandler;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import net.minecraft.class_156;
import net.minecraft.class_310;
import org.apache.commons.codec.digest.DigestUtils;

public class SDL3NativesManager {
   private static final String SDL3_VERSION = String.valueOf(getJavaBindingsVersion()) + ".075c033";
   private static final Map<SDL3NativesManager.Target, SDL3NativesManager.NativeFileInfo> NATIVE_LIBRARIES;
   private static final String NATIVE_LIBRARY_URL;
   private static boolean loaded;
   private static boolean attemptedLoad;
   private static CompletableFuture<Boolean> initFuture;
   private static final ControlifyLogger logger;

   public static CompletableFuture<Boolean> maybeLoad() {
      if (initFuture != null) {
         return initFuture;
      } else if (!Controlify.instance().config().globalSettings().loadVibrationNatives) {
         return initFuture = CompletableFuture.completedFuture(false);
      } else if (attemptedLoad) {
         return initFuture = CompletableFuture.completedFuture(loaded);
      } else {
         attemptedLoad = true;
         if (tryOfflineLoadAndStart()) {
            return initFuture = CompletableFuture.completedFuture(true);
         } else if (!isSupportedOnThisPlatform()) {
            CUtil.LOGGER.warn("No native library for current platform, skipping SDL3 load");
            return initFuture = CompletableFuture.completedFuture(false);
         } else {
            Path nativesFolder = getNativesFolderPath();
            Path localLibraryPath = nativesFolder.resolve(SDL3NativesManager.Target.CURRENT.getArtifactName());
            Path checksumPath = nativesFolder.resolve(SDL3NativesManager.Target.CURRENT.getArtifactMD5Name());
            if (Files.exists(localLibraryPath, new LinkOption[0])) {
               if (Files.notExists(checksumPath, new LinkOption[0])) {
                  logger.log("Downloading checksum for existing SDL natives");
                  downloadChecksum(checksumPath);
               }

               if (verifyFileMd5(localLibraryPath, checksumPath, true) && loadAndStart(localLibraryPath)) {
                  return initFuture = CompletableFuture.completedFuture(true);
               }

               CUtil.LOGGER.warn("Failed to load SDL3 from local file, attempting to re-download");
            }

            return initFuture = downloadAndStart(localLibraryPath);
         }
      }
   }

   public static boolean tryOfflineLoadAndStart() {
      if (initFuture != null) {
         throw new IllegalStateException("Tried to start offline mode but initialization already in progress.");
      } else {
         String path = "SDL3";
         if (CUtil.IS_POJAV_LAUNCHER) {
            logger.log("Detected PojavLauncher.");
            String nativesFolderName = System.getenv("POJAV_NATIVEDIR");
            Path libsLocation = Path.of(nativesFolderName, new String[0]).toAbsolutePath();
            path = libsLocation.resolve("libSDL3.so").toString();
         }

         try {
            SdlNativeLibraryLoader.loadLibSDL3FromFilePathNow(path);
         } catch (UnsatisfiedLinkError var4) {
            if (CUtil.IS_POJAV_LAUNCHER) {
               logger.error("Failed to find SDL3, even though PojavLauncher should provide it. Is it up to date?");
            }

            return false;
         }

         initFuture = new CompletableFuture();

         try {
            startSDL3();
            loaded = true;
            initFuture.complete(true);
            return true;
         } catch (Throwable var3) {
            CUtil.LOGGER.error("Failed to start SDL3", var3);
            initFuture.complete(false);
            return false;
         }
      }
   }

   private static boolean loadAndStart(Path localLibraryPath) {
      try {
         if (!verifyJarMd5(localLibraryPath)) {
            throw new IllegalStateException("SDL3 native library jar checksum did not match.");
         } else {
            SdlNativeLibraryLoader.loadLibSDL3FromFilePathNow(localLibraryPath.toAbsolutePath().toString());
            startSDL3();
            loaded = true;
            return true;
         }
      } catch (Throwable var2) {
         CUtil.LOGGER.error("Failed to start SDL3", var2);
         return false;
      }
   }

   private static void startSDL3() {
      SdlHints.SDL_SetHint("SDL_JOYSTICK_HIDAPI", "1");
      SdlHints.SDL_SetHint("SDL_JOYSTICK_ENHANCED_REPORTS", "1");
      SdlHints.SDL_SetHint("SDL_JOYSTICK_HIDAPI_STEAM", "1");
      SdlHints.SDL_SetHint("SDL_JOYSTICK_ROG_CHAKRAM", "1");
      SdlHints.SDL_SetHint("SDL_JOYSTICK_ALLOW_BACKGROUND_EVENTS", "1");
      SdlHints.SDL_SetHint("SDL_JOYSTICK_LINUX_DEADZONES", "1");
      SdlVersionRecord nativesVersion = SdlVersionRecord.fromPacked(SdlVersion.SDL_GetVersion());
      SdlVersionRecord javaVersion = SdlVersion.SDL_GetJavaBindingsVersion();
      logger.log("Loading SDL3 version: {}. Java bindings targeting: {}", nativesVersion, javaVersion);
      if (!nativesVersion.equals(javaVersion)) {
         logger.warn("SDL3 NATIVE LIBRARY VERSION MISMATCH! Java bindings are targeting a different version of SDL3 than the loaded native library. This may cause issues.");
      }

      if (!SdlInit.SDL_Init(25104)) {
         CUtil.LOGGER.error("Failed to initialise SDL3: {}", SdlError.SDL_GetError());
         throw new RuntimeException("Failed to initialise SDL3: " + SdlError.SDL_GetError());
      } else {
         logger.log("Successfully initialised SDL subsystems");
      }
   }

   private static CompletableFuture<Boolean> downloadAndStart(Path localLibraryPath) {
      return downloadLibrary(localLibraryPath.getParent()).thenCompose((success) -> {
         return !success ? CompletableFuture.completedFuture(false) : CompletableFuture.completedFuture(loadAndStart(localLibraryPath));
      }).thenCompose((success) -> {
         return class_310.method_1551().method_5385(() -> {
            return success;
         });
      });
   }

   private static CompletableFuture<Boolean> downloadLibrary(Path targetFolder) {
      String artifactName = SDL3NativesManager.Target.CURRENT.getArtifactName();
      String md5Name = SDL3NativesManager.Target.CURRENT.getArtifactMD5Name();
      Path artifactPath = targetFolder.resolve(artifactName);
      Path md5Path = targetFolder.resolve(md5Name);

      try {
         Files.deleteIfExists(artifactPath);
         Files.deleteIfExists(md5Path);
         Files.createDirectories(targetFolder);
         Files.createFile(artifactPath);
         Files.createFile(md5Path);
      } catch (Exception var14) {
         CUtil.LOGGER.error("Failed to delete existing SDL3 native library file", (Throwable)var14);
         return CompletableFuture.completedFuture(false);
      }

      String url = NATIVE_LIBRARY_URL + artifactName;
      String md5Url = NATIVE_LIBRARY_URL + md5Name;
      HttpClient httpClient = HttpClient.newHttpClient();
      HttpRequest libRequest = HttpRequest.newBuilder(URI.create(url)).build();
      HttpRequest hashRequest = HttpRequest.newBuilder(URI.create(md5Url)).build();
      class_310 minecraft = class_310.method_1551();
      DownloadingSDLScreen downloadScreen = new DownloadingSDLScreen(minecraft.field_1755, 0L, artifactPath);
      minecraft.method_1507(downloadScreen);
      CompletableFuture<?> libFuture = downloadTracked(httpClient, libRequest, downloadScreen, targetFolder, minecraft);
      CompletableFuture<?> hashFuture = downloadTracked(httpClient, hashRequest, downloadScreen, targetFolder, minecraft);
      return CompletableFuture.allOf(libFuture, hashFuture).handle((response, throwable) -> {
         if (throwable != null) {
            CUtil.LOGGER.error("Failed to download SDL3 native library", throwable);
            return false;
         } else {
            CUtil.LOGGER.log("Finished downloading SDL3 native library");
            Objects.requireNonNull(downloadScreen);
            minecraft.execute(downloadScreen::finishDownload);
            return verifyFileMd5(artifactPath, md5Path, true);
         }
      });
   }

   private static boolean verifyMd5(Path filePath, InputStream md5Hash) {
      try {
         String fileMd5 = DigestUtils.md5Hex(Files.newInputStream(filePath));
         String checksum = (new String(md5Hash.readAllBytes())).trim();
         if (!fileMd5.equals(checksum)) {
            throw new Exception("Checksum did not match");
         } else {
            return true;
         }
      } catch (Exception var4) {
         CUtil.LOGGER.error("Failed to verify checksum for " + String.valueOf(filePath), (Throwable)var4);
         return false;
      }
   }

   private static boolean verifyFileMd5(Path localLibraryPath, Path checksumPath, boolean deleteOnFail) {
      try {
         InputStream md5Stream = Files.newInputStream(checksumPath);

         boolean var5;
         try {
            boolean verified = verifyMd5(localLibraryPath, md5Stream);
            if (!verified && deleteOnFail) {
               Files.deleteIfExists(localLibraryPath);
            }

            var5 = verified;
         } catch (Throwable var7) {
            if (md5Stream != null) {
               try {
                  md5Stream.close();
               } catch (Throwable var6) {
                  var7.addSuppressed(var6);
               }
            }

            throw var7;
         }

         if (md5Stream != null) {
            md5Stream.close();
         }

         return var5;
      } catch (IOException var8) {
         CUtil.LOGGER.error("Failed to read SDL3 native library checksum", (Throwable)var8);
         return false;
      }
   }

   private static boolean verifyJarMd5(Path localLibraryPath) {
      if (!DebugProperties.USE_JAR_CHECKSUM) {
         if (!PlatformMainUtil.isDevEnv()) {
            CUtil.LOGGER.warn("Jar checksum verification is disabled in production environment. Only enable this setting if you really know what you're doing. You're leaving yourself open to security vulnerabilities.");
         }

         return true;
      } else {
         String md5Name = SDL3NativesManager.Target.CURRENT.getArtifactMD5Name();

         try {
            InputStream md5Stream = SDL3NativesManager.class.getResourceAsStream("/sdl3-hashes/" + md5Name);

            boolean var3;
            label58: {
               try {
                  if (md5Stream == null) {
                     CUtil.LOGGER.error("Failed to find SDL3 native library checksum in jar");
                     var3 = false;
                     break label58;
                  }

                  var3 = verifyMd5(localLibraryPath, md5Stream);
               } catch (Throwable var6) {
                  if (md5Stream != null) {
                     try {
                        md5Stream.close();
                     } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                     }
                  }

                  throw var6;
               }

               if (md5Stream != null) {
                  md5Stream.close();
               }

               return var3;
            }

            if (md5Stream != null) {
               md5Stream.close();
            }

            return var3;
         } catch (IOException var7) {
            CUtil.LOGGER.error("Failed to read SDL3 native library checksum from jar", (Throwable)var7);
            return false;
         }
      }
   }

   private static CompletableFuture<?> downloadTracked(HttpClient client, HttpRequest request, DownloadingSDLScreen downloadScreen, Path folder, class_310 minecraft) {
      BodyHandler var10002 = BodyHandlers.ofFileDownload(folder, new OpenOption[]{StandardOpenOption.WRITE});
      Objects.requireNonNull(downloadScreen);
      return client.sendAsync(request, TrackingBodySubscriber.bodyHandler(var10002, new TrackingConsumer(downloadScreen::increaseTotal, (received, total) -> {
         downloadScreen.updateDownloadProgress(received);
      }, (error) -> {
         if (error.isPresent()) {
            CUtil.LOGGER.error("Failed to download SDL3 native library", (Throwable)error.get());
            minecraft.execute(() -> {
               downloadScreen.failDownload((Throwable)error.get());
            });
         }

      })));
   }

   private static void downloadChecksum(Path checksumPath) {
      try {
         Path nativesFolder = checksumPath.getParent();
         Files.deleteIfExists(checksumPath);
         Files.createDirectories(nativesFolder);
         Files.createFile(checksumPath);
         HttpClient client = HttpClient.newHttpClient();
         String var10000 = NATIVE_LIBRARY_URL;
         HttpRequest request = HttpRequest.newBuilder(URI.create(var10000 + SDL3NativesManager.Target.CURRENT.getArtifactMD5Name())).build();
         client.send(request, BodyHandlers.ofFileDownload(nativesFolder, new OpenOption[]{StandardOpenOption.WRITE}));
      } catch (Exception var4) {
         CUtil.LOGGER.error("Failed to download checksum", (Throwable)var4);
      }

   }

   public static boolean isLoaded() {
      return loaded;
   }

   public static boolean hasAttemptedLoad() {
      return attemptedLoad;
   }

   public static boolean isSupportedOnThisPlatform() {
      return SDL3NativesManager.Target.CURRENT.hasNativeLibrary();
   }

   private static Path getNativesFolderPath() {
      Path nativesFolderPath = PlatformMainUtil.getGameDir();
      ControlifyConfig config = Controlify.instance().config();
      String customPath = config.globalSettings().customVibrationNativesPath;
      if (!customPath.isEmpty()) {
         try {
            nativesFolderPath = Path.of(customPath, new String[0]);
         } catch (InvalidPathException var4) {
            CUtil.LOGGER.error("Invalid custom SDL3 native library path. Using default and resetting custom path.", (Throwable)var4);
            config.globalSettings().customVibrationNativesPath = "";
            config.save();
         }
      }

      return nativesFolderPath.resolve("controlify-natives");
   }

   private static SdlVersionRecord getJavaBindingsVersion() {
      return new SdlVersionRecord(3, 1, 9);
   }

   static {
      NATIVE_LIBRARIES = Map.of(new SDL3NativesManager.Target(Platform.WINDOWS, true, false), new SDL3NativesManager.NativeFileInfo("win32-x86-64", "windows-x86_64", "dll"), new SDL3NativesManager.Target(Platform.WINDOWS, false, false), new SDL3NativesManager.NativeFileInfo("win32-x86", "window-x86", "dll"), new SDL3NativesManager.Target(Platform.LINUX, true, false), new SDL3NativesManager.NativeFileInfo("linux-x86-64", "linux-x86_64", "so"), new SDL3NativesManager.Target(Platform.LINUX, true, true), new SDL3NativesManager.NativeFileInfo("linux-aarch64", "linux-aarch64", "so"), new SDL3NativesManager.Target(Platform.MAC, true, false), new SDL3NativesManager.NativeFileInfo("darwin-x86-64", "macos-universal", "dylib"), new SDL3NativesManager.Target(Platform.MAC, true, true), new SDL3NativesManager.NativeFileInfo("darwin-aarch64", "macos-universal", "dylib"));
      NATIVE_LIBRARY_URL = "https://maven.isxander.dev/releases/dev/isxander/libsdl4j-natives/%s/".formatted(new Object[]{SDL3_VERSION});
      loaded = false;
      attemptedLoad = false;
      logger = CUtil.LOGGER.createSubLogger("SDL3NativesManager");
   }

   public static record Target(Platform platform, boolean is64Bit, boolean isARM) {
      public static final SDL3NativesManager.Target CURRENT = (SDL3NativesManager.Target)class_156.method_656(() -> {
         Platform platform = Platform.current();
         String arch = System.getProperty("os.arch");
         boolean is64bit = arch.contains("64");
         boolean isARM = arch.contains("arm") || arch.contains("aarch");
         return new SDL3NativesManager.Target(platform, is64bit, isARM);
      });

      public Target(Platform platform, boolean is64Bit, boolean isARM) {
         this.platform = platform;
         this.is64Bit = is64Bit;
         this.isARM = isARM;
      }

      public boolean hasNativeLibrary() {
         return SDL3NativesManager.NATIVE_LIBRARIES.containsKey(this);
      }

      public String getArtifactName() {
         SDL3NativesManager.NativeFileInfo file = (SDL3NativesManager.NativeFileInfo)SDL3NativesManager.NATIVE_LIBRARIES.get(this);
         return "libsdl4j-natives-" + SDL3NativesManager.SDL3_VERSION + "-" + file.downloadSuffix + "." + file.fileExtension;
      }

      public String getArtifactMD5Name() {
         return this.getArtifactName() + ".md5";
      }

      public String formatted() {
         String var10000 = this.platform().name();
         return var10000 + " 64bit=" + this.is64Bit() + ";isARM=" + this.isARM();
      }

      public Platform platform() {
         return this.platform;
      }

      public boolean is64Bit() {
         return this.is64Bit;
      }

      public boolean isARM() {
         return this.isARM;
      }
   }

   public static record NativeFileInfo(String folderName, String downloadSuffix, String fileExtension) {
      public NativeFileInfo(String folderName, String downloadSuffix, String fileExtension) {
         this.folderName = folderName;
         this.downloadSuffix = downloadSuffix;
         this.fileExtension = fileExtension;
      }

      public Path getNativePath() {
         return this.getSearchPath().resolve(this.folderName).resolve("SDL3." + this.fileExtension);
      }

      public Path getSearchPath() {
         return PlatformMainUtil.getGameDir().resolve("controlify-natives").resolve(SDL3NativesManager.SDL3_VERSION);
      }

      public String folderName() {
         return this.folderName;
      }

      public String downloadSuffix() {
         return this.downloadSuffix;
      }

      public String fileExtension() {
         return this.fileExtension;
      }
   }
}
