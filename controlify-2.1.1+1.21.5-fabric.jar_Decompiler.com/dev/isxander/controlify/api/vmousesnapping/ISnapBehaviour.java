package dev.isxander.controlify.api.vmousesnapping;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

public interface ISnapBehaviour {
   default void controlify$collectSnapPoints(Consumer<SnapPoint> consumer) {
      Iterator var2 = this.getSnapPoints().iterator();

      while(var2.hasNext()) {
         SnapPoint point = (SnapPoint)var2.next();
         consumer.accept(point);
      }

   }

   /** @deprecated */
   @Deprecated
   default Set<SnapPoint> getSnapPoints() {
      return Set.of();
   }
}
