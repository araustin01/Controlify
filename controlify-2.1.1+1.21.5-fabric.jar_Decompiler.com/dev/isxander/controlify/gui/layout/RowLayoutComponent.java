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

public class RowLayoutComponent<T extends RenderComponent> extends AbstractLayoutComponent<T> {
   private final int elementPaddingHorizontal;
   private final int rowPaddingLeft;
   private final int rowPaddingRight;
   private final int rowPaddingTop;
   private final int rowPaddingBottom;
   private final RowLayoutComponent.ElementPosition elementPosition;

   private RowLayoutComponent(Collection<? extends T> elements, int elementPaddingHorizontal, int rowPaddingLeft, int rowPaddingRight, int rowPaddingTop, int rowPaddingBottom, RowLayoutComponent.ElementPosition elementPosition) {
      Iterator var8 = elements.iterator();

      while(var8.hasNext()) {
         T element = (RenderComponent)var8.next();
         this.insertTop(element);
      }

      this.elementPaddingHorizontal = elementPaddingHorizontal;
      this.rowPaddingLeft = rowPaddingLeft;
      this.rowPaddingRight = rowPaddingRight;
      this.rowPaddingTop = rowPaddingTop;
      this.rowPaddingBottom = rowPaddingBottom;
      this.elementPosition = elementPosition;
   }

   public void render(class_332 graphics, int x, int y, float deltaTime) {
      int height = this.getMaxChildHeight();
      if (height != -1) {
         int xOffset = 0;
         Iterator var7 = this.getChildComponents().iterator();

         while(var7.hasNext()) {
            T element = (RenderComponent)var7.next();
            if (element.isVisible()) {
               element.render(graphics, x + this.rowPaddingLeft + xOffset, y + this.rowPaddingTop + (Integer)this.elementPosition.positionFunction.apply(height, element.size().y()), deltaTime);
               xOffset += element.size().x() + this.elementPaddingHorizontal;
            }
         }

      }
   }

   public Vector2ic size() {
      return new Vector2i(this.getSumWidth() + this.rowPaddingLeft + this.rowPaddingRight, this.getMaxChildHeight() + this.rowPaddingTop + this.rowPaddingBottom);
   }

   private int getMaxChildHeight() {
      return this.getChildComponents().stream().filter(RenderComponent::isVisible).map(RenderComponent::size).mapToInt(Vector2ic::y).max().orElse(-1);
   }

   private int getSumWidth() {
      return this.getChildComponents().stream().filter(RenderComponent::isVisible).map(RenderComponent::size).mapToInt((size) -> {
         return size.x() + this.elementPaddingHorizontal;
      }).sum() - this.elementPaddingHorizontal;
   }

   public boolean isVisible() {
      return this.getChildComponents().stream().anyMatch(RenderComponent::isVisible);
   }

   public static <T extends RenderComponent> RowLayoutComponent.Builder<T> builder() {
      return new RowLayoutComponent.Builder();
   }

   public static enum ElementPosition {
      TOP((rowHeight, elementHeight) -> {
         return 0;
      }),
      BOTTOM((rowHeight, elementHeight) -> {
         return rowHeight - elementHeight;
      }),
      MIDDLE((rowHeight, elementHeight) -> {
         return rowHeight / 2 - elementHeight / 2;
      });

      public final BiFunction<Integer, Integer, Integer> positionFunction;

      private ElementPosition(BiFunction<Integer, Integer, Integer> positionFunction) {
         this.positionFunction = positionFunction;
      }

      // $FF: synthetic method
      private static RowLayoutComponent.ElementPosition[] $values() {
         return new RowLayoutComponent.ElementPosition[]{TOP, BOTTOM, MIDDLE};
      }
   }

   public static class Builder<T extends RenderComponent> {
      private final List<T> elements = new ArrayList();
      private int elementPaddingHorizontal;
      private int rowPaddingLeft;
      private int rowPaddingRight;
      private int rowPaddingTop;
      private int rowPaddingBottom;
      private RowLayoutComponent.ElementPosition elementPosition = null;

      public RowLayoutComponent.Builder<T> element(T element) {
         this.elements.add(element);
         return this;
      }

      @SafeVarargs
      public final RowLayoutComponent.Builder<T> elements(T... elements) {
         this.elements.addAll(Arrays.asList(elements));
         return this;
      }

      public RowLayoutComponent.Builder<T> elements(Collection<? extends T> elements) {
         this.elements.addAll(elements);
         return this;
      }

      public RowLayoutComponent.Builder<T> spacing(int padding) {
         this.elementPaddingHorizontal = padding;
         return this;
      }

      public RowLayoutComponent.Builder<T> rowPadding(int left, int right, int top, int bottom) {
         this.rowPaddingLeft = left;
         this.rowPaddingRight = right;
         this.rowPaddingTop = top;
         this.rowPaddingBottom = bottom;
         return this;
      }

      public RowLayoutComponent.Builder<T> rowPadding(int horizontal, int vertical) {
         return this.rowPadding(horizontal, horizontal, vertical, vertical);
      }

      public RowLayoutComponent.Builder<T> rowPadding(int padding) {
         return this.rowPadding(padding, padding, padding, padding);
      }

      public RowLayoutComponent.Builder<T> elementPosition(RowLayoutComponent.ElementPosition elementPosition) {
         this.elementPosition = elementPosition;
         return this;
      }

      public RowLayoutComponent<T> build() {
         Validate.notEmpty(this.elements, "No elements were added to the row!", new Object[0]);
         Validate.notNull(this.elementPosition, "Element position cannot be null!", new Object[0]);
         return new RowLayoutComponent(this.elements, this.elementPaddingHorizontal, this.rowPaddingLeft, this.rowPaddingRight, this.rowPaddingTop, this.rowPaddingBottom, this.elementPosition);
      }
   }
}
