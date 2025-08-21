package dev.isxander.splitscreen.client;

import com.mojang.datafixers.util.Either;
import io.netty.buffer.ByteBuf;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.class_8030;
import net.minecraft.class_9135;
import net.minecraft.class_9139;

public interface SplitscreenPosition {
   SplitscreenPosition.Visible FULL = new SplitscreenPosition.Visible(0, 0, 1, 1);
   SplitscreenPosition.Hidden HIDDEN = SplitscreenPosition.Hidden.INSTANCE;
   SplitscreenPosition.Visible LEFT = new SplitscreenPosition.Visible(0, 0, 2, 1);
   SplitscreenPosition.Visible RIGHT = new SplitscreenPosition.Visible(1, 0, 2, 1);
   SplitscreenPosition.Visible[] LEFT_RIGHT = new SplitscreenPosition.Visible[]{LEFT, RIGHT};
   SplitscreenPosition.Visible TOP = new SplitscreenPosition.Visible(0, 0, 1, 2);
   SplitscreenPosition.Visible BOTTOM = new SplitscreenPosition.Visible(0, 1, 1, 2);
   SplitscreenPosition.Visible[] TOP_BOTTOM = new SplitscreenPosition.Visible[]{TOP, BOTTOM};
   SplitscreenPosition.Visible TOP_LEFT = new SplitscreenPosition.Visible(0, 0, 2, 2);
   SplitscreenPosition.Visible TOP_RIGHT = new SplitscreenPosition.Visible(1, 0, 2, 2);
   SplitscreenPosition.Visible BOTTOM_LEFT = new SplitscreenPosition.Visible(0, 1, 2, 2);
   SplitscreenPosition.Visible BOTTOM_RIGHT = new SplitscreenPosition.Visible(1, 1, 2, 2);
   SplitscreenPosition.Visible[] FOUR_WAY = new SplitscreenPosition.Visible[]{TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT};
   SplitscreenPosition.Visible LEFT_THIRD = new SplitscreenPosition.Visible(0, 0, 3, 1);
   SplitscreenPosition.Visible CENTER_THIRD = new SplitscreenPosition.Visible(1, 0, 3, 1);
   SplitscreenPosition.Visible RIGHT_THIRD = new SplitscreenPosition.Visible(2, 0, 3, 1);
   SplitscreenPosition.Visible[] LEFT_CENTER_RIGHT = new SplitscreenPosition.Visible[]{LEFT_THIRD, CENTER_THIRD, RIGHT_THIRD};
   SplitscreenPosition.Visible[] LEFT_TOP_BOTTOM = new SplitscreenPosition.Visible[]{LEFT, TOP_RIGHT, BOTTOM_RIGHT};
   SplitscreenPosition.Visible[] LEFT_RIGHT_BOTTOM = new SplitscreenPosition.Visible[]{TOP_LEFT, TOP_RIGHT, BOTTOM};
   class_9139<ByteBuf, SplitscreenPosition> STREAM_CODEC = class_9135.method_57995(SplitscreenPosition.Visible.STREAM_CODEC, SplitscreenPosition.Hidden.STREAM_CODEC).method_56432((either) -> {
      return (SplitscreenPosition)either.map(Function.identity(), Function.identity());
   }, (pos) -> {
      Objects.requireNonNull(pos);
      int index$1 = 0;
      Either var10000;
      switch(pos.typeSwitch<invokedynamic>(pos, index$1)) {
      case 0:
         SplitscreenPosition.Visible visible = (SplitscreenPosition.Visible)pos;
         var10000 = Either.left(visible);
         break;
      case 1:
         SplitscreenPosition.Hidden ignored = (SplitscreenPosition.Hidden)pos;
         var10000 = Either.right(HIDDEN);
         break;
      default:
         throw new MatchException((String)null, (Throwable)null);
      }

      return var10000;
   });

   public static record Visible(int x, int y, int width, int height, int cellCountX, int cellCountY) implements SplitscreenPosition {
      public static final class_9139<ByteBuf, SplitscreenPosition.Visible> STREAM_CODEC;

      public Visible(int x, int y, int cellCountX, int cellCountY) {
         this(x, y, 1, 1, cellCountX, cellCountY);
      }

      public Visible(int x, int y, int width, int height, int cellCountX, int cellCountY) {
         this.x = x;
         this.y = y;
         this.width = width;
         this.height = height;
         this.cellCountX = cellCountX;
         this.cellCountY = cellCountY;
      }

      public class_8030 applyToRealDims(int x, int y, int width, int height) {
         int quadrantWidth = width / this.cellCountX;
         int quadrantHeight = height / this.cellCountY;
         int realX = x + this.x * quadrantWidth;
         int realY = y + this.y * quadrantHeight;
         return new class_8030(realX, realY, quadrantWidth, quadrantHeight);
      }

      public static SplitscreenPosition.Visible[] arrangeInGridForN(int n) {
         int cellCountX = (int)Math.ceil(Math.sqrt((double)n));
         int cellCountY = (int)Math.ceil((double)n / (double)cellCountX);
         SplitscreenPosition.Visible[] positions = new SplitscreenPosition.Visible[n];

         for(int i = 0; i < n; ++i) {
            int x = i % cellCountX;
            int y = i / cellCountX;
            positions[i] = new SplitscreenPosition.Visible(x, y, cellCountX, cellCountY);
         }

         return positions;
      }

      public int x() {
         return this.x;
      }

      public int y() {
         return this.y;
      }

      public int width() {
         return this.width;
      }

      public int height() {
         return this.height;
      }

      public int cellCountX() {
         return this.cellCountX;
      }

      public int cellCountY() {
         return this.cellCountY;
      }

      static {
         STREAM_CODEC = class_9139.method_58025(class_9135.field_49675, SplitscreenPosition.Visible::x, class_9135.field_49675, SplitscreenPosition.Visible::y, class_9135.field_49675, SplitscreenPosition.Visible::width, class_9135.field_49675, SplitscreenPosition.Visible::height, class_9135.field_49675, SplitscreenPosition.Visible::cellCountX, class_9135.field_49675, SplitscreenPosition.Visible::cellCountY, SplitscreenPosition.Visible::new);
      }
   }

   public static record Hidden() implements SplitscreenPosition {
      private static final SplitscreenPosition.Hidden INSTANCE = new SplitscreenPosition.Hidden();
      public static final class_9139<ByteBuf, SplitscreenPosition.Hidden> STREAM_CODEC;

      static {
         STREAM_CODEC = class_9139.method_56431(INSTANCE);
      }
   }
}
