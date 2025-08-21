package dev.isxander.controlify.reacharound;

import dev.isxander.controlify.Controlify;
import dev.isxander.yacl3.api.NameableEnum;
import java.util.function.BiFunction;
import net.minecraft.class_2561;
import net.minecraft.class_310;

public enum ReachAroundMode implements NameableEnum {
   OFF((minecraft, controlify) -> {
      return false;
   }),
   SINGLEPLAYER_ONLY((minecraft, controlify) -> {
      return minecraft.method_47392();
   }),
   SINGLEPLAYER_AND_LAN((minecraft, controlify) -> {
      return minecraft.method_1542();
   }),
   EVERYWHERE((minecraft, controlify) -> {
      return true;
   });

   private final BiFunction<class_310, Controlify, Boolean> canReachAround;
   private final class_2561 displayName;

   private ReachAroundMode(BiFunction<class_310, Controlify, Boolean> canReachAround) {
      this.canReachAround = canReachAround;
      this.displayName = class_2561.method_43471("controlify.reach_around." + this.name().toLowerCase());
   }

   public boolean canReachAround() {
      return (Boolean)this.canReachAround.apply(class_310.method_1551(), Controlify.instance());
   }

   public class_2561 getDisplayName() {
      return this.displayName;
   }

   // $FF: synthetic method
   private static ReachAroundMode[] $values() {
      return new ReachAroundMode[]{OFF, SINGLEPLAYER_ONLY, SINGLEPLAYER_AND_LAN, EVERYWHERE};
   }
}
