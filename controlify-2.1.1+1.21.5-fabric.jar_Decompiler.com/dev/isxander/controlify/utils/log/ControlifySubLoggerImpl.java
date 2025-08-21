package dev.isxander.controlify.utils.log;

import org.jetbrains.annotations.Nullable;

public class ControlifySubLoggerImpl extends AbstractControlifyLogger implements ControlifySubLogger {
   private final AbstractControlifyLogger parent;
   private final String name;

   public ControlifySubLoggerImpl(AbstractControlifyLogger parent, String name) {
      this.parent = parent;
      this.name = name;
   }

   public String name() {
      return this.name;
   }

   protected void log0(String message, Object[] args, @Nullable Throwable throwable, boolean debug, LogLevel level) {
      super.log0(this.withName(message, this.name), args, throwable, debug, level);
      this.parent.log0(this.withName(message, this.name), args, throwable, debug, level);
   }

   public ControlifyLogger createSubLogger(String name) {
      return new ControlifySubLoggerImpl(this, name);
   }

   private String withName(String message, String name) {
      return String.format("[%s] %s", name, message);
   }
}
