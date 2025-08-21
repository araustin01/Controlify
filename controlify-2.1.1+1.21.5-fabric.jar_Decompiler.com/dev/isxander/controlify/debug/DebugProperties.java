package dev.isxander.controlify.debug;

import dev.isxander.controlify.platform.main.PlatformMainUtil;
import dev.isxander.controlify.utils.CUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;

public class DebugProperties {
   private static final List<DebugProperties.DebugProperty<?>> properties = new ArrayList();
   public static final boolean DEBUG_LOGGING = boolProp("controlify.debug.logging", false, true);
   public static final boolean PRINT_VID_PID = boolProp("controlify.debug.print_vid_pid", false, true);
   public static final boolean DEBUG_SNAPPING = boolProp("controlify.debug.snapping", false, false);
   public static final boolean FORCE_JOYSTICK = boolProp("controlify.debug.force_joystick", false, false);
   public static final boolean INIT_DUMP = boolProp("controlify.debug.init_dump", false, true);
   @Nullable
   public static final String STEAM_DECK_CUSTOM_CEF_URL = strProp("controlify.debug.steam_deck_custom_cef_url", (String)null, (String)null);
   public static final boolean MIXIN_AUDIT = boolProp("controlify.debug.mixin_audit", false, false);
   public static final boolean USE_JAR_CHECKSUM = boolProp("controlify.use_jar_checksum", true, false);

   public static void printProperties() {
      if (!properties.stream().noneMatch((propx) -> {
         return propx.state() != propx.def();
      })) {
         String header = "*----------------- Controlify Debug Properties -----------------*";
         CUtil.LOGGER.error(header);
         int maxWidth = properties.stream().mapToInt((propx) -> {
            return propx.name().length();
         }).max().orElse(0);
         Iterator var2 = properties.iterator();

         while(var2.hasNext()) {
            DebugProperties.DebugProperty<?> prop = (DebugProperties.DebugProperty)var2.next();
            String line = "| %s%s = %s".formatted(new Object[]{prop.name(), " ".repeat(maxWidth - prop.name().length()), prop.state()});
            line = line + " ".repeat(header.length() - line.length() - 1) + "|";
            CUtil.LOGGER.error(line);
         }

         CUtil.LOGGER.error("*---------------------------------------------------------------*");
      }
   }

   private static boolean boolProp(String name, boolean defProd, boolean defDev) {
      boolean def = PlatformMainUtil.isDevEnv() ? defDev : defProd;
      boolean enabled = Boolean.parseBoolean(System.getProperty(name, Boolean.toString(def)));
      properties.add(new DebugProperties.DebugProperty(name, enabled, def, (b) -> {
         return Boolean.toString(b);
      }));
      return enabled;
   }

   private static String strProp(String name, String defProd, String defDev) {
      String def = PlatformMainUtil.isDevEnv() ? defDev : defProd;
      String enabled = System.getProperty(name, def);
      properties.add(new DebugProperties.DebugProperty(name, enabled, def, Function.identity()));
      return enabled;
   }

   private static record DebugProperty<T>(String name, T state, T def, Function<T, String> typeToString) {
      private DebugProperty(String name, T state, T def, Function<T, String> typeToString) {
         this.name = name;
         this.state = state;
         this.def = def;
         this.typeToString = typeToString;
      }

      public String name() {
         return this.name;
      }

      public T state() {
         return this.state;
      }

      public T def() {
         return this.def;
      }

      public Function<T, String> typeToString() {
         return this.typeToString;
      }
   }
}
