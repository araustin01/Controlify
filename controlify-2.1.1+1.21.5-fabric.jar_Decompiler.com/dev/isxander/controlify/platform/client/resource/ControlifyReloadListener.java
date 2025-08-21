package dev.isxander.controlify.platform.client.resource;

import java.util.Collection;
import java.util.Collections;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.class_2960;
import net.minecraft.class_3302;

public interface ControlifyReloadListener extends class_3302, IdentifiableResourceReloadListener {
   class_2960 getReloadId();

   default Collection<class_2960> getDependencies() {
      return Collections.emptyList();
   }

   default class_2960 getFabricId() {
      return this.getReloadId();
   }

   default Collection<class_2960> getFabricDependencies() {
      return this.getDependencies();
   }
}
