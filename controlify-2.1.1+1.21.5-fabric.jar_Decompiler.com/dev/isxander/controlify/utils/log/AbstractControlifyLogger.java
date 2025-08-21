package dev.isxander.controlify.utils.log;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.class_128;
import net.minecraft.class_9813;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractControlifyLogger implements ControlifyLogger {
   private final Queue<LogMessage> logMessages = new ConcurrentLinkedQueue();

   public void log(String message) {
      this.log0(message, new Object[0], (Throwable)null, false, LogLevel.INFO);
   }

   public void log(String message, Object... args) {
      this.log0(message, args, (Throwable)null, false, LogLevel.INFO);
   }

   public void log(String message, Throwable throwable, Object... args) {
      this.log0(message, args, throwable, false, LogLevel.INFO);
   }

   public void log(String message, Throwable throwable) {
      this.log0(message, new Object[0], throwable, false, LogLevel.INFO);
   }

   public void warn(String message) {
      this.log0(message, new Object[0], (Throwable)null, false, LogLevel.WARN);
   }

   public void warn(String message, Object... args) {
      this.log0(message, args, (Throwable)null, false, LogLevel.WARN);
   }

   public void warn(String message, Throwable throwable) {
      this.log0(message, new Object[0], throwable, false, LogLevel.WARN);
   }

   public void warn(String message, Throwable throwable, Object... args) {
      this.log0(message, args, throwable, false, LogLevel.WARN);
   }

   public void error(String message) {
      this.log0(message, new Object[0], (Throwable)null, false, LogLevel.ERROR);
   }

   public void error(String message, Object... args) {
      this.log0(message, args, (Throwable)null, false, LogLevel.ERROR);
   }

   public void error(String message, Throwable throwable) {
      this.log0(message, new Object[0], throwable, false, LogLevel.ERROR);
   }

   public void error(String message, Throwable throwable, Object... args) {
      this.log0(message, args, throwable, false, LogLevel.ERROR);
   }

   public void debugLog(String message) {
      this.log0(message, new Object[0], (Throwable)null, true, LogLevel.INFO);
   }

   public void debugLog(String message, Object... args) {
      this.log0(message, args, (Throwable)null, true, LogLevel.INFO);
   }

   public void debugLog(String message, Throwable throwable) {
      this.log0(message, new Object[0], throwable, true, LogLevel.INFO);
   }

   public void debugLog(String message, Throwable throwable, Object... args) {
      this.log0(message, args, throwable, true, LogLevel.INFO);
   }

   public void debugWarn(String message) {
      this.log0(message, new Object[0], (Throwable)null, true, LogLevel.WARN);
   }

   public void debugWarn(String message, Object... args) {
      this.log0(message, args, (Throwable)null, true, LogLevel.WARN);
   }

   public void debugWarn(String message, Throwable throwable) {
      this.log0(message, new Object[0], throwable, true, LogLevel.WARN);
   }

   public void debugWarn(String message, Throwable throwable, Object... args) {
      this.log0(message, args, throwable, true, LogLevel.WARN);
   }

   public void debugError(String message) {
      this.log0(message, new Object[0], (Throwable)null, true, LogLevel.ERROR);
   }

   public void debugError(String message, Object... args) {
      this.log0(message, args, (Throwable)null, true, LogLevel.ERROR);
   }

   public void debugError(String message, Throwable throwable) {
      this.log0(message, new Object[0], throwable, true, LogLevel.ERROR);
   }

   public void debugError(String message, Throwable throwable, Object... args) {
      this.log0(message, args, throwable, true, LogLevel.ERROR);
   }

   public void crashReport(class_128 report) {
      this.debugError(report.method_60920(class_9813.field_52181));
   }

   public void validateIsTrue(boolean condition, String message) {
      if (!condition) {
         this.debugError("Validation failed: " + message);
         throw new AssertionError("Validation failed: " + message);
      }
   }

   protected void log0(String message, Object[] args, @Nullable Throwable throwable, boolean debug, LogLevel level) {
      this.logMessages.add(new LogMessage(message, args, throwable, debug, level));
   }

   public String export() {
      return ((StringBuilder)this.logMessages.stream().reduce(new StringBuilder(), (b, m) -> {
         return m.append(b);
      }, StringBuilder::append)).toString();
   }
}
