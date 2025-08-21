package dev.isxander.controlify.reacharound;

import dev.isxander.controlify.Controlify;
import dev.isxander.controlify.server.ServerPolicies;
import net.minecraft.class_1297;
import net.minecraft.class_2338;
import net.minecraft.class_239;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;

public class ReachAroundHandler {
   public static class_239 getReachAroundHitResult(class_1297 entity, class_239 hitResult) {
      if (hitResult.method_17783() != class_240.field_1333) {
         return hitResult;
      } else if (!canReachAround(entity)) {
         return hitResult;
      } else {
         class_2338 supportingBlockPos = entity.method_23312();
         return (class_239)(entity.method_37908().method_8320(supportingBlockPos).method_26215() ? hitResult : new class_3965(supportingBlockPos.method_46558(), entity.method_5735(), supportingBlockPos, false));
      }
   }

   private static boolean canReachAround(class_1297 cameraEntity) {
      boolean var10000;
      switch(ServerPolicies.REACH_AROUND.getPolicy()) {
      case DISALLOWED:
         var10000 = false;
         break;
      case UNSET:
         var10000 = Controlify.instance().config().globalSettings().reachAround.canReachAround();
         break;
      case ALLOWED:
         var10000 = Controlify.instance().config().globalSettings().reachAround != ReachAroundMode.OFF;
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      boolean serverAllowed = var10000;
      return serverAllowed && cameraEntity.method_5854() == null && cameraEntity.method_36455() >= 45.0F && cameraEntity.method_24828();
   }
}
