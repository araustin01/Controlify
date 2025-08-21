package dev.isxander.controlify.platform.client.resource;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.class_3300;
import net.minecraft.class_3302.class_4045;
import org.jetbrains.annotations.NotNull;

public interface SimpleControlifyReloadListener<T> extends ControlifyReloadListener {
   @NotNull
   default CompletableFuture<Void> method_25931(class_4045 helper, class_3300 manager, Executor loadExecutor, Executor applyExecutor) {
      CompletableFuture var10000 = this.load(manager, loadExecutor);
      Objects.requireNonNull(helper);
      return var10000.thenCompose(helper::method_18352).thenCompose((o) -> {
         return this.apply(o, manager, applyExecutor);
      });
   }

   CompletableFuture<T> load(class_3300 var1, Executor var2);

   CompletableFuture<Void> apply(T var1, class_3300 var2, Executor var3);
}
