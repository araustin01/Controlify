package dev.isxander.splitscreen.client.features.relaunch;

import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jetbrains.annotations.Nullable;

public record RelaunchQuickPlayFormat(String ip, Optional<String> nonce) {
   public static final Pattern PATTERN = Pattern.compile("splitscreen;([^;]+);([a-zA-Z0-9]+)?");

   public RelaunchQuickPlayFormat(String ip, Optional<String> nonce) {
      this.ip = ip;
      this.nonce = nonce;
   }

   public static Either<RelaunchQuickPlayFormat, String> parse(String string) {
      Matcher matcher = PATTERN.matcher(string);
      if (matcher.matches()) {
         String ip = matcher.group(1);
         String nonce = matcher.group(2);
         return Either.left(new RelaunchQuickPlayFormat(ip, Optional.ofNullable(nonce)));
      } else {
         return Either.right(string);
      }
   }

   public static String asString(String ip, @Nullable String nonce) {
      return (new RelaunchQuickPlayFormat(ip, Optional.ofNullable(nonce))).format();
   }

   public String format() {
      String var10000 = this.ip();
      return "splitscreen;" + var10000 + ";" + (String)this.nonce().orElse("");
   }

   public String ip() {
      return this.ip;
   }

   public Optional<String> nonce() {
      return this.nonce;
   }
}
