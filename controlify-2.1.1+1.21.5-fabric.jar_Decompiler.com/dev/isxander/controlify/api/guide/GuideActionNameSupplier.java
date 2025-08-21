package dev.isxander.controlify.api.guide;

import java.util.Optional;
import net.minecraft.class_2561;

@FunctionalInterface
public interface GuideActionNameSupplier<T> {
   Optional<class_2561> supply(T var1);
}
