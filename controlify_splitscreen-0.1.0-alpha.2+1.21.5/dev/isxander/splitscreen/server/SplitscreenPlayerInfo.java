package dev.isxander.splitscreen.server;

import com.mojang.authlib.GameProfile;
import dev.isxander.splitscreen.config.SplitscreenServerSharedConfig;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import net.minecraft.class_3222;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;

public interface SplitscreenPlayerInfo {
   static Optional<SplitscreenPlayerInfo> get(class_3222 player) {
      return Optional.ofNullable(((SplitscreenPlayerInfo.SplitscreenPlayerInfoHolder)player).splitscreen$getPlayerInfo());
   }

   static Optional<SplitscreenPlayerInfo.Controller> getController(class_3222 player) {
      return get(player).filter(SplitscreenPlayerInfo::isController).map((info) -> {
         return (SplitscreenPlayerInfo.Controller)info;
      });
   }

   static Optional<SplitscreenPlayerInfo.SubPlayer> getSubPlayer(class_3222 player) {
      return get(player).filter(SplitscreenPlayerInfo::isSubPlayer).map((info) -> {
         return (SplitscreenPlayerInfo.SubPlayer)info;
      });
   }

   SplitscreenPlayerInfo.Controller controller();

   SplitscreenServerSharedConfig sharedConfig();

   boolean isController();

   boolean isSubPlayer();

   class_3222 player();

   public interface SplitscreenPlayerInfoHolder {
      @Nullable
      SplitscreenPlayerInfo splitscreen$getPlayerInfo();

      void splitscreen$setPlayerInfo(SplitscreenPlayerInfo var1);
   }

   public static record SubPlayer(GameProfile controllerProfile, SplitscreenServerSharedConfig sharedConfig, int pawnIndex, MinecraftServer server, class_3222 player) implements SplitscreenPlayerInfo {
      public SubPlayer(GameProfile controllerProfile, SplitscreenServerSharedConfig sharedConfig, int pawnIndex, MinecraftServer server, class_3222 player) {
         this.controllerProfile = controllerProfile;
         this.sharedConfig = sharedConfig;
         this.pawnIndex = pawnIndex;
         this.server = server;
         this.player = player;
      }

      public SplitscreenPlayerInfo.Controller controller() {
         return (SplitscreenPlayerInfo.Controller)SplitscreenPlayerInfo.get(this.server.method_3760().method_14602(this.controllerProfile.getId())).orElseThrow();
      }

      public boolean isController() {
         return false;
      }

      public boolean isSubPlayer() {
         return true;
      }

      public GameProfile controllerProfile() {
         return this.controllerProfile;
      }

      public SplitscreenServerSharedConfig sharedConfig() {
         return this.sharedConfig;
      }

      public int pawnIndex() {
         return this.pawnIndex;
      }

      public MinecraftServer server() {
         return this.server;
      }

      public class_3222 player() {
         return this.player;
      }
   }

   public static record Controller(GameProfile[] subPlayerProfiles, SplitscreenServerSharedConfig sharedConfig, MinecraftServer server, class_3222 player) implements SplitscreenPlayerInfo {
      public Controller(GameProfile[] subPlayerProfiles, SplitscreenServerSharedConfig sharedConfig, MinecraftServer server, class_3222 player) {
         this.subPlayerProfiles = subPlayerProfiles;
         this.sharedConfig = sharedConfig;
         this.server = server;
         this.player = player;
      }

      public SplitscreenPlayerInfo.Controller controller() {
         return this;
      }

      public GameProfile[] subPlayerProfiles() {
         return this.subPlayerProfiles;
      }

      public int subPlayerCount() {
         return this.subPlayerProfiles.length;
      }

      public List<class_3222> subPlayers() {
         return Stream.of(this.subPlayerProfiles()).map((profile) -> {
            return this.server.method_3760().method_14602(profile.getId());
         }).filter(Objects::nonNull).toList();
      }

      public List<SplitscreenPlayerInfo.SubPlayer> subPlayerInfos() {
         return this.subPlayers().stream().map(SplitscreenPlayerInfo::get).map((opt) -> {
            return (SplitscreenPlayerInfo.SubPlayer)opt.orElseThrow();
         }).toList();
      }

      public boolean isController() {
         return true;
      }

      public boolean isSubPlayer() {
         return false;
      }

      public SplitscreenServerSharedConfig sharedConfig() {
         return this.sharedConfig;
      }

      public MinecraftServer server() {
         return this.server;
      }

      public class_3222 player() {
         return this.player;
      }
   }
}
