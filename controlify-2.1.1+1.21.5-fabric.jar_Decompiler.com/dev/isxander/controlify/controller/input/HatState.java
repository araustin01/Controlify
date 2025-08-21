package dev.isxander.controlify.controller.input;

import com.mojang.serialization.Codec;
import dev.isxander.yacl3.api.NameableEnum;
import net.minecraft.class_2561;
import net.minecraft.class_3542;
import org.jetbrains.annotations.NotNull;

public enum HatState implements NameableEnum, class_3542 {
   CENTERED,
   UP,
   RIGHT,
   DOWN,
   LEFT,
   RIGHT_UP,
   RIGHT_DOWN,
   LEFT_UP,
   LEFT_DOWN;

   public static final Codec<HatState> CODEC = class_3542.method_28140(HatState::values);

   public boolean isCentered() {
      return this == CENTERED;
   }

   public boolean isRight() {
      return this == RIGHT || this == RIGHT_UP || this == RIGHT_DOWN;
   }

   public boolean isUp() {
      return this == UP || this == RIGHT_UP || this == LEFT_UP;
   }

   public boolean isLeft() {
      return this == LEFT || this == LEFT_UP || this == LEFT_DOWN;
   }

   public boolean isDown() {
      return this == DOWN || this == RIGHT_DOWN || this == LEFT_DOWN;
   }

   public class_2561 getDisplayName() {
      return class_2561.method_43471("controlify.hat_state." + this.name().toLowerCase());
   }

   @NotNull
   public String method_15434() {
      return this.name();
   }

   // $FF: synthetic method
   private static HatState[] $values() {
      return new HatState[]{CENTERED, UP, RIGHT, DOWN, LEFT, RIGHT_UP, RIGHT_DOWN, LEFT_UP, LEFT_DOWN};
   }
}
