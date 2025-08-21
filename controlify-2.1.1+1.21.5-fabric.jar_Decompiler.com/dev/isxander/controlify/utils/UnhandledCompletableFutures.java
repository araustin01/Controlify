package dev.isxander.controlify.utils;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;
import net.minecraft.class_148;
import net.minecraft.class_310;

public final class UnhandledCompletableFutures {
   public static CompletableFuture<Void> run(Runnable runnable, class_310 executor) {
      return supply(() -> {
         runnable.run();
         return null;
      }, executor);
   }

   public static <T> CompletableFuture<T> supply(Supplier<T> supplier, class_310 executor) {
      CompletableFuture<T> future = new CompletableFuture();
      executor.method_63588(() -> {
         try {
            future.complete(supplier.get());
         } catch (Exception var3) {
            future.completeExceptionally(var3);
            if (var3 instanceof class_148) {
               throw var3;
            }
         }

      });
      return future;
   }
}
