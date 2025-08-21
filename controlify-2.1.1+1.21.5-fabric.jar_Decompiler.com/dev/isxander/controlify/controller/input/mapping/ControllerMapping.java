package dev.isxander.controlify.controller.input.mapping;

import dev.isxander.controlify.controller.impl.ControllerStateImpl;
import dev.isxander.controlify.controller.input.ControllerState;
import dev.isxander.controlify.controller.input.DeadzoneGroup;
import dev.isxander.controlify.controller.input.ModifiableControllerState;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import net.minecraft.class_2960;

public record ControllerMapping(List<MappingEntry> mappings, LinkedHashMap<class_2960, DeadzoneGroup> deadzones) implements StateMapper {
   public static final ControllerMapping NO_MAPPING = (new ControllerMapping.Builder()).build();

   public ControllerMapping(List<MappingEntry> mappings, LinkedHashMap<class_2960, DeadzoneGroup> deadzones) {
      this.mappings = mappings;
      this.deadzones = deadzones;
   }

   public ControllerState mapState(ControllerState state) {
      if (this.mappings.isEmpty()) {
         return state;
      } else {
         ModifiableControllerState newState = new ControllerStateImpl();
         Iterator var3 = this.mappings.iterator();

         while(var3.hasNext()) {
            MappingEntry mapping = (MappingEntry)var3.next();
            mapping.apply(state, newState);
         }

         return newState;
      }
   }

   public List<MappingEntry> mappings() {
      return this.mappings;
   }

   public LinkedHashMap<class_2960, DeadzoneGroup> deadzones() {
      return this.deadzones;
   }

   public static class Builder {
      private final List<MappingEntry> mappings = new ArrayList();
      private final LinkedHashMap<class_2960, DeadzoneGroup> deadzones = new LinkedHashMap();

      public ControllerMapping.Builder putMapping(MappingEntry mapping) {
         if (mapping == null) {
            return this;
         } else {
            this.mappings.add(mapping);
            return this;
         }
      }

      public ControllerMapping.Builder putDeadzoneGroups(Iterable<DeadzoneGroup> deadzoneGroup) {
         Iterator var2 = deadzoneGroup.iterator();

         while(var2.hasNext()) {
            DeadzoneGroup group = (DeadzoneGroup)var2.next();
            this.deadzones.put(group.name(), group);
         }

         return this;
      }

      public ControllerMapping build() {
         return new ControllerMapping(this.mappings, this.deadzones);
      }
   }
}
