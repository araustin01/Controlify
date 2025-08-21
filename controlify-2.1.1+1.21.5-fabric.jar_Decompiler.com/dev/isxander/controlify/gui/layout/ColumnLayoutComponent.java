package dev.isxander.controlify.gui.layout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.BiFunction;
import net.minecraft.class_332;
import org.apache.commons.lang3.Validate;
import org.joml.Vector2i;
import org.joml.Vector2ic;

public class ColumnLayoutComponent<T extends RenderComponent> extends AbstractLayoutComponent<T> {
   private final int componentPaddingVertical;
   private final int colPaddingLeft;
   private final int colPaddingRight;
   private final int colPaddingTop;
   private final int colPaddingBottom;
   private final ColumnLayoutComponent.ElementPosition elementPosition;

   private ColumnLayoutComponent(Collection<? extends T> elements, int componentPaddingVertical, int colPaddingLeft, int colPaddingRight, int colPaddingTop, int colPaddingBottom, ColumnLayoutComponent.ElementPosition elementPosition) {
      Iterator var8 = elements.iterator();

      while(var8.hasNext()) {
         T element = (RenderComponent)var8.next();
         this.insertTop(element);
      }

      this.componentPaddingVertical = componentPaddingVertical;
      this.colPaddingLeft = colPaddingLeft;
      this.colPaddingRight = colPaddingRight;
      this.colPaddingTop = colPaddingTop;
      this.colPaddingBottom = colPaddingBottom;
      this.elementPosition = elementPosition;
   }

   public void render(class_332 graphics, int x, int y, float deltaTime) {
      int width = this.getMaxChildWidth();
      if (width != -1) {
         int yOffset = 0;
         Iterator var7 = this.getChildComponents().iterator();

         while(var7.hasNext()) {
            T element = (RenderComponent)var7.next();
            if (element.isVisible()) {
               element.render(graphics, x + this.colPaddingLeft + (Integer)this.elementPosition.positionFunction.apply(width, element.size().x()), y + this.colPaddingTop + yOffset, deltaTime);
               yOffset += element.size().y() + this.componentPaddingVertical;
            }
         }

      }
   }

   public Vector2ic size() {
      return new Vector2i(this.getMaxChildWidth() + this.colPaddingLeft + this.colPaddingRight, this.getSumHeight() + this.colPaddingTop + this.colPaddingBottom);
   }

   private int getSumHeight() {
      return this.getChildComponents().stream().filter(RenderComponent::isVisible).map(RenderComponent::size).mapToInt((size) -> {
         return size.y() + this.componentPaddingVertical;
      }).sum() - this.componentPaddingVertical;
   }

   private int getMaxChildWidth() {
      return this.getChildComponents().stream().filter(RenderComponent::isVisible).map(RenderComponent::size).mapToInt(Vector2ic::x).max().orElse(-1);
   }

   public static <T extends RenderComponent> ColumnLayoutComponent.Builder<T> builder() {
      return new ColumnLayoutComponent.Builder();
   }

   public static enum ElementPosition {
      LEFT((rowWidth, elementWidth) -> {
         return 0;
      }),
      RIGHT((rowWidth, elementWidth) -> {
         return rowWidth - elementWidth;
      }),
      MIDDLE((rowWidth, elementWidth) -> {
         return rowWidth / 2 - elementWidth / 2;
      });

      public final BiFunction<Integer, Integer, Integer> positionFunction;

      private ElementPosition(BiFunction<Integer, Integer, Integer> positionFunction) {
         this.positionFunction = positionFunction;
      }

      // $FF: synthetic method
      private static ColumnLayoutComponent.ElementPosition[] $values() {
         return new ColumnLayoutComponent.ElementPosition[]{LEFT, RIGHT, MIDDLE};
      }
   }

   public static class Builder<T extends RenderComponent> {
      private final List<T> elements = new ArrayList();
      private int componentPaddingVertical;
      private int colPaddingLeft;
      private int colPaddingRight;
      private int colPaddingTop;
      private int colPaddingBottom;
      private ColumnLayoutComponent.ElementPosition elementPosition = null;

      public ColumnLayoutComponent.Builder<T> element(T element) {
         this.elements.add(element);
         return this;
      }

      public ColumnLayoutComponent.Builder<T> elements(T... elements) {
         this.elements.addAll(Arrays.asList(elements));
         return this;
      }

      public ColumnLayoutComponent.Builder<T> elements(Collection<? extends T> elements) {
         this.elements.addAll(elements);
         return this;
      }

      public ColumnLayoutComponent.Builder<T> spacing(int padding) {
         this.componentPaddingVertical = padding;
         return this;
      }

      public ColumnLayoutComponent.Builder<T> colPadding(int left, int right, int top, int bottom) {
         this.colPaddingLeft = left;
         this.colPaddingRight = right;
         this.colPaddingTop = top;
         this.colPaddingBottom = bottom;
         return this;
      }

      public ColumnLayoutComponent.Builder<T> colPadding(int horizontal, int vertical) {
         return this.colPadding(horizontal, horizontal, vertical, vertical);
      }

      public ColumnLayoutComponent.Builder<T> colPadding(int padding) {
         return this.colPadding(padding, padding, padding, padding);
      }

      public ColumnLayoutComponent.Builder<T> elementPosition(ColumnLayoutComponent.ElementPosition elementPosition) {
         this.elementPosition = elementPosition;
         return this;
      }

      public ColumnLayoutComponent<T> build() {
         Validate.notEmpty(this.elements, "No elements were added to the column!", new Object[0]);
         Validate.notNull(this.elementPosition, "Element position cannot be null!", new Object[0]);
         return new ColumnLayoutComponent(this.elements, this.componentPaddingVertical, this.colPaddingLeft, this.colPaddingRight, this.colPaddingTop, this.colPaddingBottom, this.elementPosition);
      }
   }
}
