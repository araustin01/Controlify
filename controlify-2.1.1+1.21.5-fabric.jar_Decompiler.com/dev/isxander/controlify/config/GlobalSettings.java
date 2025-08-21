package dev.isxander.controlify.config;

import com.google.common.collect.Lists;
import com.google.gson.annotations.SerializedName;
import dev.isxander.controlify.driver.steamdeck.SteamDeckUtil;
import dev.isxander.controlify.reacharound.ReachAroundMode;
import dev.isxander.controlify.server.ServerPolicies;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.class_310;
import net.minecraft.class_465;
import net.minecraft.class_642;

public class GlobalSettings {
   public static final GlobalSettings DEFAULT = new GlobalSettings();
   public List<Class<?>> virtualMouseScreens = Lists.newArrayList(new Class[]{class_465.class});
   @SerializedName("keyboardMovement")
   public boolean alwaysKeyboardMovement = false;
   public List<String> keyboardMovementWhitelist = new ArrayList();
   public boolean outOfFocusInput = false;
   public boolean loadVibrationNatives = false;
   public String customVibrationNativesPath = "";
   public boolean vibrationOnboarded = false;
   public ReachAroundMode reachAround;
   public boolean allowServerRumble;
   public boolean uiSounds;
   public boolean notifyLowBattery;
   public boolean quietMode;
   public float ingameButtonGuideScale;
   public boolean useEnhancedSteamDeckDriver;
   public Set<String> seenServers;

   public GlobalSettings() {
      this.reachAround = ReachAroundMode.OFF;
      this.allowServerRumble = true;
      this.uiSounds = false;
      this.notifyLowBattery = true;
      this.quietMode = false;
      this.ingameButtonGuideScale = 1.0F;
      this.useEnhancedSteamDeckDriver = true;
      this.seenServers = new HashSet();
   }

   public boolean shouldUseKeyboardMovement() {
      class_642 server = class_310.method_1551().method_1558();
      boolean var2;
      if (!this.alwaysKeyboardMovement) {
         label16: {
            if (server != null) {
               Stream var10000 = this.keyboardMovementWhitelist.stream();
               String var10001 = server.field_3761;
               Objects.requireNonNull(var10001);
               if (var10000.anyMatch(var10001::endsWith)) {
                  break label16;
               }
            }

            if (!ServerPolicies.KEYBOARD_LIKE_MOVEMENT.get()) {
               var2 = false;
               return var2;
            }
         }
      }

      var2 = true;
      return var2;
   }

   public boolean isQuietMode() {
      return this.quietMode && !SteamDeckUtil.DECK_MODE.isSteamDeck();
   }
}
