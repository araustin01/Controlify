package dev.isxander.controlify.gui.guide;

import dev.isxander.controlify.api.bind.InputBinding;
import dev.isxander.controlify.api.guide.ActionPriority;
import dev.isxander.controlify.api.guide.GuideActionNameSupplier;
import org.jetbrains.annotations.NotNull;

public record GuideAction<T>(InputBinding binding, GuideActionNameSupplier<T> name, ActionPriority priority) implements Comparable<GuideAction<T>> {
   public GuideAction(InputBinding binding, GuideActionNameSupplier<T> name) {
      this(binding, name, ActionPriority.NORMAL);
   }

   public GuideAction(InputBinding binding, GuideActionNameSupplier<T> name, ActionPriority priority) {
      this.binding = binding;
      this.name = name;
      this.priority = priority;
   }

   public int compareTo(@NotNull GuideAction<T> o) {
      return this.priority().compareTo(o.priority());
   }

   public InputBinding binding() {
      return this.binding;
   }

   public GuideActionNameSupplier<T> name() {
      return this.name;
   }

   public ActionPriority priority() {
      return this.priority;
   }
}
