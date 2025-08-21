package dev.isxander.controlify.utils.log;

import dev.isxander.controlify.debug.DebugProperties;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

public class ControlifyMasterLogger extends AbstractControlifyLogger {
   private final Logger logger;

   public ControlifyMasterLogger(Logger logger) {
      this.logger = logger;
   }

   protected void log0(String message, Object[] args, @Nullable Throwable throwable, boolean debug, LogLevel level) {
      super.log0(message, args, throwable, debug, level);
      if (!debug || DebugProperties.DEBUG_LOGGING) {
         if (throwable != null) {
            Object[] newArgs = new Object[args.length + 1];
            System.arraycopy(args, 0, newArgs, 0, args.length);
            newArgs[args.length] = throwable;
            args = newArgs;
         }

         switch(level) {
         case INFO:
            this.logger.info(message, args);
            break;
         case WARN:
            this.logger.warn(message, args);
            break;
         case ERROR:
            this.logger.error(message, args);
         }
      }

   }

   public ControlifyLogger createSubLogger(String name) {
      return new ControlifySubLoggerImpl(this, name);
   }
}
