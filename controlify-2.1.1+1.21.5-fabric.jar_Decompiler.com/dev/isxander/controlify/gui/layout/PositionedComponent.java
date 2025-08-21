package dev.isxander.controlify.gui.layout;

import net.minecraft.class_332;
import net.minecraft.class_364;
import net.minecraft.class_4068;
import net.minecraft.class_6379;
import net.minecraft.class_6382;
import net.minecraft.class_6379.class_6380;
import org.joml.Vector2ic;

public class PositionedComponent<T extends RenderComponent> implements class_4068, class_364, class_6379 {
   private final T component;
   private int x;
   private int y;
   private final AnchorPoint windowAnchor;
   private final int offsetX;
   private final int offsetY;
   private final AnchorPoint origin;

   public PositionedComponent(T component, AnchorPoint windowAnchor, int offsetX, int offsetY, AnchorPoint origin) {
      this.component = component;
      this.offsetX = offsetX;
      this.offsetY = offsetY;
      this.windowAnchor = windowAnchor;
      this.origin = origin;
   }

   public void updatePosition(int windowWidth, int windowHeight) {
      Vector2ic componentSize = this.component.size();
      Vector2ic windowPosition = this.windowAnchor.getAnchorPosition(windowWidth, windowHeight);
      Vector2ic anchoredPosition = this.origin.getAnchorPosition(componentSize.x(), componentSize.y());
      this.x = windowPosition.x() + this.offsetX - anchoredPosition.x();
      this.y = windowPosition.y() + this.offsetY - anchoredPosition.y();
   }

   public void method_25394(class_332 graphics, int mouseX, int mouseY, float delta) {
      this.renderComponent(graphics, delta);
   }

   public void renderComponent(class_332 graphics, float deltaTime) {
      this.component.render(graphics, this.x, this.y, deltaTime);
   }

   public int x() {
      return this.x;
   }

   public int y() {
      return this.y;
   }

   public T getComponent() {
      return this.component;
   }

   public void method_25365(boolean focused) {
   }

   public boolean method_25370() {
      return false;
   }

   public class_6380 method_37018() {
      return class_6380.field_33784;
   }

   public void method_37020(class_6382 builder) {
   }
}
