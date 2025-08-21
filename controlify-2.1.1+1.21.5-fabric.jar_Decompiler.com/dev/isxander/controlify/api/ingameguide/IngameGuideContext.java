package dev.isxander.controlify.api.ingameguide;

import dev.isxander.controlify.controller.ControllerEntity;
import net.minecraft.class_239;
import net.minecraft.class_310;
import net.minecraft.class_638;
import net.minecraft.class_746;

public record IngameGuideContext(class_310 client, class_746 player, class_638 level, class_239 hitResult, ControllerEntity controller) {
   public IngameGuideContext(class_310 client, class_746 player, class_638 level, class_239 hitResult, ControllerEntity controller) {
      this.client = client;
      this.player = player;
      this.level = level;
      this.hitResult = hitResult;
      this.controller = controller;
   }

   public class_310 client() {
      return this.client;
   }

   public class_746 player() {
      return this.player;
   }

   public class_638 level() {
      return this.level;
   }

   public class_239 hitResult() {
      return this.hitResult;
   }

   public ControllerEntity controller() {
      return this.controller;
   }
}
