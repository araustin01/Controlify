package dev.isxander.controlify.utils.log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public record LogMessage(String message, Object[] args, Throwable throwable, boolean debug, LogLevel level, LocalTime time) {
   private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

   public LogMessage(String message, Object[] args, Throwable throwable, boolean debug, LogLevel level) {
      this(message, args, throwable, debug, level, LocalTime.now());
   }

   public LogMessage(String message, Object[] args, Throwable throwable, boolean debug, LogLevel level, LocalTime time) {
      this.message = message;
      this.args = args;
      this.throwable = throwable;
      this.debug = debug;
      this.level = level;
      this.time = time;
   }

   public StringBuilder append(StringBuilder stringBuilder) {
      String expandedString = this.message;
      Object[] var3 = this.args;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         Object arg = var3[var5];
         expandedString = expandedString.replaceFirst("\\{}", String.valueOf(arg));
      }

      return stringBuilder.append('[').append(formatter.format(LocalTime.now())).append(']').append(' ').append(this.level == LogLevel.ERROR ? "[ERROR]" : (this.level == LogLevel.WARN ? "[WARN]" : "")).append(' ').append(expandedString).append(this.throwable != null ? "\n" + getStacktrace(this.throwable) : "").append("\n");
   }

   private static String getStacktrace(Throwable throwable) {
      StringWriter writer = new StringWriter();
      throwable.printStackTrace(new PrintWriter(writer, true));
      return writer.toString();
   }

   public String message() {
      return this.message;
   }

   public Object[] args() {
      return this.args;
   }

   public Throwable throwable() {
      return this.throwable;
   }

   public boolean debug() {
      return this.debug;
   }

   public LogLevel level() {
      return this.level;
   }

   public LocalTime time() {
      return this.time;
   }
}
