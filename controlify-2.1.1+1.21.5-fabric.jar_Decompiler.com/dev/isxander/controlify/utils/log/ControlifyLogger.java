package dev.isxander.controlify.utils.log;

import net.minecraft.class_128;
import org.jetbrains.annotations.Contract;
import org.slf4j.Logger;

public interface ControlifyLogger {
   void log(String var1);

   void log(String var1, Object... var2);

   void log(String var1, Throwable var2);

   void log(String var1, Throwable var2, Object... var3);

   void error(String var1);

   void error(String var1, Object... var2);

   void error(String var1, Throwable var2);

   void error(String var1, Throwable var2, Object... var3);

   void warn(String var1);

   void warn(String var1, Object... var2);

   void warn(String var1, Throwable var2);

   void warn(String var1, Throwable var2, Object... var3);

   void debugLog(String var1);

   void debugLog(String var1, Object... var2);

   void debugLog(String var1, Throwable var2);

   void debugLog(String var1, Throwable var2, Object... var3);

   void debugError(String var1);

   void debugError(String var1, Object... var2);

   void debugError(String var1, Throwable var2);

   void debugError(String var1, Throwable var2, Object... var3);

   void debugWarn(String var1);

   void debugWarn(String var1, Object... var2);

   void debugWarn(String var1, Throwable var2);

   void debugWarn(String var1, Throwable var2, Object... var3);

   void crashReport(class_128 var1);

   @Contract("false, _ -> fail")
   void validateIsTrue(boolean var1, String var2);

   String export();

   ControlifyLogger createSubLogger(String var1);

   static ControlifyLogger createMasterLogger(Logger logger) {
      return new ControlifyMasterLogger(logger);
   }
}
