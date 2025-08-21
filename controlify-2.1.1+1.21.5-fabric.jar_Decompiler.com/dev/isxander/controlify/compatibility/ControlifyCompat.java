package dev.isxander.controlify.compatibility;

import dev.isxander.controlify.compatibility.fancymenu.FancyMenuCompat;
import dev.isxander.controlify.compatibility.simplevoicechat.SimpleVoiceChatCompat;
import dev.isxander.controlify.platform.main.PlatformMainUtil;
import dev.isxander.controlify.utils.CUtil;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import net.minecraft.class_156;

public class ControlifyCompat {
   private static final Function<String, Boolean> modsLoaded = class_156.method_34866((modid) -> {
      return PlatformMainUtil.isModLoaded(modid);
   });
   private static final Set<String> disabledMods = new HashSet();
   public static final String IMMEDIATELY_FAST = "immediatelyfast";
   public static final String SIMPLE_VOICE_CHAT = "voicechat";
   public static final String FANCY_MENU = "fancymenu";

   public static void init() {
      try {
         wrapCompatCall("voicechat", SimpleVoiceChatCompat::init);
      } catch (NoClassDefFoundError var2) {
         disabledMods.add("voicechat");
      }

      try {
         wrapCompatCall("fancymenu", FancyMenuCompat::registerActions);
      } catch (NoClassDefFoundError var1) {
         disabledMods.add("fancymenu");
      }

   }

   private static void wrapCompatCall(String modid, Runnable runnable) throws NoClassDefFoundError {
      if ((Boolean)modsLoaded.apply(modid) && !disabledMods.contains(modid)) {
         try {
            runnable.run();
         } catch (Throwable var3) {
            CUtil.LOGGER.error("Failed to run compatibility code for {}, potentially unsupported version? Disabling '{}' compat for this instance.", modid, modid, var3);
            disabledMods.add(modid);
         }
      }

   }
}
